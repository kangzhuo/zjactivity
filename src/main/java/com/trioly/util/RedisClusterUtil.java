package com.trioly.util;

import com.trioly.config.CommonConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class RedisClusterUtil {
    private static RedisClusterUtil sInstance;

    private static synchronized void makeInstance() {
        sInstance = new RedisClusterUtil();
    }

    public static synchronized RedisClusterUtil getInstance() {
        if(null == sInstance) {
            makeInstance();
        }
        return sInstance;
    }

    private JedisCluster jc;

    private RedisClusterUtil() {
        this.init();
    }

    private void init() {
        CommonConfig conf = CommonConfig.getInstance();
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        String l_strHostAndPorts = conf.getString("redis.cluster.host", "127.0.0.1:6379");
        String[] l_strHostAndPortList = l_strHostAndPorts.split("\\|");
        for (String l_strHostAndPort : l_strHostAndPortList) {
            String l_strFirst = l_strHostAndPort.substring(0, l_strHostAndPort.indexOf(":"));
            String l_strSecond = l_strHostAndPort.substring(l_strHostAndPort.indexOf(":") + 1, l_strHostAndPort.length());
            //System.out.println("----" + l_strFirst + "------" + l_strSecond);
            jedisClusterNodes.add(new HostAndPort(l_strFirst, Integer.parseInt(l_strSecond)));
        }

        jc = new JedisCluster(jedisClusterNodes);
    }

    public JedisCluster getResource() {
        return jc;
    }
}
