## Dubbo 服务监控

Dubbo 架构如图：

![dubbo-relation](https://user-images.githubusercontent.com/19634532/61991042-5978cb80-b07d-11e9-87ea-9fc20bbc100f.jpg)



原理：

1. 通过阅读 dubbo 源码，所有的 RPC 方法调用都会经过 MonitorFilter 进行拦截。

2. 对于配置了监控的服务，会收集一些方法的基本统计信息。

3. DubboMonitor 对收集到的数据进行简单统计，诸如成功次数，失败次数，调用时间等，统计完后存储数据到本地。

4. DubboMonitor 有异步线程定时（默认每分钟）将收集到数据发送到远端监控服务。

5. 调用远端的 MonitorService.collect 方法，然后将本地缓存数据置零。



[微服务监控](https://juejin.im/post/5bf39739f265da615b711362)