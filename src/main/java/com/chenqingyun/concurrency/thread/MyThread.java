package com.chenqingyun.concurrency.thread;

/**
 * @author chenqingyun
 * @date 2019/2/17 02:20.
 * <p>
 * 创建 Thread 子类并重写 run 方法
 */
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("hello," + Thread.currentThread().getName());
    }
}
