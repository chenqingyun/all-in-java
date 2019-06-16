package com.chenqingyun.concurrency;

/**
 * @author chenqingyun
 * @date 2019-06-16 13:11.
 */
public class DeadLockDemo {
    private static final String LOCK_A = "LOCK_A";
    private static final String LOCK_B = "LOCK_B";
    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }

    private void deadLock(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK_A){
                    try {
                        Thread.currentThread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (LOCK_B){
                        System.out.println("A");
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK_B){
                    try {
                        Thread.currentThread().sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (LOCK_A){
                        System.out.println("B");
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }
}
