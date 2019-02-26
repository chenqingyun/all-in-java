package com.chenqingyun.concurrency.thread;

/**
 * @author chenqingyun
 * @date 2019/2/23 10:54.
 */
public class ThreadJoinDemo {
    public static void main(String[] args) {
        BabyThread babyThread = new BabyThread();
        MotherRunnable motherRunnable = new MotherRunnable(babyThread);
        Thread motherThread = new Thread(motherRunnable);
        motherThread.start();
        babyThread.start();
    }

    private static class MotherRunnable implements Runnable {
        private BabyThread babyThread;

        public MotherRunnable() {
        }

        public MotherRunnable(BabyThread babyThread) {
            this.babyThread = babyThread;
        }

        @Override
        public void run() {
            System.out.println("mother 开始在房间里跳广场舞");
            try {
                long time = System.currentTimeMillis();
                babyThread.join();
                System.out.println("暂停了 " + (System.currentTimeMillis() - time) + " 秒，等宝宝睡醒后，Mother 又开始跳广场舞了");
                Thread.sleep(1000);
                System.out.println("Mother 结束跳广场舞");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class BabyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Baby 开始睡觉了");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Baby 睡醒了");
        }
    }
}
