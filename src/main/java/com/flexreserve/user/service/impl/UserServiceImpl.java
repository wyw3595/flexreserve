package com.flexreserve.user.service.impl;

import com.flexreserve.user.entity.User;
import com.flexreserve.user.mapper.UserMapper;
import com.flexreserve.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}
