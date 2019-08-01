package com.chenqingyun.se;

import java.math.BigDecimal;

/**
 * @author chenqingyun
 * @date 2019-08-01 14:32.
 */
public class CloneTest {

    public static void main(String[] args) {
        // 对象赋值
        Person person1 = new Person("旺财", 19, 1);
        Person person2 = person1;
        System.out.println(person1 == person2);
        System.out.println(person2);
        person1.setName("狗蛋");
        System.out.println(person2);

        System.out.println("————————————————————————————————");

        // 浅拷贝
        House house = new House();
        house.setAddress("中央大街");
        house.setPrice(new BigDecimal(10000));
        person1.setHouse(house);
        Person person3 = (Person) person1.clone();
        System.out.println(person1 == person3);
        System.out.println(person3);
        person1.setName("二哈");
        house.setAddress("杭州");
        System.out.println(person3);

        // 深拷贝
        // 引用类型里再实现 clone 方法拷贝对象
    }


}
