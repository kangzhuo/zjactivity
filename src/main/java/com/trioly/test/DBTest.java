package com.trioly.test;

import com.trioly.services.TestServices;
import com.trioly.util.DBUtil;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by maidou on 15/12/3.
 */
public class DBTest {
    public static void main(String[] args) throws Exception{
        SqlSession session = DBUtil.getSession();
        TestServices services = new TestServices(session);
        services.getTest(11);
        DBUtil.closeSession(session);
    }
}
