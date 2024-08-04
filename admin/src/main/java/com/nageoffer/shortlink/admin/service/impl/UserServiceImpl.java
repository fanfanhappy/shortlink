package com.nageoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.common.convention.exception.ClientException;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dao.mapper.UserMapper;
import com.nageoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;


/**
 * 用户接口实现层
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper , UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.
                lambdaQuery(UserDO.class).
                eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("客户端异常");
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;

    }

    @Override
    public Boolean hasUsername(String username) {
        return userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO userRegisterReqDTO) {
        //1.判断username是否存在
        String username = userRegisterReqDTO.getUsername();
        if (hasUsername(username)) {
            throw new ClientException(USER_NAME_EXIST);
        }
        //2.1.不存在，存入数据库
        //2.1.2获取redission分布式锁
        UserDO userDO = new UserDO();
        BeanUtil.copyProperties(userRegisterReqDTO, userDO);

        RLock lock = redissonClient.getLock("LOCK_USER_REGISTER_KEY" + username);

        try {
            if (lock.tryLock()) {
                int insert = baseMapper.insert(userDO);
                if (insert < 0) {
                    throw new ClientException(USER_SAVE_ERROR);
                }
                //2.2加入布隆过滤器
                userRegisterCachePenetrationBloomFilter.add(username);
                return;
            }
            //没获取到锁，返回错误
            throw new ClientException(USER_NAME_EXIST);
        } finally {
            lock.unlock();
        }


    }

    @Override
    public void update(UserUpdateReqDTO userUpdateReqDTO) {
        // TODO 验证用户是否为当前用户
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.
                lambdaUpdate(UserDO.class).
                eq(UserDO::getUsername, userUpdateReqDTO.getUsername());
        baseMapper.update(BeanUtil.toBean(userUpdateReqDTO, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.
                lambdaQuery(UserDO.class).
                eq(UserDO::getUsername, userLoginReqDTO.getUsername())
                .eq(UserDO::getPassword, userLoginReqDTO.getPassword())
                .eq(UserDO::getDelFlag, 0);

        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("用户不存在");
        }

        //判断token是否存在
        Boolean hasKey = stringRedisTemplate.hasKey("login_" + userLoginReqDTO.getUsername());
        if (hasKey != null && hasKey) {
            throw new ClientException("用户已经登录");
        }

        //存在，登录，生成一个Token放在redis
        String token = UUID.randomUUID().toString().replace("-", "");
        //stringRedisTemplate.opsForValue().set(token , JSON.toJSONString(userDO) , 30 , TimeUnit.MINUTES);

        stringRedisTemplate.opsForHash().
                put("login_" + userLoginReqDTO.getUsername(), token, JSON.toJSONString(userDO));

        //设置过期时间
        stringRedisTemplate.expire("login_" + userLoginReqDTO.getUsername(), 30L, TimeUnit.MINUTES);
        return new UserLoginRespDTO(token);
    }

    @Override
    public Boolean checkLogin(String username, String token) {
        //拿到登录的token
        Object checkToken = stringRedisTemplate.opsForHash().get("login_" + username, token);
        if (checkToken != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void loginOut(String username, String token) {
        //判断是否正在登录状态
        if (checkLogin(username , token))
        {
            stringRedisTemplate.opsForHash().delete("login_" + username , token);
            return;
        }else {
            throw new ClientException("用户没有登录或Token不存在");
        }
    }
}
