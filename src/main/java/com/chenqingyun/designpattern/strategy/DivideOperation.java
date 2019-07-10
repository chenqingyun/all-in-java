package com.chenqingyun.designpattern.strategy;

/**
 * @author chenqingyun
 * @date 2019-07-11 04:02.
 */
public class DivideOperation implements Strategy {
    @Override
    public int operation(int a, int b) {
        int result = a / b;
        System.out.println("divide operation：" + result);
        return result;
    }
}
