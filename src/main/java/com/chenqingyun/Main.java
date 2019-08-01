package com.chenqingyun;

import com.chenqingyun.designpattern.strategy.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqingyun
 * @date 2019-07-05 12:34.
 */
public class Main {
    public static void main(String[] args) {
        Integer a = 222;
        int b =222;
        Integer c =222;
        Long d = 222L;
        Long e = 222L;
        System.out.println(c == a);
        System.out.println(d.compareTo(e));
        System.out.println(Long.compare(d,e));
        System.out.println(c.equals(a));
        System.out.println(a.equals(b));
    }
}
