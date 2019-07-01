# Java 面试题整理

## 目录

- [并发编程](#并发编程)
- [Java 虚拟机](#java-虚拟机)
- [Redis](#redis)
- [MySQL](#mysql)
- [面经](#面经)



## Java 基础



## Java 并发编程

- [可见性、原子性、有序性。什么原因导致可见性原子性有序性问题？如何保证？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E5%8E%9F%E5%AD%90%E6%80%A7%E3%80%81%E5%8F%AF%E8%A7%81%E6%80%A7%E3%80%81%E6%9C%89%E5%BA%8F%E6%80%A7.md)
- [线程的生命周期](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.md)
- [Thread 的 run 方法和 start 方法有什么区别？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/Thread%20%E7%B1%BB%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB.md#run-%E6%96%B9%E6%B3%95)
- 线程中的 wait() 和 sleep() 方法有什么区别？
- [什么是多线程上下文切换？如何引起？有哪些额外开销？如何减少额外开销？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%B8%8A%E4%B8%8B%E6%96%87%E5%88%87%E6%8D%A2.md#%E5%A6%82%E4%BD%95%E5%87%8F%E5%B0%91%E4%B8%8A%E4%B8%8B%E6%96%87%E5%88%87%E6%8D%A2%E5%AF%BC%E8%87%B4%E9%A2%9D%E5%A4%96%E7%9A%84%E5%BC%80%E9%94%80)
- 多线程之间如何进行通信？
- [volatile 关键字有什么作用？如何保证可见性和有序性？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B/volatile%20%E5%85%B3%E9%94%AE%E5%AD%97.md)
- synchronized 的实现原理与应用
- 为什么要使用线程池？
- 什么是重入锁？如何实现的？






## Java 虚拟机

- [Java 虚拟机运行时时数据区如何划分？都分别有哪些功能？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA%E8%BF%90%E8%A1%8C%E6%97%B6%E6%95%B0%E6%8D%AE%E5%8C%BA.md)
- [Java 内存模型。谈谈 volatile 关键字。什么是 Happens-Before 规则？](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/Java%20%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B.md)

- [垃圾回收机制](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6%E6%9C%BA%E5%88%B6.md)
- [类加载机制](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E8%99%9A%E6%8B%9F%E6%9C%BA/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E6%9C%BA%E5%88%B6.md)



相关文章：
[jvm面试都有什么问题？](https://www.zhihu.com/question/27339390)



## Redis

- [Redis 为什么是单线程的？为什么高性能](https://github.com/chenqingyun/all-in-java/blob/master/note/%E6%95%B0%E6%8D%AE%E5%BA%93/Redis/Redis%20%E5%8D%95%E7%BA%BF%E7%A8%8B%E4%B8%8E%E9%AB%98%E5%B9%B6%E5%8F%91.md)
- [Redis分布式锁如何续期](https://juejin.im/post/5d122f516fb9a07ed911d08c?utm_source=gold_browser_extension#comment)

  




相关文章：
- [天下无难试之Redis面试题刁难大全](https://zhuanlan.zhihu.com/p/32540678)

- [经典面试题：如何保证缓存与数据库的双写一致性？](https://mp.weixin.qq.com/s/SKXl_DXzDdHZFe5GAyMBzg)

- [面试中关于Redis的问题看这篇就够了](https://juejin.im/post/5ad6e4066fb9a028d82c4b66#comment)

- https://github.com/doocs/advanced-java?utm_source=gold_browser_extension#%E7%BC%93%E5%AD%98



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



## Spring



## MyBatis



## 分布式

- [面试官们“爱不释手”的分布式系统架构到底是个什么鬼？](https://juejin.im/post/5d00ea3b6fb9a07eec59c332?utm_source=gold_browser_extension)



## Dubbo

- dubbo的工作原理？
- dubbo支持的序列化协议？
- dubbo的负载均衡和高可用策略？动态代理策略？
- dubbo的SPI思想？
- 如何基于dubbo进行服务治理、服务降级、失败重试以及超时重试？
- dubbo服务接口的幂等性如何设计（比如不能重复扣款，不能重复生成订单，不能重复创建卡号）？
- dubbo服务接口请求的顺序性如何保证？
- 如何自己设计一个类似 dubbo 的rpc框架？



## 分布式架构常见的要解决的技术问题

- 分布式会话

- 分布式锁

- 分布式事务

- 分布式搜索

- 分布式缓存

- 分布式消息队列

- 统一配置中心

- 分布式存储，数据库分库分表

- 限流、熔断、降级等。




## Linux



## 项目经验

- [alibaba/canal 阿里巴巴 mysql 数据库 binlog 增量订阅&消费组件](https://www.cnblogs.com/niejunlei/p/10323085.html)



## 面经

- [【斩获7枚offer，入职阿里平台事业部】横扫阿里、美团、京东、 去哪儿之后，我写下了这篇面经！](https://mp.weixin.qq.com/s/BXGUU2VTP_5u4qhgHGlGIg)

- [金三银四铜五铁六，Offer收到手软！](https://mp.weixin.qq.com/s/UzR_emAr9Gh0P_9xUNOUPQ)

- [两年 JAVA 程序员的面试总结](https://mp.weixin.qq.com/s/gIiopR-uXq58lCrx1LoK9w)

- [一个学渣的阿里之路](https://mp.weixin.qq.com/s/-2pwc9yus6n5mn37fQqNEA)

- [字节跳动、腾讯后台开发面经分享(2019.5)](https://juejin.im/post/5cf7ea91e51d4576bc1a0dc2?utm_source=gold_browser_extension)
