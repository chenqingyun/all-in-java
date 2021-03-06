# ArrayList 源码解读

## 目录

- [概要](#概要)

- [属性](#属性)

- [初始容量](#初始容量)

- [从 add 方法谈扩容机制](#从-add-方法谈扩容机制)

- [为什么线程不安全](#为什么线程不安全)

  

> 本文源码基于 Java 8

## 概要

ArrayList 继承 AbstractList，实现 List、RandomAccess、Cloneable 和 Serializable

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```

以下 ArrayList 继承关系图

<div align="center"><img src="https://upload-images.jianshu.io/upload_images/3297676-c222aa2756dab872.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width= "600px"></div>
- 继承 AbstractList，实现 List：实现添加、删除、修改、遍历等功能。
- 实现 RandomAccess 接口：提供了随机访问功能，也就是通过下标获取元素对象的功能，说明 ArrayList 拥有**随机快速访问**的能力。
- 实现 Cloneable 接口：实现 clone 方法，说明 ArrayList 可以被克隆。
- 实现 Serializable 接口：表示 ArrayList 支持序列化，能通过序列化去传输。



**ArrayList 的特点是有序，元素可重复，可存储 null 元素，查询快，增删慢，线程不安全（与 Vector 的区别）。**

## 属性

源码如下：

```java
    /**
     * 默认初始容量为10
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 空数组，用于带初始化容量参数的构造函数指定容量为0时的初始化。
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 默认空数组，用于默认构造函数初始化。
     * 区别于 EMPTY_ELEMENTDATA，以便在添加第一个元素时知道要扩容多少
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /** 
     * 所有 ArrayList 元素都保存在 elementData 中
     * ArrayList 的容量就是该数组的大小
     * 当第一个元素被添加，默认初始容量将被扩大
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * ArrayList 实际大小，即 ArrayList 中所含有的元素数量
     */
    private int size;
```

这里区分下 capacity 和 size，capacity 表示列表所能容纳的最大元素个数，size 表示列表实际存储的元素个数。

## 初始容量

看下 ArrayList 无参构造函数源码：

```java
    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
```

源码上注释说明该构造函数构造一个初始容量为 10 的空列表。

可是这里只返回了一个空数组，哪里设定了初始容量为 10 呢？这个先留个 TODO。

elementData 是 Object 类型的数组，可知 ArrayList 底层实现是数组。

可以通过下面这个带参构造函数构造具有指定初始容量的空列表

```java
    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param  initialCapacity  the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *         is negative
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        }
    }
```



建议在创建一个 ArrayList 实例时设定容量大小，后面讲到扩容机制会提到。

另外还有个 `ArrayList(Collection<? extends E> c)`构造函数，这里不讲了。

## 从 add 方法谈扩容机制

ArrayList 方法有两个 add 方法。

一个是 add(E e) 方法，另一个是 add(int index, E element) 方法。我们从 add(E e) 方法入手谈 ArrayList 的扩容机制。


先来看下 add(E e) 方法源码：

```java
    /**
     * 将指定的元素追加到此列表的末尾
     */
    public boolean add(E e) {
       	// 确认内部容量
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
```

在该 add 方法内部实现中，首先调用 ensureCapacityInternal(int minCapacity) 方法确认内部容量，然后在数组末尾追加元素，成功返回 true。

下面讲一下 ensureCapacityInternal(int minCapacity) 方法具体实现。

```java
    private void ensureCapacityInternal(int minCapacity) {
      	// 如果列表是通过无参构造函数创建的，设置最小容量为 10。
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        // 确认实际容量
        ensureExplicitCapacity(minCapacity);
    }
```

以上源码，在该方法中，先判断 elementData 是否 == DEFAULTCAPACITY_EMPTY_ELEMENTDATA，如果相等，就对 minCapacity 重新赋值。在调用无参构造函数构造列表时 elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA，所以这里的判断做的就是如果列表是通过无参构造函数创建的且第一次调用 add 方法，就设置列表初始容量为 10。

然后调用 ensureExplicitCapacity(int minCapacity) 方法确定实际容量。

```java
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
          	// 对数组进行扩容
            grow(minCapacity);
    }
```

首先对 modCount 进行加 1 操作，该变量记录列表被修改的次数（modCount 详解 TODO）。

然后判断数组所需的容量是否大于当前数组的长度，如果大于表示要对数组进行扩容。调用 grow(int minCapacity) 方法对数组进行扩容 。

```java 
    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
      	// 新的容量为原来的 1.5 倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
      	// 如果扩大 1.5 倍后还是小于所需容量，那么就用所需的最小容量作为新的容量
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
      	// 如果新的容量超过列表最大容量时，就调用 hugeCapacity() 方法进行处理
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
      	// 将原数组的内容拷贝到新扩容的数组
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```

**grow 方法是 ArrayLIst 实现扩容机制的核心方法**，该方法增加数组容量以确保数组至少能容纳由 minCapacity 参数指定的元素数。

该方法扩容的实现步骤：

- 扩大容量为原来的 1.5 倍；

- 如果扩大 1.5 倍后还是小于所需容量，那么就用所需的最小容量作为新的容量。

- 如果新的容量超过列表最大容量就调用 hugeCapacity() 方法进行处理。

  ```java
      /**
       * The maximum size of array to allocate.
       * Some VMs reserve some header words in an array.
       * Attempts to allocate larger arrays may result in
       * OutOfMemoryError: Requested array size exceeds VM limit
       */
      private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
  
      private static int hugeCapacity(int minCapacity) {
            if (minCapacity < 0) // overflow
                throw new OutOfMemoryError();
          // 如果超过列表的最大容量，就用 Integer.MAX_VALUE
            return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
        }
  ```

  > ArrayList 的最大容量是 Integer.MAX_VALUE - 8，一些虚拟机需要存储一些 header words 在数组中，需要预留一些空间出来。
  >
  > 虚拟机给列表分配的的最大容量为 Integer.MAX_VALUE - 8，为什么超过了最大容量可以到 Integer.MAX_VALUE ？？？

- 最后调用 Arrays.copyOf 方法将原数组的内容拷贝到新扩容的数组。



**对 add(E e) 方法以及扩容机制的总结：**

- 先要确定数组的容量是否能够容纳下一个元素，然后再将元素追加到数组末尾。
- 如果所需容量超过当前数组长度，将进行扩容，数组扩容的倍数是原数组大小的 1.5 倍。
- ArrayLIst 实现扩容机制的核心方法是 grow(int minCapacity) 方法，依赖 Arrays.copyOf 方法。
- 如果列表是通过无参构造方法创建的，第一次调用 add 方法时，最小容量会设定为 10，最后通过 grow 方法去创建一个大小为 10 的数组。所以解释了通过无参构造函数创建列表时明明是空数组但初始化容量却为 10，这个其实是在第一次添加元素的时候创建了大小为 10 的数组。



## 为什么线程不安全

就拿 ArrayList 的 add 方法来说，首先看源码：

```java
public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
```

其中 `elementData[size++] = e` 操作就不是原子操作，可以拆分为两步

```java
elementData[size] = e;
size = size + 1;
```

**非原子操作，在多线程环境下不加锁的情况下就可能会有线程安全问题**。

如果有线程 A 和 B 同时调用 add 方法，此时 size =0 ，线程 A 在下标 0 的位置放置元素 a，还没执行到 `size = size + 1`	，线程 B 就执行到 `elementData[size] = e` 也在下标 0 的位置放置元素 b，这样线程 A 要插入到列表中的数据就被覆盖了，与实际需求不符。后面线程 A 和 B 又分别对 size 加 1，最后 size = 2，数组下标 1 的位置没有数据。

还有另一点，**有可能会抛出数组越界异常**。

我们知道使用无参构造创建的列表初始数组容量为 10，如果此时 size = 9，线程 A 和 B 在执行 `ensureCapacityInternal(size + 1)` 都判断了不需要扩容，线程 A 先在下标 9 的位置上放置元素，并且进行 size 自增 1，等线程 B 再来放入元素时就是在下标 10 位置上放置了，因为数组没有扩容，最大下标为 9，所以线程 B 放入元素时会抛出 **ArrayIndexOutOfBoundsException** 异常。

所以，ArrayList 是线程不安全的。

**在 java.util.concurrent 包下提供了线程安全的 CopyOnWriteArrayList 类**，后续分析下这个类。（TODO）



## 时间复杂度

- 查询操作都是 O(1)；

- 如果从头尾删除或添加元素，时间复杂度是 O(1)，否则是 O(n)，总体上是 O(n)。