package com.ywxt.Utils;

import redis.clients.jedis.Jedis;

public class RedisUtils {

    private Jedis jedis;

    public RedisUtils() {
        jedis = new Jedis(Parameter.redisHost, Parameter.redisPort);
    }

    public Jedis getJedis() {
        return this.jedis;
    }

}
