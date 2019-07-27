## ZooKeeper 原理



### 目录

- [什么是 ZooKeeper？有哪些功能？](#什么是-zooKeeper-有哪些功能)
- [ZooKeeper 有哪些特性？](#zooKeeper-有哪些特性)
- [ZooKeeper 的数据模型](#zooKeeper-的数据模型)
- [ZooKeeper 集群架构](#zooKeeper-集群架构)
- [如何保证分布式数据一致性：ZAB 协议](#如何保证分布式数据一致性：zab-协议)
  - [Leader 选举过程](#leader-选举过程 ) 
  - [如何进行数据同步](#如何进行数据同步)
  - [消息广播过程](#消息广播过程)
- [Watcher 机制](#watcher-机制)
- [有哪些应用场景？](#有哪些应用场景)



### 什么是 ZooKeeper？有哪些功能？

ZooKeeper 是一个开源的分布式应用程序协调服务，提供典型的分布式数据一致性解决方案。

ZooKeeper 可以为分布式系统提供包括数据发布/订阅，负载均衡，命名服务，分布式协调/通知，集群管理，Master 选举，分布式锁和分布式队列等功能。



### ZooKeeper 有哪些特性？

- **顺序一致性：** 从同一客户端发起的事务请求，最终将会严格地按照顺序被应用到 ZooKeeper 中去。
- **原子性：** 所有事务请求的处理结果在整个集群中所有机器上的应用情况是一致的，也就是说，要么整个集群中所有的机器都成功应用了某一个事务，要么都没有应用。
- **单一系统映像 ：** 无论客户端连到哪一个 ZooKeeper 服务器上，其看到的服务端数据模型都是一致的。
- **可靠性：** 一旦一次更改请求被应用，更改的结果就会被持久化，直到被下一次更改覆盖。



### ZooKeeper 的数据模型

ZooKeeper 的数据模型类似于文件系统，是一个树形结构（ZNode Tree），每个节点叫做「 ZNode 」，每个节点以斜杆「 / 」分割的路径来标识。**ZooKeeper 数据保存在内存中，这意味着 ZooKeeper 可以实现高吞吐量和低延迟。**

![image](https://user-images.githubusercontent.com/19634532/61872679-937b8d80-af16-11e9-8fe7-bf8e2e99e975.png)

每个 ZNode 上都会保存自己的数据内容，同时还会保存一系列属性信息。

- data：ZNode 存储的数据。
- ACL：AccessControlLists，记录 ZNode 的访问权限，即哪些人或哪些 IP 可以访问本节点。
- stat：包含 ZNode 的各种元数据，比如事务 ID、版本号、时间戳、大小等等。
- child：当前节点的子节点引用，类似于二叉树的左孩子右孩子。



ZNode 有四种类型：

- PERSISTENT：**持久节点。指一旦这个 ZNode 被创建了，除非主动进行 ZNode 的移除操作，否则这个 ZNode 将一直保存在 Zookeeper 服务器上**。
- PERSISTENT_SEQUENTIAL：持久顺序节点。相比持久节点，其新增了顺序特性。在创建这个节点的时候，Zookeeper 会自动在其节点名后面追加上一个整型数字，这个整型数字是一个由父节点维护的自增数字。
- EPHEMERAL：**临时节点。临时节点的生命周期与客户端会话绑定，客户端会话失效，节点会被自动清理**。同时，ZooKeeper 规定不能基于临时节点来创建子节点，即临时节点只能作为叶子节点。
- EPHEMERAL_SEQUENTIAL：临时顺序节点。



### ZooKeeper 集群架构

为了保证高可用，ZooKeeper 最好集群形态部署，只要有过半以上的节点存活，ZooKeeper 就能正常服务。

![image](https://user-images.githubusercontent.com/19634532/61871825-5b734b00-af14-11e9-84ab-49ee6b167ada.png)

ZooKeeper 集群是一主多从的结构。

客户端使用 ZooKeeper 时会连接到集群中的任意节点，所有的节点都能够直接对外提供读操作，但是写操作都会被从节点路由到主节点，由主节点进行处理，然后再同步到从节点，读数据时直接从从节点读取。

ZooKeeper 集群有 3 种角色：

- **Leader**：是整个 ZooKeeper 集群工作机制中的核心，负责响应读写请求。其主要工作是，集群内各服务器的调度，事务请求的唯一调度和处理，保证集群事务处理的顺序性。
- **Follower**：是 ZooKeeper 集群状态的跟随者。响应读请求外，Follower 还要处理 Leader 的提议，并在 Leader 提交该提议时在本地也进行提交。Follower 还参与 Leader 选举过程。
- **Observer**：观察者角色。只响应读请求，Observer 不参与选举过程，也不参与写操作的「 过半写成功 」策略。因此 Observer 机器可以在不影响写性能的情况下提升集群的读性能。如果 ZooKeeper 集群的读取负载很高，或者客户端多到跨机房，可以设置一些 Observer 服务器，以提高读取的吞吐量。其次是 Observer 不需要将事务持久化到磁盘，一旦 Observer 被重启，需要从 Leader 重新同步整个名字空间。

> 「 过半 」是指大于集群机器数量的一半，即大于或等于（n/2+1），集群机器数量不包括 Observer 角色节点。Leader 广播一个事务消息后，当收到半数以上的 ACK 信息时，就认为集群中所有节点都收到了消息，然后 Leader 就不需要再等待剩余节点的 ACK，直接广播 commit 消息，提交事务。选举中的投票提议及数据同步时，也是如此。



ZooKeeper 集群中的 Leader 节点是通过一个 所有「 Leader 选举过程 」来选定的。





### 如何保证分布式数据一致性：ZAB 协议

ZAB（ZooKeeper Atomic Broadcast）协议 ，是为分布式协调服务 ZooKeeper 专门设计的一种支持崩溃恢复的原子广播协议。 基于该协议，ZooKeeper 实现了一种主备模式的系统架构来保持集群中各个副本之间的数据一致性。

ZAB 包含两种基本模式，崩溃恢复模式和消息广播模式

- **奔溃恢复模式**：当整个服务框架在启动过程中，或是当 Leader 出现网络中断、崩溃退出与重启等异常情况时，ZAB 协议就会进人恢复模式，选举产生新的 Leader。当选举产生了新的 Leader，同时集群中已经有过半的机器与该 Leader 服务器完成了数据同步之后，ZAB 协议就会退出恢复模式。
- **消息广播模式**：当集群中已经有过半的 Follower 服务器完成了和 Leader 服务器的数据同步，那么就进人消息广播模式了。消息广播过程使用的是一个原子广播协议，类似于一个二阶段提交。Follower 接收到客户端的写路由给 Leader ，然后由 Leader 像其他 Follower 广播事务，如果过半 Follower 返回 ACK 消息，那么写请求事务提交。如果新加入一台 Follower，此时 Leader 服务器在负责进行消息广播，那么新加人的服务器就会自觉地进人数据恢复模式，找到 Leader 所在的服务器，并与其进行数据同步，然后一起参与到消息广播流程中去。



### Leader 选举过程 

当整个服务框架启动或 Leader 服务器出现网络中断，崩溃退出或重启或 Leader 失去大多数的 Follower 等异常情况时，ZAB 协议就会进入恢复模式，选举新的 Leader 服务器。

ZooKeeper 的默认选举算法是「 FastLeaderElection 」。

在选举过程，节点有三种状态：

- **LOOKING**：寻找 Leader 状态，处于该状态需要进入选举流程。
- **FOLLOWING** ：跟随者状态，表示 Leader 已经选举出来，当前节点角色是 Follower。
- **LEADING** ：领导者状态，处于该状态的节点说明是角色已经是 Leader。
- **OBSERVING**：观察者状态，不参与投票。



选举算法流程：

1. **发送选票**：参与投票的节点向其他节点发送自己所推荐的 Leader。
   - 第一次投票，每个节点都会选举自己为 Leader，各自将投票发送给集群中其他机器。投票的内容包括自己的服务器 myid 和最新事务 ID（ZXID）；
   - 投票的内容还包括 logicalclock，逻辑时钟，每次选举对应一个值，用来判断是否为同一次选举。每调用一次选举函数，logicalclock 自增1。

2. **判断选举轮次**：集群的每个节点收到投票后，如果是 LOOKING 状态，判断选举轮次。
   - 如果发送过来的 logicalclock 大于自身的 logicalclock。说明这是更新的一次选举，需要更新本机的logicalclock，同时清空已经收集到的选票，因为这些数据已经不再有效。然后「 选票 PK 」确定是否需要更新自己的选举情况。
   - 如果外部投票的选举轮次小于内部投票轮次，说明对方处于一个比较早的选举进程，那么 ZooKeeper 就会直接忽略该外部投票，将本节点的数据发送给对方。
   - 如果外部投票的选举轮次等于内部投票。此时可以开始进行选票 PK。

3. **选票 PK**：节点将自己的投票和其他投票一一比较。
   - 优先比较 ZXID，如果比自己大，节点会重新发出投票，投票给 ZXID 最大的那个机器，
   - 如果 ZXID 相同，再比较 myid，如果 myid 比自己大，发出投票给 myid 最大的那个机器。
4. **统计投票**：每次投票后，服务器都会统计投票数量，判断是否有某个节点得到半数以上的投票。如果存在这样的节点，该节点将会成为准 Leader，状态变为 LEADING。其他节点的状态变为 FOLLOWING。



还有一阶段，用于在从节点中发现最新的 ZXID 和事务日志。既然 Leader 被选为主节点，已经是集群里数据最新的了，为什么还要从节点中寻找最新事务呢？

这是为了防止某些意外情况，比如因网络原因在上一阶段产生多个 Leader 的情况。

这时候节点处于 LEADING / FOLLOWING 状态。

- 如果 logicalclock相同，将数据保存在 recvset，如果节点宣称自己是 Leader，那么判断是不是半数以上的服务器都选举它，如果是设置角色并退出选举。
- 否则，这是一条与当前 logicalclock 不符合的消息，说明在另一个选举过程中已经有了选举结果，于是将该选举结果加入到 OutOfElection 集合中，根据 OutOfElection 来判断是否可以结束选举，如果可以也是保存logicalclock，更新角色，退出选举。



> ZXID 是一个 64 位的数字，它高 32 位是 epoch ，用来标识 Leader 关系是否改变，每次一个 Leader 被选出来，它都会有一个新的 epoch。低 32 位是个递增计数。



[Zookeeper源码分析-Zookeeper Leader选举算法](https://juejin.im/post/5b949d595188255c6a041c22#heading-3)

[【分布式】Zookeeper的Leader选举](https://www.cnblogs.com/leesf456/p/6107600.html)



### 如何进行数据同步

Leader 选举出了后还要解决两个问题：

- 已经被 Commit 的数据不能丢失

- 未被 Commit 的数据对客户端不可见。



[深入浅出Zookeeper（一） Zookeeper架构及FastLeaderElection机制 - 一致性保证](http://www.jasongj.com/zookeeper/fastleaderelection/)



同步阶段主要是利用 Leader 前一阶段获得的最新提议历史，同步集群中所有的副本。只有当集群过半机器都同步完成，准 Leader 才会成为真正的 Leader。**Follower 只会接收 zxid 比自己的 lastZxid 大的提议**。

当完成 Leader 选举后，进行故障恢复的第二步就是数据同步：

数据同步过程就是 Leader 服务器将那些没有在 Learner 服务器（ Follower 和 Observer ）上提交过的事务请求同步给 Learner 服务器。



**总结**：Leader 服务器会为每一个 Learner 服务器准备一个队列，并将那些没有被各个 Learner 服务器同步的事务以 Proposal 的形式逐条发给各个 Learner 服务器，并在每一个 Proposal 后都紧跟一个 commit 消息，表示该事务已经被提交，当 Learner 服务器将所有尚未同步的事务 proposal 都从 Leader 服务器同步过来并成功应用到本地后，Leader 服务器就会将 learner 加入 forwardingFollowers 或 observingLearners 队列中。



集群中有过半机器完成同步，集群就具有对外服务的能力了。



[zookeeper-数据同步源码分析](https://juejin.im/post/5cdd57966fb9a03212507c7b#comment)





### 消息广播过程

1. 客户端发出写请求给任意 Follower；
2. Follower 把写请求转发给 Leader；
3. Leader 采用二阶段提交方式，先发送事务提案（ proposal） 广播给 Follower；
4. Follower 接到事务提案消息，写入日志成功后，返回 ACK 消息给 Leader；
5. Leader 接到半数以上 ACK 消息，返回成功给客户端，并且广播 Commit 请求给 Follower。



![image](https://user-images.githubusercontent.com/19634532/61966764-07e12a00-b006-11e9-8998-aac3ad907227.png)

广播模式需要保证提案被按顺序处理，因此 ZooKeeper 采用了递增的事务 id 号（ZXID）来保证。所有的提案都在被提出的时候加上了 ZXID。





### Watcher 机制

Watcher（事件监听器）机制是 ZooKeeper 实现分布式协调服务的重要特性。



Watcher 机制采用异步非阻塞的主动通知模式。该机制主要包括客户端线程、客户端 WatchManager 和 ZooKeeper 服务器三部分。

具体工作流程上，简单地说：

1. 客户端先向 ZooKeeper 服务端成功注册想要监听的节点（ZNode），同时客户端本地会存储该监听器相关的信息在 WatchManager 中。
2. 当 ZooKeeper 服务端监听的 ZNode 数据状态发生变化时，ZooKeeper 就会主动通知相关会话客户端，客户端线程从 WatchManager 中取出对应的 Watcher 对象来执行回调逻辑。

![image](https://user-images.githubusercontent.com/19634532/61968067-a327ce80-b009-11e9-9efd-5e884a6c8419.png)

**Watcher 机制的特性：**

- Watcher 是一次性的，触发后即销毁，需要重新注册，并且客户端在会话异常结束时不会收到任何通知，而快速重连接时仍不影响接收通知。
- Watcher 的回调执行都是顺序执行的，并且客户端在没有收到关注数据的变化事件通知之前是不会看到最新的数据，另外需要注意不要在 Watcher 回调逻辑中阻塞整个客户端的 Watcher 回调。
- Watcher 是轻量级的，WatchEvent 是最小的通信单元，结构上只包含通知状态、事件类型和节点路径。ZooKeeper 服务端只会通知客户端发生了什么，并不会告诉具体内容。
- 父节点、子节点的修改都能触发其 Watcher，Watcher 不能监听到孙节点。
- 一个节点上可以有多个 Watcher 监听。

[ZooKeeper Watcher机制](https://juejin.im/post/5b0784ab6fb9a07abf72f098)



**Watcher 机制使用场景**：

[发布订阅，即统一配置中心。](#发布订阅配置中心)



客户的如何注册监听？

服务端如何处理监听器的请求？

服务端如何触发监听？

客户端如何回调监听？

参考 [zookeeper-watcher机制](http://www.zzcblogs.top/2018/11/06/zookeeper-watcher%E6%9C%BA%E5%88%B6/)



### 有哪些应用场景？

#### 发布订阅（配置中心）

数据发布/订阅（Publish/Subscribe）系统，即所谓的配置中心。顾明思义就是发布者将数据发布到 ZooKeeper 的一个或一系列的节点上，供订阅者进行数据订阅，进而达到动态获取数据的目的，实现配置信息的集中式管理和数据的动态更新。

ZooKeeper 采用推拉结合的方式来实现发布订阅系统：客户端向服务端注册自己需要关注的节点，一旦该节点的数据发生变更，那么服务端就会向相应的客户端发送 Watcher 事件通知，客户端接收到这个消息通知之后，需要主动到服务端获取最新的数据。



#### 注册中心

**ZooKeeper 一个最常用的使用场景就是用于担任服务生产者和服务消费者的注册中心。** 服务生产者将自己提供的服务注册到 Zookeeper 中心，服务的消费者在进行服务调用的时候先到 Zookeeper 中查找服务，获取到服务生产者的详细信息之后，再去调用服务生产者的内容与数据。如在 Dubbo 架构中 Zookeeper 就担任了注册中心这一角色。使用临时节点存储的 Dubbo 地址，这样，当服务宕机时，这个节点就会自动消失，不再提供服务，服务消费者也不会再请求。



#### 负载均衡

每台服务端在启动时都会去 ZooKeeper 的 servers 节点下注册临时节点（注册临时节点是因为，当服务不可用时，这个临时节点会消失，客户端也就不会请求这个服务端），每台客户端在启动时都会去 servers 节点下取得所有可用的工作服务器列表，并通过一定的负载均衡算法计算得出应该将请求发到哪个服务器上。



#### 分布式锁

ZooKeeper 实现排他锁，共享锁（读锁）

这里只简单介绍一下排他锁的实现方式

实现原理和 master 选举类似，所有客户端在 /exclusive_lock 节点下创建临时子节点 /exclusive_lock/lock，ZooKeeper 会保证在所有的客户端中，最终只有一个客户端能够创建成功，那么就认为该客户端获取了锁，其他没有获取到锁的客户端就需要到 /exclusive_lock 节点看上注册一个子节点变更的 watcher 监听，以便实时监听到lock 节点的变更情况。



释放锁的情况有如下两种：

- 当前获取锁的客户端发生宕机，那么 ZooKeeper 上的这个临时节点就会被删除。
- 正常执行完业务逻辑后，客户端会主动将自己创建的临时节点删除。



[图解ZooKeeper的典型应用场景](https://www.javazhiyin.com/28435.html)



### 参考

- [可能是全网把 ZooKeeper 概念讲的最清楚的一篇文章](https://segmentfault.com/a/1190000016349824)

- [漫画：什么是ZooKeeper？](https://juejin.im/post/5b037d5c518825426e024473)

