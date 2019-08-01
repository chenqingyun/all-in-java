package com.chenqingyun.se;

/**
 * @author chenqingyun
 * @date 2019-07-14 01:35.
 */

public class Person implements Cloneable {

    private String name;
    private Integer age;
    private Integer gender;

    private House house = new House();

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, Integer age, Integer gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public Object clone() {
        try {
            // 浅拷贝
            return super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return null;
    }

    @Override
    public String toString() {
        return "person：" + name + "，" + age + "，" + gender + "；house：" +  house.getAddress();
    }
}
