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

[更多命令]([http://www.redis.cn/commands/incr.html](http://www.redis.cn/commands/incr.html))

**注意：**

- 字符串最大长度为 512MB。
- value 自增范围：long 的最大值和最小值。



**常见用途：**

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



## set



## sorted set