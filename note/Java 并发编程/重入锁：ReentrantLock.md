## 重入锁：ReentrantLock

### 目录


- [ReentrantLock 如何实现锁的重入](#reentrantLock-如何实现锁的重入)
- [锁类型](#锁类型)
- [获取锁](#获取锁)
- [释放锁](#获取锁)
- [公平与非公平获取锁的区别](#公平与非公平获取锁的区别)



ReentrantLock 基于 AQS（AbstractQueuedSynchronizer）实现的。



### ReentrantLock 如何实现锁的重入

锁重入指线程在获取到锁后能够再次获取到锁而不会被锁所阻塞。重入锁的实现要解决以下两个问题：

- 线程再次获取锁。锁要判断线程是否为当前持有该锁的线程，如果是则再次获取锁成功，计数值加 1。
- 线程最终释放锁。锁被释放时，计数减 1，当计数等于 0 时表示锁成功被释放。



### 锁类型

ReentrantLock 分为**公平锁**和**非公平锁**，可以通过构造方法来指定具体类型：

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

默认使用**非公平锁**，它的效率和吞吐量都比公平锁高的多。



### 获取锁

ReentrantLock 通过组合自定义同步器来实现锁的获取和释放。

**锁获取流程：**

1. 首先根据判断同步状态是否等于 0 判断锁是否已被获取；
2. 如果没有被获取，则将状态值设置为 1，设置当前线程为获得锁的独占线程，该线程获取锁成功；
3. 如果锁已被获取，先判断持有的线程是否是当前线程，如果是，则将状态值加 1，表示线程重入该锁；
4. 如果是其他线程获取的，则获取失败，将当前线程构造成节点，添加到等待队列的末尾，通过自旋加 CAS 的方式确保节点成功添加到队尾；
5. 添加成功后，进入一个自旋的过程。每个节点（线程）都在观察，当条件满足时，获取到同步状态，就可以从这个自旋过程退出，否则依旧留在自旋过程中并会阻塞节点的线程。

公平锁与非公平锁的区别在于，公平锁获取锁时多了一个同步队列中当前节点是否有前驱节点的判断。



以下源码分析获取锁的流程

获取锁源码：

```java
  public void lock() {
        sync.lock();
    }
```

使用 sync 的方法，而这个方法是一个抽象方法，具体是由其子类 NonfairSync 和 FairSync 来实现的。



#### 非公平锁获取

非公平锁获取调用的是 NonfairSync 的 lock() 方法：

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

**如果首次加锁，将状态值加 1，如果返回 true，表明状态设置成功，线程获得锁**。这里是调用 compareAndSetState 方法对状态值进行修改。该方法有两个参数，一个是期望的值，一个是新值。如果当前状态值等于预期值，则以原子方式将「 同步状态 」设置为给定的更新值。此操作的内存语义为「 volatile 的写和读 」。返回 false 表示实际值与预期值不相等。这里给定的期望值是 0，新值为 1，即期望当前线程是首次加锁，并将状态值加 1。

**如果不是首次加锁，则调用 AQS 的 acquire() 方法尝试获取锁**。acquire 方法中，先调用子类的 tryAcquire 方法尝试获取锁。

```java
// AbstractQueuedSynchronizer 中的 acquire()
    public final void acquire(int arg) {
        // AQS 子类的 tryAcquire() 方法
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

在非公平锁中的实现 tryAcquire 方法实际调用的 nonfairTryAcquire(int acquires) 方法获取锁。

```java

    // ReentrantLock 中 NonfairSync 实现的 tryAcquire() 方法
    protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    // Sync 中的 nonfairTryAcquire() 方法
    final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            // 状态等于 0，表示没有线程获得锁
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

**首先根据同步状态值判断是否等于 0，如果等于 0 表示目前没有其他线程获得锁，当前线程就可以尝试获取锁**。利用 CAS 来将 AQS 中的 state 修改为 1，也就是获取锁，获取成功则将当前线程置为获得锁的独占线程  ( setExclusiveOwnerThread(current) )。

**如果不等于 0，表示锁已被获取，需先判断获取锁的线程是否是当前持有该锁的线程**，如果是则将状态值加 1 并返回 true。如果不是，获取锁失败，返回 false。

回到 AQS 的 acquire() 方法中，**如果  tryAcquire(arg) 获取锁失败，则需要用 addWaiter(Node.EXCLUSIVE) 将当前线程写入队列中**。

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

首先判断队列是否为空，不为空时则将封装好的 Node 利用 CAS 确保节点能够被线程安全的插入队尾，如果出现并发写入失败就需要调用 enq(node) 来写入了。 enq() 方法**通过死循环来保证节点的能添加到队列中**，只有通过 CAS 将节点设置为队列的尾节点，当前线程才能从该方法中返回，否则将不断尝试。

添加成功后通过 acquireQueued 方法阻塞线程

```java
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
               // 1 
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

进入一个「 自旋的过程 」。每个节点（线程）都在观察，当条件满足时，获取到同步状态。

如果前一个节点正好是 head，表示自己排在第一位，**只有前驱节点是头结点才能获取到同步状态**，可以马上调用 tryAcquire 尝试。如果获取成功，直接修改自己为 head。这步是实现公平锁的核心，保证释放锁时，由下个排队线程获取锁。

否则依然自旋，进入等待状态。

<div align="center"><img src="https://user-images.githubusercontent.com/19634532/60482316-8c6cb100-9cc3-11e9-832c-1b32f8ff335b.png" width= "500px"></div>


#### 公平与非公平获取锁的区别

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



### 锁释放

重新获取锁只是增加了同步状态值，那么在释放锁的时候就应该是减少同步状态值。

公平锁和非公平锁的释放流程都是一样的，释放锁的代码如下：

```java
public void unlock() {
    sync.release(1);
}
public final boolean release(int arg) {
    if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0)
        	   //唤醒被挂起的线程
            unparkSuccessor(h);
        return true;
    }
    return false;
}
//尝试释放锁
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

释放之后需要调用 unparkSuccessor(h) 来唤醒被挂起的线程。





