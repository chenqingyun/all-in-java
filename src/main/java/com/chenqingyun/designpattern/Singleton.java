package com.chenqingyun.designpattern;

/**
 * @author chenqingyun
 * @date 2019-07-10 18:38.
 * <p>
 * 单例模式
 */
public class Singleton {
    private Singleton(){}
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
