package com.chenqingyun.concurrency;

import java.util.concurrent.*;

/**
 * @author chenqingyun
 * @date 2019-07-09 23:54.
 */
public class ThreadPoolExecutorDemo {
    private static ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(4,10,
                    2000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());

    public static void main(String[] args) {
        threadPoolExecutor.execute(()->{
            // do something
        });

        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        Future<Object> future = threadPoolExecutor.submit(callable);
        try {
            Object o = future.get();
        }catch (InterruptedException e){
            // 处理中断异常
        }catch (ExecutionException e){
            // 处理无法执行的任务的异常
        }
        finally {
            // 关闭线程池
            threadPoolExecutor.shutdown();
        }
    }
}
