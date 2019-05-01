# Redis 基础数据结构

## 目录

- [string](#string)
- [list](#list)
- [hash](#hash)
- [set](#set)
- [sorted set](#sorted-set)

Redis 有五种基础数据结构：

- string (字符串)
- list (列表)
- hash (散列)
- set (集合)
- sorted set (有序集合)

## [string](http://www.redis.cn/topics/data-types-intro.html#strings)

内部表示是一个字符数组。Redis 的字符串是动态字符串，是可以修改的字符串。内部结构的实现类似于 Java 的 ArrayList。



**常用命令：**

<img width="1215" alt="string 常用命令" src="https://user-images.githubusercontent.com/19634532/57011186-13090180-6c33-11e9-8b9d-8d57c1db1505.png">

[更多命令](http://www.redis.cn/commands/incr.html)

**注意：**

- 字符串最大长度为 512MB。
- value 自增范围：long 的最大值和最小值。



**使用场景：**

- 缓存用户信息
- 计数



## [list](http://www.redis.cn/topics/data-types-intro.html#lists)

Redis 的 list 是基于 「Linked List 」实现的，相当于 Java 里的 LinkedList，它是链表而不是数组。

list 的插入和删除操作非常快，时间复杂度为 O(1)，但是利用索引访问元素的速度很慢，时间复杂度为 O(n)。

当列表中弹出最后一个元素，该数据结构会自动删除，内存被回收。



**常用命令**：

![image](https://user-images.githubusercontent.com/19634532/57014275-fc1ddb80-6c41-11e9-9c7d-210ca418e295.png)

[更多命令](http://www.redis.cn/commands/blpop.html)



**使用场景：**

- 用于消息队列

  队列是先进先出的数据结构，可以使用 「rpush 」命令和 「 lpop 」命令右进左出来实现队列。

  > 右边进右边出来实现栈，这样的业务场景不多见。

- 实现聊天系统。
- 在评级系统中，比如社会化新闻网站 reddit.com，你可以把每个新提交的链接添加到一个 list，用 LRANGE 可简单的对结果分页。
- 在博客引擎实现中，你可为每篇日志设置一个 list，在该 list 中推入博客评论，等等。



**注意：**

- lindex，lrange 命令执行时间复杂度为 O(n)，谨慎使用。



## [hash](http://www.redis.cn/topics/data-types-intro.html#hashes)

相当于 Java 的 HashMap 。它是无序字典，内部结构跟 HashMap 一样，也是「数组 + 链表」的二维结构。

不同的是 Redis 的字典的值只能是字符串，rehash 采用的是渐进式 rehash 的策略。

当 hash 的最后一个元素被移除，该数据结构就被自动删除，内存被回收。



**使用场景：**

- 存储用户信息。可以对用户信息中的每个字段单独存储，部分获取。



**常用命令：**

![image](https://user-images.githubusercontent.com/19634532/57020689-adca0600-6c5c-11e9-9d02-f89c25b1b540.png)

[更多命令](http://www.redis.cn/commands/hdel.html)

**注意：**

- 小的 hash 被用特殊方式编码，非常节约内存。
- hash 的存储消耗要高于单个字符串。



## [set](http://www.redis.cn/topics/data-types-intro.html#sets)

相当于 Java 的 HashSet。它内部的键值对是无序，唯一的。

当集合最后一个元素被移除，该数据结构就被自动删除，内存被回收。



**常用命令：**

![image](https://user-images.githubusercontent.com/19634532/57024187-95f77f80-6c66-11e9-9484-a1b3683d8cb8.png)

[更多命令](http://www.redis.cn/commands/sadd.html)

**使用场景：**

- 可以用来存储中奖用户的 ID，可以保证同一个用户不会中奖多次。



## [sorted set](http://www.redis.cn/topics/data-types-intro.html#sorted-sets)

类似于 Java 中的 SortedSet 和 HashMap 的结合体。一方面是 set，保证唯一性，另一方面给每个 value 赋予一个 score，代表这个 value 的排序权重。

它的内部实现是一种叫做「 跳跃列表 」的数据结构。

sorted set 最后一个元素被移除后，该数据结构就被自动回收，内存被回收。



**常用命令：**

![image](https://user-images.githubusercontent.com/19634532/57026796-3355b200-6c6d-11e9-8719-ccf3474e42a8.png)

[更多命令](http://www.redis.cn/commands/zadd.html)



**使用场景：**

- 存储学生的成绩，value 是学生 ID，score 是分数。
- 排行榜。



**注意：**

- 内部 score 使用 double 类型存储，所以返回的 score 存在小数点精度问题。