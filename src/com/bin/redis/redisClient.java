package com.bin.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class redisClient {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        jedis.auth("123456");

        //bitoperation();

        //发布订阅
        //pubsub(jedis);

        //
        //setNxEx(jedis);

        //
        //copyonwriteTest();
    }

    private static void copyonwriteTest() {
        int num = 1024;
        Object[] array = new Object[num];
        String content = "this content for test:";
        System.out.println("start-to-generate-date:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
        for (int i = 0; i < num; i++) {
            array[i] = content + i;
        }
        System.out.println("end-to-generate-data:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
        int length = array.length;
        System.out.println("start-time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
        Arrays.copyOf(array, length);
        System.out.println("end-time" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
        CopyOnWriteArrayList<Object> elements = new CopyOnWriteArrayList<>();
        System.out.println("array-length:" + elements.toArray().length);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "start:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
            for (int i = 0; i < length; i++) {
                elements.add("this content for test in new thread:" + i);
            }
            System.out.println(Thread.currentThread().getName() + "end:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
        }).start();
        System.out.println("copy-start" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
        for (int j = 0; j < length; j++) {
            if (j == 1) {
                System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
            }
            elements.add(array[j]);
        }
        System.out.println("copy-end" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));


        System.out.println("=========================\n" + elements.toString().replace(",", "\n"));
    }

    private static void setNxEx(Jedis jedis) {
        String result = jedis.set("test", "222", "NX", "PX", 10000);
        System.out.println("result:" + result);
        boolean stop = true;
        int i = 0;
        while (stop) {
            System.out.println(i++);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            if (i == 10)
                stop = false;
        }
        System.out.println(jedis.get("test"));
    }

    private static void bitoperation() {
        String key = "play:" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String userId = "xingshulin";
        // jedis.set(key.getBytes(), userId.getBytes());
        BitSet bitSet = BitSet.valueOf(userId.getBytes());

        System.out.println("bitSet.length:" + bitSet.length() + "bitSet:" + bitSet + "    card:" + bitSet.cardinality());

        //
        System.out.println("+++++++++++++++++++");
        BitSet all = new BitSet();
        String k = "play";
        BitSet kset = BitSet.valueOf(k.getBytes());
        String v = "game";
        BitSet vset = BitSet.valueOf(v.getBytes());
        System.out.println("set:" + kset + "kset.length:" + kset.cardinality());
        System.out.println("vSet:" + vset + "vset.length:" + vset.cardinality());
        all.or(kset);
        all.or(vset);
        System.out.println("all:" + all + " all.car" + all.cardinality());
    }

    private static void pubsub(Jedis jedis) {
        List<String> infos = jedis.brpop(20, "warning");
        System.out.println(infos);
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String s, String s1) {
                System.out.println("onMessage:" + s + "--" + s1);
            }

            @Override
            public void onPMessage(String s, String s1, String s2) {
                System.out.println("onPMessage:" + s + "-" + s1 + "--" + s2);
            }

            @Override
            public void onSubscribe(String s, int i) {
                System.out.println("onSubscribe:" + s);
            }

            @Override
            public void onUnsubscribe(String s, int i) {
                System.out.println("onUnsubscribe:" + s);
            }

            @Override
            public void onPUnsubscribe(String s, int i) {
                System.out.println("onPUnsubscribe");
            }

            @Override
            public void onPSubscribe(String s, int i) {
                System.out.println("onPSubscribe:" + s);
            }
        };
        jedis.subscribe(jedisPubSub, "warning");
    }
}
