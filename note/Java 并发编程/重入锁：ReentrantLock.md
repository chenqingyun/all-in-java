## 重入锁：ReentrantLock

### 目录


- [ReentrantLock 如何实现锁的重入](#reentrantLock-如何实现锁的重入)
- [公平与非公平获取锁的区别](#公平与非公平获取锁的区别)
- [compareAndSetState 和 setState 的区别](#compareAndSetState-和-setState-的区别)



### ReentrantLock 如何实现锁的重入

锁重入指线程在获取到锁后能够再次获取到锁而不会被锁所阻塞。重入锁的实现要解决以下两个问题：

- 线程再次获取锁。锁要判断线程是否为当前持有该锁的线程，如果是则再次获取锁成功，计数值加 1。
- 线程最终释放锁。锁被释放时，计数减 1，当计数等于 0 时表示锁成功被释放。



ReentrantLock 通过组合自定义同步器来实现锁的获取和释放。

加锁源码：

```java
  public void lock() {
        sync.lock();
    }
```

非公平锁的 lock() 实现：

```java
    /**
     * Sync object for non-fair locks
     */
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        /**
         * Performs lock.  Try immediate barge, backing up to normal
         * acquire on failure.
         */
        final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }
```

lock 方法中，先调用 compareAndSetState 方法对状态值进行修改，该方法源码：

```java
 protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
```

该方法有两个参数，一个是期望的值，一个是新值。如果当前状态值等于预期值，则以原子方式将「 同步状态 」设置为给定的更新值。此操作的内存语义为「 volatile 的写和读 」。返回 false 表示实际值与预期值不相等。

这里给定的期望值是 0，新值为 1，即如果当前线程是首次加锁，将状态值加 1，返回 true。

如果不是首次加锁，则调用 acquire(1) 方法，该方法实际调用 nonfairTryAcquire(int acquires) 方法。

nonfairTryAcquire 方法实现：

```java
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
```

该方法首先根据同步状态值判断是否是第一次获取锁，如果不是，判断获取锁的线程是否是当前持有该锁的线程，如果是则将状态值加 1 并返回 true。

重新获取锁只是增加了同步状态值，那么在释放锁的时候就应该是减少同步状态值。

ReentrantLock 释放锁的代码如下：

```java
 public void unlock() {
        sync.release(1);
    }
```

非公平锁的 tryRelease 方法的实现：

```java
    protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }
```

释放一次锁就对同步状态值减 1，当同步状态值为 0 时，将占有线程设置为 null，并返回 true，表示锁释放成功。



### 公平与非公平获取锁的区别

公平锁的获取实现：

```java
static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        final void lock() {
            acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }
```

该方法与 nonfairTryAcquire(int acquires) 方法不同的是，多了个 hasQueuedPredecessors() 判断条件，即加入了同步队列中当前节点是否有前驱节点的判断，该方法实现如下：

```java
public final boolean hasQueuedPredecessors() {
        Node t = tail; 
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }
```

如果返回 true，则表示有线程比当前线程更早地请求获取锁。

因此，需要等待前面的线程获取锁并释放锁之后才能继续获取锁。



### compareAndSetState 和 setState 的区别

在 nonfairTryAcquire 方法中修改状态值用到了compareAndSetState 和 setState 两个不同的方法，那么这两个方法有什么不同呢？

- compareAndSetState 通常用于在获取到锁之前，尝试加锁时，对 state 进行修改，这种场景下，由于当前线程不是锁持有者，所以对 state 的修改是线程不安全的，也就是说可能存在多个线程都尝试修改 state，所以需要保证对 state 修改的原子性操作，即使用了unsafe 类的本地 CAS 方法；
- state 方法通常用于当前正持有锁的线程对 state 变量进行修改，不存在竞争，是线程安全的，所以此处没必要用 CAS 保证原子性，修改的性能更重要。


