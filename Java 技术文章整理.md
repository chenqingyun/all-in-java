# Java 技术文章整理

## 目录
- [Java 基础](#java-基础)
- [Java 并发编程](#java-并发编程)
- [Java 虚拟机](#java-虚拟机)
- [设计模式](#设计模式)
- [数据库](#数据库)
- [数据结构与算法](#数据结构与算法)
- [Linux](#linux)
- [框架](#框架)
- [中间件](#中间件)
  - [Dubbo](#dubbo)
  - [ZooKeeper](#zookeeper)
  - [消息中间件](#消息中间件)
- [系统架构]()
- [工具](#工具)
- [面经](#面经)



## Java 基础

- [Static 关键字的 5 种用法](https://mp.weixin.qq.com/s?__biz=MzI3ODcxMzQzMw==&mid=2247488852&idx=2&sn=812067bdf0f0acf1cff83055200ada3d&chksm=eb539062dc241974b25fbb471526f838f9d076761e96dced2a943a21f42606a96866d29ae94d&scene=21#wechat_redirect)



**面试常见：**

- [阿里面试题：为什么 Map 桶中个数超过8才转为红黑树](https://www.jianshu.com/p/f6487d4103d0?utm_campaign=haruki&utm_content=note&utm_medium=reader_share&utm_source=weixin)

- [这一次，彻底解决Java的值传递和引用传递](https://juejin.im/post/5bce68226fb9a05ce46a0476#comment)



## Java 并发编程

- [分享一道阿里Java并发面试题 (详细分析篇)](https://mp.weixin.qq.com/s/wvBziVsRNS7pXyknAHEYKg)

- [Java 并发源码合集](https://mp.weixin.qq.com/s?__biz=MzUzMTA2NTU2Ng==&mid=2247486261&idx=1&sn=bd69f26aadfc826f6313ffbb95e44ee5&chksm=fa497484cd3efd92352d6fb3d05ccbaebca2fafed6f18edbe5be70c99ba088db5c8a7a8080c1&scene=21#wechat_redirect)



**面试常见：**



## Java 虚拟机

- [Java虚拟机-JVM各种参数配置大全详细](https://blog.csdn.net/luckystar92/article/details/77320144)



**面试常见：**

- [JVM+GC 面试题](https://juejin.im/post/5cb872a2e51d456e79545c6c?utm_source=gold_browser_extension)
- [再有人问你Java内存模型是什么，就把这篇文章发给他](https://juejin.im/post/5cad4adc5188251b1f4d87d2#comment)
- [一文学会 Java 垃圾回收机制](https://juejin.im/post/5caf2b5cf265da036c577751#comment)
- [JVM的最基本的区域划分以及工作原理](https://mp.weixin.qq.com/s/zDYphlLpp0hrFbyJbw7umQ)



## 设计模式

- [Java设计模式－单例模式：单例的六种实现](https://juejin.im/post/5ce56e9e518825332d13c266?utm_source=gold_browser_extension)

## 数据库

### MySQL


**面试常见：**

- [腾讯面试：一条SQL语句执行得很慢的原因有哪些？](https://juejin.im/post/5cbed894e51d456e8240dd00?utm_source=gold_browser_extension)
- [答上这几个问题，简历再写熟悉数据库！](https://mp.weixin.qq.com/s/LSdMZD7sPIkdotCw2ZQNjg)
- [面试题：MySQL索引为什么用B+树？](https://juejin.im/post/5ce50d866fb9a07eb94f626c?utm_source=gold_browser_extension)


### Redis

- [Redis闲谈（1）：构建知识图谱](https://juejin.im/post/5cce56cee51d453aa307c80e?utm_source=gold_browser_extension)

- [【面试】吃透了这些Redis知识点，面试官一定觉得你很NB](https://juejin.im/post/5cc6bb975188252e8925f0c8?utm_source=gold_browser_extension#comment)

- [Redis 集群模式的工作原理能说一下么？在集群模式下，Redis 的 key 是如何寻址的？分布式寻址都有哪些算法？了解一致性 hash 算法吗？如何动态增加和删除一个节点？](https://www.javazhiyin.com/22957.html)



**面试常见：**

- [经典面试题：如何保证缓存与数据库的双写一致性？](https://mp.weixin.qq.com/s/ulFxFs_E226oAGEernOjNg)
- [除了缓存，Redis 都解决了哪些问题？](https://mp.weixin.qq.com/s/HUmByMp__slWxoOFdbQCOg)
- [百度社招面试题——如何用Redis实现分布式锁](https://juejin.im/post/5cadf58e6fb9a068973ec722?utm_source=gold_browser_extension)
- [你所不知道的Redis热点问题以及如何发现热点](https://juejin.im/post/5cee39a26fb9a07ed36e8c4c?utm_source=gold_browser_extension)



## 框架

- [腾讯这套SpringMvc面试题你懂多少（面试必备）](https://juejin.im/post/5cc2de6f5188252d9109875d?utm_source=gold_browser_extension)



## 中间件

### 消息中间件
- [简历写了会Kafka，面试官90%会让你讲讲acks参数对消息持久化的影响](https://mp.weixin.qq.com/s/IxS46JAr7D9sBtCDr8pd7A)
- [17 个方面，综合对比 Kafka、RabbitMQ、RocketMQ、ActiveMQ 四个分布式消息队列](https://mp.weixin.qq.com/s/gb4DmxhpdKlY_1wgj5ng2g)
- [如何发现 Redis 热点 Key ，解决方案有哪些？](https://mp.weixin.qq.com/s/3mw5kliTo-4Pzq-PH-ly1w)
- [【Kafka】《Kafka权威指南》入门](https://juejin.im/post/5ce4a0705188253382696dbf?utm_source=gold_browser_extension)



## 系统架构

- [谈谈对分布式事务的一点理解和解决方案](https://mp.weixin.qq.com/s/cBb2htYEs6awfuxrXJznzw)
- [面试官们“爱不释手”的分布式系统架构到底是个什么鬼？](https://juejin.im/post/5d00ea3b6fb9a07eec59c332?utm_source=gold_browser_extension)

