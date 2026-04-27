package com.flexreserve.interceptor;

import cn.hutool.json.JSONUtil;
import com.flexreserve.common.Result;
import com.flexreserve.user.entity.DTO.UserDTO;
import com.flexreserve.utils.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(UserHolder.getUser() == null){
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONUtil.toJsonStr(Result.error("未登录或登录已过期")));
            return false;
        }
        return true;
    }
}
