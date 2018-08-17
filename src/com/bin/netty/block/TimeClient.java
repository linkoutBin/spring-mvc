package com.bin.netty.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TimeClient {
    private static int port = 8080;

    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        for (int i = 0; i < 4; i++) {
            new Thread(() -> {
                //while (true) {
                //System.out.println("当前时间：" + new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date()));
                try {
                    //Thread.sleep(1000);
                    sendRequest(port);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                //}
            }).start();
        }
    }

    private static void sendRequest(int port) {
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", port),30000);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                socket.setSoTimeout(60000);
                System.out.println(Thread.currentThread().getName()+":开始发送请求");
                out.println("QUERY ORDER TIME");
                System.out.println(Thread.currentThread().getName()+":请求发送完成");
                while (true) {
                    String result = in.readLine();
                    System.out.println(Thread.currentThread().getName()+":响应结果：" + result);
                    if ("end".equalsIgnoreCase(result)) {
                        break;
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println(Thread.currentThread().getName()+":客户端发送请求异常");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName()+":连接异常");
            return;
        } finally {
            if (null != socket) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
