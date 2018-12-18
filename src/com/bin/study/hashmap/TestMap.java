package com.bin.study.hashmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMap {
    public static void main(String[] args) {
        /*HashMap map = new HashMap();
        map.put("key", "value");

        int n = 1 << 2 - 1;
        n |= n >>> 1;
        System.out.println(n >>> 1);
        n |= n >>> 2;
        System.out.println(n);
        n |= n >>> 4;
        System.out.println(n);
        n |= n >>> 8;
        System.out.println(n);
        n |= n >>> 16;
        System.out.println(n);*/


        String[] strs = {"a", "v", "c"};
        for (int i = 0; i < strs.length; i++) {
            String[] temp = Arrays.copyOf(strs, strs.length - i);
            System.out.println(Arrays.toString(temp));
        }

        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        String vs = String.join("...",list);
        System.out.println(vs);

    }
}
