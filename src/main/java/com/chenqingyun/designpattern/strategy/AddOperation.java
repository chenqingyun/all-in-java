package com.chenqingyun.designpattern.strategy;

/**
 * @author chenqingyun
 * @date 2019-07-11 03:59.
 */
public class AddOperation implements Strategy {
    @Override
    public int operation(int a,int b) {
        int result = a + b;
        System.out.println("add operation："+result);
        return result;
    }
}
