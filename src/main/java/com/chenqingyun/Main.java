package com.chenqingyun;

import com.chenqingyun.designpattern.strategy.*;

/**
 * @author chenqingyun
 * @date 2019-07-05 12:34.
 */
public class Main {
    public static void main(String[] args) {
        StrategyHandler handler = new StrategyHandler();
        handler.setStrategy(new AddOperation());
        handler.execute(1, 5);

        handler.setStrategy(new SubstractOperation());
        handler.execute(4, 2);

        handler.setStrategy(new MultiplyOperation());
        handler.execute(4, 4);

        handler.setStrategy(new DivideOperation());
        handler.execute(6, 2);
    }
}
