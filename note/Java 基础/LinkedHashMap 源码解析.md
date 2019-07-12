## LinkedHashMap 源码解析



> 本文源码基于 Java 8

### 概要

LinkedHashMap 继承自 HashMap，实现 Map 接口。

```java
public class LinkedHashMap<K,V>
    extends HashMap<K,V>
    implements Map<K,V>
```



LinkedHashMap 与 HashMap 不同的是 LInkedHashMap 的遍历顺序和插入顺序是一致的。



### 底层数据结构

 LinkedHashMap 继承自 HashMap，所以它的底层数据结构依然是**数组 + 链表 + 红黑树**，不过这个链表是双向链表。

LinkedHashMap 中的中的 Entry 节点：

```java
    static class Entry<K,V> extends HashMap.Node<K,V> {
        // 用以维护双向链表
        Entry<K,V> before, after;
        Entry(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }
```



LinkedHashMap 在 HashMap 基础上，通过维护一条**双向链表**，解决了 HashMap 不能随时保持遍历顺序和插入顺序一致的问题。



[LinkedHashMap 源码详细分析（JDK1.8）](https://segmentfault.com/a/1190000012964859)