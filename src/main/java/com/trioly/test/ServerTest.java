package com.trioly.test;

import com.trioly.util.SocketServerUtil;

public class ServerTest {
    public static void main(String[] args) {
        int port = 65432;
        SocketServerUtil socketServerUtil = new SocketServerUtil(port);
        socketServerUtil.start();
    }

}
