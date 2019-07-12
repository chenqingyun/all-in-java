package com.chenqingyun.designpattern.factory;

/**
 * @author chenqingyun
 * @date 2019-07-12 08:57.
 */
public class FactoryTest {
    public static void main(String[] args) {

        Goods mouse = new MouseFactory().produce();
        System.out.println(mouse.getName());

        Goods keyboard = new KeyboardFactory().produce();
        System.out.println(keyboard.getName());
    }
}
