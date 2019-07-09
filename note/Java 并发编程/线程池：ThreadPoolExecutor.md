## 线程池：ThreadPoolExecutor

### 目录

- [为什么使用线程池？使用线程池有什么好处？](#为什么使用线程池使用线程池有什么好处)
- [线程池的创建](#线程池的创建)
- [向线程池提交任务](#向线程池提交任务)
- [关闭线程池](#关闭线程池)
- [如何合理的配置线程池](#如何合理的配置线程池)
  - [如何选择线程数](#如何选择线程数)
- [线程池的监控](#线程池的监控)



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

Executors 工厂类也可以用来创建线程池，但阿里巴巴规约中规定，线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，这样 

的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险 。

说明：Executors 返回的线程池对象的弊端如下:

- FixedThreadPool 和 SingleThreadPool：允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
- CachedThreadPool 和 ScheduledThreadPool：允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。



### 线程池的工作流程

1. 如果线程池中的线程小于 corePoolSize 时就会创建新线程直接执行任务。
2. 如果线程池中的线程大于 corePoolSize 时就会暂时把任务存储到工作队列 workQueue 中等待执行。
3. 如果工作队列 workQueue 也满时：当线程数小于最大线程池数 maximumPoolSize 时就会创建新线程来处理，而线程数大于等于最大线程池数 maximumPoolSize 时就会执行拒绝策略。



### 向线程池提交任务

可以使用两个方法向线程池提交任务，execute() 方法和submit() 方法。

- execute() 方法用以提交不需要返回值的任务，所以无法判断任务是否执行成功。

- submit() 方法用以提交需要返回值的任务。线程池会返回一个 Future 类型的对象，通过这个对象可以判断任务是否执行完成。通过 future.get() 方法获得返回值，会阻塞当前线程，直到任务完成拿到返回值。通过 get(long timeout, TimeUnit unit) 方法会阻塞当前线程一段时间后立即返回，这时候任务有可能没有完成。

  ```java
       Callable<Object> callable = new Callable<Object>() {
              @Override
              public Object call() throws Exception {
                  return null;
              }
          };
          Future<Object> future = threadPoolExecutor.submit(callable);
          try {
              Object o = future.get();
          }catch (InterruptedException e){
              // 处理中断异常
          }catch (ExecutionException e){
              // 处理无法执行的任务的异常
          }
          finally {
              // 关闭线程池
              threadPoolExecutor.shutdown();
          }
  ```



execute() 方法源码：

```java 
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        int c = ctl.get();
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            if (! isRunning(recheck) && remove(command))
                reject(command);
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
        else if (!addWorker(command, false))
            reject(command);
    }
```

1. 获取当前线程池的状态。
2. 当前线程数量小于 corePoolSize 时创建一个新的线程运行。
3. 如果当前线程处于运行状态，并且写入阻塞队列成功。
4. 双重检查，再次获取线程状态；如果线程状态变了（非运行状态）就需要从阻塞队列移除任务，并尝试判断线程是否全部执行完毕。同时执行拒绝策略。
5. 如果当前线程池为空就新创建一个线程并执行。
6. 如果在第三步的判断为非运行状态，尝试新建线程，如果失败则执行拒绝策略。

[如何优雅的使用和理解线程池](https://segmentfault.com/a/1190000015808897)



### 关闭线程池

线程池自动关闭的两个条件：

- 线程池的引用不可达；
- 线程池中没有线程。指线程池中的所有线程都已运行完自动消亡。通常我们创建线程池没有超时策略，因此并不会自动关闭。



可以通过调用线程池的 shutdown() 和 shutdownNow() 方法来手动关闭线程池。

- shutdown() 方法：线程池拒接收新提交的任务，同时立马关闭线程池，线程池里的任务不再执行。
- shutdownNow() 方法：线程池拒接收新提交的任务，同时等待线程池里的任务执行完毕后关闭线程池。

两个方法源码：

```java
    public List<Runnable> shutdownNow() {
        List<Runnable> tasks;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            checkShutdownAccess();
            advanceRunState(STOP);
            interruptWorkers();
            tasks = drainQueue();
        } finally {
            mainLock.unlock();
        }
        tryTerminate();
        return tasks;
    }

    public void shutdown() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            checkShutdownAccess();
            advanceRunState(SHUTDOWN);
            interruptIdleWorkers();
            onShutdown(); // hook for ScheduledThreadPoolExecutor
        } finally {
            mainLock.unlock();
        }
        tryTerminate();
    }
```

原理是：遍历线程池中的工作线程，然后逐个调用线程的 interrupt() 方法中断线程。所以，无法响应中断的任务可能无法终止。

两者的不同之处：

- shutdownNow() 方法首先将线程池的状态设置为「 STOP 」，然后尝试停止所有正在执行或暂停任务的线程，并返回等待执行的列表。

- shutdown() 方法只将线程池状态设置为「 SHUTDOWN 」，然后中断所有没有在执行任务的线程。



只要调用shutdown() 或 shutdownNow() 方法，isShutdown() 方法就会返回 true。

当所有的任务都被关闭了，才表示线程池关闭成功。要想等待线程池关闭，通过调用 awaitTermination(long timeout, TimeUnit unit) 方法阻塞来阻塞等待，直到调用 isTerminaed() 方法返回 true。



通常调用 shutdown() 方法来关闭线程池，如果任务不一定要执行完，则可以调用 shutdownNow() 方法。



### 如何合理的配置线程池

首先要分析任务的特性，从几个方面：

- 任务的性质：CPU 密集型任务、IO 密集型任务和混合型任务。
- 任务的优先级：高、中、低。
- 任务的执行时间：长、中、短。
- 任务的依赖性：是否依赖其他系统资源，如数据库连接。



#### 如何选择线程数

- CPU 密集型任务应配置尽可能小的线程，如 **N + 1** 个线程。（N 为 CPU 总核数）
- IO 密集型任务并不一定一直在执行任务，可以配置尽可能多的线程，如配置 **2 * N + 1**。



可以通过 Runtime.getRuntime().availableProcessors() 获得 CPU 核数。

这个额外的线程能确保利用等待空闲， CPU 的时钟周期不会被浪费。



IO优化中，这样的估算公式可能更适合：

```
最佳线程数目 = （线程等待时间与线程CPU时间之比 + 1）* CPU 数目
```



**线程等待时间所占比例越高，需要越多线程。线程 CPU 时间所占比例越高，需要越少线程。**

[如何合理地估算线程池大小？](http://ifeve.com/how-to-calculate-threadpool-size/)





- 优先级不同的任务可以使用优先级队列 PriorityBlockingQueue 来处理。它可以让优先级高的任务先得到执行。如果一直有优先级高的任务提交到队列里，那么优先级低的任务可能永远不能执行。
- 执行时间不同的任务可以交给不同规模的线程池来处理，或者也可以使用优先级队列，让执行时间短的任务先执行。
- 依赖数据库连接池的任务，因为线程提交 SQL 后需要等待数据库返回结果，如果等待的时间越长 CPU 空闲时间就越长，那么线程数应该设置越大，这样才能更好的利用 CPU。
- **建议使用有界队列**，有界队列能增加系统的稳定性和预警能力，可以根据需要设大一点，比如几千。如果使用无界队列，线程池的队列会越来越多，有可能会撑满内存，导致整个系统不可用。



扩展 [线程池调优策略](https://blog.csdn.net/luofenghan/article/details/78596950#调优策略-3)

### 线程池的监控

**通过线程池提供的参数进行监控**。线程池里有一些属性在监控线程池的时候可以使用

- taskCount：线程池需要执行的任务数量。
- completedTaskCount：线程池在运行过程中已完成的任务数量。小于或等于 taskCount。
- largestPoolSize：线程池曾经创建过的最大线程数量。通过这个数据可以知道线程池是否满过。如等于线程池的最大大小，则表示线程池曾经满了。
- getPoolSize：线程池的线程数量。如果线程池不销毁的话，池里的线程不会自动销毁，所以这个大小只增不减。
- getActiveCount：获取活动的线程数。

**通过扩展线程池进行监控**。通过继承线程池并重写线程池的 beforeExecute，afterExecute 和 terminated 方法，我们可以在任务执行前，执行后和线程池关闭前干一些事情。如监控任务的平均执行时间，最大执行时间和最小执行时间等。这几个方法在线程池里是空方法。





### 参考

- 《 Java 并发编程的艺术》方腾飞 魏鹏 程晓玥 著



