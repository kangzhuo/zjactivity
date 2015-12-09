package com.trioly.config;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maidou on 15/12/3.
 */
public class CommonConfig {
    private static final Logger logger = Logger.getLogger(CommonConfig.class);
    String path = "config.properties";
    Map<String,Object> init = new HashMap();
    Config conf = null;

    public final static String REDIS_HOST = "redis.host";
    public final static String REDIS_HOST_DEFAULT = "127.0.0.1";

    private static CommonConfig sInstance;

    private static synchronized void makeInstance() {
        sInstance = new CommonConfig();
    }

    public static synchronized CommonConfig getInstance() {
        if(null == sInstance) {
            makeInstance();
        }
        return sInstance;
    }

    private CommonConfig(){
        conf = new Config(path);
    }

    public int getInt(String name,int defaults){
        if(init.containsKey(name)){
            return (Integer)init.get(name);
        }
        int result = conf.getPropertiesAsInt(name, defaults);
        init.put(name,result);
        return result;
    }

    public String getString(String name,String defaults){
        if(init.containsKey(name)){
            return (String)init.get(name);
        }
        String result = conf.getProperties(name, defaults);
        if("".equals(result)){
            result =  null;
        }
        init.put(name,result);
        return result;
    }

    public long getLong(String name,long defaults){
        if(init.containsKey(name)){
            return (Long)init.get(name);
        }
        long result = conf.getPropertiesAsLong(name, defaults);
        init.put(name,result);
        return result;
    }

    public boolean getBoolean(String name,boolean defaults){
        if(init.containsKey(name)){
            return (Boolean)init.get(name);
        }
        boolean result = conf.getPropertiesAsBoolean(name, defaults);
        init.put(name,result);
        return result;
    }

    public boolean debug(){
        return getBoolean("debug",false);
    }
}
