package com.flexreserve.interceptor;

import cn.hutool.json.JSONUtil;
import com.flexreserve.user.entity.DTO.UserDTO;
import com.flexreserve.utils.TenantHolder;
import com.flexreserve.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;


public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
        TenantHolder.remove();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的TOKEN
        String token = request.getHeader("authorization");
        if (token == null){
            return true;
        }
        // 2.重看redis中是否存在，存在则刷新并保存到当前线程中，不存在放行
        String key = "login:token:" + token;
        String User = stringRedisTemplate.opsForValue().get(key);
        if (User != null){
            stringRedisTemplate.expire(key,30, TimeUnit.MINUTES);
            UserHolder.saveUser(JSONUtil.toBean(User, UserDTO.class));
        }
        UserDTO userDTO = UserHolder.getUser();
        if (userDTO!=null&&userDTO.getTenantId()!=null){
            TenantHolder.setTenantId(userDTO.getTenantId());
        }

        return true;
    }
}
