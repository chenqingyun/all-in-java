package com.chenqingyun.designpattern.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqingyun
 * @date 2019-07-11 04:00.
 */
public class SubstractOperation implements Strategy {
    @Override
    public int operation(int a, int b) {
        int result = a - b;
        System.out.println("substract operation：" + result);
        return result;
    }

}
