<h1 align='center'> 
All In Java
</h1>



## 目录

- [Java 基础](#java-基础)
- [Java 并发编程](#java-并发编程)
- [Java 虚拟机](#java-虚拟机)
- [Redis](#redis)
- [MySQL](#mysql)
- [面经](#面经)



## Java 基础

- [介绍一下 ArrayList 和 LinkedList 的区别及实现原理](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/LinkedList%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90.md#%E4%B8%8E-arraylist-%E7%9A%84%E6%AF%94%E8%BE%83)
- [HashMap 的 底层数结构？get / put / values 方法实现过程？扩容机制？如何解决 hash 碰撞？HashTable 和 HashMap的区别？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/HashMap%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90.md#hashmap-%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90)
- ConcurrentHashMap 数据结构？实现原理？与 HashMap 比较？
- 谈谈一致 hash 算法？
- String 类为什么是 final 的？
  
- Java 中的队列都有哪些，有什么区别。

- Java8 的新特性

- Java数组和链表两种结构的操作效率，在哪些情况下(从开头开始，从结尾开始，从中间开始)，哪些操作(插入，查找，删除)的效率高

- Collections.sort 底层排序方式？

- 排序稳定性？具体场景的排序策略？

  

## 设计模式

- 对设计模式的看法和认知？有哪些设计模式？
- [单例模式：懒汉、饿汉](https://github.com/chenqingyun/all-in-java/blob/master/note/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/%E5%8D%95%E5%88%97%E6%A8%A1%E5%BC%8F.md)
- 工厂模式、装饰者模式、观察者模式等

  

## Java 并发编程

面试遇到的：

- [线程有哪些状态？如何流转的？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.md)

- [volatile 关键字？如何保证可见性和有序性？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/volatile%20%E5%85%B3%E9%94%AE%E5%AD%97.md)

- [synchronized 的实现原理与应用](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/synchronized%20%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86%E4%B8%8E%E5%BA%94%E7%94%A8.md)

- [synchronized 和 Lock 比较](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E6%98%BE%E7%A4%BA%E9%94%81%EF%BC%9ALock.md#synchronized-%E5%92%8C-lock-%E7%9A%84%E6%AF%94%E8%BE%83)

- [队列同步器 AQS 实现原理？CAS 实现原子操作有哪些问题？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E9%98%9F%E5%88%97%E5%90%8C%E6%AD%A5%E5%99%A8%EF%BC%9AAbstractQueuedSynchronizer.md#%E9%98%9F%E5%88%97%E5%90%8C%E6%AD%A5%E5%99%A8%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%88%86%E6%9E%90)

- [什么是重入锁？Reentrantlock 实现原理？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E9%87%8D%E5%85%A5%E9%94%81%EF%BC%9AReentrantLock.md)

- [乐观锁和悲观锁？](https://www.cnblogs.com/exceptioneye/p/5373477.html)

- [什么场景下应该用线程池？线程池的工作流程？介绍主要的参数？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E6%B1%A0%EF%BC%9AThreadPoolExecutor.md#%E7%BA%BF%E7%A8%8B%E6%B1%A0%E7%9A%84%E5%88%9B%E5%BB%BA)

- [线程池的调优策略？线程数的选择？线程池的最大线程数目根据什么确定？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E6%B1%A0%EF%BC%9AThreadPoolExecutor.md#%E5%A6%82%E4%BD%95%E9%80%89%E6%8B%A9%E7%BA%BF%E7%A8%8B%E6%95%B0)

- [多线程之间如何进行通信？wait 和notify ？wait 和 notify 为什么要加 synchronized？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%AD%89%E5%BE%85%E9%80%9A%E7%9F%A5%E6%9C%BA%E5%88%B6%EF%BC%9Await%20%E5%92%8C%20notify.md)

- [[译]Java虚拟机是如何执行线程同步的](https://www.hollischuang.com/archives/1876)

- [多线程如何避免死锁](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E9%94%81%E6%80%BB%E7%BB%93.md)

- 同步的数据结构有哪些？例如 concurrentHashMap 的源码理解以及内部实现原理，为什么他是同步的且效率高？

- [如何让多个线程顺序执行](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E5%A6%82%E4%BD%95%E8%AE%A9%E5%A4%9A%E4%B8%AA%E7%BA%BF%E7%A8%8B%E9%A1%BA%E5%BA%8F%E6%89%A7%E8%A1%8C.md)

- [sleep 和 wait 的区别](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%AD%89%E5%BE%85%E9%80%9A%E7%9F%A5%E6%9C%BA%E5%88%B6%EF%BC%9Await%20%E5%92%8C%20notify.md#waitsleep-%E5%92%8C-yield-%E7%9A%84%E5%8C%BA%E5%88%AB)

  

  

其他：

- [可见性、原子性、有序性。什么原因导致可见性原子性有序性问题？如何保证？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E5%8E%9F%E5%AD%90%E6%80%A7%E3%80%81%E5%8F%AF%E8%A7%81%E6%80%A7%E3%80%81%E6%9C%89%E5%BA%8F%E6%80%A7.md)

- [Thread 的 run 方法和 start 方法有什么区别？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/Thread%20%E7%B1%BB%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB.md#run-%E6%96%B9%E6%B3%95)

- [什么是多线程上下文切换？如何引起？有哪些额外开销？如何减少额外开销？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%B8%8A%E4%B8%8B%E6%96%87%E5%88%87%E6%8D%A2.md#%E5%A6%82%E4%BD%95%E5%87%8F%E5%B0%91%E4%B8%8A%E4%B8%8B%E6%96%87%E5%88%87%E6%8D%A2%E5%AF%BC%E8%87%B4%E9%A2%9D%E5%A4%96%E7%9A%84%E5%BC%80%E9%94%80)

- [什么是读写锁？如何实现的？什么是锁降级？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E8%AF%BB%E5%86%99%E9%94%81%EF%BC%9AReentrantReadWriteLock.md#reentrantReadWriteLock-%E7%9A%84%E7%89%B9%E6%80%A7)

  



## Java 虚拟机

面试遇到的：

- [Java 虚拟机运行时时数据区如何划分？都分别有哪些功能？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA%E8%BF%90%E8%A1%8C%E6%97%B6%E6%95%B0%E6%8D%AE%E5%8C%BA.md)
- [Java 内存模型。谈谈 volatile 关键字。什么是 Happens-Before 规则？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/Java%20%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B.md)
- [垃圾回收机制](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6%E6%9C%BA%E5%88%B6.md)
- [类加载机制](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E6%9C%BA%E5%88%B6.md)
- JVM内存模型
  - 线程之间如何通信 / 如何同步
  - Java 类加载流程(加载 验证 初始化 使用 销毁)，类加载器(http://ifeve.com/jvm-classloader/)
  -  （例如jdbc等的加载不同的）
- 解释一下双亲委派模型，JDK 中的所有的类加载都遵循双亲委派模型吗？为什么使用双亲委派模型？
- 频繁老年代回收怎么分析解决？
- Java内存泄露的问题调查定位：jmap，jstack的使用等等



相关文章：
[jvm面试都有什么问题？](https://www.zhihu.com/question/27339390)



## Redis

面试遇到的：

- [Redis 为什么是单线程的？为什么高性能](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E5%8D%95%E7%BA%BF%E7%A8%8B%E4%B8%8E%E9%AB%98%E5%B9%B6%E5%8F%91.md)
- Redis 的基本数据结构
- Redis 热点 key？ qps 太高如何解决?
- 介绍一下 Redis 缓存穿透(大面积访问不存在的key)？ 如何解决(拦截机制/isExists)? ; 缓存雪崩(随机失效时间/双缓存然后异步更新),动态更新
- Redis 怎么实现分布式锁？
- Redis 的持久化策略有几种？区别是什么?
- 使用 Redis 的原因是什么? 为什么 redis 这么快? (纯内存，单线程无线程切换开销， io多路复用)
- 过期删除策略(过期) -> 内存淘汰机制(内存满后): 定时删除(随机取样删除)+惰性删除  ->  maxmemory-policy:删除使用最少的/随机删除
- Redis的数据一致性问题（分布式多节点环境 & 单机环境）？
- redis 集群的理解，怎么动态增加或者删除一个节点，而保证数据不丢失。（一致性哈希问题）
- Redis的并发竞争问题如何解决
- 了解Redis事务的CAS操作吗
- 缓存机器增删如何对系统影响最小，一致性哈希的实现
- 缓存穿透的解决办法
- redis集群，高可用，原理
- mySQL里有2000w数据，redis中只存20w的数据，如何保证redis中的数据都是热点数据
- 用Redis和任意语言实现一段恶意登录保护的代码，限制1小时内每用户Id最多只能登录5次
- redis的数据淘汰策略

- [Redis分布式锁如何续期](https://juejin.im/post/5d122f516fb9a07ed911d08c?utm_source=gold_browser_extension#comment)




相关文章：
- [天下无难试之Redis面试题刁难大全](https://zhuanlan.zhihu.com/p/32540678)

- [经典面试题：如何保证缓存与数据库的双写一致性？](https://mp.weixin.qq.com/s/SKXl_DXzDdHZFe5GAyMBzg)

- [面试中关于Redis的问题看这篇就够了](https://juejin.im/post/5ad6e4066fb9a028d82c4b66#comment)

- https://github.com/doocs/advanced-java?utm_source=gold_browser_extension#%E7%BC%93%E5%AD%98



## MySQL 

面试遇到的：

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
- 数据库会死锁吗，举一个死锁的例子，mysql怎么解决死锁
- MySQL InnoDB存储的文件结构
- 索引树是如何维护的？
- 数据库自增主键可能的问题
- 数据库锁表的相关处理
- 索引失效场景
- 高并发下如何做到安全的修改同一行数据，乐观锁和悲观锁是什么，INNODB的行级锁有哪2种，解释其含义



## Spring

面试遇到的：

- 对 IOC 的理解？IOC 初始化流程？
- AOP 的理解？AOP 流程？AOP 的几种实现方式？
- 介绍一下 Spring 中的容器
- bean 的生命周期



其他：

- BeanFactory 和 FactoryBean 的区别
- BeanFactoryPostProcessor 和 BeanPostProcessor 的区别
- 有哪些设计模式？具体体现在哪里？
- Spring 事务隔离级别？实现原理？如何实现事务管理？
- srpingMVC 的原理
- springMVC 注解的意思
- spring中 beanFactory 和 ApplicationContext 的联系和区别
- spring 注入的几种方式
- spring 中循环注入的方式
- 对Spring的理解，非单例注入的原理？它的生命周期？循环注入的原理，aop的实现原理，说说aop中的几个术语，它们是怎么相互工作的？
- spring boot特性，优势，适用场景等



## MyBatis

面试遇到的：

- 介绍一下 mybatis 的查询缓存机制
- $ 和 # 的区别，使用场景分别是什么？



其他：

- 有哪些设计模式？具体体现在哪里？
- mapper 文件加载流程
- 执行流程
- SqlSession 执行流程




## 分布式相关

- [面试官们“爱不释手”的分布式系统架构到底是个什么鬼？](https://juejin.im/post/5d00ea3b6fb9a07eec59c332?utm_source=gold_browser_extension)
- 如何实现分布式缓存
- [对分布式事务及两阶段提交、三阶段提交的理解](https://www.cnblogs.com/binyue/p/3678390.html)
- 服务器雪崩是怎么造成的？之前有这样的经历吗？怎么防备？
- 高并发架构的设计思路
- session 和 cookie 的区别和联系，session的生命周期，多个服务部署时session管理。
- redis/zk节点宕机如何处理
- 数据的垂直拆分水平拆分。



## MQ

面试遇到的：

- 异步 解耦 削峰填谷
- 消息丢失， 消息重复
- 性能设计 / 可靠性设计 / 可用性设计
- [如何实现何高并发下的削峰，限流？](https://blog.csdn.net/lx_Frolf/article/details/86132291)
- MQ的连接是线程安全的吗
- MQ系统的数据如何保证不丢失



### zookeeper

面试遇到的：

- 介绍一下 zk 的选举算法
- zookeeper原理和适用场景
- zookeeper watch机制



### 分布式服务框架：Dubbo

面试遇到的：

- dubbo 的工作原理？注册中心挂了可以继续通信吗？
- dubbo 支持哪些序列化协议？说一下 hessian 的数据结构？PB 知道吗？为什么 PB 的效率是最高的？
- 如何自己设计一个类似 dubbo 的 rpc 框架？需要考虑的点有哪些呢？（动态代理 通信协议 负载均衡 序列化与反序列化）
- 说一下 RPC 的调用过程。
- dubbo 的异步调用。
- 服务发现和服务监控？
- 如何基于 dubbo 进行服务治理、服务降级、失败重试以及超时重试？
- 描述一个服务从发布到被消费的详细过程



其他：

- dubbo 的负载均衡和高可用策略？动态代理策略？
- dubbo 的 SPI 思想是什么？
- dubbo 服务接口的幂等性如何设计（比如不能重复扣款，不能重复生成订单，不能重复创建卡号）？
- dubbo 服务接口请求的顺序性如何保证？



### 分布式架构常见的要解决的技术问题

- 分布式会话

- 分布式锁

- 分布式事务

- 分布式搜索

- 分布式缓存

- 分布式消息队列

- 统一配置中心

- 分布式存储，数据库分库分表

- 限流、熔断、降级等



## 算法和数据结构

- 使用随机算法产生一个数，要求把1-1000W之间这些数全部生成。（考察高效率，解决产生冲突的问题）
- 两个有序数组的合并排序
- 一个数组的倒序
- 计算一个正整数的正平方根
- 说白了就是常见的那些查找排序算法
- 数组和链表数据结构描述，各自的时间复杂度
- 二叉树遍历
- 快速排序
- BTree相关的操作
- 在工作中遇到过哪些设计模式，是如何应用的
- hash算法的有哪几种，优缺点，使用场景
- 什么是一致性hash
- paxos算法


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



## 面经

- [【斩获7枚offer，入职阿里平台事业部】横扫阿里、美团、京东、 去哪儿之后，我写下了这篇面经！](https://mp.weixin.qq.com/s/BXGUU2VTP_5u4qhgHGlGIg)

- [金三银四铜五铁六，Offer收到手软！](https://mp.weixin.qq.com/s/UzR_emAr9Gh0P_9xUNOUPQ)

- [两年 JAVA 程序员的面试总结](https://mp.weixin.qq.com/s/gIiopR-uXq58lCrx1LoK9w)

- [一个学渣的阿里之路](https://mp.weixin.qq.com/s/-2pwc9yus6n5mn37fQqNEA)

- [字节跳动、腾讯后台开发面经分享(2019.5)](https://juejin.im/post/5cf7ea91e51d4576bc1a0dc2?utm_source=gold_browser_extension)
