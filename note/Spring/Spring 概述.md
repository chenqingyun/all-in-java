## Spring 概述

### 目录

- [Spring 中用到了哪些设计模式](#spring-中用到了哪些设计模式)





### Spring 中用到了哪些设计模式

Spring 中使用了大量的设计模式，下面列举了一些比较有代表性的设计模式。

- 单例模式：在 Spring 配置文件中定义的 Bean 默认为单例模式。 
- 模板模式：用来解决代码重复问题，比如 RestTemplate、JmsTemplate、JpaTemplate。 
- 委派模式：Spring 提供了 DispatcherServlet 来对请求进行分发。 
- 工厂模式：BeanFactory 用来创建对象的实例，贯穿于 BeanFactory 和 ApplicationContext 接口。 
- 代理模式：代理模式 AOP 思想的底层实现技术，Spring 中采用 JDK Proxy 和 CGLib 类库。

[面试官:“谈谈Spring中都用到了那些设计模式?”](https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485303&idx=1&sn=9e4626a1e3f001f9b0d84a6fa0cff04a&chksm=cea248bcf9d5c1aaf48b67cc52bac74eb29d6037848d6cf213b0e5466f2d1fda970db700ba41&token=1449566086&lang=zh_CN&scene=21#wechat_redirect)



[27道高频Spring面试题，你能答对几个？](https://mp.weixin.qq.com/s/bOXZ7Tbat3QNImubXfhINA)