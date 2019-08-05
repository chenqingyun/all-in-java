package com.chenqingyun.jvm;

/**
 * @author chenqingyun
 * @date 2019/3/30 09:08.
 */
public class Main {
    public static void main(String[] args){
        String s1 = new String("abc");
        String s2 = new String("abc");
        String s3 = new String("hhh");

        System.out.println(s1==s2);
        String s11 = s1.intern();
        String s22= s2.intern();
        System.out.println(s11 == s1);
        System.out.println(s11 ==s22);


    }
}
