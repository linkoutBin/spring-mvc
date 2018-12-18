package com.bin.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadService {
    private ExecutorService executorService;

    private ThreadService(int threadsNum) {
        //executorService = Executors.newFixedThreadPool(threadsNum);
        executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                System.out.println("create thread");
                return new Thread(r, "worker");
            }
        });
        //executorService = Executors.newSingleThreadExecutor();
        //executorService = Executors.newScheduledThreadPool(5);
    }

    public void execute(Runnable task) {
        executorService.execute(task);
    }

    public void shutdown() {
        if (!executorService.isShutdown())
            executorService.shutdown();
    }


    public static void main(String[] args) {
        ThreadService service = new ThreadService(5);
        /*int port = 8080;
        int requestNum = 0;
        service.execute(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println(Thread.currentThread().getName() + ":request");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });*/
        for (int i = 0; i < 10; i++) {
            service.execute(() -> {
                System.out.println("ThreadName:" + Thread.currentThread().getName());
            });
        }
        service.shutdown();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("JVM closing");
            //System.exit(0);//在钩子中调用会导致卡住JVM关闭过程，
        }));
    }
}
