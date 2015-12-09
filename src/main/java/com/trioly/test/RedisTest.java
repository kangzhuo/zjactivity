package com.trioly.test;

import com.trioly.util.RedisUtil;
import redis.clients.jedis.Jedis;

/**
 * Created by maidou on 15/12/3.
 */
public class RedisTest {
    public static void main(String[] args) throws Exception{
        Jedis jedis = RedisUtil.getInstance().getResource();
        jedis.set("key","val");
        RedisUtil.getInstance().returnResource(jedis);
    }
}
