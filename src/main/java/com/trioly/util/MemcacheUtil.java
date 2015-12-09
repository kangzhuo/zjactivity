package com.trioly.util;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.trioly.config.CommonConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by maidou on 15/12/3.
 */
public class MemcacheUtil {
    private static MemcacheUtil sInstance;

    /**
     * memcached客户端单例
     */
    private static MemCachedClient cachedClient = new MemCachedClient();

    private static synchronized void makeInstance() {
        sInstance = new MemcacheUtil();
    }

    public static synchronized MemcacheUtil getInstance() {
        if(null == sInstance) {
            makeInstance();
        }
        return sInstance;
    }

    private MemcacheUtil() {
        this.init();
    }

    private void init() {
        //获取连接池的实例
        SockIOPool pool = SockIOPool.getInstance();

        //服务器列表及其权重
        String[] servers = {"127.0.0.1:11211"};
        Integer[] weights = {3};

        //设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);

        //设置初始连接数、最小连接数、最大连接数、最大处理时间
        pool.setInitConn(10);
        pool.setMinConn(10);
        pool.setMaxConn(1000);
        pool.setMaxIdle(1000*60*60);

        //设置连接池守护线程的睡眠时间
        pool.setMaintSleep(60);

        //设置TCP参数，连接超时
        pool.setNagle(false);
        pool.setSocketTO(60);
        pool.setSocketConnectTO(0);

        //初始化并启动连接池
        pool.initialize();

        //压缩设置，超过指定大小的都压缩
//      cachedClient.setCompressEnable(true);
//      cachedClient.setCompressThreshold(1024*1024);
    }

    public static boolean add(String key, Object value) {
        return cachedClient.add(key, value);
    }

    public static boolean add(String key, Object value, Integer expire) {
        return cachedClient.add(key, value, expire);
    }

    public static boolean put(String key, Object value) {
        return cachedClient.set(key, value);
    }

    public static boolean put(String key, Object value, Integer expire) {
        return cachedClient.set(key, value, expire);
    }

    public static boolean replace(String key, Object value) {
        return cachedClient.replace(key, value);
    }

    public static boolean replace(String key, Object value, Integer expire) {
        return cachedClient.replace(key, value, expire);
    }

    public static Object get(String key) {
        return cachedClient.get(key);
    }
}
