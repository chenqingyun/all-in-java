## MyBatis 缓存机制

MyBatis 为查询提供一级缓存和二级缓存



### 一级缓存

**一级缓存是指 Session 缓存，它的作用域是一个 SqlSession，Mybatis 默认开启一级缓存**。

也就是在同一个 SqlSession 中，执行相同的查询 SQL，第一次会去数据库进行查询，并写到缓存中； 第二次以后是直接去缓存中取。当执行 SQL 查询中间发生了增删改的操作，MyBatis 会把 SqlSession 的缓存清空。

在一次数据库会话内执行了修改操作后，一级缓存会失效，再次执行相同的查询，会直接查询数据库。



一级缓存有两个级别： SESSION 和 STATEMENT ，默认是 SESSION 级别。

- SESSION：即在一个 MyBatis 会话中执行的所有语句，都会共享这一个缓存，
- STATEMENT：可以理解为缓存只对当前执行的这一个 Statement 有效。

可以在 Mybatis 的配置文件中，通过 localCacheScope 指定。 

```java
<setting name="localCacheScope" value="STATEMENT"/>
```



**需要注意的是：**
当 Mybatis 整合 Spring 后，直接通过 Spring 注入 Mapper 的形式，如果不是在同一个事务中每个 Mapper 的每次查询操作都对应一个全新的 SqlSession 实例，这个时候就不会有一级缓存的命中，但是在同一个事务中时共用的是同一个 SqlSession。 



#### 总结

1. MyBatis 一级缓存的生命周期和 SqlSession 一致，SqlSession 关闭，一级缓存就清空。
2. MyBatis 一级缓存内部设计简单，只是一个没有容量限定的 HashMap，在缓存的功能性上有所欠缺。
3. MyBatis 的一级缓存最大范围是 SqlSession 内部，有多个 SqlSession 或者分布式的环境下，数据库写操作会引起脏数据，建议设定缓存级别为 Statement。



### 二级缓存

二级缓存是指 mapper 映射文件，作用域是同一个 namespace 下的 mapper 映射文件内容，如果多个SqlSession 之间需要共享缓存，则需要手动设置启动二级缓存。



**如何开启二级缓存？**

在 MyBatis 的配置文件中开启二级缓存，默认是开启的。

```java
<setting name="cacheEnabled" value="true"/>
```

还需要在每个具体的 mapper.xml 中配置 cache 或者 cache-ref 。

```java
<mapper namespace="...UserMapper">
    <cache/><!-- 加上该句即可，使用默认配置 -->
</mapper>
```

加上 **cache** 标签即可声明这个 namespace 使用二级缓存。



开启二级缓存后，会使用 CachingExecutor 装饰 Executor，进入一级缓存的查询流程前，先在 CachingExecutor 进行二级缓存的查询，即当开启二级缓存后，数据的查询执行的流程就是**二级缓存 -> 一级缓存 -> 数据库**。



**如果二级缓存想要命中实现，则必须要将上一次 SqlSession commit 之后才能生效，不然将不会命中**。

两个不同的 session 必须提交前面一个 session 才能缓存生效的原因：

因为 mybatis 的缓存会被一个 transactioncache 类包装住，所有的 cache.putObject 全部都会被暂时存到一个 map 里，等事务提交以后，这个map 里的缓存对象才会被真正的 cache 类执行 putObject 操作。

是为了防止事务执行过程中出异常导致回滚，如果 get 到 object 后直接 put 进缓存，万一发生回滚，就很容易导致 mybatis 缓存被脏读。



增删改操作，无论是否进行提交 SqlSession commit 操作，均会清空一级、二级缓存，查询从数据库中查询。



**MyBatis 的二级缓存不适应用于映射文件中存在多表查询的情况。**

通常我们会为每个单表创建单独的映射文件，由于 MyBatis 的二级缓存是基于 namespace 的，多表查询语句所在的 namspace 无法感应到其他 namespace 中的语句对多表查询中涉及的表进行的修改，引发脏数据问题。



可以使用 **Cache ref**，让一个 mapper 引用另一个 mapper 的命名空间，这样两个映射文件对应的 SQL 操作都使用的是同一块缓存了。

不过这样做的后果是，缓存的粒度变粗了，多个 Mapper namespace 下的所有操作都会对缓存使用造成影响。



#### 总结

1. MyBatis 的二级缓存相对于一级缓存来说，实现了 SqlSession 之间缓存数据的共享，同时粒度更加的细，能够到 namespace 级别，通过 Cache 接口实现类不同的组合，对 Cache 的可控性也更强。
2. MyBatis 在多表查询时，极大可能会出现脏数据，有设计上的缺陷，安全使用二级缓存的条件比较苛刻。
3. 在分布式环境下，由于默认的 MyBatis Cache 实现都是基于本地的，分布式环境下必然会出现读取到脏数据，需要使用集中式缓存将 MyBatis 的 Cache 接口实现，有一定的开发成本，直接使用 Redis、Memcached等分布式缓存可能成本更低，安全性也更高。



[聊聊MyBatis缓存机制](https://tech.meituan.com/2018/01/19/mybatis-cache.html)