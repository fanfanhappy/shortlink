package com.nageoffer.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @projectName: shortlink
 * @package: com.nageoffer.shortlink.admin.dto.req
 * @className: UserRegisterReqDTO
 * @author: 姬紫衣
 * @description: TODO
 * @date: 2024/7/30 15:58
 * @version: 1.0
 */
@Data
public class UserRegisterReqDTO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}
