## Java 内存模型

### 目录

- 主内存与工作内存
- volatile
- Happens-Before 规则



Java 内存模型，Java Memory Model，JMM

### 主内存与工作内存

Java 内存模型规定了所有的变量都存储在主内存中。

每条线程都有自己的工作内存，保存了被该线程使用到的变量的主内存副本拷贝 (对象的引用，对象被访问到的字段) ，线程对变量的读取都在工作内存中进行。不同线程之间无法访问对方工作内存中的变量。

主内存主要对应于 Java 堆中的对象实例数据部分，工作内存对应虚拟机栈中的部分区域。



#### 内存间交互操作
8种操作：lock，unlock，read，load，use，assign，store，write

相关文章：
[Java 内存模型-同步八种操作](https://blog.csdn.net/timchen525/article/details/80412699)



### volatile
轻量级的 synchronized，大多数场景下 volatile 的总开销比加锁低。

volatile 关键字可以保证可见性，有序性，但不保证原子性。可以保证 long / double 型变量读写操作的原子性。



#### 如何保证可见性

被 volatile 定义的变量，每次修改变量后都必须立即同步至主内存中，每次使用该变量都必须先从主内存刷新最新的值，保证能看见其他线程对该变量所做的修改。

**缓存一致性协议**



#### 如何保证有序性

「 内存屏障 」禁止指令重排序。

对于 volatile 变量的写操作，JVM 会在该操作之前插入一个释放屏障，在该操作之后插入一个存储屏障。

```java
a = 1; // 普通变量的读写操作

// Release Barrier. JVM 插入一个内存屏障
volatileVal = true; // volatile 变量的写操作
// Store Barrier. JVM 插入一个内存屏障
```

释放屏障禁止了 volatile 变量的写操作与之前的操作进行重排序，保证了之前的操作会在 volatile 变量的写操作之前提交，即保证了多个线程执行的感知顺序与相应的源代码的顺序一致，即保证了有序性。

存储屏障具有冲刷处理器缓存的作用，因此在 volatile 变量的写操作之后插入一个存储屏障可以使得在存储屏障之前的操作的结果对其他处理器来说是同步的。



对于 volatile 变量的读操作，JVM 会在该操作之前插入一个加载屏障，在该操作之后插入一个获取屏障。

```java
// Load Barrier. JVM 插入一个内存屏障
localVal = volatileVal; // volatile 变量的读操作
// Acquire Barrier. JVM 插入一个内存屏障

b = a; // 普通变量的读写操作
```

加载屏障也具有冲刷处理器缓存的作用

获取屏障禁止了 volatile 变量读操作与之后的其他操作进行重排序。





### 原子性、可见性和有序性



### Happens-Before 规则

**前面一个操作的结果对后续操作是可见的**。

如果 A 事件是导致 B 事件的起因，那么 A 事件一定是先于 (Happens-Before) B 事件发生的，这个就是 Happens-Before 语义的现实理解。

Happens-Before 规则约束了编译器的优化行为，虽允许编译器优化，但要求编译器优化后一定要遵守「 Happens-Before 规则 」。



一共有以下几项规则：

- **程序的顺序性规则**：在一个线程中，按照程序的顺序，前面的操作 Happens-Before 后面的任意操作。

- **volatile 规则**：指对一个 volatile 变量的写操作，Happens-Before 后续对这个 volatile 变量的读操作。

- **传递性规则**：如果 A Happens-Before B，且 B Happens-Before C，那么 A Happens- Before C。

- **管程中的锁规则**：对一个锁的解锁操作 Happens-Before 后续对这个锁的加锁操作。

- **线程 start() 规则**：如果在线程 A 中调用线程 B 的 start() 方法，那么线程 B 能看到线程 A 在启动线程 B 之前的操作。

- **线程 join() 规则**：如果在线程 A 中调用线程 B 的 join() 方法，那么当线程 B 完成之后，线程 A 能看到线程 B 的操作。

- **线程中断规则**：对线程 interrupt() 方法的调用先行发生于被中断线程的代码检测到中断事
  件的发生，可以通过 Thread.interrupted() 方法检测到是否有中断发生。

- **对象终结规则**：一个对象的初始化的完成，也就是构造函数执行的结束一定 Happens-Before 它的 finalize() 方法。

  

## 参考

- 《深入理解 Java 虚拟机 第2版》周志明
- 极客时间「 Java 并发编程实战-02 Java 内存模型：看 Java 如何解决可见性和有序性问题 」


