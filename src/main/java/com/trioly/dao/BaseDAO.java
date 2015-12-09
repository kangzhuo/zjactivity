package com.trioly.dao;

import org.apache.ibatis.session.SqlSession;

/**
 * Created by maidou on 15/12/3.
 */
public abstract class BaseDAO {
    private SqlSession sqlSession;

    public void setSqlSession(SqlSession session) {
        this.sqlSession = session;
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public BaseDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public BaseDAO() {

    }
}
