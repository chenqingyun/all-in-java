package com.chenqingyun.designpattern.strategy;

/**
 * @author chenqingyun
 * @date 2019-07-11 04:06.
 */
public class StrategyHandler {
    private Strategy strategy;

    public StrategyHandler(){

    }
    public StrategyHandler(Strategy strategy){
        this.strategy = strategy;
    }
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public int execute(int a, int b) {
        return strategy.operation(a, b);
    }
}
