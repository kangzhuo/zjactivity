package com.trioly.util;

import com.trioly.config.CommonConfig;
import com.trioly.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by maidou on 15/12/3.
 */
public class RedisUtil {
    private static RedisUtil sInstance;

    private static synchronized void makeInstance() {
        sInstance = new RedisUtil();
    }

    public static synchronized RedisUtil getInstance() {
        if(null == sInstance) {
            makeInstance();
        }
        return sInstance;
    }

    private JedisPool jedisPool;

    private RedisUtil() {
        this.init();
    }

    private void init() {
        CommonConfig conf = CommonConfig.getInstance();
        String redisHost = conf.getString(CommonConfig.REDIS_HOST,CommonConfig.REDIS_HOST_DEFAULT);
        this.jedisPool = new JedisPool(new JedisPoolConfig(), redisHost);
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }

    public void returnResource(Jedis resource) {
        jedisPool.returnResource(resource);
    }

    public void destory() {
        jedisPool.destroy();
    }
}
