package com.trioly.test;

import sun.misc.*;
import java.io.*;
import java.net.*;

/**
 * Created by kangzhuo on 15/12/9.
 */
public class XmlCreateTest {
    private static String g_strAccount = "dx711B300201";
    private static String g_strPassword = "Db300201";
    private static String g_strIp = "121.43.106.10";
    private static String g_strName = "";
    private static String g_strAcctSeq = "";

    public static void main(String[] args) throws Exception{
        sendMsg();
    }

    /**
     *
     */
    public static void sendMsg() throws Exception {
        String g_strAccount = "dx711B300201";
        String g_strPassword = "Db300201";
        String g_strIp = "121.43.106.10";
        String g_strName = "";
        String g_strAcctSeq = "";

        String l_strPass64 = new BASE64Encoder().encode(g_strPassword.getBytes("utf-8"));
        System.out.println("base64:" + l_strPass64);

        String l_url1 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/login/login3!passwordCheck.action";
        String l_postdata1 = "ComputerName=&UserName=&ipAddress=" + g_strIp + "&macAddress=&cpuSerial=&fingerFlag=1&appId=10&url=http%3A%2F%2Fccsct.chnl.zj.chinamobile.com&channeltype=&acctSeq=&appAcctId=&appAcctName=&appCode=&loginName=" + g_strAccount + "&loginPassword=" + l_strPass64;
        String l_referer1 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/local/zhejiang/login.jsp?redirectUrl=http%3A%2F%2Fccsct.chnl.zj.chinamobile.com%3A80%2Fbusiness%3Fservice%3Dpage%2Fbusiness%26menuType%3D2%26isTest%3D1&resKey=10";
        //String l_callback1 = sendHttpMsg(l_url1, l_postdata1.getBytes("utf-8"), l_referer1);
        //System.out.println("result1:" + l_callback1);


        String l_url2 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/zjzdsso/appLocalQuery3!isFrequentlyLogin.action";
        String l_postdata2 = "loginName=" + g_strAccount;
        String l_referer2 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/local/zhejiang/login.jsp?redirectUrl=http%3A%2F%2Fccsct.chnl.zj.chinamobile.com%3A80%2Fbusiness%3Fservice%3Dpage%2Fbusiness%26menuType%3D2%26isTest%3D1&resKey=10";
        //String l_callback2 = sendHttpMsg(l_url2, l_postdata2.getBytes("utf-8"), l_referer2);
        //System.out.println("result2:" + l_callback2);

        String l_url3 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/login/login3!login.action";
        String l_postdata3 = "ComputerName=&UserName=&ipAddress=" + g_strIp + "&macAddress=&cpuSerial=&fingerFlag=1&appId=10&url=http%3A%2F%2Fccsct.chnl.zj.chinamobile.com&channeltype=&acctSeq=&appAcctId=&appAcctName=&appCode=";
        String l_referer3 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/local/zhejiang/login.jsp?redirectUrl=http%3A%2F%2Fccsct.chnl.zj.chinamobile.com%3A80%2Fbusiness%3Fservice%3Dpage%2Fbusiness%26menuType%3D2%26isTest%3D1&resKey=10";
        //String l_callback3 = sendHttpMsg(l_url3, l_postdata3.getBytes("utf-8"), l_referer3);
        //System.out.println("result3:" + l_callback3);

        String l_url4 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/zjzdsso/appLocalQuery3!getAppAcctByMainAcctAndAppId.action";
        String l_postdata4 = "appId=10";
        String l_referer4 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/local/zhejiang/login.jsp?redirectUrl=http%3A%2F%2Fccsct.chnl.zj.chinamobile.com%3A80%2Fbusiness%3Fservice%3Dpage%2Fbusiness%26menuType%3D2%26isTest%3D1&resKey=10";
        //String l_callback4 = sendHttpMsg(l_url4, l_postdata4.getBytes("utf-8"), l_referer4);
        //System.out.println("result4:" + l_callback4);

        g_strName = "";
        g_strAcctSeq = "";

        String l_url5 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/resource/app/appRes3!accessAppRes.action";
        String l_postdata5 = "ComputerName=&UserName=&ipAddress=" + g_strIp + "&macAddress=&cpuSerial=&fingerFlag=1&appId=10&url=http%3A%2F%2Fccsct.chnl.zj.chinamobile.com&channeltype=&acctSeq=" + g_strAcctSeq + "&appAcctId=" + g_strAccount + "&appAcctName=" + URLDecoder.decode(g_strName, "utf-8") + "&appCode=CRM";
        String l_referer5 = "http://4assoct.chnl.zj.chinamobile.com/uac/web3/jsp/local/zhejiang/login.jsp?redirectUrl=http%3A%2F%2Fccsct.chnl.zj.chinamobile.com%3A80%2Fbusiness%3Fservice%3Dpage%2Fbusiness%26menuType%3D2%26isTest%3D1&resKey=10";
        //String l_callback5 = sendHttpMsg(l_url5, l_postdata5.getBytes("utf-8"), l_referer5);
        //System.out.println("result5:" + l_callback5);

        String l_url6 = "http://ccsct.chnl.zj.chinamobile.com/business?service=ajax&page=CommonPage&listener=execute&uid=c0001WS&logSerialId=&billId=" + tel + "&_=" + 返回随机小数(15);
        String l_postdata6 = "";
        String l_referer6 = "";
        String l_callback6 = sendHttpMsg(l_url6, l_postdata6.getBytes("utf-8"), l_referer6);
        System.out.println("result6:" + l_callback6);
    }

        private static String sendHttpMsg(String p_strUrl, byte[] p_postData, String p_strReferer) throws Exception {
        URL l_url = new URL(p_strUrl);
        HttpURLConnection l_httpConn = (HttpURLConnection) l_url.openConnection();

        l_httpConn.setConnectTimeout(5000);
        l_httpConn.setReadTimeout(5000);
        l_httpConn.setRequestMethod("POST");
        l_httpConn.setDoOutput(true);
        l_httpConn.setDoInput(true);

        if (p_strReferer.length() > 0) {
            l_httpConn.setRequestProperty("referer", p_strReferer);
        }

        if (p_postData.length > 0) {
            OutputStream out = l_httpConn.getOutputStream();
            out.write(p_postData);
            out.close();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(l_httpConn.getInputStream(), "utf-8"));
        String l_strResponse = "", l_strTmp;
        while ((l_strTmp = in.readLine()) != null)
            l_strResponse +=  l_strTmp;
        in.close();

        return l_strResponse;
    }
}
