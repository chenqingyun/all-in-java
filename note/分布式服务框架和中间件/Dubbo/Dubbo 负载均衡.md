## Dubbo 负载均衡

### 负载均衡策略

Dubbo的负载均衡策略主要有以下几种：

#### Random LoadBalance

- **随机**，按权重设置随机概率，权重越大随机概率越高，分配流量越高。
- RandomLoadBalance 的算法思想比较简单，在经过多次请求后，能够将调用请求按照权重值进行「 均匀 」分配。当然 RandomLoadBalance 也存在一定的缺点，当调用次数比较少时，Random 产生的随机数可能会比较集中，此时多数请求会落到同一台服务器上。这个缺点并不是很严重，多数情况下可以忽略。RandomLoadBalance 是一个简单，高效的负载均衡实现，因此 Dubbo 选择它作为缺省实现。

#### RoundRobin LoadBalance

- **轮询**，按公约后的权重设置轮询比率。
- 轮询是一种无状态负载均衡算法，实现简单，适用于每台服务器性能相近的场景下。但现实情况下，我们并不能保证每台服务器性能均相近。如果我们将等量的请求分配给性能较差的服务器，存在慢的提供者累积请求的问题，比如：第二台机器很慢，但没挂，当请求调到第二台时就卡在那，久而久之，所有请求都卡在调到第二台上。因此，这个时候我们需要对轮询过程进行加权，以调控每台服务器的负载，让性能差的机器承载权重小一些，流量少一些。

#### LeastActive LoadBalance

- **最小活跃数负载均衡**。每收到一个请求，活跃数加 1，完成请求后则将活跃数减 1。活跃调用数越小，表明该服务提供者效率越高，单位时间内可处理更多的请求。
- **相同活跃数的按权重随机**，权重越大，获取到新请求的概率就越大。如果两个服务提供者权重相同，此时随机选择一个即可。

#### ConsistentHash LoadBalance

- **[一致性 Hash 算法]()**，相同参数的请求总是发到同一提供者。
- 当某一台提供者挂时，原本发往该提供者的请求，基于虚拟节点，平摊到其它提供者，不会引起剧烈变动。
- 算法参见：http://en.wikipedia.org/wiki/Consistent_hashing
- 缺省只对第一个参数 Hash，如果要修改，请配置 `<dubbo:parameter key="hash.arguments" value="0,1" />`
- 缺省用 160 份虚拟节点，如果要修改，请配置 `<dubbo:parameter key="hash.nodes" value="320" />`



默认是 **Random** 随机调用。



### 配置

#### 服务端服务级别

```xml
<dubbo:service interface="..." loadbalance="roundrobin" />
```

#### 客户端服务级别

```xml
<dubbo:reference interface="..." loadbalance="roundrobin" />
```

#### 服务端方法级别

```xml
<dubbo:service interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:service>
```

### 客户端方法级别

```xml
<dubbo:reference interface="...">
    <dubbo:method name="..." loadbalance="roundrobin"/>
</dubbo:reference>
```



参考：

- [Dubbo 官网-负载均衡](https://dubbo.apache.org/zh-cn/docs/user/demos/loadbalance.html)

- [dubbo中的算法-负载均衡算法](https://www.jianshu.com/p/121592a06f3d)