## 显示锁：Lock

### 目录

- [Lock 接口](#lock-接口)
- [显示锁的调度策略](#显示锁的调度策略)
- [synchronized 和 Lock 的比较](#synchronized-和-lock-的比较)



显示锁：java.util.concurrent.locks.Lock 接口的实例

java.util.concurrent.locks.ReentrantLock 是 Lock 接口的默认实现类。



### Lock 接口

Lock 接口源码：

```java
public interface Lock {

    /**
     * 获得锁
     *
     * 如果锁被其他线程持有，则出于线程调度的目的，当前线程将被禁用，并处于休眠状态，直到获取锁
     */
    void lock();

    /**
     * 获取锁时能响应中断。
     * 如果当前线程未被中断，则获取锁。
     * 如果锁被其他线程持有，那么当前线程将被禁用，并且在发生以下两种情况之一之前，将一直处于休眠状态：
     *    1. 锁由当前线程获得;或者
     *    2. 其他线程中断了当前线程，即在锁的获取中可以中断当前线程。
     * 
     * 如果当前线程：
     *    1. 在进入该方法时是中断状态；或者
     *    2. 在等待获取锁的同时被中断。
     * 然后抛出 InterruptedException，当前当前线程的中断状态被清除。
     * 
     * 因为此方法是一个显式中断点，所以要优先考虑响应中断，而不是响应锁的普通获取或重入获取。 
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * 尝试非阻塞的获取锁
     *
     * 如果锁可用则获取锁并立即返回 true。如果锁不可用，则此方法将立即返回 false 。
     * 
     * 此方法的典型应用：
     *  
     * Lock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     *
     * 此用法可确保当锁被获取时是未锁定的，如果未获得锁，则不会去解锁。
     *
     */
    boolean tryLock();

    /**
     * 在指定时间内获取锁，并且线程未被中断，则获取锁，并立即返回 true。 
     * 如果锁被其他线程占用，则当前线程将被禁用，并且处于休眠状态，直至发生以下三种情况之一：
     *    1. 锁被当前线程获取；或者
     *    2. 其他线程中断了当前线程；或者
     *    3. 指定的等待时间过去了。
     *
     * 如果锁获取，则返回值true。
     *
     * 如果当前线程:
     *    1. 在进入该方法时是中断状态；或者
     *    2. 在等待获取锁的同时被中断。
     * 然后抛出 InterruptedException，当前线程的中断状态被清除。
     *
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * 释放锁
     */
    void unlock();

    /**
     * 返回一个绑定到该 Lock 实例的新 Condition 实例。
     */
    Condition newCondition();
}
```



总结下几种 lock 方法的区别：

- lock()：调用后一直阻塞直到获得锁。
- tryLock()：尝试是否能获得锁，如果不能获得立即返回。
- tryLock( long time, TimeUnit unit )：在指定时间内尝试获得锁。
- lockInterruptibly() ：调用后一直阻塞直到获得锁，但是接受中断信号



### 显示锁的调度策略

锁的调度包括公平策略和非公平策略，对应锁就称为公平锁和非公平锁。

内部锁属于非公平锁。



在非公平锁策略之下，不一定说先来排队的线程就就先会得到机会加锁，而是出现各种线程随意抢占的情况。

公平锁策略下，线程会进入等待队列，AQS (AbstractQueuedSynchronizer ) ，按照先来后到的顺序，依次进入等待队列中排队的，不会抢占加锁，非常的公平。



ReentrantLock 有两个构造器：

```java
    /**
     * 默认构造器，创建一个非公平锁
     */
    public ReentrantLock() {
        sync = new NonfairSync();
    }

    /**
     * 可传入 fair 参数，true 表示创建公平锁，false 表示创建非公平锁
     */
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
```

公平锁保障锁调度的公平性一般是以增加了线程的暂停和唤醒的可能性，即增加了上下文切换的代价的。

因此，公平锁适用于持有锁时间较长或者线程申请锁的平均间隔时间较长的情形。

总的来说，使用公平锁的开销比使用非公平锁的开销大。因此显示锁默认使用非公平锁策略。



### synchronized 和 Lock 的比较

- synchronized 是 JVM 层面上实现的，Lock 是通过代码层面实现的。

- synchronized 简单易用，且不会导致锁泄露；Lock 容易被错用而导致锁泄露，所以要注意在 finally 块中释放锁。
- synchronized 无灵活性，不支持跨方法；Lock 较为灵活，可在一个方法中加锁，另一个方法中释放锁。
- 如果内部锁的持有线程一直不释放这个锁，那么会阻塞其他线程；Lock 可以使用 tryLock 方法尝试获取锁，如果锁被其他线程持有不会导致当前线程暂停而是直接返回 false。
- 在锁的调度方面，synchronized 只支持非公平锁；Lock 支持公平锁和非公平锁策略。
- 在高争用下，显示锁的性能比内部锁的性能好。在 JDK 1.6 之后，对内部锁做了一些优化，显示锁和内部锁的可伸缩性差异已经变得非常小了。

