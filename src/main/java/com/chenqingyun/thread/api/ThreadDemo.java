package com.chenqingyun.thread.api;

/**
 * @author chenqingyun
 * @date 2019/2/17 02:19.
 */
public class ThreadDemo {
    public static void main(String[] args) {
        // 创建线程
        MyThread myThread = new MyThread();
        // 设置线程的名称
        myThread.setName("Thread sir");
        // myThread.run(); 只调用 run 方法，并没有启动线程，就是以普通的方法进行调用，打印结果是"hello,main"

        // 运行线程，调用 run 方法，打印结果"hello,Thread sir"
        myThread.start();
        // myThread.start(); 重复调用 start 方法会抛"Exception in thread "main" java.lang.IllegalThreadStateException"

        // 匿名子类的形式
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println("hello," + Thread.currentThread().getName());
            }
        };
        thread.start();
    }
}
