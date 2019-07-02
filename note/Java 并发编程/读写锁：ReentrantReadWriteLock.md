## 读写锁：ReentrantReadWriteLock

### 目录

- [ReentrantReadWriteLock 的特性](#reentrantReadWriteLock-的特性)
- [读写锁的实现分析](#读写锁的实现分析)
- [锁降级](#锁降级)

排他锁：如ReentrantLock，同一时刻只能有一个线程获取锁。

读写锁：同一时刻可以允许多个线程同时获取到锁。

Java 并发包提供读写锁的实现是 ReentrantReadWriteLock：

```java
package java.util.concurrent.locks;

public class ReentrantReadWriteLock
        implements ReadWriteLock, java.io.Serializable
```



读写锁维护了两个锁，一个读锁，一个写锁。



当写锁被获取到，后续其他线程的读写操作都会被阻塞，写锁释放后，其他操作继续执行。



应用场景：

读场景多于写场景的情况下，在读多于写的情况下，读写锁能够提供比排他锁更高的并发性和吞吐量。



### ReentrantReadWriteLock 的特性

- 支持非公平策略（默认）和公平策略的锁获取；
- 支持锁重入。以读写线程为例，读线程在获取读锁后，可以再次获得读锁；写线程在获得写锁后，能够再次获得写锁和读锁。
- 锁降级。遵循获取写锁，获取读锁再释放写锁的顺序，写锁能够降级成为读锁。



### 读写锁的实现分析



#### 写锁的获取与释放

写锁是一个支持重进入的排他锁。如果当前线程获取了写锁，则增加写状态。如果当前线程在获取写锁时，读锁已经被获取（读状态不为 0）或该线程不是已经获取写锁的线程，则当前线程进入等待状态。

写锁的获取通过 tryAcquire(int acquires) 方法实现：

```java
    protected final boolean tryAcquire(int acquires) {
           
            Thread current = Thread.currentThread();
            int c = getState();
            int w = exclusiveCount(c);
            if (c != 0) {
                // (Note: if c != 0 and w == 0 then shared count != 0)
                // 存在读锁或该线程不是已经获取写锁的线程
                if (w == 0 || current != getExclusiveOwnerThread())
                    return false;
                if (w + exclusiveCount(acquires) > MAX_COUNT)
                    throw new Error("Maximum lock count exceeded");
                // Reentrant acquire
                setState(c + acquires);
                return true;
            }
            if (writerShouldBlock() ||
                !compareAndSetState(c, c + acquires))
                return false;
            setExclusiveOwnerThread(current);
            return true;
        }
```

该方法除了判断重入条件（ current != getExclusiveOwnerThread() ），还判断了是否存在读锁（ w == 0 ）。

如果存在读锁则不能申请写锁。

> 读写锁要确保写锁的操作对读锁是可见的。如果读锁已经获取的情况下运行获取写锁，已经正在执行的其他读线程是无法感知到当前线程的写操作的。

写锁的释放与 ReentrantLock 的释放过程基本相识，每次释放减少写状态，当写状态为 0 时表示写锁被释放，等待的读写线程能够继续访问读写锁，同时，写线程的操作后续读写线程可见。



#### 读锁的获取与释放

```java
    protected final int tryAcquireShared(int unused) {
         
            Thread current = Thread.currentThread();
            int c = getState();
            if (exclusiveCount(c) != 0 &&
                getExclusiveOwnerThread() != current)
                return -1;
            int r = sharedCount(c);
            if (!readerShouldBlock() &&
                r < MAX_COUNT &&
                compareAndSetState(c, c + SHARED_UNIT)) {
                if (r == 0) {
                    firstReader = current;
                    firstReaderHoldCount = 1;
                } else if (firstReader == current) {
                    firstReaderHoldCount++;
                } else {
                    HoldCounter rh = cachedHoldCounter;
                    if (rh == null || rh.tid != getThreadId(current))
                        cachedHoldCounter = rh = readHolds.get();
                    else if (rh.count == 0)
                        readHolds.set(rh);
                    rh.count++;
                }
                return 1;
            }
            // 死循环获取读锁。包含锁降级策略。
            return fullTryAcquireShared(current);
        }
```

- 如果其他线程已经获取了写锁，则获取读锁失败，进入等待状态；
- 如果当前线程获取了写锁或写锁未被其他获取，则当前线程增加读状态，获取读锁成功；
- 如果当前线程获取了写锁，再来获取读锁，这其实就是锁降级过程。



读锁的释放需要保证线程安全的减少读状态，因为可能会有多个读线程同时释放读锁。减少的值是（1 << 16）。



### 锁降级

**锁降级写锁降级为读锁。锁降级是当前线程已获取到写锁，再次获取到了读锁，然后再把写锁释放的过程。**

**为什么要先获取读锁，再释放写锁？**

如果当前线程先释放了写锁，在获取到读锁之前，写锁有可能被其他线程获取到，那么当前线程就无法获取到读锁。



总的来说，**锁降级就是一种特殊的锁重入机制**。如果没有锁降级机制，就会出现死锁。因为当持有写锁的线程想获取读锁，但却无法降级，进入了等待队列，就会死锁。

JDK 使用「 先获取写锁，然后获取读锁，最后释放写锁 」这个步骤，是为了提高获取锁的效率，而不是所谓的可见性。



### 参考

- 《 Java 并发编程的艺术》方腾飞 魏鹏 程晓玥 著
- [并发编程之——读锁源码分析（解释关于锁降级的争议）](https://www.jianshu.com/p/cd485e16456e)