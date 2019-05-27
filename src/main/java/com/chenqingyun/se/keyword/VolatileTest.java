package com.chenqingyun.se.keyword;

/**
 * @author chenqingyun
 * @date 2019-05-20
 */
public class VolatileTest {

    private static int finished = 0;
    private  static void chekFinished(){
        while (finished == 0){

        }
        System.out.println("finished");
    }

    private static void finish(){
        finished = 1;
    }
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> chekFinished()).start();
        Thread.sleep(1000);
        finish();
        System.out.println("main finished");
    }
}
