package com.trioly.test;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataCreate {
    private static Connection g_conn;

    public static void main(String[] args) throws Exception{
        String l_strFileName = args[0];

        try{
            //连接MySql数据库
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/sdk?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;transformedBitIsBoolean=true" ;
            String username = "root" ;
            String password = "" ;
            g_conn = DriverManager.getConnection(url , username , password ) ;
        }catch(SQLException se){
            System.out.println("数据库连接失败！");
            se.printStackTrace() ;
        }

        if (l_strFileName.contains("1")) {
            dataInsert();
        } else if(l_strFileName.contains("2")) {
            md5Data();
        } else {
            makeData1();
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
    public  static void dataInsert () throws Exception{
        String l_strHlrFile = "";
        //读取全局配置文件内容
        File l_file = new File(System.getProperty("user.home") + "/config/DataDeal.conf");
        BufferedReader l_reader = null;

        try {
            String l_strSql = "insert into data_inif_1 (bill_id) values (?)";
            PreparedStatement l_stmt = g_conn.prepareStatement(l_strSql);

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
            int l_iNum = 0;
            while ((l_strtemp = l_reader.readLine()) != null) {
                for (int i = 0; i < 10000; i++) {
                    String l_strTmp = "";
                    if (i <= 9)
                        l_strTmp = "000" + String.valueOf(i);
                    else if (i <= 99)
                        l_strTmp = "00" + String.valueOf(i);
                    else if (i <= 999)
                        l_strTmp = "0" + String.valueOf(i);
                    else
                        l_strTmp = String.valueOf(i);

                    l_strTmp = l_strtemp + l_strTmp;
                    l_stmt.setString(1, l_strTmp);
                    l_stmt.executeUpdate();
                }

                l_iNum ++;
                if (l_iNum > 600)
                    break;
            }
            l_reader.close();

            if (l_stmt != null) {   // 关闭声明
                try {
                    l_stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    //用户指数信息入库
    public  static void makeData1 () throws Exception{
        //读取文件内容
        File l_file1 = new File("/Users/kangzhuo/config/initData1.txt");
        File l_file2 = new File("/Users/kangzhuo/config/initData2.txt");
        BufferedWriter l_writer1 = null;
        BufferedWriter l_writer2 = null;

        try {
            l_writer1 = new BufferedWriter(new FileWriter(l_file1));
            l_writer2 = new BufferedWriter(new FileWriter(l_file2));

            int l_i1 = 1;
            int l_i2 = 2;
            int l_i3 = 3;
            int l_i4 = 4;
            int l_i5 = 5;

            String l_strSqlQry = "select * from sdk.data_inif_1";
            PreparedStatement l_stmtQry = g_conn.prepareStatement(l_strSqlQry);
            ResultSet rs = l_stmtQry.executeQuery();
            while (rs.next()) {
                String l_strBillId = rs.getString("bill_id");


                l_writer1.write(l_strBillId + "," + l_strBillId + "," + l_i1 + ",0,0," + l_i2 + ",0,0," + l_i3 + ",0,0," + l_i4 + "," + l_i5 + "\n");
                l_i1 = l_i1 + 1; if (l_i1 > 30) l_i1=1;
                l_i2 = l_i2 + 1; if (l_i2 > 1000) {l_i2=1;l_writer1.flush();l_writer2.flush();}
                l_i3 = l_i3 + 3; if (l_i3 > 100) l_i3=1;
                l_i4 = l_i4 + 4; if (l_i4 > 100) l_i4=1;
                l_i5 = l_i5 + 5; if (l_i5 > 100) l_i5=1;
                System.out.println("----il2:" + l_i2);

                l_strSqlQry = "select * from sdk.data_inif_1 limit 0,21";
                PreparedStatement l_stmtQry2 = g_conn.prepareStatement(l_strSqlQry);
                ResultSet rs2 = l_stmtQry2.executeQuery();
                int l_iNum = 0;
                while (rs2.next()) {
                    String l_strTargetBillId = rs2.getString("bill_id");
                    l_writer2.write(l_strBillId + "," + l_strTargetBillId + "," + l_i1 + "," + l_i2 + "," + l_i3 + "," + l_i4 + "\n");

                    l_iNum = l_iNum + 1;
                    if (l_iNum > 19)
                        break;
                }
                rs2.close();
                l_stmtQry2.close();
            }
            rs.close();
            l_stmtQry.close();

            l_writer1.flush();
            l_writer1.close();

            l_writer2.flush();
            l_writer2.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (l_writer1 != null) {
                try {
                    l_writer1.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (l_writer2 != null) {
                try {
                    l_writer2.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //用户指数信息入库
    public  static void md5Data () throws Exception{
        //读取文件内容
        File l_file1 = new File("/Users/kangzhuo/config/initData1.txt");
        File l_file2 = new File("/Users/kangzhuo/config/initData2.txt");
        BufferedWriter l_writer1 = null;
        BufferedWriter l_writer2 = null;

        try {
            l_writer1 = new BufferedWriter(new FileWriter(l_file1));
            l_writer2 = new BufferedWriter(new FileWriter(l_file2));

            int l_i1 = 1;
            int l_i2 = 2;
            int l_i3 = 3;
            int l_i4 = 4;
            int l_i5 = 5;

            String l_strSqlQry = "select * from sdk.data_inif_1";
            PreparedStatement l_stmtQry = g_conn.prepareStatement(l_strSqlQry);
            ResultSet rs = l_stmtQry.executeQuery();
            while (rs.next()) {
                String l_strBillId = rs.getString("bill_id");


                l_writer1.write(l_strBillId + "," + l_strBillId + "," + l_i1 + ",0,0," + l_i2 + ",0,0," + l_i3 + ",0,0," + l_i4 + "," + l_i5 + "\n");
                l_i1 = l_i1 + 1; if (l_i1 > 30) l_i1=1;
                l_i2 = l_i2 + 1; if (l_i2 > 1000) {l_i2=1;l_writer1.flush();l_writer2.flush();}
                l_i3 = l_i3 + 3; if (l_i3 > 100) l_i3=1;
                l_i4 = l_i4 + 4; if (l_i4 > 100) l_i4=1;
                l_i5 = l_i5 + 5; if (l_i5 > 100) l_i5=1;
                System.out.println("----il2:" + l_i2);

                l_strSqlQry = "select * from sdk.data_inif_1 limit 0,21";
                PreparedStatement l_stmtQry2 = g_conn.prepareStatement(l_strSqlQry);
                ResultSet rs2 = l_stmtQry2.executeQuery();
                int l_iNum = 0;
                while (rs2.next()) {
                    String l_strTargetBillId = rs2.getString("bill_id");
                    l_writer2.write(l_strBillId + "," + l_strTargetBillId + "," + l_i1 + "," + l_i2 + "," + l_i3 + "," + l_i4 + "\n");

                    l_iNum = l_iNum + 1;
                    if (l_iNum > 19)
                        break;
                }
                rs2.close();
                l_stmtQry2.close();
            }
            rs.close();
            l_stmtQry.close();

            l_writer1.flush();
            l_writer1.close();

            l_writer2.flush();
            l_writer2.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (l_writer1 != null) {
                try {
                    l_writer1.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (l_writer2 != null) {
                try {
                    l_writer2.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
