package com.trioly.test;

import com.trioly.util.RedisUtil;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.sql.*;

public class DataDealToRedis {
    private static Connection g_conn;

    public static void main(String[] args) throws Exception{
        String l_strDealType = args[0];

        try{
            //连接MySql数据库
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/sdk?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true" ;
            String username = "root" ;
            String password = "" ;
            g_conn = DriverManager.getConnection(url, username, password) ;
        }catch(SQLException se){
            System.out.println("数据库连接失败！");
            se.printStackTrace() ;
        }

        if (l_strDealType.compareTo("1") == 0) {
            oneToRedis();
        } else if (l_strDealType.compareTo("2") == 0) {
            twoToRedis();
        } else {
            hlrToRedis();
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
            int l_iCharKey = l_strBillId.charAt(0) - 'a';
            int l_iMod = l_iCharKey%3;
            if (l_iMod == 1) {
                Jedis jedis = RedisUtil.getInstance().getResource(1);
                jedis.set(l_strBillId, l_strValue);
                RedisUtil.getInstance().returnResource(jedis, 1);
            } else if (l_iMod == 2) {
                Jedis jedis = RedisUtil.getInstance().getResource(2);
                jedis.set(l_strBillId, l_strValue);
                RedisUtil.getInstance().returnResource(jedis, 2);
            } else {
                Jedis jedis = RedisUtil.getInstance().getResource(3);
                jedis.set(l_strBillId, l_strValue);
                RedisUtil.getInstance().returnResource(jedis, 3);
            }
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

            String l_strKey = l_strBillId + "－" + l_strTargetBillId;
            int l_iCharKey = l_strBillId.charAt(0) - 'a';
            int l_iMod = l_iCharKey%3;
            if (l_iMod == 1) {
                Jedis jedis = RedisUtil.getInstance().getResource(1);
                jedis.set(l_strKey, l_strQmd);
                RedisUtil.getInstance().returnResource(jedis, 1);
            } else if (l_iMod == 2) {
                Jedis jedis = RedisUtil.getInstance().getResource(2);
                jedis.set(l_strKey, l_strQmd);
                RedisUtil.getInstance().returnResource(jedis, 2);
            } else {
                Jedis jedis = RedisUtil.getInstance().getResource(3);
                jedis.set(l_strKey, l_strQmd);
                RedisUtil.getInstance().returnResource(jedis, 3);
            }
        }
    }

    //用户指数信入库
    public  static void hlrToRedis () throws Exception{
        String l_strHlrFile = "";
        //读取全局配置文件内容
        File l_file = new File(System.getProperty("user.home") + "/config/DataDeal.conf");
        BufferedReader l_reader = null;

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            String l_strtemp;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                if (l_strtemp.contains("hlrFile:")) {
                    l_strHlrFile = l_strtemp.substring(8, l_strtemp.length());
                    l_strHlrFile = l_strHlrFile.trim();
                }
            }
            l_reader.close();

            l_reader = new BufferedReader(new FileReader(l_strHlrFile));
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                Jedis jedis = RedisUtil.getInstance().getResource(1);
                jedis.set(l_strtemp, "");
                RedisUtil.getInstance().returnResource(jedis, 1);
            }
            l_reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (l_reader != null) {
                try {
                    l_reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
