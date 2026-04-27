package com.flexreserve.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.flexreserve.common.Result;
import com.flexreserve.user.entity.DTO.UserDTO;
import com.flexreserve.user.entity.User;
import com.flexreserve.user.mapper.UserMapper;
import com.flexreserve.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;


    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public Result<String> getCode(String phone) {
        if (phone == null){
            return Result.error("手机号不能为空！");
        }
        // 验证手机号格式是否正确
        if (!PhoneUtil.isMobile( phone)){
            return Result.error("手机号格式错误！");
        }
        // 查看验证码是否存在
        if(redisTemplate.hasKey("code:" + phone)){
            return Result.error("验证码已发送，请勿重复发送！");
        }
        // 判断是否限流
        String key = "code:limit:" + phone;
        // 获取当前次数
        Long increment = redisTemplate.opsForValue().increment(key);
        // 设置过期时间
        if (increment != null && increment == 1){
            redisTemplate.expire(key,1,TimeUnit.HOURS);
        }
        // 判断是否超过限制
        if (increment != null && increment > 5){
            return Result.error("操作过于频繁，请一小时后再试！");
        }

        // 生成验证码
        String numbers = RandomUtil.randomNumbers(6);

       // 保存验证码到redis
        Boolean success = redisTemplate.opsForValue().setIfAbsent("code:" + phone, numbers, 5, TimeUnit.MINUTES);
        if (Boolean.TRUE.equals( success)){
            return Result.success("验证码已发送！");
        }
        else {
            return Result.error("验证码已发送，请勿重复发送！！");
        }


    }

    @Override
    public Result<String> login(String phone , String code) {

        // 1. 手机号格式校验
        if (!PhoneUtil.isMobile(phone)){
            return Result.error("手机号格式错误！");
        }
        // 2. 验证码校验
        if (code == null){
            return Result.error("验证码不能为空！");
        }
        // 3.获取验证码
        String newCode = redisTemplate.opsForValue().get("code:" + phone);
        if (newCode == null){
            return Result.error("验证码已过期！");
        }
        if (!newCode.equals(code)){
            return Result.error("验证码错误！");
        }
        // 判断用户是否存在
        User user = userMapper.getUserByPhone(phone);
        if (user == null){
            return Result.error("用户不存在,请先注册！");
        }
        if (user.getStatus() == 0){
            return Result.error("用户被禁用！");
        }
        // 4.UUID生成Token
        String token = UUID.fastUUID().toString(true);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        String jsonStr = JSONUtil.toJsonStr(userDTO);
        String key = "login:token:" + token;
        redisTemplate.opsForValue().set(key,jsonStr, 30, TimeUnit.MINUTES);

        // 4. 删除redis中的验证码
        redisTemplate.delete("code:" + phone);

        return Result.success(token);
    }
}
