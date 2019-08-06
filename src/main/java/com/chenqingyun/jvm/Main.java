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

        System.out.println(s1==s2);// false


        String s11 = s1.intern();
        String s111 =s1.intern();
        String s22= s2.intern();
        String s4= "abc";
        System.out.println(s4==s1);// false
        System.out.println(s4==s11); // true
        System.out.println(s11 == s1);  // false
        System.out.println(s11 ==s111);  // true
        System.out.println(s11 ==s22);  // true

        String str2 = new String("str")+new String("01");
        str2.intern();
        String str1 = "str01";
        System.out.println(str2==str1);  // true


        String a = new String("1234");
        a.intern();
        String b = "1234";
        System.out.println(a==b);

    }
}
