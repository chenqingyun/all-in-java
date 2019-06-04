## Redis 单线程与高并发



### 为什么 Redis 是单线程的

Redis 单线程指的是网络请求模块使用了一个线程（所以不需考虑并发安全性），即一个线程处理所有网络请求，其他模块仍用了多个线程。

### 为什么能支撑高并发



[为什么说Redis是单线程的以及Redis为什么这么快！](https://blog.csdn.net/xlgen157387/article/details/79470556)

[Redis为什么是单线程，高并发快的3大原因详解](https://zhuanlan.zhihu.com/p/58038188)

[阿里大师带你解析： 为什么Redis单线程却能支撑高并发？](https://juejin.im/post/5ccaef8cf265da038932a357#comment)

