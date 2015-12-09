package com.trioly.dao;

import com.trioly.model.TestModel;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;

/**
 * Created by maidou on 15/12/3.
 */
public class TestDAO extends BaseDAO{
    public TestDAO(SqlSession sqlSession){
        super(sqlSession);
    }

    public TestModel getTest(int id){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return this.getSqlSession().selectOne("TestDAO.queryTestById", params);
    }

    public int updateTest(TestModel test){
        return this.getSqlSession().update("TestDAO.updateTest", test);
    }

    public int deleteTest(TestModel test){
        return this.getSqlSession().delete("TestDAO.deleteTest", test);
    }
}
