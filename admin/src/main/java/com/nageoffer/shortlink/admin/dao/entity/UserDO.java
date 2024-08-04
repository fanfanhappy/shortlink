package com.nageoffer.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nageoffer.shortlink.admin.common.database.BaseDO;
import lombok.Data;

import java.util.Date;

/**
 * @projectName: shortlink
 * @package: com.nageoffer.shortlink.admin.dao.entity
 * @className: UserDO
 * @author: 姬紫衣
 * @description: TODO
 * @date: 2024/7/30 12:30
 * @version: 1.0
 */

/*
用户持久层实体类
* */
@TableName("t_user")
@Data
public class UserDO extends BaseDO {
    /**
     * ID
     */
    private Long id;

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

    /**
     * 注销时间戳
     */
    private Long deletionTime;



}
