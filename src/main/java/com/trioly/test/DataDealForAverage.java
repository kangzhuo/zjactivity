package com.trioly.test;

import java.io.*;

public class DataDealForAverage {
    private static long g_llNum;
    private static long g_llSumCS; //汇总通话次数
    private static long g_llSumSC; //汇总通话时长
    private static long g_llSumRS; //汇总通话人数
    private static long g_llSumSS; //汇总通话省数

    public static void main(String[] args) throws Exception{
        String l_strFileName = args[0];

        dealUser(l_strFileName);
    }

    //处理用户间通话记录
    public  static void dealUser (String p_strFileName) throws Exception{
        System.out.println("Deal average:" + p_strFileName + " begin");

        //读取文件内容
        File l_file = new File(p_strFileName);
        File l_fileResult = new File(p_strFileName + ".average");
        BufferedReader l_reader = null;
        BufferedWriter l_writer = null;

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            l_writer = new BufferedWriter(new FileWriter(l_fileResult));
            String l_strtemp;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
               dealLineOne(l_strtemp);
            }
            l_reader.close();

            l_writer.write("" + g_llNum + "," + g_llSumCS + "," + g_llSumSC + "," + g_llSumRS + "," + g_llSumSS + "\n");

            l_writer.flush();
            l_writer.close();

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

        System.out.println("Deal average:" + p_strFileName + " end");
    }

    //处理单行用户间记录
    public  static void dealLineOne (String p_strLine) throws Exception{
        String[] l_strLines = p_strLine.split(",");
        if (l_strLines.length < 12)
            return;

        g_llNum = g_llNum + 1;

        g_llSumCS = g_llSumCS + Integer.parseInt(l_strLines[1]);
        g_llSumSC = g_llSumSC + Integer.parseInt(l_strLines[4]);
        g_llSumRS = g_llSumRS + Integer.parseInt(l_strLines[7]);
        g_llSumSS = g_llSumSS + Integer.parseInt(l_strLines[10]) + Integer.parseInt(l_strLines[11])*2;
    }
}
