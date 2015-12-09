package com.trioly.test;

import java.io.*;
import java.util.*;
import java.lang.*;

public class InitData {

    public static Map<String, String> initMap() throws Exception {

        String l_strHlrFile = "";
        Map<String, String> l_map = new HashMap<String, String>();
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
                l_map.put(l_strtemp, "");
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
        }

        return l_map;
    }

    public static Map<String, String> initRMZSMap() throws Exception {

        String l_strRMZSFile = "";
        Map<String, String> l_map = new HashMap<String, String>();
        //读取全局配置文件内容
        File l_file = new File(System.getProperty("user.home") + "/config/DataDeal.conf");
        BufferedReader l_reader = null;

        try {
            l_reader = new BufferedReader(new FileReader(l_file));
            String l_strtemp;
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                if (l_strtemp.contains("RMZSFile:")) {
                    l_strRMZSFile = l_strtemp.substring(8, l_strtemp.length());
                    l_strRMZSFile = l_strRMZSFile.trim();
                }
            }
            l_reader.close();

            l_reader = new BufferedReader(new FileReader(l_strRMZSFile));
            // 一次读入一行，直到读入null为文件结束
            while ((l_strtemp = l_reader.readLine()) != null) {
                String[] l_strLines = l_strtemp.split(",");
                if (l_strLines.length < 2)
                    continue;

                l_map.put(l_strLines[0], l_strLines[1]);
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
        }

        return l_map;
    }
}
