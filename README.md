<h1 align='center'> 
All In Java
</h1>



## 目录

- [Java 基础](#java-基础)
- [Java 并发编程](#java-并发编程)
- [Java 虚拟机](#java-虚拟机)
- [MySQL](#mysql)
- [Redis](#redis)
- [Elasticsearch](#elasticsearch)
- [框架](#框架)
  - [Spring，SpringMVC](#springspringmvc)
  - [MyBatis](#MyBatis)
- [分布式架构](#分布式架构)
  - [分布式架构常见的要解决的技术问题](#分布式架构常见的要解决的技术问题)
  - [ZooKeeper](#zookeeper)
  - [Dubbo](#dubbo)
  - [RabbitMQ](#rabbitmq)
- [设计模式](#设计模式)
- [算法和数据结构](#算法和数据结构)
- [Linux](#linux)
- [项目经验](#项目经验)
- [参考](#参考)
- [面经](#面经)





> 以下内容有些只是简单的知识性总结，更为深入的分析 TODO。



## Java 基础

- [Java 异常有哪些类型？如何处理异常？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/Java%20%E5%BC%82%E5%B8%B8%E5%A4%84%E7%90%86%E6%9C%BA%E5%88%B6.md)

- [ArrayList 实现原理？如何扩容？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/ArrayList%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90.md)

- [LinkedList 实现原理？与 ArrayList 的区别](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/LinkedList%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90.md#%E4%B8%8E-arraylist-%E7%9A%84%E6%AF%94%E8%BE%83)
  
- [CopyOnWriteArrayList 原理？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/CopyOnWriteArrayList.md)
  
- [HashMap 的 底层数结构？get / put / values 方法实现过程？扩容机制？如何解决 hash 碰撞？HashTable 和 HashMap的区别？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/HashMap%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90.md#hashmap-%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90)

- [ConcurrentHashMap 数据结构？实现原理？与 HashMap 比较？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/ConcurrentHashMap%20%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8%E5%8E%9F%E7%90%86.md)

- 基本数据类型原子类实现原理？（volatile + CAS）

  


## Java 并发编程

- [线程有哪些状态？如何流转的？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.md)

- [volatile 关键字？如何保证可见性和有序性？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/volatile%20%E5%85%B3%E9%94%AE%E5%AD%97.md)

- [synchronized 的实现原理与应用](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/synchronized%20%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86%E4%B8%8E%E5%BA%94%E7%94%A8.md)

- [synchronized 和 Lock 比较](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E6%98%BE%E7%A4%BA%E9%94%81%EF%BC%9ALock.md#synchronized-%E5%92%8C-lock-%E7%9A%84%E6%AF%94%E8%BE%83)

- [队列同步器 AQS 实现原理？CAS 实现原子操作有哪些问题？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E9%98%9F%E5%88%97%E5%90%8C%E6%AD%A5%E5%99%A8%EF%BC%9AAbstractQueuedSynchronizer.md#%E9%98%9F%E5%88%97%E5%90%8C%E6%AD%A5%E5%99%A8%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%88%86%E6%9E%90)

- [什么是重入锁？Reentrantlock 实现原理？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E9%87%8D%E5%85%A5%E9%94%81%EF%BC%9AReentrantLock.md)

- [什么是读写锁？如何实现的？什么是锁降级？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E8%AF%BB%E5%86%99%E9%94%81%EF%BC%9AReentrantReadWriteLock.md#reentrantReadWriteLock-%E7%9A%84%E7%89%B9%E6%80%A7)

- [乐观锁和悲观锁？](https://juejin.im/post/5b4977ae5188251b146b2fc8#comment)

- [什么场景下应该用线程池？线程池的工作流程？介绍主要的参数？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E6%B1%A0%EF%BC%9AThreadPoolExecutor.md)

- [线程池的调优策略？线程数的选择？线程池的最大线程数目根据什么确定？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E6%B1%A0%EF%BC%9AThreadPoolExecutor.md#%E5%A6%82%E4%BD%95%E9%80%89%E6%8B%A9%E7%BA%BF%E7%A8%8B%E6%95%B0)

- [多线程之间如何进行通信？wait 和notify ？wait 和 notify 为什么要加 synchronized？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%AD%89%E5%BE%85%E9%80%9A%E7%9F%A5%E6%9C%BA%E5%88%B6%EF%BC%9Await%20%E5%92%8C%20notify.md)

- [sleep 和 wait 的区别](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%AD%89%E5%BE%85%E9%80%9A%E7%9F%A5%E6%9C%BA%E5%88%B6%EF%BC%9Await%20%E5%92%8C%20notify.md#waitsleep-%E5%92%8C-yield-%E7%9A%84%E5%8C%BA%E5%88%AB)

- [[译]Java虚拟机是如何执行线程同步的](https://www.hollischuang.com/archives/1876)

- [多线程如何避免死锁](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E9%94%81%E6%80%BB%E7%BB%93.md#%E5%A6%82%E4%BD%95%E9%81%BF%E5%85%8D%E6%AD%BB%E9%94%81)

- [如何让多个线程顺序执行](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E5%A6%82%E4%BD%95%E8%AE%A9%E5%A4%9A%E4%B8%AA%E7%BA%BF%E7%A8%8B%E9%A1%BA%E5%BA%8F%E6%89%A7%E8%A1%8C.md)

- [可见性、原子性、有序性。什么原因导致可见性原子性有序性问题？如何保证？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E5%8E%9F%E5%AD%90%E6%80%A7%E3%80%81%E5%8F%AF%E8%A7%81%E6%80%A7%E3%80%81%E6%9C%89%E5%BA%8F%E6%80%A7.md)

- [Thread 的 run 方法和 start 方法有什么区别？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/Thread%20%E7%B1%BB%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB.md#run-%E6%96%B9%E6%B3%95)

- [什么是多线程上下文切换？如何引起？有哪些额外开销？如何减少额外开销？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%B8%8A%E4%B8%8B%E6%96%87%E5%88%87%E6%8D%A2.md#%E5%A6%82%E4%BD%95%E5%87%8F%E5%B0%91%E4%B8%8A%E4%B8%8B%E6%96%87%E5%88%87%E6%8D%A2%E5%AF%BC%E8%87%B4%E9%A2%9D%E5%A4%96%E7%9A%84%E5%BC%80%E9%94%80)

- CountDownLatch，CyclicBarrier，Exchanger，Semaphore

  

## Java 虚拟机

- [Java 虚拟机运行时时数据区如何划分？都分别有哪些功能？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA%E8%BF%90%E8%A1%8C%E6%97%B6%E6%95%B0%E6%8D%AE%E5%8C%BA.md)

- [OutOfMemoryError 异常出现原因及解决方案](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/OutOfMemoryError%20%E8%AF%A6%E8%A7%A3.md)

- [Java 内存模型？线程之间如何通信 / 如何同步？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/Java%20%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B.md#%E7%BA%BF%E7%A8%8B%E4%B9%8B%E9%97%B4%E7%9A%84%E9%80%9A%E4%BF%A1)

- [垃圾回收机制：如何判断对象是否存活？垃圾收集算法？垃圾收集器？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6%E6%9C%BA%E5%88%B6.md)

- [如何减少 GC 开销？Major GC 和 Minor GC 频繁如何解决？什么时候可能会触发 STW 的 Full GC ？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6%E6%9C%BA%E5%88%B6.md#%E5%A6%82%E4%BD%95%E5%87%8F%E5%B0%91-gc-%E5%BC%80%E9%94%80)

- [类加载机制？双亲委派模型？为什么使用双亲委派模型？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E6%9C%BA%E5%88%B6.md)

- [Java 性能调优，Java内存泄露的问题调查定位：jstack，jstat，jmap 的使用等等](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/JVM%20%E6%80%A7%E8%83%BD%E8%B0%83%E4%BC%98.md)

- [理解String.intern()](https://blog.csdn.net/soonfly/article/details/70147205)

  


## MySQL 

- [基础架构：一条 SELECT 语句是如何执行的](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/MySQL%20%E9%80%BB%E8%BE%91%E6%9E%B6%E6%9E%84%EF%BC%9A%E4%B8%80%E6%9D%A1%20SELECT%20%20%E8%AF%AD%E5%8F%A5%E7%9A%84%E6%89%A7%E8%A1%8C%E8%BF%87%E7%A8%8B.md)
- [日志模块：一条 SQL 更新语句如何执行](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/MySQl%20%E6%97%A5%E5%BF%97%E7%B3%BB%E7%BB%9F%EF%BC%9A%E4%B8%80%E6%9D%A1%20SQL%20%E6%9B%B4%E6%96%B0%E8%AF%AD%E5%8F%A5%E6%98%AF%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C%E7%9A%84.md)
- [事务隔离级别](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/MySQL%20%E4%BA%8B%E5%8A%A1.md)
- [索引原理](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/%E7%B4%A2%E5%BC%95.md)
  - 为什么使用索引查询效率高
  - 为什么使用 B+ 树
  - 基于主键索引和普通索引的查询有什么区别？
  - 普通索引和唯一索引如何选择？
- [锁](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/%E9%94%81.md)
- [全局锁、表级锁、行级锁](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/%E9%94%81.md#%E5%A6%82%E4%BD%95%E8%A7%A3%E5%86%B3%E7%94%B1%E7%83%AD%E7%82%B9%E8%A1%8C%E6%9B%B4%E6%96%B0%E5%AF%BC%E8%87%B4%E7%9A%84%E6%80%A7%E8%83%BD%E9%97%AE%E9%A2%98)
  - 如何安全地给小表加字段？
  - 死锁的处理策略
  - 如何解决由热点行更新导致的性能问题
- [SQL 语句为什么变慢了？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/SQL%20%E8%AF%AD%E5%8F%A5%E4%B8%BA%E4%BB%80%E4%B9%88%E5%8F%98%E6%85%A2%E4%BA%86.md)
- [cont(*) 为什么慢？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/cont(*)%20%E4%B8%BA%E4%BB%80%E4%B9%88%E6%85%A2.md)
  - cont(*) 的实现
  - MySQL 对 cont(*) 做了什么优化？
  - 如何提高计数效率？
  - count(*)，count(id)，count(1)，count(字段) 的效率和区别
- MySQL 如何保证主备一致？
- [MySQL 性能优化](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/MySQL%20%E6%80%A7%E8%83%BD%E4%BC%98%E5%8C%96.md)
- 当前读和快照读？
- MySql 的主从实时备份同步的配置，以及原理(从库读主库的binlog)，读写分离
- 事物的四个特性，以及各自的特点（原子、隔离）等等，项目怎么解决这些问题
- 数据库会死锁吗，举一个死锁的例子，mysql 怎么解决死锁
- 索引树是如何维护的？
- 数据库自增主键可能的问题
- 数据库锁表的相关处理
- 索引失效场景
- 高并发下如何做到安全的修改同一行数据，乐观锁和悲观锁是什么，INNODB的行级锁有哪2种，解释其含义



## Redis

- [Redis 为什么是单线程的？为什么高性能](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E5%8D%95%E7%BA%BF%E7%A8%8B%E4%B8%8E%E9%AB%98%E5%B9%B6%E5%8F%91.md)
- [Redis 的基本数据结构](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E5%9F%BA%E7%A1%80%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84.md)
- Redis 热点 key ？ qps 太高如何解决?
- 介绍一下 Redis 缓存穿透(大面积访问不存在的key)？ 如何解决(拦截机制/isExists)? ; 缓存雪崩(随机失效时间/双缓存然后异步更新),动态更新
- [了解什么是 redis 的雪崩、穿透和击穿？Redis 崩溃之后会怎么样？系统该如何应对这种情况？如何处理 Redis 的穿透（拦截机制 / isExists）？](https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/redis-caching-avalanche-and-caching-penetration.md)
- [Redis 怎么实现分布式锁？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E7%9A%84%E6%AD%A3%E7%A1%AE%E5%AE%9E%E7%8E%B0.md)
- [Redis 的持久化策略有几种？区别是什么?](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E6%8C%81%E4%B9%85%E5%8C%96%E6%9C%BA%E5%88%B6.md)
- Redis 的数据一致性问题（分布式多节点环境 & 单机环境）？
- [如何保证缓存与数据库的双写一致性？](https://mp.weixin.qq.com/s/SKXl_DXzDdHZFe5GAyMBzg)
- Redis 集群的理解，怎么动态增加或者删除一个节点，而保证数据不丢失。（一致性哈希问题）
- Redis的并发竞争问题如何解决
- [Redis 事务及 CAS ？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E4%BA%8B%E5%8A%A1.md)
- redis 集群，高可用，原理
- MySQL 里有 2000w 数据，redis 中只存 20w 的数据，如何保证 redis 中的数据都是热点数据
- 用 Redis 和任意语言实现一段恶意登录保护的代码，限制1小时内每用户Id最多只能登录5次
- [Redis 过期删除策略和内存淘汰策略](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E8%BF%87%E6%9C%9F%E5%88%A0%E9%99%A4%E7%AD%96%E7%95%A5%E5%92%8C%E5%86%85%E5%AD%98%E6%B7%98%E6%B1%B0%E7%AD%96%E7%95%A5.md)
- [Redis分布式锁如何续期](https://juejin.im/post/5d122f516fb9a07ed911d08c?utm_source=gold_browser_extension#comment)



相关文章：

- [天下无难试之Redis面试题刁难大全](https://zhuanlan.zhihu.com/p/32540678)
- [面试中关于Redis的问题看这篇就够了](https://juejin.im/post/5ad6e4066fb9a028d82c4b66#comment)
- https://github.com/doocs/advanced-java?utm_source=gold_browser_extension#%E7%BC%93%E5%AD%98



## Elasticsearch



## 框架

### Spring，SpringMVC

- [对 IOC 的理解？IOC 初始化流程？](https://github.com/chenqingyun/all-in-java/blob/master/note/Spring/Spring%20IoC.md)
- [介绍一下 Spring 中的容器？BeanFactory 和 ApplicationContext 的联系和区别](https://github.com/chenqingyun/all-in-java/blob/master/note/Spring/Spring%20IoC.md#%E4%BB%80%E4%B9%88%E6%98%AF-spring-%E5%AE%B9%E5%99%A8)
- [bean 的生命周期](https://github.com/chenqingyun/all-in-java/blob/master/note/Spring/Spring%20IoC.md#bean-%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
- [AOP 的理解？AOP 流程？AOP 的几种实现方式？](https://github.com/chenqingyun/all-in-java/blob/master/note/Spring/Spring%20AOP.md)

- [有哪些设计模式？具体体现在哪里？](https://github.com/chenqingyun/all-in-java/blob/master/note/Spring/Spring%20%E6%A6%82%E8%BF%B0.md#spring-%E4%B8%AD%E7%94%A8%E5%88%B0%E4%BA%86%E5%93%AA%E4%BA%9B%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F)
- [Spring 事务隔离级别？实现原理？如何实现事务管理？](https://github.com/chenqingyun/all-in-java/blob/master/note/Spring/Spring%20%E4%BA%8B%E5%8A%A1.md)
- [SpringMVC 的原理](https://github.com/chenqingyun/all-in-java/blob/master/note/Spring/SpringMVC.md)
- Spring boot 特性，优势，适用场景等



### MyBatis

- [MyBatis 只写了接口为啥就能执行 SQL ？SqlSession 执行流程？](https://github.com/chenqingyun/all-in-java/blob/master/note/MyBatis/MyBatis%20%E5%8F%AA%E5%86%99%E4%BA%86%E6%8E%A5%E5%8F%A3%E4%B8%BA%E5%95%A5%E5%B0%B1%E8%83%BD%E6%89%A7%E8%A1%8C%20SQL.md)

- [介绍一下 mybatis 的查询缓存机制](https://github.com/chenqingyun/all-in-java/blob/master/note/MyBatis/MyBatis%20%E7%BC%93%E5%AD%98%E6%9C%BA%E5%88%B6.md)
- [$ 和 # 的区别，使用场景分别是什么？](https://github.com/chenqingyun/all-in-java/blob/master/note/MyBatis/%24%20%E5%92%8C%20%23%20%E7%9A%84%E5%8C%BA%E5%88%AB.md)

- [有哪些设计模式？具体体现在哪里？](http://www.crazyant.net/2022.html)




## 分布式架构



### 分布式架构常见的要解决的技术问题

- [面试官们“爱不释手”的分布式系统架构到底是个什么鬼？](https://juejin.im/post/5d00ea3b6fb9a07eec59c332?utm_source=gold_browser_extension)
- 分布式会话
  - session 和 cookie 的区别和联系，session的生命周期，多个服务部署时session管理。
- 分布式锁
- 分布式事务
  - https://juejin.im/post/5b5a0bf9f265da0f6523913b
  - [[对分布式事务及两阶段提交、三阶段提交的理解](https://www.cnblogs.com/binyue/p/3678390.html)](https://www.cnblogs.com/binyue/p/3678390.html)
- 分布式搜索
- 分布式缓存
- 分布式消息队列
- 统一配置中心
- 分布式存储，数据库分库分表
- [服务雪崩、限流、熔断、降级等](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/%E6%9C%8D%E5%8A%A1%E9%9B%AA%E5%B4%A9%E3%80%81%E7%86%94%E6%96%AD%E3%80%81%E9%99%8D%E7%BA%A7%E5%92%8C%E9%99%90%E6%B5%81.md)
- redis / zk 节点宕机如何处理
- 高并发架构的设计思路



### ZooKeeper

- [介绍一下 ZooKeeper 的选举算法](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/ZooKeeper%20%E5%8E%9F%E7%90%86.md#leader-%E9%80%89%E4%B8%BE%E8%BF%87%E7%A8%8B)
- [ZooKeeper 原理和适用场景](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/ZooKeeper%20%E5%8E%9F%E7%90%86.md#%E6%9C%89%E5%93%AA%E4%BA%9B%E5%BA%94%E7%94%A8%E5%9C%BA%E6%99%AF)
- [ZooKeeper Watcher 机制](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/ZooKeeper%20%E5%8E%9F%E7%90%86.md#watcher-%E6%9C%BA%E5%88%B6)
- [ZK 集群如何实现高可用部署？](https://cloud.tencent.com/developer/article/1442971)
- [使用 Redis 如何设计分布式锁？使用 Zookeeper 来设计分布式锁可以吗？以上两种分布式锁的实现方式哪种效率比较高？](https://github.com/doocs/advanced-java/blob/master/docs/distributed-system/distributed-lock-redis-vs-zookeeper.md)



### Dubbo

- [Dubbo 的工作原理？注册中心挂了可以继续通信吗？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo.md)
- [说一下 RPC 的调用过程](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8%E8%BF%87%E7%A8%8B%E5%88%86%E6%9E%90.md#dubbo-%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8%E8%BF%87%E7%A8%8B%E5%88%86%E6%9E%90)
- [dubbo 支持哪些序列化协议？介绍一下，说一下 hessian 的数据结构？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20%E5%BA%8F%E5%88%97%E5%8C%96%E5%8D%8F%E8%AE%AE.md)
- [如何自己设计一个类似 dubbo 的 rpc 框架？需要考虑的点有哪些呢？（动态代理、通信协议、负载均衡、序列化与反序列化）](https://github.com/doocs/advanced-java/blob/master/docs/distributed-system/dubbo-rpc-design.md)
- [Dubbo 的异步调用](http://dubbo.apache.org/zh-cn/blog/dubbo-invoke.html)
- [Dubbo 服务监控](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20%E6%9C%8D%E5%8A%A1%E7%9B%91%E6%8E%A7.md)
- [如何基于 dubbo 进行服务治理、服务降级、失败重试以及超时重试？](https://github.com/doocs/advanced-java/blob/master/docs/distributed-system/dubbo-service-management.md)
- [Dubbo 协议](http://dubbo.apache.org/zh-cn/docs/user/references/protocol/dubbo.html) 
- [描述一个服务从发布到被消费的详细过程，服务注册、服务暴露、服务发现、服务调用](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20%E6%9C%8D%E5%8A%A1%E6%B3%A8%E5%86%8C%E3%80%81%E6%9C%8D%E5%8A%A1%E6%9A%B4%E9%9C%B2%E3%80%81%E6%9C%8D%E5%8A%A1%E5%8F%91%E7%8E%B0%E5%92%8C%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8.md)
- [Dubbo 的负载均衡](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1.md)
- [Dubbo 集群容错策略](http://dubbo.apache.org/zh-cn/docs/user/demos/fault-tolerent-strategy.html)
- [Dubbo 的 SPI 思想是什么？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20SPI%20%E6%9C%BA%E5%88%B6.md)
- [dubbo 服务接口的幂等性如何设计（比如不能重复扣款，不能重复生成订单，不能重复创建卡号）？](https://github.com/doocs/advanced-java/blob/master/docs/distributed-system/distributed-system-idempotency.md)
- [dubbo 服务接口请求的顺序性如何保证？](https://github.com/doocs/advanced-java/blob/master/docs/distributed-system/distributed-system-request-sequence.md)



### RabbitMQ

- [为什么使用消息队列？有哪些适用场景？使用消息队列有哪些问题？Kafka、ActiveMQ、RabbitMQ、RocketMQ 都有什么优点和缺点？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/%E6%B6%88%E6%81%AF%E9%98%9F%E5%88%97/%E6%B6%88%E6%81%AF%E9%98%9F%E5%88%97.md)
- [如何保证消息队列的高可用？](https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/how-to-ensure-high-availability-of-message-queues.md#%E9%95%9C%E5%83%8F%E9%9B%86%E7%BE%A4%E6%A8%A1%E5%BC%8F%E9%AB%98%E5%8F%AF%E7%94%A8%E6%80%A7)
- [如何保证消息不被重复消费？（如何保证消息消费的幂等性）](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/%E6%B6%88%E6%81%AF%E9%98%9F%E5%88%97/RabbitMQ%20%E6%B6%88%E6%81%AF%E9%87%8D%E5%A4%8D%E6%B6%88%E8%B4%B9.md)
- [如何保证消息的可靠性传输？（如何处理消息丢失的问题）](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/%E6%B6%88%E6%81%AF%E9%98%9F%E5%88%97/RabbitMQ%20%E6%B6%88%E6%81%AF%E4%B8%A2%E5%A4%B1%E9%97%AE%E9%A2%98.md)
- [如何保证消息的顺序性？](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/%E6%B6%88%E6%81%AF%E9%98%9F%E5%88%97/RabbitMQ%20%E6%B6%88%E6%81%AF%E9%A1%BA%E5%BA%8F%E6%80%A7.md)
- [如何解决消息队列的延时以及过期失效问题？消息队列满了以后该怎么处理？有几百万消息持续积压几小时，说说怎么解决？](https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/mq-time-delay-and-expired-failure.md)
- [如果让你写一个消息队列，该如何进行架构设计啊？说一下你的思路。（可用性、高可靠性、可扩展性等）](https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/mq-design.md)
- [如何实现何高并发下的削峰，限流？](https://blog.csdn.net/lx_Frolf/article/details/86132291)



推荐阅读：

《RabbitMQ 实战指南》



## 设计模式

- 在工作中遇到过哪些设计模式，是如何应用的？
- [单例模式：懒汉、饿汉](https://github.com/chenqingyun/all-in-java/blob/master/note/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/%E5%8D%95%E5%88%97%E6%A8%A1%E5%BC%8F.md)
- [策略模式：优化 if-else](https://github.com/chenqingyun/all-in-java/blob/master/note/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/%E7%AD%96%E7%95%A5%E6%A8%A1%E5%BC%8F.md)
- [工厂模式](https://github.com/chenqingyun/all-in-java/blob/master/note/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/%E5%B7%A5%E5%8E%82%E6%A8%A1%E5%BC%8F.md)
- 装饰者模式、观察者模式等



## 算法和数据结构

- 使用随机算法产生一个数，要求把1-1000W之间这些数全部生成。（考察高效率，解决产生冲突的问题）
- 两个有序数组的合并排序
- 一个数组的倒序
- 计算一个正整数的正平方根
- 数组和链表数据结构描述，各自的时间复杂度
- 二叉树遍历
- 快速排序
- BTree 相关的操作
- hash 算法的有哪几种，优缺点，使用场景
- 一致性hash 算法
- paxos 算法
- 各种排序算法的稳定性？具体场景的排序策略？




## Linux



## 项目经验

- [alibaba/canal 阿里巴巴 mysql 数据库 binlog 增量订阅&消费组件](https://www.cnblogs.com/niejunlei/p/10323085.html)
- 介绍一下项目里面如何处理异常
- 介绍一下项目里面如何处理日志
- 如果应用里面的一个接口突然变得很慢， 怎么样去排查呢？
- 架构设计：权限系统
- 遇到大数据量的报表：应该如何保证正常下载和吞吐量(多用户下载/excel生成速度))
- 挑一个你觉得做的最好或者最熟悉的项目着重的介绍一下吧（架构 ; 面试者在其中担任的角色 ; 亮点(性能，监控，优化)）
- 项目中你遇到的最大的一个挑战是什么？ (新技术; 架构设计)
- 项目中有没有使用什么算法之类的? 有的话介绍一下 (算法介绍)	
- 项目中有没有遇到其他的问题呢? （OOM ; 系统优化; 其他亮点)
- 项目中用的中间件的理解(Dubbo、MQ、Redis、kafka、zk)



## 参考

- [doocs / advanced-java](https://github.com/doocs/advanced-java)



## 面经

- [【斩获7枚offer，入职阿里平台事业部】横扫阿里、美团、京东、 去哪儿之后，我写下了这篇面经！](https://mp.weixin.qq.com/s/BXGUU2VTP_5u4qhgHGlGIg)

- [金三银四铜五铁六，Offer收到手软！](https://mp.weixin.qq.com/s/UzR_emAr9Gh0P_9xUNOUPQ)

- [两年 JAVA 程序员的面试总结](https://mp.weixin.qq.com/s/gIiopR-uXq58lCrx1LoK9w)

- [一个学渣的阿里之路](https://mp.weixin.qq.com/s/-2pwc9yus6n5mn37fQqNEA)

- [字节跳动、腾讯后台开发面经分享(2019.5)](https://juejin.im/post/5cf7ea91e51d4576bc1a0dc2?utm_source=gold_browser_extension)



