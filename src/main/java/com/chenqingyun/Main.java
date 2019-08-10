package com.chenqingyun;

import com.chenqingyun.designpattern.strategy.*;

import java.util.*;

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

        Map<String , Strategy> map = new HashMap<>();
        map.put("add",new AddOperation());
        map.put("divide",new DivideOperation());

        StrategyHandler handler = new StrategyHandler(map.get("add"));
        handler.execute(1,3);

        handler.setStrategy(map.get("divide"));
        handler.execute(4,2);
    }
}
