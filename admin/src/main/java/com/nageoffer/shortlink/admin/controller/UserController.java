package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.nageoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.nageoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Result<UserRespDTO> getUserByUserName(@PathVariable("username") String username)
    {
        UserRespDTO userRespDTO = userService.getUserByUsername(username);
        return Results.success(userRespDTO);
    }

    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username")String username)
    {
        return Results.success(userService.hasUsername(username));
    }


    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO userRegisterReqDTO)
    {
        userService.register(userRegisterReqDTO);
        return Results.success();
    }
}
