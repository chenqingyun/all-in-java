package com.chenqingyun.concurrency;

/**
 * @author chenqingyun
 * @date 2019-07-07 20:26.
 */
public class WaitNotify {
    private static final Object lock = new Object();
    private boolean flag = true;

    public static void main(String[] args) {

    }

    private void waitThread(Object lock) {
        new Thread(() -> {
            synchronized (lock) {
                try {
                    while (flag) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {

                }
            }
        }).start();
    }

    private void notifyThread(Object lock) {
        new Thread(() -> {
            synchronized (lock) {
                flag = false;
                lock.notify();
            }
        }).start();
    }

}
