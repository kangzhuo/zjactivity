package com.trioly.services;

import com.trioly.dao.TestDAO;
import com.trioly.model.TestModel;
import com.trioly.util.DBUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;

/**
 * Created by maidou on 15/12/3.
 */
public class TestServices extends BaseServices {
    private boolean auto = true;
    private SqlSession sqlSession;
    private TestDAO dao;
    public TestServices(){
        this.sqlSession = DBUtil.getSession();
        dao = new TestDAO(sqlSession);
    }

    public TestServices(SqlSession sqlSession){
        this.sqlSession = sqlSession;
        dao = new TestDAO(sqlSession);
        auto = false;
    }

    public TestModel getTest(int id){
        return dao.getTest(id);
    }

    public int updateTest(TestModel test){
        return dao.updateTest(test);
    }

    public int deleteTest(TestModel test){
        return dao.deleteTest(test);
    }
}
