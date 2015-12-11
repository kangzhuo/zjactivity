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

    private JedisPool jedisPool1, jedisPool2, jedisPool3;

    private RedisUtil() {
        this.init();
    }

    private void init() {
        CommonConfig conf = CommonConfig.getInstance();
        String redisHost1 = conf.getString("redis.host1", CommonConfig.REDIS_HOST_DEFAULT);
        String redisHost2 = conf.getString("redis.host2", CommonConfig.REDIS_HOST_DEFAULT);
        String redisHost3 = conf.getString("redis.host3", CommonConfig.REDIS_HOST_DEFAULT);
        this.jedisPool1 = new JedisPool(new JedisPoolConfig(), redisHost1);
        this.jedisPool2 = new JedisPool(new JedisPoolConfig(), redisHost2);
        this.jedisPool3 = new JedisPool(new JedisPoolConfig(), redisHost3);
    }

    public Jedis getResource(int p_iNum) {
        if (1 == p_iNum) {
            return jedisPool1.getResource();
        } else if (2 == p_iNum) {
            return jedisPool2.getResource();
        } else {
            return jedisPool3.getResource();
        }

    }

    public void returnResource(Jedis resource, int p_iNum) {
        if (1 == p_iNum) {
            jedisPool1.returnResource(resource);
        } else if (2 == p_iNum) {
            jedisPool2.returnResource(resource);
        } else {
            jedisPool3.returnResource(resource);
        }
    }

    public void destory() {
        jedisPool1.destroy();
        jedisPool2.destroy();
        jedisPool3.destroy();
    }
}
