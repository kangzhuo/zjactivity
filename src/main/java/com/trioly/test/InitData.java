package com.trioly.test;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;
import java.util.*;
import java.lang.*;

public class InitData {

    private static String g_strKey = "trioly.crypt.key";

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
                    l_strRMZSFile = l_strtemp.substring(9, l_strtemp.length());
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

        return l_map;
    }

    public static Map<String, String> getDBUrl() throws Exception {

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
                if (l_strtemp.contains("url:")) {
                    String l_strValue = l_strtemp.substring(4, l_strtemp.length());
                    l_strValue = l_strValue.trim();
                    l_map.put("url", l_strValue);
                }

                if (l_strtemp.contains("username:")) {
                    String l_strValue = l_strtemp.substring(9, l_strtemp.length());
                    l_strValue = l_strValue.trim();
                    l_map.put("username", l_strValue);
                }

                if (l_strtemp.contains("password:")) {
                    String l_strValue = l_strtemp.substring(9, l_strtemp.length());
                    l_strValue = l_strValue.trim();
                    l_strValue = new String(decrypt(String2byte(l_strValue.getBytes()), g_strKey.getBytes()));
                    l_map.put("password", l_strValue);
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

        return l_map;
    }

    //加密
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");

        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        // 执行加密操作
        return cipher.doFinal(src);
    }

    //解密
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        // 正式执行解密操作
        return cipher.doFinal(src);
    }

    private static String byte2String(byte[] b) {
        String hs="";
        String stmp="";
        for(int n=0;n<b.length;n++){
            stmp=(java.lang.Integer.toHexString(b[n]&0XFF));
            if(stmp.length() == 1)
                hs+=hs+"0"+stmp;
            else
                hs=hs+stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] String2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static void main(String[] args) throws Exception{
        String l_strInput = "";
        if (args.length > 0)
            l_strInput = args[0];
        String l_strCrypt = byte2String(encrypt(l_strInput.getBytes(), g_strKey.getBytes()));
        System.out.println("encrypt key :" + l_strCrypt);
    }
}

