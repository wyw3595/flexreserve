package com.flexreserve.user.mapper;

import com.flexreserve.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsertAndSelect() {
        // ==========================================
        // 1. 测试插入数据 (模拟用户注册)
        // ==========================================
        User newUser = new User();
        newUser.setUsername("zhangsan_test"); // 测试账号
        newUser.setPassword("123456_hash");   // 模拟加密后的密码
        newUser.setPhone("13800138000");
        newUser.setRole(0);   // 0代表普通用户
        newUser.setStatus(1); // 1代表正常可用

        System.out.println("准备执行插入 SQL...");
        int rows = userMapper.insert(newUser);
        System.out.println("插入成功！影响行数: " + rows);

        // MyBatis 的 useGeneratedKeys 会把数据库生成的主键回填到对象里
        System.out.println("MyBatis 回填的新用户 ID 是: " + newUser.getId());
        System.out.println("--------------------------------------------------");

        // ==========================================
        // 2. 测试查询数据 (模拟用户登录)
        // ==========================================
        System.out.println("准备执行查询 SQL...");
        User dbUser = userMapper.selectByUsername("zhangsan_test");

        System.out.println("查询到的用户信息如下：");
        System.out.println(dbUser);
    }
}