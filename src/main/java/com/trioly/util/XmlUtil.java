package com.trioly.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Map;

public class XmlUtil {
	static Logger logger = Logger.getLogger(XmlUtil.class);

	public static String createXml(String p_xmlName, Map<String, String> p_xmlMap) {
        try {
            InputStream is = XmlUtil.class.getResourceAsStream("/send.xml");
            if(is == null){
                File resource = new File("./send.xml");
                is = new FileInputStream(resource);
            }
            BufferedReader l_reader = new BufferedReader(new InputStreamReader (is));

            String l_strXml = "", l_strTmp;
            int l_iTab = 0;
            while ((l_strTmp = l_reader.readLine()) != null) {
                if (l_strTmp.contains(":") && l_strTmp.contains(p_xmlName)) {
                    l_iTab = 1;
                    continue;
                }
                if (1 == l_iTab) {
                    if (l_strTmp.contains("-----")) {
                        break;
                    } else {
                        if (l_strXml.length() == 0) {
                            l_strXml = l_strTmp;
                        } else {
                            l_strXml = l_strXml + "\n" + l_strTmp;
                        }
                    }
                }
            }

            for (Map.Entry<String, String> l_entry: p_xmlMap.entrySet()) {
                l_strXml = l_strXml.replaceAll(l_entry.getKey(), l_entry.getValue());
            }

            return l_strXml;

        } catch (IOException e) {
            logger.debug("IOException in config file load", e);
            return "";
        }
	}
}
