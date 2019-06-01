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



注意：

- Redis 分布式锁不能解决超时问题，所以不要用于较长时间的任务。



优点：

- 性能高
- 实现简单



缺点：

- 加锁时只作用在一个 Redis 节点上；
- Redis 分布式锁不能解决超时问题；

### Redlock 算法

**Redlock** 支持多节点 Redis 实现分布式锁


实现：

使用「大多数机制」，加锁时，向过半节点发送 set(key,value,nx=True,ex=xxx)指令，只要过半节点 set 成功就认为加锁成功。释放锁时，向所有节点发送 del 指令。

相关文章：


- [Redlock：Redis分布式锁最牛逼的实现](https://mp.weixin.qq.com/s?__biz=MzU5ODUwNzY1Nw==&mid=2247484155&idx=1&sn=0c73f45f2f641ba0bf4399f57170ac9b&chksm=fe426b1dc935e20b34b9c2c26662b24229d196a46535c74a209572b6c3e9680dde09c91e065d&mpshare=1&scene=24&srcid=1201hrhwRdKz7B4Id9UXTYc2#rd)

- [拜托，面试请不要再问我Redis分布式锁的实现原理！【石杉的架构笔记】](https://juejin.im/post/5bf3f15851882526a643e207#comment)

- [基于Redis的分布式锁到底安全吗](http://zhangtielei.com/posts/blog-redlock-reasoning.html)



### 锁冲突处理

如果客户端在处理请求时加锁没有成功，一般有以下 3 种策略来处理加锁失败。

- 直接抛出异常，通知用户稍后重试。
- sleep 一会，然后稍后重试。
- 将请求转移至延时队列，过一会再试。



#### 直接抛出特定类型的异常

该方式适合由用户直接发起的请求，如果失败，前端提示错误，然后在重试，起到人工延时的效果。



#### sleep

sleep 会阻塞当前的消息处理线程，会导致队列的后续消息处理出现延迟。

不适合用于碰撞得比较频繁或者队列里消息比较多的场景。

如果因为个别死锁的 key 导致加锁失败，线程会彻底堵死，导致后续消息永远得不到及时处理。



#### 延时队列

将请求转移至延时队列，过一会再试。

参考[延时队列的实现]()



### 参考

《Redis 深度历险 核心原理与应用实践》钱文品/著