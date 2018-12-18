package com.bin.spring;

public class TestSpring {
    private static Object object = new Object();

    private static Thread thread = Thread.currentThread();

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                System.out.println("线程准备中断！");
                Thread.sleep(5000);
                thread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            System.out.println("线程开始休眠");
            Thread.sleep(10000);
            System.out.println("线程休眠结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
