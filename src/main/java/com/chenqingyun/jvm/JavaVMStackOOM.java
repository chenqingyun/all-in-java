package com.chenqingyun.jvm;

/**
 * @author chenqingyun
 * @date 2019/3/31 16:49.
 */
public class JavaVMStackOOM {

    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }

    private void stackLeakByThread(){
        while (true){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                   dontStop();
                }
            });
            thread.start();
        }
    }

    private void dontStop(){
        while (true){

        }
    }
}
