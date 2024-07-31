package com.nageoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.common.convention.exception.ClientException;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dao.mapper.UserMapper;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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


    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.
                lambdaQuery(UserDO.class).
                eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null)
        {
            throw new ClientException("客户端异常");
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO , result);
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
        if (hasUsername(username))
        {
            throw new ClientException(USER_NAME_EXIST);
        }
        //2.1.不存在，存入数据库
        //2.1.2获取redission分布式锁
        UserDO userDO = new UserDO();
        BeanUtil.copyProperties(userRegisterReqDTO , userDO);

        RLock lock = redissonClient.getLock("LOCK_USER_REGISTER_KEY"+ username);

        try {
            if(lock.tryLock())
            {
                int insert = baseMapper.insert(userDO);
                if(insert < 0)
                {
                    throw new ClientException(USER_SAVE_ERROR);
                }
                //2.2加入布隆过滤器
                userRegisterCachePenetrationBloomFilter.add(username);
                return;
            }
            //没获取到锁，返回错误
            throw new ClientException(USER_NAME_EXIST);
        }finally {
            lock.unlock();
        }


    }
}
