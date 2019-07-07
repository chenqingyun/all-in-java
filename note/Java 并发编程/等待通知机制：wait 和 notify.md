## 等待/通知机制：wait 和 notify

等待/通知的相关方法定义在 Object 类上，以下具体描述：

- notify()：通知正在对象上等待的单个线程，使 wait 方法返回，返回的前提是线程获得对象的锁。
- notify()：唤醒所有等待该对象的线程。

- wait()：调用该方法线程进入 WAITING 状态，需要等待另一个线程调用该对象的 notify() / notifyAll() 方法才能唤醒。调用 wait 系列方法，线程会释放对象的锁。
- wait(long timeout)：超时等待一段时间，时间是毫秒。
- wait(long timeout, int nanos)：超时时间增加额外的纳秒时间。



等待 / 通知机制的过程，一个持有对象 O 的锁的线程 A 调用了对象 O 的 wait() 方法进入 WAITING 状态，线程 A 释放对象 O 的锁，然后被线程 B 持有。当线程 B 调用对象的 O 的 notify() / notifyAll 方法，线程 B 释放对象 O 的锁后，线程 A 获得锁，线程 A 才能被唤醒。



### wait 方法

wait() 方法实际就是调用的 wait(0) 。

当前线程必须持有该对象的监视器，才能调用 wait() 方法进入等待状态。

- 如果当前线程不是该对象监视器的所有者，将抛出 IllegalMonitorStateException 异常。

- 如果任何线程在当前线程等待通知之前或当前线程中断当前线程，会抛出 InterruptedException 异常，当前线程的中断状态将被清除。

调用 wait() 方法后，线程状态由 RUNNABLE 变为 WAITING，并将当前线程放置到该对象的等待队列。



### notify 方法

notify() 方法用以唤醒正在等待对象监视器的单个线程。如果有多个线程在等待，任意选择一个唤醒。

notifyAll() 方法唤醒正在等待对象监视器的所有线程。

当前线程必须持有该对象的监视器，才能调用 notify() / notifyAll() 方法。如果当前线程不是该对象监视器的所有者，将抛出 IllegalMonitorStateException 异常。

当前线程调用对象的 notify() / notifyAll() 方法后，等待线程需要等到该线程释放该对象的锁之后，然后等待获得锁后才能从 wait() 方法返回。

notify() 方法将等待队列中的一个线程从等待队列中移到同步队列，notifyAll() 方法将等待队列中的所有线程移到同步队列。被移动的线程状态由 WAITING 状态变为 BLOCKED 状态。





### wait / notify 过程

![image](https://user-images.githubusercontent.com/19634532/60763433-f4960b00-a0a6-11e9-99e9-1753d10f43cf.png)

总结：

- wait()、notify()、notifyAll() 时需要先持有对象的锁。
- 调用 wait() 方法后，线程状态由 RUNNABLE 变为 WAITING，并将当前线程放置到该对象的等待队列。
- 当前线程调用对象的 notify() / notifyAll() 方法后，等待线程需要等到该线程释放该对象的锁之后，然后等待线程获得锁才能被唤醒。
- notify() 方法将等待队列中的一个线程从等待队列中移到同步队列，notifyAll() 方法将等待队列中的所有线程移到同步队列。被移动的线程状态由 WAITING 状态变为 BLOCKED 状态。
- 从 wait() 方法返回的前提是获得了调用对象的锁。

