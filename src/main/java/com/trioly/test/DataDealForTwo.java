package com.trioly.test;

import java.io.*;
import java.lang.*;
import java.util.*;
//import java.security.*;

public class DataDealForTwo {
    private static Map<String, String> g_map;
    private static Map<String, String> g_mapRelationNum;
    private static String g_strFileTwoTmpFile;

    public static void main(String[] args) throws Exception{
        String l_strFileName = args[0];

        g_map = InitData.initMap();
        g_mapRelationNum = new HashMap<String, String>(20480);

        dealUser(l_strFileName);
    }

    //处理用户间通话记录
    public  static void dealUser (String p_strFileName) throws Exception{
        System.out.println("Deal Two:" + p_strFileName + " begin");

        //读取文件内容
        File l_file = new File(p_strFileName);
        File l_fileResult = new File(p_strFileName + ".TwoResult");
        g_strFileTwoTmpFile = p_strFileName + ".TwoTmp";
        BufferedReader l_reader = null;
        BufferedWriter l_writer = null;

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            l_writer = new BufferedWriter(new FileWriter(l_fileResult));
            String l_strtemp;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                // 处理一行数据
                String l_strLineResult = dealLineOne(l_strtemp);
                // 结果写入文件
                if (l_strLineResult.length() > 0)
                    l_writer.write(l_strLineResult);
            }
            l_reader.close();
            l_writer.close();

            //MAP中最后记录写入文件
            writeRelationNum();

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

            if (l_writer != null) {
                try {
                    l_writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        System.out.println("Deal Two:" + p_strFileName + " end");
    }

    //处理单行用户间记录
    public  static String dealLineOne (String p_strLine) throws Exception{
        String l_str1;
        String l_str2;
        String l_str3;

        String[] l_strLines = p_strLine.split(",");

        if (l_strLines.length < 6)
            return "";

        //移除主号码非浙江移动号码
        if (32 != l_strLines[0].length()) {
            return "";
        }
        //if (!g_map.containsKey(l_strLines[0].substring(0,7))) {
        //    return "";
        //}

        //计算总次数
        int l_iTmp = Integer.parseInt(l_strLines[2]) + Integer.parseInt(l_strLines[3]);

        //对号码的各级亲密度累加
        sumRelationNum(l_strLines[0], l_iTmp);

        //用户间默认为一星，所以一星用户不需要存储
        if (l_iTmp < 5) {
            return "";
        }

        //移除副号码非浙江移动号码
        if (32 != l_strLines[1].length()) {
            return "";
        }
        //if (!g_map.containsKey(l_strLines[1].substring(0,7))) {
        //    return "";
        //}

        //2个号码间重复记录只保留一个
        if (0 < l_strLines[0].compareTo(l_strLines[1])) {
            return "";
        }

        //手机号码加密
        //l_str1 = token(l_strLines[0]);
        l_str1 = l_strLines[0];

        //对端手机号码加密
        //l_str2 = token(l_strLines[1]);
        l_str2 = l_strLines[1];

        //亲密度
        if (l_iTmp < 5)
            l_str3 = "1";
        else if (l_iTmp < 20)
            l_str3 = "2";
        else if (l_iTmp < 40)
            l_str3 = "3";
        else if (l_iTmp < 60)
            l_str3 = "4";
        else
            l_str3 = "5";

        double l_dZJ = Double.parseDouble(l_strLines[2]);
        double l_dBJ = Double.parseDouble(l_strLines[3]);
        double l_dDB= (l_dZJ/(l_dZJ+l_dBJ))*100;
        int l_iDB = (int)l_dDB;

        return l_str1 + "," + l_str2 + "," + l_str3 + "," + l_iDB + "\n";
    }

    public  static void sumRelationNum (String p_strPhoneNum, int p_iRelationNum) {
        String l_strValue;
        int l_i1, l_i2, l_i3, l_i4, l_i5;

        if (g_mapRelationNum.containsKey(p_strPhoneNum)) {

            l_strValue = g_mapRelationNum.get(p_strPhoneNum);
            String[] l_strNums = l_strValue.split(",");
            if (l_strNums.length < 5) {
                return;
            }

            l_i1 = Integer.parseInt(l_strNums[0]);
            l_i2 = Integer.parseInt(l_strNums[1]);
            l_i3 = Integer.parseInt(l_strNums[2]);
            l_i4 = Integer.parseInt(l_strNums[3]);
            l_i5 = Integer.parseInt(l_strNums[4]);

            if (p_iRelationNum < 5)
                l_i1 = l_i1 + 1;
            else if (p_iRelationNum < 20)
                l_i2 = l_i2 + 1;
            else if (p_iRelationNum < 40)
                l_i3 = l_i3 + 1;
            else if (p_iRelationNum < 60)
                l_i4 = l_i4 + 1;
            else
                l_i5 = l_i5 + 1;

            g_mapRelationNum.put(p_strPhoneNum, "" + l_i1 + "," + l_i2 + "," + l_i3 + "," + l_i4 + "," + l_i5);
        } else {
            l_i1 = 0;
            l_i2 = 0;
            l_i3 = 0;
            l_i4 = 0;
            l_i5 = 0;

            if (p_iRelationNum < 5)
                l_i1 = l_i1 + 1;
            else if (p_iRelationNum < 20)
                l_i2 = l_i2 + 1;
            else if (p_iRelationNum < 40)
                l_i3 = l_i3 + 1;
            else if (p_iRelationNum < 60)
                l_i4 = l_i4 + 1;
            else
                l_i5 = l_i5 + 1;

            g_mapRelationNum.put(p_strPhoneNum, "" + l_i1 + "," + l_i2 + "," + l_i3 + "," + l_i4 + "," + l_i5);

            //容量大于14000条写文件
            if (g_mapRelationNum.size() > 14000)
                writeRelationNum();
        }
    }

    public  static void writeRelationNum () {
        //读取文件内容
        BufferedWriter l_writer = null;

        try {
            l_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(g_strFileTwoTmpFile, true)));
            for (Map.Entry<String, String> entry: g_mapRelationNum.entrySet()) {
                l_writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            l_writer.close();

            //清空MAP
            g_mapRelationNum.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (l_writer != null) {
                try {
                    l_writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //对号码加密
    /*
    public  static String token (String p_strPhoneNum) throws Exception{
        String l_strTmp = md5(p_strPhoneNum + "123456");
        String l_strMD5 = md5(l_strTmp);
        String l_strSalt = l_strMD5.substring(l_strMD5.length()-5, l_strMD5.length());

        String l_strFirst = l_strSalt.substring(0, 1) + p_strPhoneNum.substring(0, 1) +
                l_strSalt.substring(1, 2) + p_strPhoneNum.substring(1, 3) +
                l_strSalt.substring(2, 3) + p_strPhoneNum.substring(3, 6) +
                l_strSalt.substring(3, 4) + p_strPhoneNum.substring(6, 10) +
                l_strSalt.substring(4, 5) + p_strPhoneNum.substring(10, 11);
        String l_strSecond = l_strFirst.substring(8, 12) + l_strFirst.substring(0, 4) +
                l_strFirst.substring(12, 16) + l_strFirst.substring(4, 8);
        String l_strLast = l_strSecond.substring(0, 1) + l_strMD5.substring(0, 2) +
                l_strSecond.substring(1, 2) + l_strMD5.substring(2, 4) +
                l_strSecond.substring(2, 3) + l_strMD5.substring(4, 6) +
                l_strSecond.substring(3, 4) + l_strMD5.substring(6, 8) +
                l_strSecond.substring(4, 5) + l_strMD5.substring(8, 10) +
                l_strSecond.substring(5, 6) + l_strMD5.substring(10, 12) +
                l_strSecond.substring(6, 7) + l_strMD5.substring(12, 14) +
                l_strSecond.substring(7, 8) + l_strMD5.substring(14, 16) +
                l_strMD5.substring(16, 18) + l_strSecond.substring(8, 9) +
                l_strMD5.substring(18, 20) + l_strSecond.substring(9, 10) +
                l_strMD5.substring(20, 22) + l_strSecond.substring(10, 11) +
                l_strMD5.substring(22, 24) + l_strSecond.substring(11, 12) +
                l_strMD5.substring(24, 26) + l_strSecond.substring(12, 13) +
                l_strMD5.substring(26, 28) + l_strSecond.substring(13, 14) +
                l_strMD5.substring(28, 30) + l_strSecond.substring(14, 15) +
                l_strMD5.substring(30, 32) + l_strSecond.substring(15, 16);

        return l_strLast;
    }

    //对号码求MD5
    public static String md5(String p_strParam){
        String l_strReturn = null;

        try {
            MessageDigest l_md5 = MessageDigest.getInstance("MD5");
            byte[] l_bytes = l_md5.digest(p_strParam.getBytes());
            StringBuffer l_strBuffer = new StringBuffer();
            for (byte b : l_bytes){
                int bt = b&0xff;
                if (bt < 16){
                    l_strBuffer.append(0);
                }
                l_strBuffer.append(Integer.toHexString(bt));
            }
            l_strReturn = l_strBuffer.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return l_strReturn;
    }
    */
}
