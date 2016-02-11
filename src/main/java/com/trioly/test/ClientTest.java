package com.trioly.test;

import com.trioly.util.SocketClientUtil;

import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) {
        SocketClientUtil socketClientUtil = SocketClientUtil.getInstance();
        socketClientUtil.connect();
        Socket l_socket = socketClientUtil.sendMessage("kkbbccdd");
        socketClientUtil.getMessage(l_socket, new printObjectAction ());

    }

    public static final class printObjectAction implements SocketClientUtil.ObjectAction {
        public int doAction(Object obj) {
            System.out.println("服务端返回：\t"+obj.toString());
            return 1;
        }
    }
}
