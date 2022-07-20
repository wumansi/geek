package com.demo.cache.concurrent;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 分布式计数器.
 */
@Component
public class RedisCount {
    @Resource
    private RedisTemplate redisTemplate ;

    public int incrementInt(String key, int increment){
        Long curvalue = redisTemplate.opsForValue().increment(key, increment);
        return curvalue.intValue();
    }

    public int decrementInt(String key, int increment){
        Long curvalue = redisTemplate.opsForValue().decrement(key, increment);
        return curvalue.intValue();
    }

    public void setKeyExpire(String key, long expireTime){
        redisTemplate.expire(key, expireTime, TimeUnit.HOURS);
    }

    public boolean existKey(String key){
        return redisTemplate.hasKey(key);
    }

    public int getByKey(String key){
        Integer curvalue = (Integer)redisTemplate.opsForValue().get(key);
        return curvalue;
    }
}
