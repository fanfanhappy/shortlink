package com.nageoffer.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nageoffer.shortlink.admin.dao.entity.UserDO;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
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

}
