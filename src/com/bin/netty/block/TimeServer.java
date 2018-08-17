package com.bin.netty.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (null != args && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        try (ServerSocket server = new ServerSocket(port,3)) {
            Socket socket;
            Thread.sleep(20000);
            while (true) {
                socket = server.accept();
                dealWithRequest(socket);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }catch (InterruptedException ie){

        }
    }

    private static void dealWithRequest(Socket socket) {
        new Thread(new TimeServerHandler(socket)).start();
    }


    private static class TimeServerHandler implements Runnable {
        private Socket socket;

        public TimeServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "开始处理请求");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                String currentTime;
                String request;
                while (true) {
                    request = in.readLine();
                    System.out.println(Thread.currentThread().getName() + "接收到的请求：" + request);
                    if (request == null)
                        break;
                    currentTime = "query order time".equalsIgnoreCase(request) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : "bad request";
                    //Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "响应请求：" + currentTime);
                    out.println(currentTime);
                    out.println("end");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}
