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



### 与 HashMap 比较

- HashMap 线程不安全
- ConcurrentHashMap 部分属性用 volatile 修饰保证了可见性，比如 Node 节点 value 和 next 属性用 volatile 修饰了。
- ConcurrentHashMap 不允许 null 作为 key 和 value。



### 与 HashTable 比较

同样是线程安全的，ConcurrentHashMap 线程安全是通过  **CAS 操作 + synchronized 同步块** 来保证的，HashTable 是在方法上加 synchronized 关键字，性能上 ConcurrentHashMap 比 HashTable 好。



### 为什么 ConcurrentHashMap 不能完全替代 HashTable

因为 ConcurrentHashMap 是弱一致性，其 get 方法没有上锁，会导致 get 元素的并不是当前并行还未执行完的 put 的值，读取到的数据并不一定是最终的值，在一些要求强一致性的场景下可能会出错。例如：需要判断当前值是否为 A 如果不为 A 则修改为 C，但是当前值为 B 而有个 put 方法将其更新为 A 还没执行完，则最终改值就是 A ，可能会造成后续程序或业务的异常。**如果要求强一致性，那么可以使用 Collections.synchronizedMap() 方法**。



[jdk1.8的HashMap和ConcurrentHashMap](https://my.oschina.net/pingpangkuangmo/blog/817973)