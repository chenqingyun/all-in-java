# HashMap 源码解析

## 目录

- [概要](#概要)
- [属性](#属性)



> 本文源码基于 Java 8

## 概要

HashMap 位于 java.util 包下，继承 AbstractMap，实现自 Map、Cloneable 和 Serializable 接口：

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable
```

- 继承 AbstractMap，实现 Map：实现添加、删除、获取和遍历等功能。
- 实现 Cloneable：实现了clone()方法，可被克隆
- 实现 Serializable：支持序列化。



**HashMap 特点：**

- 底层数据结构是数组 + 链表 + 红黑树

- 允许 key 为 null，value 为 null
- 线程不安全
- 不保证映射的顺序



## 属性

```java
    /**
     * HashMap 默认初始化容量 16，必须为 2 的幂
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; 

    /**
     * 最大容量。当传入构造函数的值过大时将使用这个值替换。必须为 2 的幂且小于 2 的 30 次方.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 默认加载因子为 0.75，当使用默认构造函数初始化时。
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * The bin count threshold for using a tree rather than list for a
     * bin.  Bins are converted to trees when adding an element to a
     * bin with at least this many nodes. The value must be greater
     * than 2 and should be at least 8 to mesh with assumptions in
     * tree removal about conversion back to plain bins upon
     * shrinkage.
     * 桶的树化阈值，即链表转成红黑树的阈值。
     * 当添加到桶中的元素个数 >= 8 个节点时，桶将被转换成树。
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * The bin count threshold for untreeifying a (split) bin during a
     * resize operation. Should be less than TREEIFY_THRESHOLD, and at
     * most 6 to mesh with shrinkage detection under removal.
     * 桶的链表还原阈值，即红黑树转为链表的阈值。
     * 当执行 resize 操作时，桶中的元素 < 8，且红黑树节点 <= 6 时，红黑树结构会转换为链表结构
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * The smallest table capacity for which bins may be treeified.
     * (Otherwise the table is resized if too many nodes in a bin.)
     * Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
     * between resizing and treeification thresholds.
     * 最小树化容量阈值。
     * 当哈希表的容量 >= 64时，桶将转换为树。否则，若桶内元素过多，哈希表的容量则会扩大，无法树形化。
     * 为了避免扩容和树形化的冲突，这个值不能小于 4 * TREEIFY_THRESHOLD。
     */
    static final int MIN_TREEIFY_CAPACITY = 64;

    /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     * (We also tolerate length zero in some operations to allow
     * bootstrapping mechanics that are currently not needed.)
     * 首次使用时进行初始化，必要时进行扩容。当允许时，长度总为 2 的幂。
     */
    transient Node<K,V>[] table;

    /**
     * Holds cached entrySet(). Note that AbstractMap fields are used
     * for keySet() and values().
     */
    transient Set<Map.Entry<K,V>> entrySet;

    /**
     * map 中键值对的数量
     */
    transient int size;

    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     *
     * HashMap 结构被修改的次数
     */
    transient int modCount;

    /**
     * 下次要调整的阈值，等于 capacity * load factor
     * 用于判断是否需要调整 HashMap 的容量
     * @serial
     */
    int threshold;

    /**
     * 哈希表加载因子
     * @serial
     */
    final float loadFactor;
```




