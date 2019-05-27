## OutOfMemoryError 异常出现原因及解决方案
### 目录

- [Java 堆内存溢出](#java-堆内存溢出)
- [虚拟机栈和本地方法栈溢出](#虚拟机栈和本地方法栈溢出)
- [方法区和运行时常量池溢出](#方法区和运行时常量池溢出)
- [本地直接内存溢出](#本地直接内存溢出)
- [堆外内存溢出](#堆外内存溢出)
- [GC回收时内存溢出](#回收时内存溢出)

### Java 堆内存溢出
> java.lang.OutOfMemoryError: Java heap space



 JVM 启动参数：

- 「-Xms」 设置最小堆内存

- 「-Xmx」 设置最大堆内存，设置一样避免堆自动扩展
- 「-XX:+HeapDumpOnOutOfMemoryError」 可以让虚拟机在出现内存溢出异常时 Dump 出当前的内存堆转储快照以便事后分析。

```
/**
 *
 * Java Heap OutOfMemoryError
 *
 * VM Args:-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 *
 */
public class HeapOOM {
    public static void main(String[] args) {
        List<Object> objects = new ArrayList<>();
        while (true){
            objects.add(new Object());
        }

    }
}
```



**原因：**

- 内存泄漏（Memory Leak）：指由于疏忽或错误造成程序未能释放已经不再使用的内存，从而造成了内存的浪费。
- 堆内存设置太小。
- 存在过多大对象，对象生命周期过长。

**解决：**

先通过内存映像分析工具对 Dump 出来的堆转储快照进行分析，确认内存中的对象是否是必要的，也就是分清是内存泄露还是内存溢出。

- 如果是内存泄漏

  查看泄露对象到 GC Roots 的引用链，就能查找到泄漏对象是通过怎样的路径与GC Roots相关联并导致垃圾收集器无法自动回收它们的。掌握了泄漏对象的类型信息及GC Roots引用链的信息，就可以比较准确的定位出泄漏代码的位置。

- 不存在内存泄漏

  - 检查虚拟机的堆参数(-Xms 与 -Xmx ) 是否设置太小；
  - 从代码上检查是否存在某些对象生命周期过长，持有状态时间过长的情况；
  - 检查是否存在大循环中创建对象，可在循环外创建对象引用，循环内引用到 new 的对象，循环结束后将对象引用赋值为 null。



### 虚拟机栈和本地方法栈溢出

>java.lang.StackOverflowError
>
>java.lang.OutOfMemoryError: Unable to create new native thread

设置栈容量参数「-Xss」 

如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出 StackOverflowError 异常；

如果虚拟机在扩展栈时无法申请到足够的内存空间，将抛出 OutOfMemoryError 异常。



**java.lang.StackOverflowError** 

在单个线程下，虚拟机栈容量太小或栈帧太大，当内存无法分配的时候，虚拟机栈抛出的都是StackOverflowError。

StackOverflowError 异常详细见文章 [StackOverflowError 详解](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/StackOverflowError.md)



**java.lang.OutOfMemoryError: Unable to create new native thread**

在高并发请求服务器时，会出现该异常。

原因：

- 创建过多线程。



解决：

- 减少线程数量；

- 在不能减少线程数的情况下，减少堆内存和减少每个线程分配的栈容量来换取更多的线程，即通过「减少内存」的手段来解决内存溢出。 



### 方法区和运行时常量池溢出

> java.lang.OutOfMemoryError: Metaspace



参数设置：「-XX:MetaspaceSize」 和 「-XX:MaxMetaspaceSize」 



原因：
运行时产生大量的类



解决：

- 将方法区的大小调大。
- 在经常动态生成大量 Class 的应用中，需要特别注意类的回收状况。



### 本机直接内存溢出

> java.lang.OutOfMemoryError:



直接内存容量通过 「-XX:MaxDirectMemorySize」设定，如果不指定，与 Java 堆最大值一样。 



### 堆外内存溢出

> java.lang.OutOfMemoryError: Direct buffer memory



https://my.oschina.net/dabird/blog/593646/

https://www.raychase.net/1526





### GC回收时内存溢出

> java.lang.OutOfMemoryError: GC overhead limit exceeded



[详解 java.lang.OutOfMemoryError: GC overhead limit exceeded 错误！](https://www.xttblog.com/?p=3347)



### 参考

- 《深入理解 Java 虚拟机 第2版》周志明