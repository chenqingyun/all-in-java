# Redis 基础数据结构

## 目录

- [string](#string)
- [list](#list)
- hash
- set
- zset

Redis 有五种基础数据结构：

- string (字符串)
- list (列表)
- hash (散列)
- set (集合)
- zset (有序集合)

## string

内部表示是一个字符数组。Redis 的字符串是动态字符串，是可以修改的字符串。

**键值对**

**set** 命令设置键值对，**get** 命令获取值

```
set key value
get key
```



**批量键值对**

**mset** 命令批量设置键值对，**mget** 命令批量获取值。

多个字符串进行批量读写，节省网络耗时开销。

```
mset key1 value1 key2 value2 key3 value3 
mget key1 key2 key3
```



**设置过期时间**

先设置键值，在设置过期时间

**expire** 命令设置过期时间

```
set key value
expire key seconds
```

一步到位，**setex** 命令设置键值对并设置过期时间。原子操作

```
setex key seconds value   // 等价于 set + expire 
```



**先检查在设置键值对**

命令：**setnx** 

```
setnx key value
```



**计数**

如果 value 值是整数，可以进行自增操作。

自增是有范围的，范围在 long 的最大值和最小值之间。

命令：incr 自增 1，incrby 自增多少

```
incr key 
incr key 5 // 自增 5
```



## list

