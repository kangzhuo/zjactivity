package com.trioly.test;

import java.io.*;
import java.sql.*;
import java.util.Map;

public class DataDealToDB {
    private static Connection g_conn;
    private static Map<String, String> g_map;

    public static void main(String[] args) throws Exception{
        String l_strFileName = args[0];

        g_map = InitData.initRMZSMap();

        try{
            //连接MySql数据库
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //String url = "jdbc:mysql://localhost:3306/sdk?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true" ;
            //String username = "root" ;
            //String password = "" ;
            Map<String, String> l_map = InitData.getDBUrl();
            String url = l_map.get("url");
            String username = l_map.get("username");
            String password = l_map.get("password");
            g_conn = DriverManager.getConnection(url , username , password ) ;
        }catch(SQLException se){
            System.out.println("数据库连接失败！");
            se.printStackTrace() ;
        }

        if (l_strFileName.contains("OneResult")) {
            oneResultToDB(l_strFileName);
        } else if (l_strFileName.contains("TwoResult")) {
            twoResultToDB(l_strFileName);
        } else if (l_strFileName.contains("TwoTmp")) {
            twoTmpToDB(l_strFileName);
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
    public  static void oneResultToDB (String p_strFileName) throws Exception{
        //读取文件内容
        File l_file = new File(p_strFileName);
        BufferedReader l_reader = null;

        String l_strSql = "insert into one_result_1 (bill_id, rmzs, pm) values (?, ?, ?)";
        PreparedStatement l_stmt = g_conn.prepareStatement(l_strSql);

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            String l_strtemp;
            String l_strValue;
            int l_iCount = 1;
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

                l_stmt.setString(1, l_strLines[0]);
                l_stmt.setString(2, l_strLines[1]);
                l_stmt.setString(3, l_strValue);
                l_stmt.executeUpdate();

                l_iCount = l_iCount + 1;
                if (10000 == l_iCount) {
                    //g_conn.commit();
                    l_iCount = 1;
                }
            }
            l_reader.close();

            try {
                l_stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

            if(g_conn != null){
                try{
                    g_conn.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
    }

    //用户间亲密度入库
    public  static void twoResultToDB (String p_strFileName) throws Exception{
        //读取文件内容
        File l_file = new File(p_strFileName);
        BufferedReader l_reader = null;

        String l_strSql = "insert into two_result (bill_id, target_bill_id, qmd, db) values (?, ?, ?, ?)";
        PreparedStatement l_stmt = g_conn.prepareStatement(l_strSql);

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            String l_strtemp;
            int l_iCount = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                String[] l_strLines = l_strtemp.split(",");
                if (l_strLines.length < 4)
                    continue;

                l_stmt.setString(1, l_strLines[0]);
                l_stmt.setString(2, l_strLines[1]);
                l_stmt.setString(3, l_strLines[2]);
                l_stmt.setString(4, l_strLines[3]);
                l_stmt.executeUpdate();

                l_iCount = l_iCount + 1;
                if (10000 == l_iCount) {
                    //g_conn.commit();
                    l_iCount = 1;
                }
            }
            l_reader.close();

            try {
                l_stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

            if(g_conn != null){
                try{
                    g_conn.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
    }

    //用户亲密度分布入库
    public  static void twoTmpToDB (String p_strFileName) throws Exception{
        //读取文件内容
        File l_file = new File(p_strFileName);
        BufferedReader l_reader = null;

        String l_strSqlQry = "select * from one_result_2 where bill_id = ?";
        String l_strSqlUpd = "update one_result_2 set one=?, two=?, three=?, four=?, five=? where bill_id=?";
        String l_strSqlIns = "insert into one_result_2 (bill_id, one, two, three, four, five) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement l_stmtQry = g_conn.prepareStatement(l_strSqlQry);
        PreparedStatement l_stmtUpd = g_conn.prepareStatement(l_strSqlUpd);
        PreparedStatement l_stmtIns = g_conn.prepareStatement(l_strSqlIns);

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            String l_strtemp;
            int l_iCount = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                String[] l_strLines = l_strtemp.split(",");
                if (l_strLines.length < 6)
                    continue;

                l_stmtQry.setString(1, l_strLines[0]);
                ResultSet rs = l_stmtQry.executeQuery();
                if (rs.next()) {
                    int l_iOne = rs.getInt("one");
                    int l_iTwo = rs.getInt("two");
                    int l_iThree = rs.getInt("three");
                    int l_iFour = rs.getInt("four");
                    int l_iFive = rs.getInt("five");
                    int l_iOneTmp = l_iOne + Integer.parseInt(l_strLines[1]);
                    int l_iTwoTmp = l_iTwo + Integer.parseInt(l_strLines[2]);
                    int l_iThreeTmp = l_iThree + Integer.parseInt(l_strLines[3]);
                    int l_iFourTmp = l_iFour + Integer.parseInt(l_strLines[4]);
                    int l_iFiveTmp = l_iFive + Integer.parseInt(l_strLines[5]);

                    l_stmtUpd.setInt(1, l_iOneTmp);
                    l_stmtUpd.setInt(2, l_iTwoTmp);
                    l_stmtUpd.setInt(3, l_iThreeTmp);
                    l_stmtUpd.setInt(4, l_iFourTmp);
                    l_stmtUpd.setInt(5, l_iFiveTmp);
                    l_stmtUpd.setString(6, l_strLines[0]);
                    l_stmtUpd.executeUpdate();
                } else {
                    l_stmtIns.setString(1, l_strLines[0]);
                    l_stmtIns.setString(2, l_strLines[1]);
                    l_stmtIns.setString(3, l_strLines[2]);
                    l_stmtIns.setString(4, l_strLines[3]);
                    l_stmtIns.setString(5, l_strLines[4]);
                    l_stmtIns.setString(6, l_strLines[5]);
                    l_stmtIns.executeUpdate();
                }

                try{
                    rs.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }

                l_iCount = l_iCount + 1;
                if (10000 == l_iCount) {
                    //g_conn.commit();
                    l_iCount = 1;
                }
            }
            l_reader.close();

            try {
                l_stmtQry.close();
                l_stmtUpd.close();
                l_stmtIns.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

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

            if(g_conn != null){
                try{
                    g_conn.close() ;
                }catch(SQLException e){
                    e.printStackTrace() ;
                }
            }
        }
    }
}
