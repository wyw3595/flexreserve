package com.flexreserve.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//注解RestControllerAdvice拦截整个系统中所有 Controller 抛出的异常
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 👇 拦截所有类型的 Exception（最高级别的兜底）
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        // 1. 在后台控制台打印出详细的红色报错日志，方便你修 Bug
        log.error("系统出现异常: ", e);

        // 2. 把报错信息包装成统一的 JSON 格式返回给浏览器 (500 代表服务器内部错误)
        // 注意：在真实的生产环境中，为了安全，我们通常不会把底层 e.getMessage() 直接抛给前端，
        // 而是返回类似 "系统繁忙，请稍后再试" 的统一文案。这里为了方便你调试，先返回真实报错。
        return Result.error(500, "系统开小差了：" + e.getMessage());
    }
}
