package com.xuyao.chat.util;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisUtil {

    private static StringRedisTemplate redisTemplate;

    public RedisUtil(StringRedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static void set(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    public static void set(String key, String value, long timeout, TimeUnit unit){
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public static String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public static Boolean delete(String key){
        return redisTemplate.delete(key);
    }
}
