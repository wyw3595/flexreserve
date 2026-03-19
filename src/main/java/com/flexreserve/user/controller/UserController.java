package com.flexreserve.user.controller;

import com.flexreserve.common.Result;
import com.flexreserve.user.entity.User;
import com.flexreserve.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/{username}")
    public Result<User> getUserInfo(@PathVariable String username) {
        // 调用 Service 获取数据，Spring Boot 会自动把它转成 JSON 返回给浏览器
      
        User user = userService.getUserByUsername(username);

        return Result.success(user);
    }
}
