package com.flexreserve.user.controller;

import com.flexreserve.common.Result;
import com.flexreserve.user.entity.DTO.UserDTO;
import com.flexreserve.user.entity.User;
import com.flexreserve.user.service.UserService;
import com.flexreserve.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理模块")
public class UserController {

    private final UserService userService;
    @GetMapping("/{username}")
    @Operation(summary = "获取用户信息")
    public Result<User> getUserInfo(@PathVariable String username) {
        // 调用 Service 获取数据，Spring Boot 会自动把它转成 JSON 返回给浏览器
      
        User user = userService.getUserByUsername(username);

        return Result.success(user);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录 (获取 Token)")
    public Result<String> login(String phone, String code){
        return userService.login(phone, code);
    }
    @PostMapping("/code")
    @Operation(summary = "获取验证码")
    public Result<String> getCode(String phone){

        return userService.getCode(phone);
    }

}
