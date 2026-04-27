package com.flexreserve.config;

import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.password}")
    private String redisPassword;
    @Bean
    public RedisClient redisClient() {
        String redisUri = String.format("redis://:%s@%s:%d", redisPassword, redisHost, redisPort);
        RedisClient redisClient = RedisClient.create(redisUri);
        return redisClient;
    }


}
