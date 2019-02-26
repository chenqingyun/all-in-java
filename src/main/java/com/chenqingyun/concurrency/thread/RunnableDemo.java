package com.chenqingyun.concurrency.thread;

/**
 * @author chenqingyun
 * @date 2019/2/17 03:19.
 */
public class RunnableDemo {
    public static void main(String[] args) {
        // 创建 Runnable 实现类的对象
        MyRunnable myRunnable = new MyRunnable();

        // 创建 Thread 类的对象，将实现类对象作为构造参数传递进去
        Thread thread = new Thread(myRunnable);
        // 设置线程名称
        thread.setName("Runnable sir");

        // 调用 start 方法运行线程
        thread.start();
    }
}
