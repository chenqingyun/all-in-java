package com.chenqingyun.concurrency.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author chenqingyun
 * @date 2019-07-02 10:00.
 * <p>
 * 独占锁
 */
public class Mutex implements Lock {


    /**
     * 静态内部类，自定义同步器
     */
    private static class Sync extends AbstractQueuedSynchronizer {
        /**
         * 是否处于独占状态
         *
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * 同步状态为 0 的时候获取锁
         *
         * @param acquires
         * @return
         */
        @Override
        protected boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 释放锁，同步状态设置为 0
         *
         * @param acquires
         * @return
         */
        @Override
        protected boolean tryRelease(int acquires) {
            /**
             * 首先判断同步状态是否为 0
             */
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }

            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        /**
         * 返回一个 Condition，每个 condition 都包含了一个 condition 队列
         *
         * @return
         */
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.tryRelease(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked(){
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads(){
        return sync.hasQueuedThreads();
    }
}
