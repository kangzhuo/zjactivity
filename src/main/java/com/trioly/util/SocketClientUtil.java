package com.trioly.util;

import com.td.sms.web.config.CommonConfig;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class SocketClientUtil {
    private static final Logger logger = Logger.getLogger(SocketClientUtil.class);

    private String g_strIp = "111.13.19.105";
    private int g_iPort = 7890;
    private int g_ikeepAliveDelay = 180; //心跳检测间隔 单位 秒
    private int g_iRetryTime = 60;       //重试间隔时间 单位 秒
    private int g_iRetryNum = 3;         //重试次数 单位 次
    private int g_iCheckDelay = 10;      //检测间隔时间 单位 秒
    private int g_iConnNum = 2;          //链路数量
    private CommonConfig g_commonConfig = null; //配置

    private static SocketClientUtil sInstance;

    private static synchronized void makeInstance() {
        sInstance = new SocketClientUtil();
    }

    public static synchronized SocketClientUtil getInstance() {
        if(null == sInstance) {
            makeInstance();
        }
        return sInstance;
    }

    private SocketClientUtil() {
        g_commonConfig = CommonConfig.getInstance();
        g_strIp = g_commonConfig.getString("ip", g_strIp);
        g_iPort = g_commonConfig.getInt("port", g_iPort);
        g_ikeepAliveDelay = g_commonConfig.getInt("keepAliveDelay", g_ikeepAliveDelay);
        g_iRetryTime = g_commonConfig.getInt("retryTime", g_iRetryTime);
        g_iRetryNum = g_commonConfig.getInt("retryNum", g_iRetryNum);
        g_iCheckDelay = g_commonConfig.getInt("checkDelay", g_iCheckDelay);
        g_iConnNum = g_commonConfig.getInt("connNum", g_iConnNum);
    }

    public static interface ObjectAction{
        int doAction(Object obj);
    }

    public static final class DefaultObjectAction implements ObjectAction{
        public int doAction(Object obj) {
            System.out.println("服务端返回：\t"+obj.toString());
            return 1;
        }
    }




    private ConcurrentHashMap<Socket, Integer> g_mapsocketStatus = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Socket, Long> g_mapsocketTime = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Socket, String> g_mapsocketMsg = new ConcurrentHashMap<>();

    public int connect() {
        if (g_mapsocketStatus.size() >= g_iConnNum) {
            return 1;
        }

        try {
            for (int i = 0; i < g_iConnNum; i++) {
                Socket l_socket = new Socket(g_strIp, g_iPort);
                g_mapsocketStatus.put(l_socket, 0);
                g_mapsocketTime.put(l_socket, System.currentTimeMillis());
                g_mapsocketMsg.put(l_socket, "");
            }
        } catch (UnknownHostException e) {
            logger.error("UnknownHostException in SocketClientUtil", e);
            SocketClientUtil.this.disconnect();
            return -1;
        } catch (IOException e) {
            logger.error("IOException in SocketClientUtil", e);
            SocketClientUtil.this.disconnect();
            return -1;
        }

        new Thread(new KeepAliveWatchDog()).start();

        return 1;
    }

    public Socket sendMessage(String p_strSendMsg) {
        while (g_mapsocketStatus.size() > 0) {
            for (Entry<Socket, Integer> l_entryStatus: g_mapsocketStatus.entrySet()) {
                if(0 == l_entryStatus.getValue()){
                    sendMessage(l_entryStatus.getKey(), p_strSendMsg);
                    return l_entryStatus.getKey();
                }
            }

            try {
                logger.debug("socket is busy. wait " + g_iCheckDelay * 1000 + "\n");
                Thread.sleep(g_iCheckDelay * 1000);
            } catch (InterruptedException e) {
                logger.error("InterruptedException in SocketClientUtil", e);
            }
        }

        return null;
    }

    public int sendMessage(Socket p_socket, String p_strSendMsg) {
        try {
            if (!g_mapsocketStatus.containsKey(p_socket)) {
                logger.debug("socket is not exists \n");
                return -1;
            }

            while (0 != g_mapsocketStatus.get(p_socket)) {
                logger.debug("socket is busy. wait 0.01 \n");
                Thread.sleep(10);
            }

            g_mapsocketStatus.put(p_socket, 1);
            while (!(p_socket.isConnected() && !p_socket.isClosed())) {
                logger.debug("socket is not connect. reconnect \n");
                SocketClientUtil.this.reconnect(p_socket);
            }
            ObjectOutputStream oos = new ObjectOutputStream(p_socket.getOutputStream());
            oos.writeObject(p_strSendMsg);
            System.out.println("发送:\t" + p_strSendMsg);
            oos.flush();
        } catch (InterruptedException e) {
            logger.error("InterruptedException in SocketClientUtil", e);
            g_mapsocketStatus.put(p_socket, 0);
            return -1;
        } catch (IOException e) {
            logger.error("IOException in SocketClientUtil", e);
            SocketClientUtil.this.reconnect(p_socket);
            return -1;
        }

        g_mapsocketTime.put(p_socket, System.currentTimeMillis());
        g_mapsocketMsg.put(p_socket, p_strSendMsg);
        return 1;
    }

    public int getMessage(Socket p_socket, ObjectAction p_objectAction) {
        try {
            if (!(p_socket.isConnected() && !p_socket.isClosed())) {
                logger.debug("socket is not connect. reconnect \n");
                SocketClientUtil.this.reconnect(p_socket);
                return -1;
            }

            InputStream in = p_socket.getInputStream();
            for (int i = 0; i < g_iRetryNum; i++) {
                while (true) {
                    if(in.available() > 0){
                        ObjectInputStream ois = new ObjectInputStream(in);
                        Object obj = ois.readObject();
                        g_mapsocketStatus.put(p_socket, 0);
                        g_mapsocketMsg.put(p_socket, "");
                        return p_objectAction.doAction(obj);
                    }

                    if(System.currentTimeMillis() - g_mapsocketTime.get(p_socket) > g_iRetryTime * 1000) {
                        logger.debug("message is not receive to resend \n");
                        SocketClientUtil.this.sendMessage(p_socket, g_mapsocketMsg.get(p_socket));
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException in SocketClientUtil", e);
            g_mapsocketStatus.put(p_socket, 0);
            g_mapsocketMsg.put(p_socket, "");
            return -1;
        } catch (IOException e) {
            logger.error("IOException in SocketClientUtil", e);
            SocketClientUtil.this.reconnect(p_socket);
            return -1;
        }

        logger.debug("message is not receive return -1 \n");
        return -1;
    }

    public String getMessage(Socket p_socket) {
        try {
            if (!(p_socket.isConnected() && !p_socket.isClosed())) {
                logger.debug("socket is not connect. reconnect \n");
                SocketClientUtil.this.reconnect(p_socket);
                return "";
            }

            InputStream in = p_socket.getInputStream();
            for (int i = 0; i < g_iRetryNum; i++) {
                while (true) {
                    if(in.available() > 0){
                        ObjectInputStream ois = new ObjectInputStream(in);
                        Object obj = ois.readObject();
                        g_mapsocketStatus.put(p_socket, 0);
                        g_mapsocketMsg.put(p_socket, "");
                        return (String) obj;
                    }

                    if(System.currentTimeMillis() - g_mapsocketTime.get(p_socket) > g_iRetryTime * 1000) {
                        logger.debug("message is not receive to resend \n");
                        SocketClientUtil.this.sendMessage(p_socket, g_mapsocketMsg.get(p_socket));
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException in SocketClientUtil", e);
            g_mapsocketStatus.put(p_socket, 0);
            g_mapsocketMsg.put(p_socket, "");
            return "";
        } catch (IOException e) {
            logger.error("IOException in SocketClientUtil", e);
            SocketClientUtil.this.reconnect(p_socket);
            return "";
        }

        logger.debug("message is not receive return null \n");
        return "";
    }

    private int reconnect(Socket p_socket) {
        try {
            p_socket.close();
            g_mapsocketStatus.remove(p_socket);
            g_mapsocketTime.remove(p_socket);
            g_mapsocketMsg.remove(p_socket);

            Socket l_socket = new Socket(g_strIp, g_iPort);
            g_mapsocketStatus.put(l_socket, 0);
            g_mapsocketTime.put(l_socket, System.currentTimeMillis());
            g_mapsocketMsg.put(l_socket, "");
        } catch (IOException e) {
            logger.error("IOException in SocketClientUtil", e);
        }

        return 1;
    }

    private int disconnect() {
        for (Entry<Socket, Integer> l_entryStatus: g_mapsocketStatus.entrySet()) {
            try {
                while (0 != l_entryStatus.getValue()) {
                    Thread.sleep(100);
                }
                l_entryStatus.getKey().close();
                g_mapsocketStatus.remove(l_entryStatus.getKey());
                g_mapsocketTime.remove(l_entryStatus.getKey());
                g_mapsocketMsg.remove(l_entryStatus.getKey());
            } catch (IOException e) {
                logger.error("IOException in SocketClientUtil", e);
            } catch (InterruptedException e) {
                logger.error("InterruptedException in SocketClientUtil", e);
            }
        }

        return 1;
    }

    class KeepAliveWatchDog implements Runnable{
        public void run() {
            while (g_mapsocketTime.size() > 0) {
                for (Entry<Socket, Long> l_entryTime: g_mapsocketTime.entrySet()) {
                    if(System.currentTimeMillis() - l_entryTime.getValue() > g_ikeepAliveDelay * 1000){
                        logger.debug("time on send message \n");
                        if (1 == SocketClientUtil.this.sendMessage(l_entryTime.getKey(), "123")) {
                            SocketClientUtil.this.getMessage(l_entryTime.getKey(), new DefaultObjectAction());
                        }
                    }
                }

                try {
                    Thread.sleep(g_iCheckDelay * 1000);
                } catch (InterruptedException e) {
                    logger.error("InterruptedException in SocketClientUtil", e);
                }
            }
        }
    }

}
