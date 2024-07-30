package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: shortlink
 * @package: com.nageoffer.shortlink.admin.controller
 * @className: UserController
 * @author: 姬紫衣
 * @description: 用户控制管理层
 * @date: 2024/7/30 11:14
 * @version: 1.0
 */

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public UserRespDTO getUserByUserName(@PathVariable("username") String username)
    {
        return userService.getUserByUsername(username);
    }
}
