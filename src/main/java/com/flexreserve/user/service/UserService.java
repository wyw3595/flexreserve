package com.flexreserve.user.service;

import com.flexreserve.user.entity.User;

public interface UserService {
    // 根据用户名获取用户信息
    User getUserByUsername(String username);
}
