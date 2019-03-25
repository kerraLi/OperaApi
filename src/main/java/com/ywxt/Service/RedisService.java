package com.ywxt.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service("redisService")
public class RedisService {

    private Jedis jedis;

    // redis
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    public Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis(redisHost, redisPort);
        }
        return this.jedis;
    }

}
