package com.flexreserve.user.mapper;

import com.flexreserve.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

        // 1. 插入一条新用户记录 (注册用)
        int insert(User user);
        // 2. 根据用户名查询用户 (登录或校验重名时用)
        User selectByUsername(String username);

        // 3. 根据手机号查询用户
        @Select("select * from sys_user where phone = #{phone}")
        User getUserByPhone(String phone);
}
