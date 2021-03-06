## Java 内存模型

### 目录

- 并发编程模型的两个关键问题
- Java 内存模型的抽象结构
- volatile
- Happens-Before 规则



Java 内存模型，Java Memory Model，JMM。Java 内存模型定义了多线程之间共享变量的可见性以及如何在需要的时候对共享变量进行同步。



### 并发编程模型的两个关键问题

- 线程之间如何通信
- 线程之间如何同步



**Java 的并发采用的是共享内存模型**



#### 线程之间的通信

- [等待 / 通知机制（wait / notify）](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%AD%89%E5%BE%85%E9%80%9A%E7%9F%A5%E6%9C%BA%E5%88%B6%EF%BC%9Await%20%E5%92%8C%20notify.md)
- 管道通信：就是使用 java.io.PipedInputStream 和 java.io.PipedOutputStream 进行通信
- 同步
- volatile 共享内存
- join 方法
- ...



[深入理解线程通信](https://crossoverjie.top/2018/03/16/java-senior/thread-communication/)

#### 线程之间的同步

同步是指程序用于控制不同线程之间操作发生相对顺序的机制。

在共享内存并发模型里，同步是显式进行的。程序员必须显式指定某个方法或某段代码需要在线程之间互斥执行。



同步方式：

- synchronized 关键字
- lock
- 阻塞队列
- CAS 机制
- ...



### Java 内存模型的抽象结构

Java 线程之间的通信由 Java 内存模型控制， JMM 决定了了一个线程对一个共享变量的写入何时对另一个线程可见。

Java 内存模型将内存划分为主内存与工作内存（工作内存是 JVM 的抽象概念，并不真实存在）。

Java 内存模型规定了所有的共享变量都存储在主内存中。

每条线程都有自己的工作内存，保存了被该线程使用到的变量的主内存副本拷贝 (对象的引用，对象被访问到的字段) ，线程对变量的读取都在工作内存中进行。不同线程之间无法访问对方工作内存中的变量。

主内存主要对应于 Java 堆中的对象实例数据部分，工作内存对应虚拟机栈中的部分区域。



#### 内存间交互操作
8种操作：lock，unlock，read，load，use，assign，store，write

相关文章：
[Java 内存模型-同步八种操作](https://blog.csdn.net/timchen525/article/details/80412699)



### volatile
[volatile 关键字有什么作用？如何保证可见性和有序性？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/volatile%20%E5%85%B3%E9%94%AE%E5%AD%97.md)



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


