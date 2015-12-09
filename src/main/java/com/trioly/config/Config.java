package com.trioly.config;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Created by maidou on 15/12/3.
 */
public class Config {
    private static final Logger logger = Logger.getLogger(Config.class);
    private Properties properties;
    public Config(String pathName){
        try {
            InputStream is = Config.class.getResourceAsStream("/"+pathName);
            if(is == null){
                File resource = new File(pathName);
                is = new FileInputStream(resource);
            }
            properties = new Properties();
            properties.load(is);
        } catch (FileNotFoundException e) {
            logger.debug("FileNotFoundException " + pathName + " ", e);
        } catch (IOException e) {
            logger.debug("IOException in config file load", e);
        }
    }

    public String getProperties(String fieldName) {
        return getProperties(fieldName,null);
    }

    public String getProperties(String fieldName,String defaults){
        if(properties.containsKey(fieldName)){
            return properties.getProperty(fieldName);
        }else{
            return defaults;
        }
    }

    public int getPropertiesAsInt(String fieldName) {
        return getPropertiesAsInt(fieldName, 0);
    }

    public int getPropertiesAsInt(String fieldName,int defaults) {
        String v = properties.getProperty(fieldName);
        try{
            return Integer.parseInt(v);
        }catch (Exception ex){
            logger.error("获取参数("+fieldName+")出错:",ex);
            return defaults;
        }
    }

    public long getPropertiesAsLong(String fieldName) {
        return getPropertiesAsLong(fieldName, 0);
    }

    public long getPropertiesAsLong(String fieldName,long defaults) {
        String v = properties.getProperty(fieldName);
        try{
            return Long.parseLong(v);
        }catch (Exception ex){
            logger.error("获取参数("+fieldName+")出错:",ex);
            return defaults;
        }
    }

    public boolean getPropertiesAsBoolean(String fieldName) {
        return getPropertiesAsBoolean(fieldName, false);
    }

    public boolean getPropertiesAsBoolean(String fieldName,boolean defaults) {
        String v = properties.getProperty(fieldName);
        try{
            return "1".equals(v)?true:false;
        }catch (Exception ex){
            logger.error("获取参数("+fieldName+")出错:",ex);
            return defaults;
        }
    }

    public boolean containProp(String prop){
        return properties.containsKey(prop);
    }
}
