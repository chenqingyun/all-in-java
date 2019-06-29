## 显示锁：Lock

显示锁是 JDK 1.5 开始引入的排他锁。

显示锁是 java.util.concurrent.locks.Lock 接口的实例。

类 java.util.concurrent.locks.ReentrantLock 是 Lock 接口的默认实现类。



### Lock 接口

Lock 接口源码：

```java
public interface Lock {

    /**
     * 获得锁
     *
     * 如果锁不可用，则当前线程将被禁用以进行线程调度，并处于休眠状态，直到获取锁
     */
    void lock();

    /**
     * 如果当前线程未被中断，则获取锁。
     *
     * 获取锁，如果可用并立即返回。
     *
     * 如果锁不可用，那么当前线程将被禁用以进行线程调度，并且处于休眠状态，直到发生两件事情之一：
     *    1. 锁是由当前线程获取的;或者
     *    2. 其他线程中断了当前线程，并且支持中断地获取锁（即在锁的获取中可以中断当前线程）。
     * 
     * 如果当前线程：
     *    1. 在进入该方法时是中断状态；或者
     *    2. 是中断状态，并且获得锁了，并且支持中断地获取锁。
     * 然后 InterruptedException 被抛出，当前线程的中断状态被清除。
     * 
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * 仅在调用时锁为空闲状态才获取锁
     *
     * 如果可用，则获取锁定，并立即返回值为true 。 如果锁不可用，则此方法将立即返回值为false 。
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
     * 如果锁在给定的时间内空闲，并且线程未被中断，则获取锁。
     * 如果可用，则获取锁定，并立即返回值为true。 
     * 如果锁不可用，则当前线程将被禁用以进行线程调度，并且处于休眠状态，直至发生三件事情之一：
     *    1. 锁被当前线程获取；或者
     *    2. 其他线程中断了当前线程，并且支持中断地获取锁；或者
     *    3. 指定的等待时间过去了。
     *
     * 如果锁获取，则返回值true。
     *
     * 如果当前线程:
     *    1. 在进入该方法时是中断状态；或者
     *    2. 是 interrupted ，同时获取锁，并支持锁中断，
     * 然后 InterruptedException 被抛出，当前线程的中断状态被清除。
     *
     * 如果指定的等待时间过去，则返回值false 。 如果时间小于或等于零，该方法根本不会等待。
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

