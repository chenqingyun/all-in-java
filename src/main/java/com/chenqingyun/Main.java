package com.chenqingyun;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqingyun
 * @date 2019-07-05 12:34.
 */
public class Main {
    public static void main(String[] args) {
        HashMap<Integer,Integer> map = new HashMap();
        for(int i=0;i<10;i++){
            map.put(i,i);
        }

        System.out.println(map.toString());
    }
}
