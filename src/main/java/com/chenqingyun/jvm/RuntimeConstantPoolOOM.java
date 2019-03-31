package com.chenqingyun.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenqingyun
 * @date 2019/3/31 19:28.
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        int i=0;
        while (true){
            strings.add(String.valueOf(i++).intern());
        }
    }
}
