## Dubbo 序列化协议

在 dubbo RPC 中，同时支持多种序列化方式，例如：

1. **dubbo 序列化：**阿里尚未开发成熟的高效 java 序列化实现，阿里不建议在生产环境使用它。
2. **hessian2 序列化：**hessian 是一种跨语言的高效二进制序列化方式。但这里实际不是原生的 hessian2 序列化，而是阿里修改过的 hessian lite，它是 dubbo RPC 默认启用的序列化方式
3. **json 序列化**：目前有两种实现，一种是采用的阿里的 fastjson 库，另一种是采用 dubbo 中自己实现的简单 json 库，但其实现都不是特别成熟，而且 json 这种文本序列化性能一般不如上面两种二进制序列化。
4. **java 序列化：**主要是采用 JDK 自带的 Java 序列化实现，性能很不理想。



新的高效序列化方式：专门针对 Java 语言的：Kryo，FST 等等。



### Hessian 的数据结构

Hessian 的对象序列化机制有 8 种原始类型：

- 原始二进制数据
- boolean
- 64-bit date（64 位毫秒值的日期）
- 64-bit double
- 32-bit int
- 64-bit long
- null
- UTF-8 编码的 string

另外还包括 3 种递归类型：

- list for lists and arrays
- map for maps and dictionaries
- object for objects

还有一种特殊的类型：

- ref：用来表示对共享对象的引用。

[Dubbo 支持哪些序列化协议？说一下 Hessian 的数据结构？PB 知道吗？为什么 PB 的效率是最高的？](https://github.com/doocs/advanced-java/blob/master/docs/distributed-system/dubbo-serialization-protocol.md)



推荐阅读：

[在Dubbo中使用高效的Java序列化（Kryo和FST）](https://dangdangdotcom.github.io/dubbox/serialization.html)