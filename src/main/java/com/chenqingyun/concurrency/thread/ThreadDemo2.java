package com.chenqingyun.concurrency.thread;

/**
 * @author chenqingyun
 * @date 2019/2/18 21:38.
 */
public class ThreadDemo2 {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread1= new Thread(myRunnable,"thread1");
        Thread thread2= new Thread(myRunnable);
        thread2.setName("thread2");
        thread1.start();
        thread2.start();
    }

    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread name:" + Thread.currentThread().getName());
        }
    }
}
