## 线程池：ThreadPoolExecutor

### 目录

- [为什么使用线程池？使用线程池有什么好处？](#为什么使用线程池使用线程池有什么好处)
- [线程池的创建](#线程池的创建)



线程池预先创建若干工作线程，将需要执行的任务提交给线程池，线程池可能将这些任务缓存在队列（工作队列）中，工作线程则不断的从队列中取出任务执行。

![image](https://user-images.githubusercontent.com/19634532/60884165-d551d600-a27e-11e9-825c-764f48733cbf.png)



### 为什么使用线程池？使用线程池有什么好处？



**为什么使用线程池？**

- 线程的创建、启动和销毁都需要开销，如果频繁的创建和销毁线程，将会带来很大的开销，耗费系统资源；
- 线程调度也会有很大的开销。线程的调度会导致上下文切换。频繁的创建和销毁和销毁线程，导致频繁的切换，从而增加处理器资源的消耗；
- 活动的线程也消耗系统资源，如果线程的创建数量没有限制，当大量的客户连接服务器的时候，就会创建出大量的工作线程，他们会消耗大量的内存空间，导致系统的内存空间不足，影响服务器的使用。



**使用线程池有什么好处？**

- **降低资源消耗**。通过重复利用已创建的线程降低线程创建和销毁造成的资源消耗。
- **提高响应速度**。当任务到达时，可以不需要等线程创建就能立即执行。
- **提高线程的可管理性**。如果无限的创建线程，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以统一进行分配、调优和监控。



### 线程池的创建

JDK 提供了 ThreadPoolExecutor 类来创建线程池。

```java
java.util.concurrent.ThreadPoolExecutor

public class ThreadPoolExecutor extends AbstractExecutorService
```

ThreadPoolExecutor 继承 AbstractExecutorService 抽象类，AbstractExecutorService 实现 ExecutorService 接口，ExecutorService 继承 Executor 接口。



ThreadPoolExecutor 类提供了四个构造方法用来创建线程池，实际调用的同一个构造方法：

```java
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
```

- **corePoolSize：核心线程池数大小**。线程池中所保存的线程数，包括空闲线程。当提交一个任务到线程池时，线程池会创建线程来执行任务，即使存在空闲线程也会创建新线程，等到需要执行的任务数大于该核心线程数就不在创建线程。

- **maximumPoolSize：线程池中允许的最大线程数**。

- **keepAliveTime：空闲线程保持存活的时间**。但线程池中的线程数大于 corePoolSize 时，多余的空闲线程在终止之前等待新任务的最大时间。所以，如果任务很多，并且每个任务的执行时间教短，那么可以调大时间，提高线程的利用率。

- **TimeUnit：keepAliveTime 的时间单位**。TimeUnit.DAYS；TimeUnit.HOURS；TimeUnit.MINUTES；TimeUnit.SECONDS；TimeUnit.MILLISECONDS；TimeUnit.MICROSECONDS；TimeUnit.NANOSECONDS。

- **workQueue：用于保存等待执行的任务的阻塞队列**。当线程池中的线程数达到 corePoolSize 时，则把任务放入 workQueue 中，只要线程池中有空闲的核心线程，就从 workQueue 中取任务并处理。该队列将仅保存 execute 方法提交的 Runnable 任务。可以选择以下几种阻塞队列：

  - ArrayBlockingQueue：是一个基于数组结构的有界阻塞队列，此队列按 FIFO（先进先出）原则对元素进行排序。
  - LinkedBlockingQueue：一个基于链表结构的阻塞队列，此队列按 FIFO（先进先出） 排序元素，吞吐量通常要高于 ArrayBlockingQueue。静态工厂方法 Executors.newFixedThreadPool() 使用了这个队列。
  - SynchronousQueue：一个不存储元素的阻塞队列。每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于 LinkedBlockingQueue，静态工厂方法 Executors.newCachedThreadPool() 使用了这个队列。
  - PriorityBlockingQueue：一个具有优先级的无限阻塞队列。

- **threadFactory：用来创建线程的工厂**。通过 Executors.defaultThreadFactory() 创建默认的线程工厂。可以通过线程工厂给每个新建的线程设置有意义的名字。可以使用开源框架 「 guava 」提供的 ThreadFactoryBuilder 给线程设置名字，代码如下：

  ```java
  new ThreadFactoryBuilder().setNameFormat("XX-task-%d").builder();
  ```

- **RejectedExecutionHandler：处理提交的任务的饱和策略**。当队列和线程池都满了，说明线程池处于饱和状态（当 workQueue 已存满，放不下新任务时则新建非核心线程入池并处理请求，直到线程数目达到 maximumPoolSize）那么必须采取一种策略处理提交的新任务。这个策略默认情况下是 AbortPolicy，表示无法处理新任务时抛出异常。有四种策略：

  - ThreadPoolExecutor.AbortPolicy：丢弃任务并抛出 RejectedExecutionException 异常。
  - ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
  - ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）。
  - ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务。



注意：

阿里巴巴规约中规定，线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，这样 

的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险 。

说明：Executors 返回的线程池对象的弊端如下:

- FixedThreadPool 和 SingleThreadPool：允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
- CachedThreadPool 和 ScheduledThreadPool：允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。

