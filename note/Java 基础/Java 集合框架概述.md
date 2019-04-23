## Java 集合框架概述

## 目录

- [Collection](#Collection)
  - [List](#List)
  - [Set](#Set)
- [Map](#Map)
- [附：各集合类详细解读](#附：各集合类详细解读)

集合框架中有两个基本接口：Collection 接口和 Map 接口

<div align="center"><img src="https://upload-images.jianshu.io/upload_images/3297676-0d6aaa884eb86dc2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width= "650px"></div>

## Collection

集合类的基本接口是 Collection 接口

```java
package java.util;
public interface Collection<E> extends Iterable<E>
```



### List

**特点：有序，可重复**

#### ArrayList

- 底层数据结构是数组
- 查询快，增删慢
- 不同步，线程不安全，效率高



### LinkedList

- 底层数据结构是链表
- 查询慢，增删快
- 不同步，线程不安全，效率高
- 实现 Deque 接口，为 add、poll 提供先进先出队列操作，以及其他堆栈和双端队列操作



### Vector

- 底层数据结构是数组
- 查询快，增删慢
- 不同步，线程不安全，效率高



### Set

**特点：无序，不可重复，最多包含一个 null 元素**



### HashSet

- 由哈希表（实际上是一个 HashMap 实例）支持
- 依赖 hasCode() 和 equals(Object o) 方法保证唯一性
- 不同步，线程不安全



### LinkedHashSet

- 底层数据结构由哈希表和链表组成
- 元素有序，唯一
- 链表保证元素有序
- 哈希表保证元素唯一
- 不同步，线程不安全



### TreeSet

- 基于 TreeMap 的 NavigableSet 实现
- 底层是二叉树结构（红黑树，一种自平衡的二叉树）
- 排序，元素唯一



## Map

集合的另一个基本接口是 Map 接口

### HashMap

- 底层数据结构哈希表结构，保证键的唯一性
- 不同步，线程不安全，效率高
- 允许使用 null 值和 null 键
- 不保证映射的顺序。



### LinkedHashMap

- 底层数据结构由链表和哈希表组成
- 由链表保证映射的顺序（存储和取出顺序一致）
- 哈希表保证键的唯一



### HashTable

- 底层数据结构是哈希表结构
- 同步的，线程安全，效率低
- 不允许使用 null 值和 null 键

> HashTable JDK 1.0 出现的，HashMap JDK 1.2 出现，是替代 HashTable 的



### TreeMap

- 基于红黑树（Red-Black tree）的 NavigableMap 实现
- 该映射根据其键的自然顺序进行排序，或者根据创建映射时提供的 Comparator 进行排序，具体取决于使用的构造方法。



## 附：各集合类详细解读

- ArrayList 源码解读
- LinkedList 源码解读
- HashSet 源码解读
- TreeSet 源码解读
- HashMap 源码解读
- HashTable 源码解读
- TreeMap 源码解读

