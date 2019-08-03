## 队列同步器：AbstractQueuedSynchronizer

### 目录

- [队列同步器的实现分析](#队列同步器的实现分析)
- [CAS](cas)
  - [CAS 实现原子操作有哪些问题](#cas-实现原子操作有哪些问题)
- [compareAndSetState 和 setState 的区别](#compareAndSetState-和-setState-的区别)



```java
package java.util.concurrent.locks;

public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable
```



队列同步器 AbstractQueuedSynchronizer 是用来构建锁或其他同步组件的基础框架。

使用一个 int 成员变量表示同步状态：

```java
private volatile int state;
```

通过内置的 FIFO 队列来完成资源获取线程的排队工作。



实现同步器的子类通常定义为自定义同步组件的静态内部类



### 队列同步器的实现分析

同步器依赖内部的同步队列（FIFO 双向队列）和同步状态来完成锁的获取和释放。

当前线程获取锁失败时，同步器会将当前线程和等待信息构造成一个节点（Node）并通过 CAS 操作添加到同步队列的尾部，同时会阻塞当前线程；当锁被释放时，会把首节点中的线程唤醒，使其再次尝试获取锁。



#### 独占式获取与释放

独占锁同一时刻有且只有一个线程能够获取到锁。ReenttrantLock、CyclicBarrier 就是独占锁。

##### 锁获取

通过 acquire( int acquires) 方法获取同步状态，该方法对中断不敏感，也就是线程添加到队列后，后续对线程进行中断操作不会从队列中移除。

```java
   public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

- 首先调用自定义同步器的实现的 tryAcquire ( int  arg) 方法尝试获取锁，原子性的更改同步状态；

- 如果获取失败，将调用 addWaiter(Node mode) 方法将当前线程构造成一个节点通过 CAS 操作加入到同步队列的尾部；

- 然后调用 acquireQueued(final Node node, int arg) 方法再次尝试获取锁，以死循环的方法获取；

- 如果没有获取到，就返回 true，让当前线程阻塞等待，调用 selfInterrupt()。   



addWaiter(Node mode) 方法的实现：

```java
    private Node addWaiter(Node mode) {
          Node node = new Node(Thread.currentThread(), mode);
          // 快速尝试在尾部添加
          Node pred = tail;
          if (pred != null) {
              node.prev = pred;
              if (compareAndSetTail(pred, node)) {
                  pred.next = node;
                  return node;
              }
          }
          enq(node);
          return node;
      }
  
     private Node enq(final Node node) {
          for (;;) {
              Node t = tail;
              if (t == null) { // Must initialize
                  if (compareAndSetHead(new Node()))
                      tail = head;
              } else {
                  node.prev = t;
                  if (compareAndSetTail(t, node)) {
                      t.next = node;
                      return t;
                  }
              }
          }
      }
```

- 调用 compareAndSetTail(Node expect, Node update) 方法确保节点能够被线程安全的添加。
- 在 enq(final Node node) 方法中，通过死循环来保证节点的正确添加，只有通过 CAS 将节点设置为队列的尾节点，当前线程才能从该方法中返回，否则将不断尝试。



节点进入队列之后，就进入一个「 自旋的过程 」。每个节点（线程）都在观察，当条件满足时，获取到同步状态，就可以从这个自旋过程退出，否则依旧留在自旋过程中并会阻塞节点的线程。

acquireQueued(final Node node, int arg) 方法的实现：

```java
 final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```



独占式获取流程：
<div align="center"><img src="https://user-images.githubusercontent.com/19634532/60482316-8c6cb100-9cc3-11e9-832c-1b32f8ff335b.png" width= "500px"></div>
##### 锁释放

在锁实例的 unlock() 方法的实现中，使用了同步器的 release( int arg ) 方法来释放锁，在释放之后会唤醒其后继节点，进而使后继节点重新尝试获取锁。

 release( int arg ) 方法实现：

```java
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
```

释放之后调用 unparkSuccessor(Node node) 方法，该方法中使用 LockSupport.unpark( Thread thread ) 唤醒节点。

##### 总结

在获取锁时，会维护一个同步队列，将获取失败的线程添加到队列的尾部，并在队列中自旋，移除队列（停止自旋）的条件是当前线程节点的前驱节点是头结点并且能成功获取锁。在释放锁时，仅仅需要将资源还回去，然后通知一下头结点的后继节点并将其唤醒。



#### 共享式获取与释放

共享式获取与独占式获取区别在于同一时刻能否有多个线程同时获取到锁。ReentrantReadWriteLock 就是共享锁

通过调用同步器的 acquireShared(int arg)  方法可以共享的获取锁。该方法实现如下：

```java
public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

private void doAcquireShared(int arg) {
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

在该方法中，调用 tryAcquireShared(int arg) 方法获取同步状态，如果返回值大于等于 0 表示获取到了同步状态。如果获取失败，调用 doAcquireShared(int arg)  方法自旋，如果当前节点的前驱节点为头结点，尝试获取同步状态，如果返回值大于等于 0，表示同步状态获取成功并退出自旋。

锁的释放通过调用 releaseShared(int arg) 方法来实现，该方法具体实现如下：

```java
 public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }
```

该方法在释放成功后会唤醒后续等待的节点。

它和独占式的区别在于 tryReleaseShared(int arg) 方法一般会通过循环或 CAS 来保证安全释放，因为释放锁的操作会来自多个线程。



### CAS

CAS （compareAndSwap），比较再交换，一种无锁原子算法，是乐观锁。

实现思想：它包含 3 个参数 CAS（V，E，N），V 表示要更新变量的值，E 表示预期值，N 表示新值。仅当 V 值等于 E 值时，才会将 V 的值设为 N，返回 true。如果 V 值和 E 值不同，则说明已经有其他线程做两个更新，则当前线程则什么都不做，返回 false。最后，CAS 返回当前 V 的真实值。




#### CAS 实现原子操作有哪些问题

**ABA 问题，循环时间长开销大，只能保证一个共享变量的原子操作**。

- **ABA 问题**。CAS 在操作值的时候先检查有没有发生变化，如果没有发生变化则更新。如果一个值原来是 A，变成了 B，后来又变回了 A，那么 CAS  进行检查的时候会认为没有发生变化，但实际上却变化了。该问题解决思路是**使用版本号**，在变量前面追加版本号。java.util.concurrent.atomic 包中提供了**AtomicStampedReference** 类来解决 ABA 问题。这个类的 compareAndSet 方法比较当前引用是否等于预期引用，当前标注是否等于预期标志，如果全部相等，则以原子方式将该引用和该标志的值设置成给定的更新值。

  ```java
  public boolean compareAndSet(V   expectedReference,
                                   V   newReference,
                                   int expectedStamp,
                                   int newStamp)
  ```

  

- **循环时间长开销大**。自旋 CAS 如果长时间不成功，会给 CPU 带来非常大的执行开销。

- **只能保证一个共享变量的原子操作**。这个时候就是使用锁了。还有一个办法就是将多个共享变量合并为一个共享变量来操作。java.util.concurrent.atomic 包中提供了 **AtomicReference** 类保证引用对象之间的原子性，就可以把多个变量放在一个对象来进行 CAS 操作。





### compareAndSetState 和 setState 的区别

AbstractQueuedSynchronizer 提供了两个方法用以修改同步状态值，compareAndSetState 和 setState 方法：

- setState( int newState )：设置当前同步状态。
- compareAndSetState( int expect, int update )：使用 CAS 设置同步状态，能够保证状态设置的原子性。



所以，这两个方法的区别是：

- compareAndSetState 通常用于在获取到锁之前，尝试加锁时，对 state 进行修改，这种场景下，由于当前线程不是锁持有者，所以对 state 的修改是线程不安全的，也就是说可能存在多个线程都尝试修改 state，所以需要保证对 state 修改的原子性操作，即使用了unsafe 类的本地 CAS 方法；
- state 方法通常用于当前正持有锁的线程对 state 变量进行修改，不存在竞争，是线程安全的，所以此处没必要用 CAS 保证原子性，修改的性能更重要。



### 参考

- 《 Java 并发编程的艺术》方腾飞 魏鹏 程晓玥 著
- [AbstractQueuedSynchronizer的介绍和原理分析](http://ifeve.com/introduce-abstractqueuedsynchronizer/)