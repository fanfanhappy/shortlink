package com.nageoffer.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;

/*
用户接口层
 */
public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    Boolean hasUsername(String username);

    /**
     * 用户注册
     * @param userRegisterReqDTO
     */
    void register(UserRegisterReqDTO userRegisterReqDTO);

    /**
     * 用户更新
     * @param userUpdateReqDTO
     */
    void update(UserUpdateReqDTO userUpdateReqDTO);


    /**
     * 用户登录
     * @param userLoginReqDTO
     * @return
     */
    UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO);

    /**
     * 检查用户是否登录
     * @param token
     * @return
     */
    Boolean checkLogin(String username , String token);

    /**
     * 用户退出
     * @param username
     * @param token
     */
    void loginOut(String username, String token);
}
