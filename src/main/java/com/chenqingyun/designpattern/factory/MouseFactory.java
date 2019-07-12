package com.chenqingyun.designpattern.factory;

/**
 * @author chenqingyun
 * @date 2019-07-12 09:24.
 */
public class MouseFactory implements GoodsFactory {
    @Override
    public Goods produce() {
        return new Mouse();
    }
}
