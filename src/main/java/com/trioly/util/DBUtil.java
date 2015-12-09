package com.trioly.util;

import com.trioly.config.CommonConfig;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maidou on 15/12/3.
 */
public class DBUtil {
    private static final String XML_RESOURCE = "mybatis-config.xml";
    private static final Logger logger = Logger.getLogger(DBUtil.class);

    private static SqlSessionFactory sqlSessionFactory = null;

    static{
        try {
            InputStream inputStream = Resources.getResourceAsStream(XML_RESOURCE);
            if(inputStream == null){
                File resource = new File(XML_RESOURCE);
                inputStream = new FileInputStream(resource);
            }
            boolean debug = CommonConfig.getInstance().debug();
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, debug ? "development" : "production");
        }catch (IOException e) {
            logger.error("Exception in getSession", e);
        }
    }

    public synchronized static SqlSession getSession() {
        return getSession(true);
    }

    public synchronized static SqlSession getSession(boolean autocommit) {
        if(null == sqlSessionFactory){
            try {
                InputStream inputStream = Resources.getResourceAsStream(XML_RESOURCE);
                boolean debug = CommonConfig.getInstance().debug();
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, debug ? "development" : "production");
            }catch (IOException e) {
                logger.error("Exception in getSession", e);
            }
        }
        SqlSession session = sqlSessionFactory.openSession(autocommit);
        return session;
    }

    public synchronized static void closeSession(SqlSession session) {
        if (session != null) {
            session.close();
        }
    }
}
