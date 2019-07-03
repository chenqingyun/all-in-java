## synchronized 的实现原理与应用



### synchronized 的底层实现

JVM 规范中描述了 synchronized 在 JVM 的实现过程。JVM 基于进入和退出 Monitor 对象来实现方法同步和代码块同步。

代码块同步是使用 **monitorenter** 和 **monitorexit** 指令实现的。方法同步是另一种方式实现的，但也可以用这两个指令实现。

monitorenter 指令是在编译后插入到同步代码块开始的位置，monitorexit 指令是插入到代码块结束处和异常处，JVM 要保证每个 monitorenter 必须要有对应的 monitorexit 与之配对。任何对象都有一个 monitor 与之关联，当一个 monitor 被持有后，它将处于锁定状态。线程执行到 monitorenter 指令时，将会尝试获取对象对应的 monitor 的所有权，即尝试获得对象的锁。



[深入理解多线程（一）——Synchronized的实现原理](https://www.hollischuang.com/archives/1883)



### 锁的升级与对比

JDK 1.6 之后，锁有 4 种状态，级别从高到底：无锁状态、偏向锁状态、轻量级锁状态和重量级锁状态。

这几种状态会随着线程的竞争情况逐渐升级。锁可以升级但不能降级。



[聊聊并发（二）Java SE1.6中的Synchronized](http://ifeve.com/java-synchronized/)

[JVM源码分析之synchronized实现](https://www.jianshu.com/p/c5058b6fe8e5)