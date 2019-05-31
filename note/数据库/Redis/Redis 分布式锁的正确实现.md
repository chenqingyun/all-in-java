## Redis 分布式锁的正确实现



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

## Redlock 算法

**Redlock** 支持多节点 Redis 实现分布式锁


实现：

使用「大多数机制」，加锁时，向过半节点发送 set(key,value,nx=True,ex=xxx)指令，只要过半节点 set 成功就认为加锁成功。释放锁时，向所有节点发送 del 指令。

相关文章：


- [Redlock：Redis分布式锁最牛逼的实现](https://mp.weixin.qq.com/s?__biz=MzU5ODUwNzY1Nw==&mid=2247484155&idx=1&sn=0c73f45f2f641ba0bf4399f57170ac9b&chksm=fe426b1dc935e20b34b9c2c26662b24229d196a46535c74a209572b6c3e9680dde09c91e065d&mpshare=1&scene=24&srcid=1201hrhwRdKz7B4Id9UXTYc2#rd)

- [拜托，面试请不要再问我Redis分布式锁的实现原理！【石杉的架构笔记】](https://juejin.im/post/5bf3f15851882526a643e207#comment)

- [基于Redis的分布式锁到底安全吗](http://zhangtielei.com/posts/blog-redlock-reasoning.html)

