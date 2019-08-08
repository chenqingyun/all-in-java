package com.chenqingyun.designpattern.strategy;

/**
 * @author chenqingyun
 * @date 2019-08-08 15:19.
 */
public enum StrategyEnum {
    ADD("add",new AddOperation()),
    DIVIDE("divide",new DivideOperation());

    private String name;
    private Strategy strategy;

    StrategyEnum(String name,Strategy strategy){
        this.name = name;
        this.strategy = strategy;
    }
}
