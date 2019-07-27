## Dubbo

Dubbo 组件角色及调用关系

![dubbo-relation](https://user-images.githubusercontent.com/19634532/61991042-5978cb80-b07d-11e9-87ea-9fc20bbc100f.jpg)



- **服务容器 Container** 负责启动，加载，运行服务提供者。

- **服务提供者 Provider** 在启动时，导出一个服务，这个服务就是可被调用的，然后向注册中心注册自己提供的服务。

- **服务消费者 Consumer** 在启动时，向注册中心订阅自己所需的服务。

- **注册中心 Registry** 返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。

- **Consumer** 会根据一定的路由规则从注册中心拿到 Provider 列表并缓存到本地，基于一定的软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。

- **Consumer 和 Provider** 在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到**监控中心 Monitor**。



这就是一个简单的 Dubbo 启动和服务调用过程。



推荐阅读：

[Dubbo 官网](http://dubbo.apache.org/zh-cn/docs/dev/design.html)

[阿里技术专家详解 Dubbo 实践，演进及未来规划](https://www.infoq.cn/article/IwZCAp3jo_H5fJFbWOZu)



### 注册中心挂了可以继续通信吗？

可以，因为刚开始初始化的时候，消费者会将提供者的地址等信息**拉取到本地缓存**，所以注册中心挂了可以继续通信。



**更多：**

- [Dubbo 服务注册、服务暴露、服务发现和服务调用]([https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20%E6%9C%8D%E5%8A%A1%E6%B3%A8%E5%86%8C%E3%80%81%E6%9C%8D%E5%8A%A1%E6%9A%B4%E9%9C%B2%E3%80%81%E6%9C%8D%E5%8A%A1%E5%8F%91%E7%8E%B0%E5%92%8C%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8.md#dubbo-%E6%9C%8D%E5%8A%A1%E6%B3%A8%E5%86%8C%E6%9C%8D%E5%8A%A1%E6%9A%B4%E9%9C%B2%E6%9C%8D%E5%8A%A1%E5%8F%91%E7%8E%B0%E5%92%8C%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8](https://github.com/chenqingyun/all-in-java/blob/master/note/分布式服务框架和中间件/Dubbo/Dubbo 服务注册、服务暴露、服务发现和服务调用.md))

- [Dubbo 服务调用过程分析](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8%E8%BF%87%E7%A8%8B%E5%88%86%E6%9E%90.md#dubbo-%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8%E8%BF%87%E7%A8%8B%E5%88%86%E6%9E%90)