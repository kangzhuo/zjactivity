package com.trioly.test;

import java.io.*;
import java.util.Map;

import redis.clients.jedis.Jedis;

import com.trioly.util.RedisUtil;

public class DataDealToRedis {
    private static Map<String, String> g_map;

    public static void main(String[] args) throws Exception{
        String l_strFileName = args[0];

        g_map = InitData.initRMZSMap();

        if (l_strFileName.contains("OneResult")) {
            oneResultToRedis(l_strFileName);
        } else if (l_strFileName.contains("TwoResult")) {
            twoResultToRedis(l_strFileName);
        } else if (l_strFileName.contains("TwoTmp")) {
            twoTmpToRedis(l_strFileName);
        } else if (l_strFileName.contains("4")) {
            hlrToRedis();
        }
    }

    //用户指数信息入库
    public  static void oneResultToRedis (String p_strFileName) throws Exception{
        //读取文件内容
        File l_file = new File(p_strFileName);
        BufferedReader l_reader = null;

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            String l_strtemp;
            String l_strValue, l_strValueInRedis;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                String[] l_strLines = l_strtemp.split(",");
                if (l_strLines.length < 2)
                    continue;

                if (g_map.containsKey(l_strLines[1])) {
                    l_strValue = g_map.get(l_strLines[1]);
                } else {
                    l_strValue = "76";
                }

                l_strValueInRedis = l_strLines[1] + "|" + l_strValue + "|0|0|0|0|0";
                int l_iCharKey = l_strLines[0].charAt(0) - 'a';
                int l_iMod = l_iCharKey%3;
                if (l_iMod == 1) {
                    Jedis jedis = RedisUtil.getInstance().getResource(1);
                    jedis.set(l_strLines[0], l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 1);
                } else if (l_iMod == 2) {
                    Jedis jedis = RedisUtil.getInstance().getResource(2);
                    jedis.set(l_strLines[0], l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 2);
                } else {
                    Jedis jedis = RedisUtil.getInstance().getResource(3);
                    jedis.set(l_strLines[0], l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 3);
                }
            }
            l_reader.close();
        } catch (IOException e){
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

    //用户间亲密度入库
    public  static void twoResultToRedis (String p_strFileName) throws Exception{
        //读取文件内容
        File l_file = new File(p_strFileName);
        BufferedReader l_reader = null;

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            String l_strtemp;
            String l_strKeyInRedis,l_strValueInRedis;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                String[] l_strLines = l_strtemp.split(",");
                if (l_strLines.length < 4)
                    continue;

                l_strKeyInRedis = l_strLines[0] + "-" + l_strLines[1];
                l_strValueInRedis = l_strLines[2] + "|" + l_strLines[3];

                int l_iCharKey = l_strLines[0].charAt(0) - 'a';
                int l_iMod = l_iCharKey%3;
                if (l_iMod == 1) {
                    Jedis jedis = RedisUtil.getInstance().getResource(1);
                    jedis.set(l_strKeyInRedis, l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 1);
                } else if (l_iMod == 2) {
                    Jedis jedis = RedisUtil.getInstance().getResource(2);
                    jedis.set(l_strKeyInRedis, l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 2);
                } else {
                    Jedis jedis = RedisUtil.getInstance().getResource(3);
                    jedis.set(l_strKeyInRedis, l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 3);
                }
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

    //用户间亲密度入库
    public  static void twoTmpToRedis (String p_strFileName) throws Exception{
        //读取文件内容
        File l_file = new File(p_strFileName);
        BufferedReader l_reader = null;

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            String l_strtemp;
            String l_strValueInRedis;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                String[] l_strLines = l_strtemp.split(",");
                if (l_strLines.length < 6)
                    continue;

                int l_iCharKey = l_strLines[0].charAt(0) - 'a';
                int l_iMod = l_iCharKey%3;
                if (l_iMod == 1) {
                    Jedis jedis = RedisUtil.getInstance().getResource(1);
                    l_strValueInRedis = jedis.get(l_strLines[0]);
                    RedisUtil.getInstance().returnResource(jedis, 1);
                } else if (l_iMod == 2) {
                    Jedis jedis = RedisUtil.getInstance().getResource(2);
                    l_strValueInRedis = jedis.get(l_strLines[0]);
                    RedisUtil.getInstance().returnResource(jedis, 2);
                } else {
                    Jedis jedis = RedisUtil.getInstance().getResource(3);
                    l_strValueInRedis = jedis.get(l_strLines[0]);
                    RedisUtil.getInstance().returnResource(jedis, 3);
                }

                String[] l_strValueLines = l_strValueInRedis.split("\\|");
                if (l_strValueLines.length < 7)
                    continue;

                int l_iOne = Integer.parseInt(l_strValueLines[2]) + Integer.parseInt(l_strLines[1]);
                int l_iTwo = Integer.parseInt(l_strValueLines[3]) + Integer.parseInt(l_strLines[2]);
                int l_iThree = Integer.parseInt(l_strValueLines[4]) + Integer.parseInt(l_strLines[3]);
                int l_iFour = Integer.parseInt(l_strValueLines[5]) + Integer.parseInt(l_strLines[4]);
                int l_iFive = Integer.parseInt(l_strValueLines[6]) + Integer.parseInt(l_strLines[5]);

                l_strValueInRedis = l_strValueLines[0] + "|" + l_strValueLines[1] + "|" + l_iOne + "|" + l_iTwo +
                        "|" + l_iThree + "|" + l_iFour + "|" + l_iFive;

                if (l_iMod == 1) {
                    Jedis jedis = RedisUtil.getInstance().getResource(1);
                    jedis.set(l_strLines[0], l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 1);
                } else if (l_iMod == 2) {
                    Jedis jedis = RedisUtil.getInstance().getResource(2);
                    jedis.set(l_strLines[0], l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 2);
                } else {
                    Jedis jedis = RedisUtil.getInstance().getResource(3);
                    jedis.set(l_strLines[0], l_strValueInRedis);
                    RedisUtil.getInstance().returnResource(jedis, 3);
                }
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

    //hlr信息人库
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

    /*
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
    */
}
