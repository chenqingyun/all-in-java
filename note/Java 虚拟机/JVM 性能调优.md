## JVM 性能调优



### JVM 调优原则

- 多数的 Java 应用不需要在服务器上进行 JVM 优化；
- 多数导致 GC 问题的 Java 应用，都不是因为我们参数设置错误，而是代码问题；
- 在应用上线之前，先考虑将机器的 JVM 参数设置到最优（最适合）；
- 减少创建对象的数量；
- 减少使用全局变量和大对象；
- JVM 优化是到最后不得已才采用的手段；
- 在实际使用中，分析 GC 情况优化代码比优化 JVM 参数更好；



### JVM调优目标

- GC低停顿；
- GC低频率；
- 低内存占用； 
- 高吞吐量；





### JVM调优的一般步骤为：

1. 分析 GC 日志及 dump 文件，判断是否需要优化，确定瓶颈问题点；

2. 确定 JVM 调优量化目标；

3. 确定 JVM 调优参数（根据历史 JVM 参数来调整）；

4. 调优一台服务器，对比观察调优前后的差异；

5. 不断的分析和调整，直到找到合适的 JVM 参数配置；

6. 找到最合适的参数，将这些参数应用到所有服务器，并进行后续跟踪。





### JVM 自带的命令

#### jps

JVM Process Status Tool，显示指定系统内所有的 HotSpot 虚拟机进程。



#### jstat

jstat(JVM statistics Monitoring) 用于输出监视虚拟机运行时状态信息，它可以显示出虚拟机进程中的类装载、内存、垃圾收集、JIT编译等运行数据。



#### jmap

jmap (JVM Memory Map) 用来查看堆内存使用状况，用于生成 heap dump 文件。一般结合 jhat 使用。



使用 jmap -heap pid 查看进程堆内存使用情况，包括使用的 GC 算法、堆配置参数和各代中堆内存使用情况。

使用 jmap -histo[:live] pid 查看堆内存中的对象数目、大小统计直方图，如果带上 live 则只统计活对象。

用 jmap 把进程内存使用情况 dump 到文件中，再用 jhat 分析查看。



#### jhat

jhat (JVM Heap Analysis Tool) 命令是与 jmap 搭配使用，用来分析 jmap 生成的 dump 文件。

jhat 内置了一个微型的HTTP/HTML服务器，生成 dump 的分析结果后，可以在浏览器中查看。

**在此要注意，一般不会直接在服务器上进行分析，因为 jhat 是一个耗时并且耗费硬件资源的过程，一般把服务器生成的 dump 文件复制到本地或其他机器上进行分析。**



#### jstack

jstack 主要用来查看某个 Java 进程内的线程堆栈信息。



找出某个 Java 进程中最耗费 CPU 的 Java 线程并定位堆栈信息，用到的命令有ps、top、printf、jstack、grep

1. grep 命令找出 Java 进程 ID；
2. 找出该进程内最耗费 CPU 的线程，可以使用 ps -Lfp pid 或者 ps -mp pid -o THREAD, tid, time或者 top -Hp pid；
3. printf  "%x\n" [ PID ] 输出十六进制值；
4. jstack 输出进程的堆栈信息，根据线程 ID 的十六进制值 grep；



参数：

##### -class

监视类装载、卸载数量、总空间以及耗费的时间。



##### -compiler

输出 JIT 编译过的方法数量耗时等。



##### - gc

垃圾回收堆的行为统计，**常用命令**





#### jinfo

jinfo(JVM Configuration info) 这个命令作用是实时查看和调整虚拟机运行参数。 之前的 jps -v 口令只能查看到显示指定的参数，如果想要查看未被显示指定的参数的值就要使用 jinfo 口令



参考：

- [jvm系列(四):jvm调优-命令篇](https://mp.weixin.qq.com/s/QNr8somjodyvU9dRAQG2oA)

- [JVM调优参数简介、调优目标及调优经验](https://blog.csdn.net/jisuanjiguoba/article/details/80176223)

- [JVM性能调优监控工具jps、jstack、jmap、jhat、jstat、hprof使用详解](https://my.oschina.net/feichexia/blog/196575) 


