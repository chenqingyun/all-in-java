# ArrayList 源码解读

## 目录

- [概要](#概要)
- [属性](#属性)
- [初始容量](#初始容量)
- [扩容机制](#扩容机制)

> 基于 Java 8 源码

## 概要

ArrayList 继承关系图：

<div align="center"><img src="https://upload-images.jianshu.io/upload_images/3297676-c222aa2756dab872.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width= "600px"></div>

todo

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

该构造函数构造一个初始容量为 10 的空列表。

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



## 扩容机制

ArrayList 方法有两个 add 方法。

一个是 add(E e) 方法，另一个是 add(int index, E element) 方法。

先来看下 add(E e) 方法源码

```java
   	/**
     * 将指定的元素追加到此列表的末尾
     */
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }

    private void ensureCapacityInternal(int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }
		
		protected transient int modCount = 0;
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
```



add(int index, E element) 方法在列表中指定位置上插入元素，将当前位于该位置的元素（如果有）和任何后续元素（向其索引添加一个）向后移动。
