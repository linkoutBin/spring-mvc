package com.bin.netty.nonblock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        boolean stop = true;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        try (ServerSocket server = new ServerSocket(port)) {
            Socket socket;
            ThreadExecutePool executePool = new ThreadExecutePool(4, 20);
            System.out.println("process" + Runtime.getRuntime().availableProcessors());
            while (stop) {
                socket = server.accept();
                executePool.execute(new TimeServerHandler(socket));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

class ThreadExecutePool {

    private ExecutorService executor;

    public ThreadExecutePool(int maxPoolSize, int queueSize) {
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize));
    }

    public void execute(Runnable task) {
        executor.execute(task);
    }
}

class TimeServerHandler implements Runnable {
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
                Thread.sleep(5000);
                System.out.println(Thread.currentThread().getName() + "响应请求：" + currentTime);
                out.println(currentTime);
                out.println("end");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
