## Spring 事务



### 目录

- [事务的隔离级别](#事务的隔离级别)
- [事务的传播行为](#事务的传播行为)

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



### 如何实现事务管理

Spring 使用 **AOP**（面向切面编程）来实现**声明式事务**。

声明式事务的实现就是通过环绕增强的方式，在目标方法执行之前开启事务，在目标方法执行之后提交或者回滚事务。

> 环绕增强：在目标方法执行前后都执行增强。



[Spring 事务原理一探](https://zhuanlan.zhihu.com/p/54067384)