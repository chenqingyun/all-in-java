package com.chenqingyun.concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenqingyun
 * @date 2019-07-02 07:31.
 */
public class ExplicitLockInfo {

    private static final Lock lock = new ReentrantLock();
    private static int sharedData = 0;

    public static void main(String[] args) {
        lock.lock();
        new Thread(() -> {
            try {
                try {
                    Thread.sleep(200000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock.unlock();
            }
            sharedData++;
        }).start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.lock();
        try {
            System.out.println("sharedData：" + sharedData);
        } finally {
            lock.unlock();
        }

    }
}
