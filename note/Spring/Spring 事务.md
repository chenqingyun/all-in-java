## Spring 事务



### 目录

- [事务的隔离级别](#事务的隔离级别)
- [事务的传播行为](#事务的传播行为)
- [事务几种实现方式](#事务几种实现方式)
- [如何实现事务管理](#如何实现事务管理)

### 事务的隔离级别

TransactionDefinition 接口中定义了五个表示隔离级别的常量：

- **TransactionDefinition.ISOLATION_DEFAULT:**	使用后端数据库默认的隔离级别，MySQL 默认采用的 REPEATABLE_READ 隔离级别， Oracle 默认采用的 READ_COMMITTED 隔离级别.
- **TransactionDefinition.ISOLATION_READ_UNCOMMITTED：** 最低的隔离级别，允许读取尚未提交的数据变更，**可能会导致脏读、幻读或不可重复读**
- **TransactionDefinition.ISOLATION_READ_COMMITTED：**允许读取并发事务已经提交的数据，**可以阻止脏读，但是幻读或不可重复读仍有可能发生**
- **TransactionDefinition.ISOLATION_REPEATABLE_READ：** 对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，**可以阻止脏读和不可重复读，但幻读仍有可能发生。**
- **TransactionDefinition.ISOLATION_SERIALIZABLE：** 最高的隔离级别，完全服从 ACID 的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，**该级别可以防止脏读、不可重复读以及幻读**。但是这将严重影响程序的性能。通常情况下也不会用到该级别。



### 事务的传播行为

事务传播行为用来描述由某一个事务传播行为修饰的方法被嵌套进另一个方法的时事务如何传播。

当事务方法被另一个事务方法调用时，必须指定事务应该如何传播。例如：方法可能继续在现有事务中运行，也可能开启一个新事务，并在自己的事务中运行。在 TransactionDefinition 定义中包括了如下几个表示传播行为的常量：

**支持当前事务的情况：**

- **TransactionDefinition.PROPAGATION_REQUIRED：** 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
- **TransactionDefinition.PROPAGATION_SUPPORTS：** 如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
- **TransactionDefinition.PROPAGATION_MANDATORY：** 如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。（mandatory：强制性）



**不支持当前事务的情况：**

- **TransactionDefinition.PROPAGATION_REQUIRES_NEW：** 创建一个新的事务，如果当前存在事务，则把当前事务挂起。
- **TransactionDefinition.PROPAGATION_NOT_SUPPORTED：** 以非事务方式运行，如果当前存在事务，则把当前事务挂起。
- **TransactionDefinition.PROPAGATION_NEVER：** 以非事务方式运行，如果当前存在事务，则抛出异常。



**其他情况：**

- **TransactionDefinition.PROPAGATION_NESTED：** 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于 TransactionDefinition.PROPAGATION_REQUIRED。





[Spring事务管理详解](https://juejin.im/post/5b00c52ef265da0b95276091#heading-9)



### 事务几种实现方式

- 编程式事务管理对基于 POJO 的应用来说是唯一选择。我们需要在代码中调用 beginTransaction()、commit()、rollback() 等事务管理相关的方法，这就是编程式事务管理。
- 基于 TransactionProxyFactoryBean 的声明式事务管理。
- 基于 @Transactional 的声明式事务管理。
- 基于 Aspectj AOP 配置事务。



#### @Transactional 注解管理事务的实现步骤

1. 在 xml 配置文件中添加事务配置信息，让 Spring 容器对添加 @Transactional 注解的 Bean 进行加工处理，以织入事务管理切面；

   ```xml
       <!-- 使用@Transactional进行声明式事务管理需要声明下面这行 -->
       <tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true" />
       <!-- 事务管理 -->
       <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
           <property name="dataSource" ref="dataSource"/>
           <!--提交失败是否回滚-->
           <property name="rollbackOnCommitFailure" value="true"/>
       </bean>
   ```

2. 将 @Transactional 注解添加到合适的方法上，还可以设置合适的属性信息。也可以添加到类级别上。当把@Transactional 注解放在类级别时，表示所有该类的公共方法都配置相同的事务属性信息。



如果在接口、实现类或方法上都指定了@Transactional 注解，则优先级顺序为方法 > 实现类 > 接口；

Spring 建议在业务的实现类上添加 @Transactional 注解。因为注解不能被继承，所以在接口上添加注解不会被实现类继承，那么实现类不会添加事务增强。如果使用 JDK 代理机制（基于接口的代理）是没问题；而使用 CGLIB 代理（继承）机制时就会遇到问题，因为其使用基于类的代理而不是接口。





### 如何实现事务管理

Spring 使用 **AOP**（面向切面编程）来实现**声明式事务**。

声明式事务的实现就是通过**环绕增强**（在目标方法执行前后都执行增强）的方式，在目标方法执行之前开启事务，在目标方法执行之后提交或者回滚事务。



1. Spring 容器初始化 Bean，在后置处理器后处理方法中生成代理对象；
2. 当 Bean 方法通过代理对象调用时，会触发对应的 AOP 增强拦截器 TransactionInterceptor，将事务处理的功能编织到拦截的方法中；



### 什么情况下事务不起作用

- 在 Spring 的 AOP 代理下，只有目标方法由外部调用，目标方法才由 Spring 生成的代理对象来管理，这会造成**自调用问题**。若同一类中的其他没有 @Transactional 注解的方法内部调用有 @Transactional 注解的方法，有 @Transactional 注解的方法的事务被忽略，不会发生回滚。
- @Transactional 只能应用到 public 方法才有效。
- 在接口上添加注解。
- 事务回滚异常只能为 RuntimeException 异常，而 Checked Exception 异常不回滚，捕获异常不抛出也不会回滚，但可以强制事务回滚：TransactionAspectSupport.currentTransactionStatus().isRollbackOnly()。
- CGLib 代理的时候，final 方法不会生效。



[透彻的掌握 Spring 中@transactional 的使用](https://www.ibm.com/developerworks/cn/java/j-master-spring-transactional-use/index.html)



- [Spring 事务原理一探](https://zhuanlan.zhihu.com/p/54067384)

- [Spring 源码分析：不得不重视的 Transaction 事务](https://mp.weixin.qq.com/s/8jWoraaWpeC7qiS6YGLJ_A)