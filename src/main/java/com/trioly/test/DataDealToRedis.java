package com.trioly.test;

import com.trioly.util.RedisUtil;
import redis.clients.jedis.Jedis;

import java.sql.*;

public class DataDealToRedis {
    private static Connection g_conn;

    public static void main(String[] args) throws Exception{
        String l_strDealType = args[0];

        try{
            //连接MySql数据库
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/sdk?useUnicode=true ;characterEncoding=UTF-8 ;zeroDateTimeBehavior=convertToNull ;transformedBitIsBoolean=true" ;
            String username = "root" ;
            String password = "" ;
            g_conn = DriverManager.getConnection(url, username, password) ;
        }catch(SQLException se){
            System.out.println("数据库连接失败！");
            se.printStackTrace() ;
        }

        if (l_strDealType.compareTo("1") == 0) {
            oneToRedis();
        } else {
            twoToRedis();
        }

        //关闭连接对象
        if(g_conn != null){
            try{
                g_conn.close() ;
            }catch(SQLException e){
                e.printStackTrace() ;
            }
        }
    }

    //用户指数信息入库
    public  static void oneToRedis () throws Exception{

        String l_strSql = "select * from one_result";
        PreparedStatement l_stmt = g_conn.prepareStatement(l_strSql);
        ResultSet rs = l_stmt.executeQuery();

        while(rs.next()){
            String l_strBillId = rs.getString("bill_id");
            String l_strRmzs = rs.getString("rmzs");
            String l_strPm = rs.getString("pm");
            String l_strOne = rs.getString("one");
            String l_strTwo = rs.getString("two");
            String l_strThree = rs.getString("three");
            String l_strFour = rs.getString("four");
            String l_strFive = rs.getString("five");

            String l_strValue = l_strRmzs + "|" + l_strPm + "|" + l_strOne + "|" + l_strTwo + "|" + l_strThree + "|" + l_strFour + "|" + l_strFive;

            Jedis jedis = RedisUtil.getInstance().getResource();
            jedis.set(l_strBillId, l_strValue);
            RedisUtil.getInstance().returnResource(jedis);
        }
    }

    //用户指数信息入库
    public  static void twoToRedis () throws Exception{

        String l_strSql = "select * from two_result";
        PreparedStatement l_stmt = g_conn.prepareStatement(l_strSql);
        ResultSet rs = l_stmt.executeQuery();

        while(rs.next()){
            String l_strBillId = rs.getString("bill_id");
            String l_strTargetBillId = rs.getString("target_bill_id");
            String l_strQmd = rs.getString("qmd");

            String l_strKey = l_strBillId + "|" + l_strTargetBillId;

            Jedis jedis = RedisUtil.getInstance().getResource();
            jedis.set(l_strKey, l_strQmd);
            RedisUtil.getInstance().returnResource(jedis);
        }
    }
}
