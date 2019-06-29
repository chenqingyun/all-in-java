# Thread 类源码解读

## 目录

- [Thread 的属性](#thread-的属性)
  - [线程优先级](#线程优先级)
  - [守护线程和用户线程](#守护线程和用户线程)
  - [线程的状态](#线程的状态)
- [Thread 的方法](#thread-的方法)
  - [run() 方法](#run-方法)
  - [start() 方法](#start-方法)
- [Thread 废弃的方法](#thread-废弃的方法)



> 本文源码基于 JDK 1.8



Thread 类是 Java 平台对线程的实现，位于 java.lang 包，实现 Runnable 接口。


## Thread 的属性
Thread 的属性包括 id、名称（name）、线程类别（daemon）和优先级（priority）等。

源码如下：

```java
/* 线程的名称 */
private volatile String name;

/* 线程的优先级 */
private int            priority;
private Thread         threadQ;
private long           eetop;

/* Whether or not to single_step this thread. */
private boolean     single_step;

/* 线程是否为守护线程 */
private boolean     daemon = false;

/* JVM state */
private boolean     stillborn = false;

/* 将会被执行的 Runnable */
private Runnable target;

/* 线程组 */
private ThreadGroup group;

/* The context ClassLoader for this thread */
private ClassLoader contextClassLoader;

/* The inherited AccessControlContext of this thread */
private AccessControlContext inheritedAccessControlContext;

/* 第几个线程，给匿名线程自动编号 */
private static int threadInitNumber;
private static synchronized int nextThreadNum() {
    return threadInitNumber++;
}

/* ThreadLocal values pertaining to this thread. This map is maintained
* by the ThreadLocal class. */
ThreadLocal.ThreadLocalMap threadLocals = null;

/*
* InheritableThreadLocal values pertaining to this thread. This map is
* maintained by the InheritableThreadLocal class.
*/
ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;

/* 该线程请求的堆栈大小，未指定大小默认是 0。一些虚拟机是忽略的 */
private long stackSize;

/*
* JVM-private state that persists after native thread termination.
*/
private long nativeParkEventPointer;

/* 线程 ID */
private long tid;

/* 生成 thread ID */
private static long threadSeqNumber;

/* 标识线程的状态，默认初始化线程是未启动状态 */

private volatile int threadStatus = 0;

volatile Object parkBlocker;

/* 可中断的阻断*/
private volatile Interruptible blocker;

/* 阻断锁*/
private final Object blockerLock = new Object();

/* 线程最小优先级 */
public final static int MIN_PRIORITY = 1;

/* 线程默认优先级 */
public final static int NORM_PRIORITY = 5;

/* 线程最大优先级 */
public final static int MAX_PRIORITY = 10;

/* 线程的状态 */
public enum State {
    NEW,
    RUNNABLE,
    BLOCKED,
    WAITING,
    TIMED_WAITING,
    TERMINATED;
}
```



### 线程的 ID

```java
/*
* Thread ID
*/
private long tid;
```

线程的 ID 是只读的，通过 ``Thread.getId()``方法获取，用于标识不同的线程。

不同的线程有不同 的 ID，但 ID 的唯一性只在 Java 虚拟机的一次运行有效，重启 Java 虚拟机（如重启 Web 服务器）某些线程的 ID 可能与上一次线程的 ID 一样，因此该属性值不适用于做某种唯一标识，如数据库的唯一标识。



### 线程的名称

```java
private volatile String name;
```

每个线程都有一个用于识别目的的名称。 多个线程可能具有相同的名称。 如果在创建线程时未指定名称，则会为其生成一个默认的名称。

查看源码可知其命名方式为 ``Thread-nextThreadNum()``，

```java
public Thread() {
    init(null, null, "Thread-" + nextThreadNum(), 0);
}
```

``nextThreadNum()``方法实现如下：

```java
/* 第几个线程，给匿名线程自动编号 */
private static int threadInitNumber;
private static synchronized int nextThreadNum() {
    return threadInitNumber++;
}
```

通过 ``getName()``方法获取线程名称，打印查看下：

```java
public class ThreadDemo {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread1= new Thread(myRunnable);
        Thread thread2= new Thread(myRunnable);
        thread1.start();
        thread2.start();
    }

    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Thread name:" + Thread.currentThread().getName());
        }
    }
}
```

打印结果：

![](https://upload-images.jianshu.io/upload_images/3297676-5f7d47bd3f00216e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



如何给线程命名呢？可以通过 ``setName()``方法给线程赋予名称，或通过``Thread(Runnable target, String name)``构造器传入。修改代码如下：

```java
MyRunnable myRunnable = new MyRunnable();
Thread thread1= new Thread(myRunnable,"thread1");
Thread thread2= new Thread(myRunnable);
thread2.setName("thread2");
thread1.start();
thread2.start();
```



**建议：为每个线程设置有意义的名称，有利于程序调试和问题定位。**



### 线程优先级

```java
private int priority;
```

每个线程都有优先级，其本质上就是给线程调度器的提示，较高优先级的线程优先于优先级较低的线程执行。

线程有 1~10 的10个优先级，默认为5。

```java
/* 线程最小优先级 */
public final static int MIN_PRIORITY = 1;

/* 线程默认优先级 */
public final static int NORM_PRIORITY = 5;

/* 线程最大优先级 */
public final static int MAX_PRIORITY = 10;
```

线程优先级高仅仅表示线程获取的 CPU 时间片的几率高，并不能保证优先级高的线程就会优先得到执行。

可以通过 ``setPriority``方法给线程设置优先级，一般情况下使用默认优先级即可，不恰当的设置优先级可能导致严重的问题**（线程饥饿）**。高优先级的线程有可能一直抢占 CPU 时间片，使得低优先级的线程抢不到 CPU 时间片导致永远得不到运行，低优先级的线程被饥饿至死，这种现象就称为线程饥饿。



### 守护线程和用户线程

按照线程是否会阻止 Java 虚拟机运行正常停止，可以将线程分为**守护线程**（Daemon Thread）和**用户线程**（User Thread，也称非守护线程）。

> 父线程为守护线程，则子线程也为守护线程。

线程的 daemon 属性

```java
/* 线程是否为守护线程 */
private boolean daemon = false;
```

可以设置该线程是否为守护线程，true 表示守护线程，false 则表示是普通线程即用户线程。

来写一段代码测试一下守护线程是怎样的：

```java
public class DaemonThread {
    public static void main(String[] args) {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i <= 100000) {
                    System.out.println("this is thread1，" + i);
                    i++;
                }
            }
        };
        // 设置 threa1 为守护线程
        thread1.setDaemon(true);
        thread1.start();

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                System.out.println("this is thread2");
            }
        };
        thread2.start();

    }
}
```

如上代码，按理说，thread1 应该会打印到 100000 的，但是执行了几次打印结果都没有到 100000

![](https://upload-images.jianshu.io/upload_images/3297676-c0b35d4ad9b54e6d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

就是因为这里设置了 thread1 为守护线程，在 thread2 和主线程都执行完了，Java 虚拟机就正常停止，thread1 守护线程就不执行了。

#### 守护线程和用户线程的区别

- 用户线程会阻止 Java 虚拟机的正常停止，当 Java 虚拟机中所有的用户线程都结束运行了，Java 虚拟机才能正常停止（除非 Java 虚拟机被强制停止）。
- 守护线程不会影响 Java 虚拟机的正常停止，即应用程序中有守护线程还在执行也不影响 Java 虚拟机正常停止。当所有用户线程执行结束，Java 虚拟机停止，守护线程也就停止了。守护线程通常用于执行重要性不高的任务，常用于为其他线程提供服务，如计时线程、监视其他线程的运行情况等。



需要注意的一点是，该属性必须在**线程启动之前设置**，即 setDaemon 方法必须在 start 方法之前调用，否则 setDaemon 方法会抛出 java.lang.IllegalThreadStateException 异常。

看下 setDaemon 的具体实现：

```java
public final void setDaemon(boolean on) {
    checkAccess();
    if (isAlive()) {
        throw new IllegalThreadStateException();
    }
    daemon = on;
}
```



### 线程的状态

线程从创建、启动到运行结束的整个生命周期有 6 种状态：

```java
public enum State {
    /**
    * 新创建状态，还未启动
    */
    NEW,

    /**
    * 可运行状态。可能正在运行，也有可能没有运行在等待操作系统的资源
    */
    RUNNABLE,

    /**
    * 锁阻塞状态。处于阻塞状态的线程正在等待获取监视器锁进入同步块/方法，
    * 或者在调用 Object.wait 方法后重新进入同步块/方法。
    */
    BLOCKED,

    /**
    * 等待状态。由于调用以下几个方法，线程处于该状态：
    * Object.wait()并且没有超时；
    * Thread.join()并且没有超时；
    * LockSupport.park();
    *
    * 一个处于等待状态的线程正在等待其他线程的特定动作。
    * 例如：一个在对象上调用了 wait() 的线程正在等待另一个线程中那个对象调用 notify() 或 notifyAll()。
    * 等待 join 线程终止。
    *
    */
    WAITING,

    /**
    * 计时等待状态，线程在指定时间内等待。
    *
    * 由于调用带有指定时间的以下几个方法线程处于该状态：
    * Object.wait(long timeout)；
    * Thread.sleep(long millis)；
    * Thread.join(long millis);
    * LockSupport.parkNanos;
    * LockSupport.parkUntil;
    * 
    */
    TIMED_WAITING,

    /**
    * 终止状态。线程执行完毕或因异常终止。
    */
    TERMINATED;
}
```

关于线程生命周期详细的分析见文章 [线程的生命周期](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.md)。




## Thread 的方法

Thread 类的常用方法有 ``start()``、``run()``、 ``sleep(long millies)``、``yield()``、``join()``、``currentThread()``方法等。

### currentThread() 方法

```java
public static native Thread currentThread();
```

该方法是一个本地方法，返回当前线程，即当前代码的执行线程。

Java 中任何一段代码都在某个线程中执行，执行当前代码的线程就称为**当前线程**。由于同一段代码可能被不同的线程执行，所以该方法在代码实际运行的时候可能返回不同的线程。



### run() 方法

run 方法相当于线程的任务处理入口，任务的处理逻辑在 run 方法中实现。

源码：
```java
/**
* 如果这个线程是使用 Runnable 运行对象构造的，则 Runnable 对象的 run 方法; 
* 否则，此方法不执行任何操作并返回。
*/
@Override
public void run() {
    if (target != null) {
        target.run();
    }
}
```

run 方法由 Java 虚拟机在运行线程时调用，而不是在代码中直接调用 run 方法来执行线程任务。

写一段测试一下：

```java
public class ThreadDemo {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread= new Thread(myRunnable);
        thread.setName("Thread sir");
        thread.run();
    }

    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("hello," + Thread.currentThread().getName());
        }
    }
}
```

打印结果是 “hello,main” ，并不是“hello,Thread sir”。所以只是调用 run 方法，线程是没有启动的，就是在主线程中以普通的方法进行调用。

run 方法执行结束包括正常结束和因抛出异常而导致终止，run 方法执行结束该线程的运行就结束了，线程所占用的资源（如内存空间）也会被 Java 虚拟机垃圾回收机制回收从而得到释放。



### start() 方法

调用 start 方法使得线程开始运行，由 Java 虚拟机去调用运行中的线程的 run 方法，从而使相应线程的任务处理逻辑代码得到执行。

源码：

```java
/**
*
* 调用该方法的结果是两个线程同时运行：当前线程（调用 start 方法的线程）和另一个线程（执行 run 方法的线程）。
*
* @exception  IllegalThreadStateException  if the thread was already
*               started.
*/
public synchronized void start() {
    /**
    * 首次调用 start() 方法 threadStatus = 0
    * 多次启动线程是不合法的，一旦线程完成执行就不能重新启动。
    * 重复调用该方法将抛 java.lang.IllegalThreadStateException 异常。
    */
    if (threadStatus != 0)
        throw new IllegalThreadStateException();

    /* 
    * 通知线程组该线程将要开始启动，该线程就会被添加到线程列表中，此时列表的 unstarted 数将会减少。
    */
    group.add(this);

    boolean started = false;
    try {
        // 调用本地方法启动线程
        start0();
        started = true;
    } finally {
        try {
            if (!started) {
                group.threadStartFailed(this);
            }
        } catch (Throwable ignore) {
            /* do nothing. If start0 threw a Throwable then
            it will be passed up the call stack */
        }
    }
}
```

调用该方法的结果是两个线程同时运行：当前线程（调用 start 方法的线程）和另一个线程（执行 run 方法的线程）。

**注意**，一个已经结束运行的线程不能通过再次调用 start 方法让其重新运行。重复调用一个线程的 start 方法会抛出“java.lang.IllegalThreadStateException" 异常。

启动一个线程的实质是请求 Java 虚拟机运行该线程，而这个线程何时能运行是由线程调度器（Scheduler，操作系统的一部分）决定。因此不是调用 start 方法启动了线程，线程就开始运行了，该线程只是处于可运行状态（Runnable），可能稍后再运行，也有可能永远不会被运行。



### sleep(long millis) 方法

调用该方法使得线程休眠（暂停运行）指定的时间。

```java
/** 
 * @param  millis 睡眠时间
 *
 * @throws  IllegalArgumentException 如果 mills 为负数则抛出此异常
 *
 * @throws  InterruptedException
 * 如果任何线程中断了当前线程，抛出 InterruptedException，当前线程的中断状态被清除。
 */
public static native void sleep(long millis) throws InterruptedException;
```

使当前正在执行的线程以指定的毫秒数暂停（暂时停止执行），具体取决于系统定时器和调度程序的精度和准确性。 线程不会丢失任何监视器的所有权。

### yield() 方法

当前线程主动放弃其对处理器的占用让给其他线程，这可能使当前线程被暂停。

```java
/**
 * 提示调度器，线程将让出所占用的处理器。调度程序可以自由地忽略这个提示。
 *
 * 很少使用该方法，它可能对调试和测试有用，可能有助于根据竟态条件重现错误。
 */
public static native void yield();
```

该方法很少使用，也不可靠，调用该方法后线程可能仍然运行（依据系统当前运行情况）。

### join() 方法

等待这个线程死亡。在当前线程中调用另一个线程的 join 方法，则当前线程暂停运行，直到另一个线程运行结束，当前线程再重新运行。

```java
	/**
     * Waits for this thread to die.
     * @throws  InterruptedException
 	 * 如果任何线程中断了当前线程，抛出 InterruptedException，当前线程的中断状态被清除。
     */
    public final void join() throws InterruptedException {
        join(0);
    }

	/**
     * 等待该线程终止的时间最长为 millis 毫秒。时间为 0 意味着要一直等下去。
     *
     * 此方法的实现是循环调用 this.wait 方法，条件为 this.isAlive。 
     * 当线程终止时会调用 this.notifyAll 方法。 建议应用程序不要在线程实例上使用 wait，notify，
     * 或 notifyAll
     *
     * @param  millis
     *         the time to wait in milliseconds
     *
     * @throws  IllegalArgumentException 时间为负数则抛此异常
     *
     * @throws  InterruptedException 
     */
    public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            // 时间为负数则抛此异常
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            // 时间为0一直等待
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }
```

写一波代码玩一下这个 join 方法

```java
public class ThreadJoinDemo {
    public static void main(String[] args) {
        BabyThread babyThread = new BabyThread();
        MotherRunnable motherRunnable = new MotherRunnable(babyThread);
        Thread motherThread = new Thread(motherRunnable);
        motherThread.start();
        babyThread.start();
    }

    private static class MotherRunnable implements Runnable {
        private BabyThread babyThread;

        public MotherRunnable() {
        }

        public MotherRunnable(BabyThread babyThread) {
            this.babyThread = babyThread;
        }

        @Override
        public void run() {
            System.out.println("Mother 开始在房间里跳广场舞");
            try {
                long time = System.currentTimeMillis();
                babyThread.join();
                System.out.println("暂停了 " + (System.currentTimeMillis() - time) + " 秒，等宝宝睡醒后，Mother 又开始跳广场舞了");

                Thread.sleep(1000);
                System.out.println("Mother 结束跳广场舞");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class BabyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Baby 开始睡觉了");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Baby 睡醒了");
        }
    }
}
```

如上代码，创建了两个线程，模拟妈妈跳广场舞和宝宝睡觉的场景。看下运行结果：

![](https://upload-images.jianshu.io/upload_images/3297676-7fcd8e736ff13e9e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

妈妈线程开始跳广场舞，然后又调用了宝宝线程的 join 方法，这时就要等宝宝线程结束睡觉了，妈妈才能继续跳广场舞。

### interrupt() 方法

标记线程为中断状态。

```java
	/**
     * 允许线程中断自己，如果不是当前线程则会调用此线程的 checkAccess 方法检查权限，
     * 这可能会导致抛出 SecurityException 。
     *
     * 如果该线程因调用 Object 类的 wait 的方法，或者 join，sleep 方法而造成阻塞时，
     * 再去调用该方法中断线程，那么它的中断状态将被清除，并且将收到一个 InterruptedException。
     * 
     * 中断不活动的线程没有任何效果。
     *
     * @throws  SecurityException 如果当前线程不能修改此线程
     *
     * @revised 6.0
     * @spec JSR-51
     */
    public void interrupt() {
        if (this != Thread.currentThread())
            checkAccess();

        synchronized (blockerLock) {
            Interruptible b = blocker;
            if (b != null) {
                interrupt0();           // Just to set the interrupt flag
                b.interrupt(this);
                return;
            }
        }
        interrupt0();
    }

```

如果该线程因调用 Object 类的 wait 的方法，或者 join，sleep 方法而造成阻塞时，再去调用该方法中断线程，那么它的中断状态将被清除，并且将收到一个 InterruptedException，看这几个方法的源码就知道，他们都抛出 InterruptedException 异常。

其实，调用该方法并不会导致线程停止运行，只是设置线程的中断状态为 true。

有两个方法可以检查当前状态是否被中断了：

- static boolean interrupted() ：检查当前线程是否被中断，当前线程的中断状态会被清除。

- isInterrupted()：检查线程是否被中断，线程的中断状态不会受该方法影响。

  

  这两个方法源码如下：

```java

    /**
     * 测试当前线程是否被中断，其中断状态将被清除，即被设置为 false。 
     * 也就是说，如果这个方法被连续调用两次，第二次肯定返回 false。
     * （除非两次调用之间调用了 interrupt() 方法）
     *
     * 不活动的线程返回 false。
     *
     * @see #isInterrupted()
     * @revised 6.0
     */
    public static boolean interrupted() {
        return currentThread().isInterrupted(true);
    }

    private native boolean isInterrupted(boolean ClearInterrupted);

    /**
     * 测试线程是否被中断，线程的中断状态不会受该方法影响 
     *
     * 不活动的线程返回 false
     *
     * @see     #interrupted()
     * @revised 6.0
     */
    public boolean isInterrupted() {
        return isInterrupted(false);
    }
```

emmm ( ⚆ _ ⚆ )……这两个方法是挺容易混淆的。



## Thread 废弃的方法

- stop 方法：停止线程的运行
- destroy 方法：销毁这个线程而没有任何清理
- suspend 方法：将线程挂起，暂停运行
- resume 方法：恢复被挂起的线程继续运行 

废弃方法就不多说了吧:-D



End，后续文章会补上关于线程生命周期的详细分析~~