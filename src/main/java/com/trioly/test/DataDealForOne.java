package com.trioly.test;

import java.io.*;
import java.util.*;

public class DataDealForOne {
    private static Map<String, String> g_map;
    private static Map<Long, Integer> g_mapDistr;
    private static String g_strFileOneTmpFile;

    public static void main(String[] args) throws Exception{
        String l_strFileName = args[0];

        String l_llAverCS = args[1];
        String l_llAverSC = args[2];
        String l_llAverRS = args[3];
        String l_llAverSS = args[4];

        g_map = InitData.initMap();
        g_mapDistr = new HashMap<Long, Integer>();

        //计算平均值
        dealUser(l_strFileName, l_llAverCS, l_llAverSC, l_llAverRS, l_llAverSS);
    }

    //处理用户间通话记录
    public  static void dealUser (String p_strFileName, String p_llAverCS, String p_llAverSC, String p_llAverRS, String p_llAverSS) throws Exception{
        System.out.println("Deal One:" + p_strFileName + " begin");

        //读取文件内容
        File l_file = new File(p_strFileName);
        File l_fileResult = new File(p_strFileName + ".OneResult");
        g_strFileOneTmpFile = p_strFileName + ".OneTmp";
        BufferedReader l_reader = null;
        BufferedWriter l_writer = null;

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            l_writer = new BufferedWriter(new FileWriter(l_fileResult));
            String l_strtemp;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                // 处理一行数据
                String l_strLineResult = dealLineOne(l_strtemp, p_llAverCS, p_llAverSC, p_llAverRS, p_llAverSS);
                // 结果写入文件
                if (l_strLineResult.length() > 0)
                    l_writer.write(l_strLineResult);
            }
            l_reader.close();
            l_writer.close();

            //MAP中指数分布写入文件
            writeDistrNum();

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

        System.out.println("Deal One:" + p_strFileName + " end");
    }

    //处理单行用户间记录
    public  static String dealLineOne (String p_strLine, String p_llAverCS, String p_llAverSC, String p_llAverRS, String p_llAverSS) throws Exception{
        String l_str1;
        String l_str2;

        String[] l_strLines = p_strLine.split(",");

        if (l_strLines.length < 12)
            return "";

        //移除主号码非浙江移动号码
        if (32 != l_strLines[0].length()) {
            return "";
        }
        //if (!g_map.containsKey(l_strLines[0].substring(0,7))) {
        //    return "";
        //}

        //转换数值
        double l_llSumCS = Long.parseLong(l_strLines[1]);
        double l_llSumSC = Long.parseLong(l_strLines[4]);
        double l_llSumRS = Long.parseLong(l_strLines[7]);
        double l_llSumSS = Long.parseLong(l_strLines[10]) + Long.parseLong(l_strLines[11])*2;
        double l_llAveCS = Long.parseLong(p_llAverCS);
        double l_llAveSC = Long.parseLong(p_llAverSC);
        double l_llAveRS = Long.parseLong(p_llAverRS);
        double l_llAveSS = Long.parseLong(p_llAverSS);

        //计算指数
        double l_dResult = (l_llSumCS/l_llAveCS)*18 + (l_llSumSC/l_llAveSC)*18 + (l_llSumRS/l_llAveRS)*18 + (l_llSumSS/l_llAveSS)*6;
        long l_llResult = Math.round(l_dResult);

        //手机号码加密
        //l_str1 = token(l_strLines[0]);
        l_str1 = l_strLines[0];

        //指数
        l_str2 = String.valueOf(l_llResult);

        //记录指数分部
        if (g_mapDistr.containsKey(l_llResult)) {
            int l_iValue = g_mapDistr.get(l_llResult);
            l_iValue = l_iValue + 1;
            g_mapDistr.put(l_llResult, l_iValue);
        } else {
            g_mapDistr.put(l_llResult, 1);
        }

        return l_str1 + "," + l_str2 + "\n";
    }

    public  static void writeDistrNum () {
        //读取文件内容
        File l_file = new File(g_strFileOneTmpFile);
        BufferedWriter l_writer = null;

        try {
            l_writer = new BufferedWriter(new FileWriter(l_file));
            for (Map.Entry<Long, Integer> entry: g_mapDistr.entrySet()) {
                l_writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
            l_writer.close();
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
}
