package com.chenqingyun;

import com.chenqingyun.designpattern.strategy.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqingyun
 * @date 2019-07-05 12:34.
 */
public class Main {
    public static void main(String[] args) {
        Map<Integer,Integer> map = new HashMap<>();
        for(int i=0;i<10;i++){
            map.put(i,i);
        }

        Collection<Integer> collection = map.values();

        map.keySet();

        System.out.println(collection);
    }
}
