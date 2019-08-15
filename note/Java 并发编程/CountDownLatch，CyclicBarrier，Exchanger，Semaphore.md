## CountDownLatch，CyclicBarrier，Exchanger，Semaphore



### CountDownLatch

CountDownLatch 闭锁，允许一个或多个线程等待其他线程操作完成。可以实现 join 的功能。

它的内部提供了一个计数器，在构造闭锁时必须指定计数器的初始值，且计数器的初始值必须大于0。

提供了一个 countDown 方法来操作计数器的值，每调用一次 countDown 方法计数器都会减1，调用 await 方法会阻塞当前线程，直到计数器的值减为 0 阻塞的线程被唤醒。



### CyclicBarrier

同步屏障，可循环使用的屏障。

让一组线程到达屏障（或叫同步点）时被阻塞，知道最后一个线程到达屏障，屏障才会打开，所有被屏障拦截的线程才会继续运行。



CycliBarrier 默认的的构造方法是 CyclicBarrier(int parties) ，参数表示屏障拦截的线程数量。

每个线程中调用 CycliBarrier 的 await 方法来告诉 CycliBarrier 我已经到达屏障，然后当前线程被阻塞，直到最后一个线程到达，所有线程被唤醒。

如构造了一个 new CyclicBarrier(3) 的同步屏障，只有两个线程调用了 CycliBarrier 的 await 方法，没有第三个线程执行 await 方法，即没有第三个线程到达屏障，所以之前到达屏障的线程都不会继续执行。





