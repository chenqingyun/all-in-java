package com.chenqingyun;

import com.chenqingyun.designpattern.Singleton;
import com.chenqingyun.designpattern.SingletonEnum;

import java.lang.reflect.Constructor;

/**
 * @author chenqingyun
 * @date 2019-07-05 12:34.
 */
public class Main {
    public static void main(String[] args) {
        SingletonEnum singleton = SingletonEnum.INSTANCE;
    }
}
