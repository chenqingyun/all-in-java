package com.chenqingyun.se.reflection;

/**
 * @author chenqingyun
 * @date 2019-07-14 01:39.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Class clazz = Person.class;
        System.out.println(clazz.getName());

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class clazz1 = classLoader.loadClass("com.chenqingyun.se.reflection.Person");
        System.out.println(clazz1.getName());
        System.out.println(clazz.equals(clazz1));

        Person person = (Person) clazz.newInstance();
        System.out.println(person.getAge());

    }
}
