package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author: zyy
 * @version: 1.0
 */
@Component
public class RedisIdWorker {

    //初始时间戳
    private static final long BEGIN_TIMESTAMP = 1640995200L; // 2000-01-01 00:00:00 的秒级时间戳
    //序列号位数
    private static final long COUNT_BITS = 32;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public long nextId(String keyPrefix){//业务前缀
        //1.生成时间戳
        // 在 nextId 方法中实现
        long nowSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        //2.生成序列号
        //2.1获取当前日期，精确到天
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //2.2自增长
        long increment = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        //3.拼接并返回
        return (timestamp << COUNT_BITS) | increment;
    }

}
