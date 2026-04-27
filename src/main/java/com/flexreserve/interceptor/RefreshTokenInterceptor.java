package com.flexreserve.interceptor;

import cn.hutool.json.JSONUtil;
import com.flexreserve.user.entity.DTO.UserDTO;
import com.flexreserve.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的TOKEN
        String token =(String)request.getAttribute("token");
        if (token == null){
            return true;
        }
        // 2.重看redis中是否存在，存在则刷新并保存到当前线程中，不存在放行
        String key = "login:token:" + token;
        String User = redisTemplate.opsForValue().get(key);
        if (User != null){
            redisTemplate.expire(key,30, TimeUnit.MINUTES);
            UserHolder.saveUser(JSONUtil.toBean(User, UserDTO.class));
        }

        return true;
    }
}
