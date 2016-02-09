package com.trioly.test;

import com.td.sms.web.util.XmlUtil;

import java.util.HashMap;
import java.util.Map;

public class XmlCreateTest {
    public static void main(String[] args) {
        Map<String, String> l_xmlMap = new HashMap<>();
        String l_strTmp = XmlUtil.createXml("testone", l_xmlMap);
        System.out.println("=====" + l_strTmp);
    }
}
