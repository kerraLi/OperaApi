package com.ywxt.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service("redisService")
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        // 序列化
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    public void set(String key, Object value, Integer ttl) {
        // 序列化
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value, ttl, TimeUnit.SECONDS);
    }


    public String get(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return (String) vo.get(key);
    }

    public void delete(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    public void expire(String key, Integer ttl) {
        redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
    }

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
}
