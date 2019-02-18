package com.chenqingyun.thread.api;

/**
 * @author chenqingyun
 * @date 2019/2/18 23:01.
 */
public class DaemonThread {
    public static void main(String[] args) {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i <= 100000) {
                    System.out.println("this is thread1，" + i);
                    i++;
                }
            }
        };
        thread1.setDaemon(true);
        thread1.start();

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                System.out.println("this is thread2");
            }
        };
        thread2.start();

    }
}
