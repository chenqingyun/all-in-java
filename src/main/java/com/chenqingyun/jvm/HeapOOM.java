package com.chenqingyun.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenqingyun
 * @date 2019/3/31 15:37.
 *
 * Java Heap OutOfMemoryError
 * VM Args:-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 *
 */
public class HeapOOM {
    public static void main(String[] args) {
        List<Object> objects = new ArrayList<>();
        while (true){
            objects.add(new Object());
        }

    }
}
