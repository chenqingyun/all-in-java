package com.chenqingyun.jvm;

/**
 * @author chenqingyun
 * @date 2019/3/31 16:37.
 * <p>
 * JVM Stack StackOverflowError
 * VM Args:-Xss256k
 */
public class JavaVMStackSOF {
    /**
     * 栈长度，即栈帧大小
     */
    private int stackLength = 1;

    public static void main(String[] args) {
        JavaVMStackSOF sof = new JavaVMStackSOF();
        try {
            sof.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length：" + sof.stackLength);
            throw e;
        }
    }

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }
}
