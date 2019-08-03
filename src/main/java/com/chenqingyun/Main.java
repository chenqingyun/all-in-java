package com.chenqingyun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chenqingyun
 * @date 2019-07-05 12:34.
 */
public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(4);
        list.add(1);
        list.add(0);

        Collections.sort(list);
        System.out.println(list);
    }
}
