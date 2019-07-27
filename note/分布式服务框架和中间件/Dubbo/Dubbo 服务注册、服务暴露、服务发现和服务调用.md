## Dubbo 服务注册、服务暴露、服务发现和服务调用

### 目录

- [服务端服务注册与暴露](#服务端服务注册与暴露)

  - [服务暴露](#服务暴露)
  - [服务注册](#服务注册)

- [消费端服务发现与调用](#消费端服务发现与调用)

  - [服务发现](#服务发现)
  - [服务调用](#服务调用)

  

### 服务端服务注册与暴露



#### 服务暴露

服务注册之前要先将服务导出，

Dubbo 服务导出过程始于 Spring 容器发布刷新事件，Dubbo 在接收到事件后，会立即执行服务导出逻辑。

整个逻辑大致可分为三个部分：

- 第一部分是前置工作，主要用于检查参数，组装 URL；
- 第二部分是导出服务，包含导出服务到本地 (JVM)，和导出服务到远程两个过程；
- 第三部分是向注册中心注册服务，用于服务发现



**暴露服务到本地**

1. 在没有注册中心，直接暴露提供者的情况下，ServiceConfig 解析出的 URL 的格式为：`dubbo://service-host/com.foo.FooService?version=1.0.0`；
2. 基于扩展点自适应机制，通过 URL 的 `dubbo://`协议头识别，若需导出，则创建一个新的 URL 并将协议头、主机名以及端口设置成新的值。然后创建 Invoker，并调用 DubboProtocol 的 export 方法导出服务，打开服务端口。



**向注册中心暴露服务**

1. 在有注册中心，需要注册提供者地址的情况下，ServiceConfig 解析出的 URL 的格式为：`registry://registry-host/org.apache.dubbo.registry.RegistryService?export=URL.encode("dubbo://service-host/com.foo.FooService?version=1.0.0")`；
2. 基于扩展点自适应机制，通过 URL 的 `registry://` 协议头识别，就会调用 RegistryProtocol 的 export() 方法，将 export 参数中的提供者 URL，先注册到注册中心；
3. 再重新传给 Protocol 进行暴露： `dubbo://service-host/com.foo.FooService?version=1.0.0`，然后基于扩展点自适应机制，通过提供者 URL 的 `dubbo://` 协议头识别，就会调用 DubboProtocol 的 export()方法，通过 netty 开启服务。



#### 服务注册

**所谓的服务注册，本质上是将服务配置数据写入到 ZooKeeper 的某个路径的节点下。**

服务配置数据就是一长串 URL（dubbo://xxx 的字符串）。

- 先创建注册中心实例
  - 先访问缓存，缓存未命中则创建注册中心，然后写入缓存。
  - 创建 ZooKeeper 客户端，意味着注册中心的创建过程就结束了
- 向注册中心注册服务，就是将服务配置数据（存储在 URL 中）写入到 ZooKeeper 的某个路径的节点下，节点路径根据 URL 生成，然后在注册方法中调用 ZooKeeper 客户端的创建方法创建服务节点，



> 另外还有 Consumer 的注册，也是类似的，会写到 ZooKeeper 里面某个临时节点。
>
> Consumer 写入的原因，是因为「 OPS 服务治理 」的时候需要实时的消费者数据。



更多详细解析阅读： [Dubbo 官网 - 服务导出](http://dubbo.apache.org/zh-cn/docs/source_code_guide/export-service.html)



### 消费端服务发现与调用

在 Dubbo 中，可以通过两种方式引用远程服务。第一种是使用服务直连的方式引用服务，第二种方式是基于注册中心进行引用。服务直连的方式仅适合在调试或测试服务的场景下使用，不适合在线上环境使用。



#### 服务发现

Dubbo 服务引用的时机有两个：

- 第一个是在 Spring 容器调用 ReferenceBean 的 afterPropertiesSet 方法时引用服务，这种是饿汉式；
- 第二个是在 ReferenceBean 对应的服务被注入到其他类中时引用，这种是懒汉式。

默认情况下，Dubbo 使用懒汉式引用服务。如果需要使用饿汉式，可通过配置 `<dubbo:reference>`的 init 属性开启。

下面我们按照 Dubbo 默认配置进行分析，整个分析过程从 ReferenceBean 的 getObject 方法开始。

1. 当我们的服务被注入到其他类中时，Spring 会第一时间调用 getObject 方法，并由该方法执行服务引用逻辑；
2. 在进行具体工作之前，需先进行配置检查与收集工作，以保证配置的正确性。
3. 接着根据收集到的信息决定服务引用的方式，有三种：第一种是引用本地 (JVM) 服务，第二是通过直连方式引用远程服务，第三是通过注册中心引用远程服务。
4. 不管是哪种引用方式，最后都会得到一个 Invoker 实例。如果有多个注册中心，多个服务提供者，这个时候会得到一组 Invoker 实例，此时需要通过集群管理类 Cluster 将多个 Invoker 合并成一个实例。
5. 合并后的 Invoker 实例已经具备调用本地或远程服务的能力了，但并不能将此实例暴露给用户使用，这会对用户业务代码造成侵入。此时框架还需要通过代理工厂类 (ProxyFactory) 为服务接口生成代理类，并让代理类去调用 Invoker 逻辑。避免了 Dubbo 框架代码对业务代码的侵入，同时也让框架更容易使用。

以上就是服务发现的大致原理。



#### Invoker

Invoker 是 Dubbo 的核心模型，代表一个可执行体。在服务提供方，Invoker 用于调用服务提供类。在服务消费方，Invoker 用于执行远程调用。Invoker 是由 Protocol 实现类构建而来。



更多详细解析：[Dubbo 官网 - 服务引入](http://dubbo.apache.org/zh-cn/docs/source_code_guide/refer-service.html)



#### 服务调用

简单的说，调用时，通过代理实例，找到相关协议方法发起远程调用。

Dubbo 服务调用过程比较复杂，包含众多步骤，比如发送请求、编解码、服务降级、过滤器链处理、序列化、线程派发以及响应请求等步骤。



[Dubbo 服务调用过程分析](https://github.com/chenqingyun/all-in-java/blob/master/note/%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%92%8C%E4%B8%AD%E9%97%B4%E4%BB%B6/Dubbo/Dubbo%20%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8%E8%BF%87%E7%A8%8B%E5%88%86%E6%9E%90.md#dubbo-%E6%9C%8D%E5%8A%A1%E8%B0%83%E7%94%A8%E8%BF%87%E7%A8%8B%E5%88%86%E6%9E%90)