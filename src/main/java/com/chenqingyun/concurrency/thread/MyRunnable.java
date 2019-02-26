package com.chenqingyun.concurrency.thread;

/**
 * @author chenqingyun
 * @date 2019/2/17 03:18.
 * <p>
 * 实现 Runnable 接口
 */
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("hello," + Thread.currentThread().getName());
    }
}
