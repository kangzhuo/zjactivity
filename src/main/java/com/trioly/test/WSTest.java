package com.trioly.test;

import org.apache.ibatis.io.Resources;

import java.io.*;
import java.net.*;

/**
 * Created by kangzhuo on 15/12/9.
 */
public class WSTest {
    public static void main(String[] args) throws Exception{
        sendSms();
    }

    /**
     *
     */
    public static void sendSms() throws Exception {
        //String l_strBillId = "18858100583";//qq号码
        //String l_strCode = "123456";//qq号码
        String urlString = "http://10.70.181.7:8080/MSP/services/MSPWebService?wsdl";
        //String xml = WSTest.class.getClassLoader().getResource("/SendInstantSms.xml").getFile();
        //String xml = Resources.getResourceAsFile("SendInstantSms.xml").getPath();
        //String xmlFileTmp=replace(xml, "$BILLID", l_strBillId).getPath();
        //String xmlFile=replace(xmlFileTmp, "$CODE", l_strCode).getPath();
        //String soapActionString = "http://WebXml.com.cn/qqCheckOnline";
        URL url = new URL(urlString);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        System.out.println("http conn ok");

        //File fileToSend = new File(xmlFile);
        String l_strSend = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <soapenv:Body>\n" +
                "        <addTask xmlns=\"http://server.mmap.msp.huawei.com\">\n" +
                "            <in0>\n" +
                "                <ns1:content xmlns:ns1=\"http://domain.server.mmap.msp.huawei.com\">CODE~123456</ns1:content>\n" +
                "                <ns2:mediaType xmlns:ns2=\"http://domain.server.mmap.msp.huawei.com\">1014001</ns2:mediaType>\n" +
                "                <ns3:receiverInfo xmlns:ns3=\"http://domain.server.mmap.msp.huawei.com\">18858100583</ns3:receiverInfo>\n" +
                "                <ns4:reserve21 xmlns:ns4=\"http://domain.server.mmap.msp.huawei.com\">0019</ns4:reserve21>\n" +
                "                <ns5:reserve22 xmlns:ns5=\"http://domain.server.mmap.msp.huawei.com\">手机营业厅平台</ns5:reserve22>\n" +
                "                <ns6:reserve23 xmlns:ns6=\"http://domain.server.mmap.msp.huawei.com\">00190001</ns6:reserve23>\n" +
                "                <ns7:reserve24 xmlns:ns7=\"http://domain.server.mmap.msp.huawei.com\">临时密码类</ns7:reserve24>\n" +
                "                <ns8:reserve25 xmlns:ns8=\"http://domain.server.mmap.msp.huawei.com\">1449454994539</ns8:reserve25>\n" +
                "                <ns9:schduleTime xmlns:ns9=\"http://domain.server.mmap.msp.huawei.com\">2015-12-09T11:00:16.937Z</ns9:schduleTime>\n" +
                "                <ns10:sendNo xmlns:ns10=\"http://domain.server.mmap.msp.huawei.com\">10086</ns10:sendNo>\n" +
                "            </in0>\n" +
                "        </addTask>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        //new FileInputStream(xmlFile).read(buf);
        byte[] buf = l_strSend.getBytes();

        System.out.println("read ok");

        httpConn.setRequestProperty("Content-Length", String.valueOf(buf.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        //httpConn.setRequestProperty("soapActionString", soapActionString);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();

        out.write(buf);
        out.close();

        System.out.println("out ok");

        byte[] datas=readInputStream(httpConn.getInputStream());
        String result=new String(datas);
        //打印返回结果
        System.out.println("result:" + result);
    }

    /**
     * 文件内容替换
     *
     * @param inFileName 源文件
     * @param from
     * @param to
     * @return 返回替换后文件
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static File replace(String inFileName, String from, String to)
            throws IOException, UnsupportedEncodingException {
        File inFile = new File(inFileName);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(inFile), "utf-8"));
        File outFile = new File(inFile + ".tmp");
        PrintWriter out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outFile), "utf-8")));
        String reading;
        while ((reading = in.readLine()) != null) {
            out.println(reading.replaceAll(from, to));
        }
        out.close();
        in.close();
        //infile.delete(); //删除源文件
        //outfile.renameTo(infile); //对临时文件重命名
        return outFile;
    }

    /**
     * 从输入流中读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len = inStream.read(buffer)) !=-1 ){
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }
}
