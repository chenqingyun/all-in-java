
# Java 数据类型
Java 是强类型语言 

两大数据类型：
- 基本数据类型
- 引用数据类型

## 基本数据类型
8种基本数据类型：
- 6种数字类型：
    - 4种整数型： byte，short，int，long
    - 2种浮点型：float，double

- 1种字符型：char
- 1种布尔型：boolean

### 整型

类型 | 存储需求 | 取值范围 | 默认值
--- | --- | --- | ---
byte | 1字节 | -128 ~ 127 ( -2^7 ~ 2^7-1 ) | 0
short | 2字节 | -32768 ~ 32767 ( -2^15 ~ 2^15-1 ) | 0
int | 4字节 | -2147483648~2147483647 ( -2^31 ~ 2^31-1 ) | 0
long | 8字节 | -9223372036854774808~9223372036854774807 ( -2^63 ~ 2^63-1 ) | 0L

注：
- 整型默认是 int 型
- long 型数字后加 L 表示

### 浮点型

类型 | 存储需求 | 取值范围 | 默认值
--- | --- | --- | ---
float | 4字节 | 有效位数6 ~ 7位 | 0.0f
double | 8字节 | 有效位数15位 | 0.0d

注：
- 浮点数默认是 double 型
- 浮点数不能表示精确的值，如货币

### char 型
- 2字节，unicode 编码
- 最小值 '\u0000'，即为0；最大值 '\uffff'，即为65535
- 可以存储任何字符

### boolean 型
- 1字节
- 默认 false

## 引用数据类型
- 对象，数组都是引用数据类型
- 所有引用数据类型的默认值都是 null



### 包装数据类型

Byte，Short，Integer，Long，Float，Double，Character，Boolean



缓存大小：

- Byte，Short，Integer，Long 都是 -128 – 127 
- Character 缓存大小 0 – 127 
- Float，Double，Boolean 没有缓存



```java
Integer i = 1
```

编译后得字节码

```java
0: iconst_1
1: invokestatic  #2  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
4: astore_1
```

看字节码，`Integer i = 1` 相当于是 `Integer a = Integer.valueOf(1)`



```java
Integer a = null;
int b = a; // 抛出NullPointException
```

上面的代码可以编译通过，但是会抛出 NullPointException。`int b = a`实际上是 `int b = a.intValue()`，由于 a 的引用值为 null，在空对象上调用方法就会抛出 NullPointException。



比较相等

在 [-128, 127] 集合范围内，valueOf() 每次都会从缓存中取出同一个 Integer 对象，所以 == 为 true。

Integer 源码：

```java
   public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
```

如果不在这个范围内，则是新建对象，所以 == 是 false。



所以包装类型的比较最好不要使用 ==

