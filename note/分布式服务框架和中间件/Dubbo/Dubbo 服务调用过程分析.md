## Dubbo 服务调用过程分析

### 目录

- [Dubbo 服务调用过程总结](#dubbo-服务调用过程总结)
- [服务调用方式](#服务调用方式)
- [服务消费方发送请求](#服务消费方发送请求)
  - [发送请求](#发送请求)
  - [请求编码](#请求编码)
    - [Request 对象的 data 字段执行序列化过程](#request-对象的-data-字段执行序列化过程)
- [服务提供方接收请求](#服务提供方接收请求)
  - [请求解码](#请求解码)
  - [调用服务](#调用服务)
    - [Dubbo 中的线程派发模型](#dubbo-中的线程派发模型)
- [服务提供方返回调用结果](#服务提供方返回调用结果)
- [服务消费方接收调用结果](#服务消费方接收调用结果)
  - [响应数据解码](#响应数据解码)
  - [向用户线程传递结果](#向用户线程传递结果)
    - [调用编号](#调用编号)



### Dubbo 服务调用过程总结

![image](https://user-images.githubusercontent.com/19634532/61999198-7b5e6680-b0ee-11e9-967b-b0bfffcf8543.png)

该图只反映了远程调用请求的发送与接收过程，服务端响应的发送与接收过程，这张图中没有表现出来。

**远程调用请求的发送与接收过程：**

1. 首先**服务消费者**通过**代理对象 Proxy** 发起远程调用，默认使用 Dubbo 协议；
2. 接着通过**网络客户端 Client** 将编码后的请求发送给服务提供方的网络层上，也就是 **Server**；
3. 服务端 Server 在收到请求后，首先要做的事情是对数据包进行解码，然后将解码后的请求发送至**分发器 Dispatcher**；
4. 再由分发器将请求派发到指定的线程池上；
5. 最后由**线程池**调用具体的服务。这就是一个远程调用请求的发送与接收过程；



**响应发送与接收过程：**

1. 服务端将方法的返回值通过协议传递给消费端；
2. 消费端对响应数据进行解码；
3. 根据响应对象传递到用户线程获取对应类型的响应对象。



以上，就是一个 Dubbo 的完整调用过程。



### 服务调用方式

**远程服务调用在调用方式上又可以分为「 同步调用 」和「 异步调用 」。**

其中异步调用还可细分为「 有返回值 」的异步调用和「 无返回值 」的异步调用。所谓「 无返回值 」异步调用是指服务消费方只管调用，但不关心调用结果，此时 Dubbo 会直接返回一个空的 RpcResult。若要使用异步特性，需要服务消费方手动进行配置。**默认情况下，Dubbo 使用同步调用方式。**



Dubbo 实现同步和异步调用比较关键的一点就在于由谁调用 ResponseFuture 的 get 方法。同步调用模式下，由框架自身调用 ResponseFuture 的 get 方法。异步调用模式下，则由用户调用该方法。



当服务消费者还未接收到调用结果时，用户线程调用 get 方法会被阻塞住。

- 同步调用模式下，框架获得 DefaultFuture 对象后，会立即调用 get 方法进行等待。
- 而异步模式下则是将该对象封装到 FutureAdapter 实例中，并将 FutureAdapter 实例设置到 RpcContext 中，供用户使用。

FutureAdapter 是一个适配器，用于将 Dubbo 中的 ResponseFuture 与 JDK 中的 Future 进行适配。这样当用户线程调用 Future 的 get 方法时，经过 FutureAdapter 适配，最终会调用 ResponseFuture 实现类对象的 get 方法，也就是 DefaultFuture 的 get 方法。



### 服务消费方发送请求

两个过程：

- 发送请求
- 对请求编码



RPC 调用在客户端（Consumer）触发，基配置文件中会有如下的定义：

```xml
<dubbo:reference id="xxxService" interface="xxx.xxx.Service" />
```



#### 发送请求

同步调用模式下，服务消费方是如何发送调用请求的？

1. 为服务接口生成一个代理对象，由于这个代理存在于本地，因此就可以像本地 bean 一样调用该服务，具体的通信过程由代理负责。

2. 根据调用的服务方法名以及传入的参数构建 RpcInvocation 对象，传入 MockClusterInvoker 的 invoke 方法。

3. 在消费者初始化时，接口方法和提供者 Invoker 对应关系保存在 RegistryDirectory 的methodInvokerMap中，**通过调用的方法名称（或方法名称+第一个参数）找到该方法对应的提供者 invoker 列表**，如注册中心设置了路由规则，对这些 invoker 根据路由规则进行过滤。**读取到所有符合条件的服务提供者 invoker 之后，由 LoadBalance 组件执行负载均衡，从中挑选一个 invoker 进行调用**。

4. 将请求信息封装成一个 Request 对象，然后再将该对象传给 NettyClient 的 send 方法，进行后续的调用。默认情况下，Dubbo 使用 Netty 作为底层的通信框架。

   > send 方法中，sent 参数决定是否等待请求消息发出，sent = true 等待消息发出，消息发送失败将抛出异常，sent = false 不等待消息发出，将消息放入 IO 队列，即刻返回。sent 的值源于 `<dubbo:method sent="true/false" />` 中 sent 的配置值，默认情况下都是 false。NettyChannel 中有 channel 属性，这个 channel 是 Netty 框架中的组件，负责客户端和服务端链路上的消息传递，channel.write 把请求消息写入，这个请求消息就是封装了调用接口、方法、参数等信息的 Request 对象。这里的 IO 模型是非阻塞的，线程不用同步等待所有消息写完，而是直接返回；调用 Netty 框架的 IO 事件之后会触发 Netty 框架的 IO 事件处理链。

5. 在 Netty 中，出站数据在发出之前还需要进行编码操作。



#### 请求编码

1. 首先会通过位运算将消息头写入到 header 数组中；
2. 然后对 Request 对象的 data 字段执行序列化操作，序列化后的数据最终会存储到 ChannelBuffer 中；
3. 序列化操作执行完后，可得到数据序列化后的长度 len，紧接着将 len 写入到 header 指定位置处；
4. 最后再将消息头字节数组 header 写入到 ChannelBuffer 中，整个编码过程就结束了。



##### Request 对象的 data 字段执行序列化过程

1. 依次序列化 dubbo version、path、version；
2. 序列化调用方法名；
3. 将参数类型转换为字符串，并进行序列化；
4. 对运行时参数进行序列化；
5. 序列化 attachments。



至此，关于服务消费方发送请求的过程就分析完了，接下来我们来看一下服务提供方是如何接收请求的。



### 服务提供方接收请求

- 对请求解码
- 调用服务



默认情况下 Dubbo 使用 Netty 作为底层的通信框架。Netty 检测到有数据入站后，首先会通过解码器对数据进行解码，并将解码后的数据传递给下一个入站处理器的指定方法。所以在进行后续的分析之前，我们先来看一下数据解码过程。

#### 请求解码

1. 读取消息头数据，检测相关信息，如魔数，消息体长度以及可读字节数等；
2. 部分字段进行解码，并将解码得到的字段封装到 Request 中；
3. 继续解码，通过反序列化将诸如 path、version、调用方法名、参数列表等信息依次解析出来，并设置到相应的字段中，最终得到一个具有完整调用信息的 DecodeableRpcInvocation 对象。

到这里，请求数据解码的过程就分析完了。此时我们得到了一个 Request 对象，这个对象会被传送到下一个入站处理器中，我们继续往下看。



#### 调用服务

解码器将数据包解析成 Request 对象后，NettyHandler 的 messageReceived 方法紧接着会收到这个对象，并将这个对象继续向下传递，最后将该对象封装到 Runnable 实现类对象中，并将 Runnable 派发到线程池中执行后续的调用逻辑。



如果是单向通信，仅向后调用指定服务即可，无需返回调用结果。

对于双向通信，HeaderExchangeHandler 首先向后进行调用，得到调用结果。然后将调用结果封装到 Response 对象中，最后再将该对象返回给服务消费方。如果请求不合法，或者调用失败，则将错误信息封装到 Response 对象中，并返回给服务消费方。



##### Dubbo 中的线程派发模型

**Dubbo 将底层通信框架中接收请求的线程称为 IO 线程**。

如果一些事件处理逻辑可以很快执行完，比如只在内存打一个标记，此时直接在 IO 线程上执行该段逻辑即可。

但如果事件的处理逻辑比较耗时，比如该段逻辑会发起数据库查询或者 HTTP 请求。此时我们就不应该让事件处理逻辑在 IO 线程上执行，而是应该派发到线程池中去执行。原因也很简单，IO 线程主要用于接收请求，如果 IO 线程被占满，将导致它不能接收新的请求。



上图中的 Dispatcher 就是线程派发器。

Dispatcher 真实的职责创建具有线程派发能力的 ChannelHandler，其本身并不具备线程派发能力。



Dubbo 支持 5 种不同的线程派发策略：

| 策略       | 用途                                                         |
| ---------- | ------------------------------------------------------------ |
| all        | 所有消息都派发到线程池，包括请求，响应，连接事件，断开事件等 |
| direct     | 所有消息都不派发到线程池，全部在 IO 线程上直接执行           |
| message    | 只有**请求**和**响应**消息派发到线程池，其它消息均在 IO 线程上执行 |
| execution  | 只有**请求**消息派发到线程池，不含响应。其它消息均在 IO 线程上执行 |
| connection | 在 IO 线程上，将连接断开事件放入队列，有序逐个执行，其它消息派发到线程池 |

默认配置下，Dubbo 使用「 all 」派发策略，即将所有的消息都派发到线程池中。



### 服务提供方返回调用结果

1. 服务提供方调用指定服务后，将调用结果封装到 Response 对象中，然后对 Response 对象进行编码，Response 对象的编码过程和前面分析的 Request 对象编码过程很相似。
2. 然后将该对象返回给服务消费方。服务提供方也是通过 NettyChannel 的 send 方法将 Response 对象返回。



### 服务消费方接收调用结果

1. 服务消费方在收到响应数据后，首先要做的事情是对响应数据进行解码，得到 Response 对象；
2. 然后再将该对象传递给下一个入站处理器，这个入站处理器就是 NettyHandler；
3. 接下来 NettyHandler 会将这个对象继续向下传递，最后 AllChannelHandler 的 received 方法会收到这个对象，并将这个对象派发到线程池中。这个过程和服务提供方接收请求的过程是一样的



重点分析两个方面的内容：

- 响应数据的解码过程
- Dubbo 如何将调用结果传递给用户线程的



####响应数据解码

1. 响应数据解码逻辑主要的逻辑封装在 DubboCodec 中，跟请求数据的解码过程相似。
2. 接着对调用结果进行反序列化，并将序列化后的结果存储起来。最后对 attachments 集合进行反序列化，并存到指定字段中。



#### 向用户线程传递调用结果

响应数据解码完成后，Dubbo 会将响应对象派发到线程池上。要注意的是，线程池中的线程并非用户的调用线程，所以要想办法将响应对象从线程池线程传递到用户线程上。

用户线程在发送完请求后的动作，即调用 DefaultFuture 的 get 方法等待响应对象的到来。当响应对象到来后，用户线程会被唤醒，并通过**调用编号**获取属于自己的响应对象，调用端即拿到了接口的调用结果（返回值或异常），整个远程服务接口的调用流程就完成了。



##### 调用编号

一般情况下，服务消费方会并发调用多个服务，每个用户线程发送请求后，会调用不同 DefaultFuture 对象的 get 方法进行等待。 一段时间后，服务消费方的线程池会收到多个响应对象。这个时候要考虑一个问题，如何将每个响应对象传递给相应的 DefaultFuture 对象，且不出错。答案是通过调用编号。DefaultFuture 被创建时，会要求传入一个 Request 对象。此时 DefaultFuture 可从 Request 对象中获取调用编号，并将 「 <调用编号, DefaultFuture 对象> 」映射关系存入到静态 Map 中，即 FUTURES。线程池中的线程在收到 Response 对象后，会根据 Response 对象中的调用编号到 FUTURES 集合中取出相应的 DefaultFuture 对象，然后再将 Response 对象设置到 DefaultFuture 对象中。最后再唤醒用户线程，这样用户线程即可从 DefaultFuture 对象中获取调用结果了。





更多详细分析见 [Dubbo 官网文档 - 服务调用过程](http://dubbo.apache.org/zh-cn/docs/source_code_guide/service-invoking-process.html)

