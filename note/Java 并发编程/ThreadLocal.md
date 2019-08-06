## ThreadLocal

保存线程本地化对象的容器。当运行于多线程环境的某个对象使用 ThreadLocal 维护变量时，ThreadLocal 为每个使用该变量的线程分配一个独立的变量副本。所以每个线程都可以独立的改变自己的副本，不会影响到其他线程的副本。从线程角度看，这个变量就是线程独有的本地变量。



**ThreadLocal 如何做到为每个线程维护一份独立的变量副本呢？**

在 ThreadLocal 类中为每个线程维护有一个 ThreadLocalMap，每个线程访问某 ThreadLocal 变量后，都会在自己的 Map 内维护该 ThreadLocal 变量与具体实例的映射。

因为是每个线程维护自己的 Map ，因此不存在线程安全问题



**内存泄漏**

但是由于每个线程访问某 ThreadLocal 变量后，都会在自己的 Map 内维护该 ThreadLocal 变量与具体实例的映射，如果不删除这些引用（映射），则这些 ThreadLocal 不能被回收，可能会造成内存泄漏。



ThreadLocalMap 的 set 方法中，通过 replaceStaleEntry 方法将所有键为 null 的 Entry 的值设置为 null，从而使得该值可被回收。另外，会在 rehash 方法中通过 expungeStaleEntry 方法将键和值为 null 的 Entry 设置为 null 从而使得该 Entry 可被回收。通过这种方式，ThreadLocal 可防止内存泄漏。



**ThreadLocal 适用于如下两种场景**

- 每个线程需要有自己单独的实例
- 实例需要在多个方法中共享，但不希望被多线程共享



对于第一点，每个线程拥有自己实例，实现它的方式很多。例如可以在线程内部构建一个单独的实例。ThreadLocal 可以以非常方便的形式满足该需求。

对于第二点，可以在满足第一点（每个线程有自己的实例）的条件下，通过方法间引用传递的形式实现。ThreadLocal 使得代码耦合度更低，且实现更优雅。



**ThreadLocal 与同步机制的比较**

- 在同步机制中，变量是多个线程共享的，通过对象的锁机制保证同一时间只有一个线程访问变量；
- ThreadLocal 为每个线程提供一个独立的变量副本，隔离了多个线程对访问数据的冲突。因为每个线程访问的是自己的变量副本，因此就不需要对变量进行同步。

概况来说，同步机制是「 以时间换空间 」的方式：访问串行化，对象共享化；ThreadLocal 是 「 以空间换时间」的方式：访问并行化，对象独享化。



如果需要进行多个线程之间进行通信，则使用同步机制；如果需要隔离多个线程之间的共享冲突，可以使用 ThreadLocal。





[正确理解Thread Local的原理与适用场景](http://www.jasongj.com/java/threadlocal/)