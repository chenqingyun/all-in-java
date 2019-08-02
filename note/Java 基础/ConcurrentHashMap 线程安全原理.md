## ConcurrentHashMap 线程安全原理

> 本文源码基于 JDK 1.8



ConcurrentHashMap 是 java.util.concurrent 包下提供的线程安全的 HashMap 版本。

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
    implements ConcurrentMap<K,V>, Serializable
```



ConcurrentHashMap 底层数据结构与 HashMap 相同，仍然采用 **数组 + 链表 + 红黑树**结构。



ConcurrentHashMap 是线程安全的，采用的方式是 **CAS 操作 + synchronized 同步块**，一些属性是 volatile 修饰的。例如 `volatile Node<K,V>[] table`。



HashTable 是锁住整个方法的，因此效率低下。



与 HashMap 比较

- HashMap 线程不安全
- ConcurrentHashMap 部分属性用 volatile 修饰保证了可见性，比如 Node 节点 value 和 next 属性用 volatile 修饰了。
- ConcurrentHashMap 不允许 null 作为 key 和 value。





[jdk1.8的HashMap和ConcurrentHashMap](https://my.oschina.net/pingpangkuangmo/blog/817973)