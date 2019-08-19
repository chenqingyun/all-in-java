## Redis 分布式锁的正确实现

### 目录

- [分布式锁实现](#分布式锁实现)
- [Redlock 算法](#redlock-算法)
- [锁冲突处理](#锁冲突处理)



### 分布式锁实现

实现要点：

- 使用 set 指令加扩展参数：「 set key value px milliseconds nx 」，保证操作原子性；
- set 指令的 value 参数设置为一个随机数，使其具有唯一性；
- 释放锁时要匹配 value 是否一致，避免误删锁；
- 匹配 value 和删除 key 不是一个原子操作，需要使用 Lua 脚本来处理。



#### 为什么释放锁时要匹配 value 避免误删锁？

比如有线程 A、B，线程 A 在执行任务的时候由于任务执行时间过长或被阻塞了，而锁过期自动释放了，然后线程 B 获取到锁，这时线程 A 执行完任务，需要执行释放锁的操作，那么就有可能释放掉线程 B 的锁。

所以在加锁时要设置 value 为一个随机数，使其具有唯一性，然后释放锁时要匹配 value 是否一致，避免误删锁。





注意：

- Redis 分布式锁不能解决超时问题，所以不要用于较长时间的任务。



优点：

- 性能高
- 实现简单



**存在的问题：**

- 通过过期时间来避免死锁，过期时间设置多长对业务来说往往比较头疼，时间短了可能会造成：持有锁的线程 A 任务还未处理完成，锁过期了，线程 B 获得了锁，导致同一个资源被 A、B 两个线程并发访问；时间长了会造成：持有锁的进程宕机，造成其他等待获取锁的进程长时间的无效等待。
- 加锁时只作用在一个 Redis 节点上；redis 的主从异步复制机制可能丢失数据，会出现如下场景：A 线程获得了锁，但锁数据还未同步到 slave 上，master 挂了，slave 顶成主，线程 B 尝试加锁，仍然能够成功，造成 A、B 两个线程并发访问同一个资源。



### Redlock 算法

**Redlock** 支持多节点 Redis 实现分布式锁


实现：

使用「大多数机制」，加锁时，向过半节点发送 set(key,value,nx=True,ex=xxx)指令，只要过半节点 set 成功就认为加锁成功。释放锁时，向所有节点发送 del 指令。

相关文章：


- [Redlock：Redis分布式锁最牛逼的实现](https://mp.weixin.qq.com/s?__biz=MzU5ODUwNzY1Nw==&mid=2247484155&idx=1&sn=0c73f45f2f641ba0bf4399f57170ac9b&chksm=fe426b1dc935e20b34b9c2c26662b24229d196a46535c74a209572b6c3e9680dde09c91e065d&mpshare=1&scene=24&srcid=1201hrhwRdKz7B4Id9UXTYc2#rd)

- [拜托，面试请不要再问我Redis分布式锁的实现原理！【石杉的架构笔记】](https://juejin.im/post/5bf3f15851882526a643e207#comment)

- [基于Redis的分布式锁到底安全吗](http://zhangtielei.com/posts/blog-redlock-reasoning.html)




###  分布式锁的异常问题


#### 如果客户端在处理请求时加锁没有成功

一般有以下 3 种策略来处理加锁失败。

- 直接抛出异常，通知用户稍后重试。
- sleep 一会，然后稍后重试。
- 将请求转移至延时队列，过一会再试。



#####  直接抛出特定类型的异常

该方式适合由用户直接发起的请求，如果失败，前端提示错误，然后在重试，起到人工延时的效果。



##### sleep

sleep 会阻塞当前的消息处理线程，会导致队列的后续消息处理出现延迟。

不适合用于碰撞得比较频繁或者队列里消息比较多的场景。

如果因为个别死锁的 key 导致加锁失败，线程会彻底堵死，导致后续消息永远得不到及时处理。



##### 延时队列

将请求转移至延时队列，过一会再试。

参考[[延时队列的实现](https://github.com/chenqingyun/all-in-java/blob/master/note/数据库/Redis/Redis 消息队列与发布订阅.md#延时队列)](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E6%B6%88%E6%81%AF%E9%98%9F%E5%88%97%E4%B8%8E%E5%8F%91%E5%B8%83%E8%AE%A2%E9%98%85.md#%E5%BB%B6%E6%97%B6%E9%98%9F%E5%88%97)



#### 任务还未处理完成，锁过期了，另一个线程获得了锁，导致同一个资源被两个线程并发访问

一种方案是引入**锁续约机制**，也就是获取锁之后，释放锁之前，会定时进行锁续约，比如以锁超时时间的 1/3 为间隔周期进行锁续约。

使用 Redisson。

Redisson 在内部提供了一个监控锁的看门狗（watchdog），它的作用是在 Redisson 实例关闭之前，不断延长锁的有效期。

Redisson 加的分布式锁的超时时间默认是 30 秒。获取锁成功就会开启一个定时任务，也就是 watchdog，定时任务会定期检查去续期。该定时任务每隔 1/3 时间，这里是 10 秒就去检查，当检查到任务执行了 2/3 时， 30 - 10 = 20 秒的时候，就会进行一次续期，把锁重置成30秒。



那么如果持有锁的服务，发生了死循环或者死锁的话，那么 watchdog 一直去延迟锁，这不也是死锁吗？



[Redis分布式锁如何续期](https://juejin.im/post/5d122f516fb9a07ed911d08c?utm_source=gold_browser_extension#comment)



#### 持有锁的服务挂了没有解锁怎么办？

设置的过期时间过长的话，造成其他等待获取锁的进程长时间的无效等待。

心跳检测？？？





- 《Redis 深度历险 核心原理与应用实践》钱文品/著

- [基于 Redis 的分布式锁](https://crossoverjie.top/2018/03/29/distributed-lock/distributed-lock-redis/)

- [如何实现靠谱的分布式锁？](https://www.infoq.cn/article/how-to-implement-distributed-lock)

- [几种常见的分布式锁的策略优缺点及对应处理](https://www.pomelolee.com/1787.html)