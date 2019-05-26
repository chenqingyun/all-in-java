## List

### 逻辑架构

select 语句的执行过程

### 并发控制

- 读写锁
  - 共享锁 shared lock（读锁 read lock）
  - 排他锁 exclusive lock（写锁 write lock）
- 锁粒度
  - 锁策略
    - 表锁
    - 行级锁



### 事务

- ACID
- 隔离级别
  - READ UNCOMMITED (未提交读) - 脏读
  - READ COMMITED (提交读) - 不可重复读
  - REPEATABLE READ (可重复读)
    - 幻读
    - MySQL 的默认事务隔离级别
  - SERIALIZABLE (可串行化)
- 死锁
- 事务日志



### 多版本并发控制