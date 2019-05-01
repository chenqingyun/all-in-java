<h1 align='center'> 
TODO List
</h1>




## 目录

- [Java 基础](#java-基础)
- [Java 并发编程](#java-并发编程)
- [Java 虚拟机](#java-虚拟机)



## Java 基础

- [Java 集合框架](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/Java%20%E9%9B%86%E5%90%88%E6%A1%86%E6%9E%B6%E6%A6%82%E8%BF%B0.md)

  - [ArrayList 源码解读](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/ArrayList%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90.md)
- 虚拟机给列表分配的的最大容量为 Integer.MAX_VALUE - 8，为什么超过了可以使用 Integer.MAX_VALUE 
  - HashMap 源码解读

## Java 并发编程

### 原子性

- long 和 double 不具备原子性 《Java 多线程编程实战指南》P45
- 循环不变表达式外提

### Java 虚拟机

- [Java 虚拟机运行时数据区](<https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA%E8%BF%90%E8%A1%8C%E6%97%B6%E6%95%B0%E6%8D%AE%E5%8C%BA.md#%E5%8F%82%E8%80%83>)
- 完善图
- [StackOverflowError 详解](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/StackOverflowError.md)
- 虚拟机参数
- class 文件格式
- 类加载机制
- 对象和引用
- finalize() 方法
- System.gc() 方法