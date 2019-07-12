package com.chenqingyun.designpattern.factory;

/**
 * @author chenqingyun
 * @date 2019-07-12 09:26.
 */
public class KeyboardFactory implements GoodsFactory {
    @Override
    public Goods produce() {
        return new Keyboard();
    }
}
