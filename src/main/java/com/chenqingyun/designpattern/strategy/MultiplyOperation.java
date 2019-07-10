package com.chenqingyun.designpattern.strategy;

/**
 * @author chenqingyun
 * @date 2019-07-11 04:01.
 */
public class MultiplyOperation implements Strategy {
    @Override
    public int operation(int a, int b) {
        int result = a * b;
        System.out.println("multiply operation：" + result);
        return result;
    }
}
