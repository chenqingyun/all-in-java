package com.chenqingyun.thread.api;

/**
 * @author chenqingyun
 * @date 2019/2/23 16:41.
 */
public class ThreadStatusDemo {
    public static void main(String[] args){
        MyThread myThread = new MyThread();
        System.out.println(myThread.getState());
        myThread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(myThread.getState());
    }
    private static class MyThread extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
