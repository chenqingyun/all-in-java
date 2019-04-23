package com.chenqingyun.se.throwable;

import java.io.FileNotFoundException;

/**
 * @author chenqingyun
 * @date 2019-04-24 02:21.
 */
public class Main {

    public static void main(String[] args) {

    }

    public static void exceptionTest() {
        try {
            // do something
        } catch (ExceptionA a) {
            ExceptionB b = new ExceptionB();
            b.initCause(a);
            throw b;
        }
    }

    public void exceptionTest2() throws FileNotFoundException {
        // do something
    }

}
