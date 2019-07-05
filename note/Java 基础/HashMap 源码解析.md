## HashMap 源码解析

### 目录

- [概要](#概要)
- [底层数据结构](#底层数据结构)
- [属性](#属性)
- [构造方法](#构造方法)
- [put 方法分析](#put-方法分析)
  - [hash(Object key) 方法](#hash(Object key)-方法)
  - [红黑树添加节点的实现](#红黑树添加节点的实现)
  - [链表树化过程](#链表树化过程)
  - [红黑树链化过程](#红黑树链化过程)
- [扩容机制：resize() 方法](#扩容机制：resize()-方法)
- [get 方法分析](#get-方法分析)
- [负载因子的作用？为什么默认是 0.75？](负载因子的作用？为什么默认是 0.75)



> 本文源码基于 Java 8

### 概要

HashMap 位于 java.util 包下，继承 AbstractMap，实现自 Map、Cloneable 和 Serializable 接口：

```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable
```

- 继承 AbstractMap，实现 Map：实现添加、删除、获取和遍历等功能。
- 实现 Cloneable：实现了clone()方法，可被克隆
- 实现 Serializable：支持序列化。



**HashMap 特点：**

- 底层数据结构是数组 + 链表 + 红黑树

- 允许 key 为 null，value 为 null
- 线程不安全
- 不保证映射的顺序



### 底层数据结构

```java
    /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     * 首次使用时进行初始化，必要时进行扩容。当允许时，长度总为 2 的幂
     */
    transient Node<K,V>[] table;
```

HashMap 成员变量 table 是一个 Node 数组，在首次使用的初始化。这也可以说明，HashMap 底层是一个基于 Node 的数组，Node 就是数组中的元素。

Node 节点源码：

```java
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }
```

Node 实现了 Map.Entry 接口。每个 Map.Entry 其实就是一个 key-value 对，它持有一个指向下一个元素的引用，这就构成了链表。当链表的长度大于等于 8 时，链表会转化为红黑树。

```java
    /**
     * 链表转成红黑树的阈值
     */
    static final int TREEIFY_THRESHOLD = 8;

    // 红黑树节点
    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
        TreeNode<K,V> parent;  // red-black tree links
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;
  
        // 以下省略一万行 ...
    }
```



当红黑树节点小于 6 时又会转成链表。

```java
    /**
     * 链表还原阈值，即红黑树转为链表的阈值。
     * 当执行 resize 操作时，红黑树节点 <= 6 时，红黑树结构会转换为链表结构
     */
    static final int UNTREEIFY_THRESHOLD = 6;
```



HashMap 底层数据结构示意图：

![image](https://user-images.githubusercontent.com/19634532/60696310-c504c880-9f17-11e9-86cb-237b253db4c7.png)





为什么链表长度大于等于 8 时要转成红黑树？



### 属性

```java
    /**
     * HashMap 默认初始化容量 16
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; 

    /**
     * 最大容量。当传入构造函数的值过大时将使用这个值替换。必须为 2 的幂且小于 2 的 30 次方.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 默认加载因子为 0.75，当使用默认构造函数初始化时。
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 桶的树化阈值，即链表转成红黑树的阈值。
     * 当数组中的元素的节点 >= 8 时，链表将被转换成树。
     */
    static final int TREEIFY_THRESHOLD = 8;

    /**
     * 桶的链表还原阈值，即红黑树转为链表的阈值。
     * 当执行 resize 操作时，桶中的元素 < 8，且红黑树节点 <= 6 时，红黑树结构会转换为链表结构
     */
    static final int UNTREEIFY_THRESHOLD = 6;

    /**
     * 最小树化容量阈值。
     * 当哈希表的容量 >= 64时，链表将转换为树。否则，若桶内元素过多，哈希表的容量则会扩大，无法树形化。
     * 为了避免扩容和树形化的冲突，这个值不能小于 4 * TREEIFY_THRESHOLD。
     */
    static final int MIN_TREEIFY_CAPACITY = 64;

    /**
     * 首次使用时进行初始化，必要时进行扩容。当允许时，长度总为 2 的幂。
     */
    transient Node<K,V>[] table;

    transient Set<Map.Entry<K,V>> entrySet;

    /**
     * map 中键值对的数量
     */
    transient int size;

    /**
     * HashMap 结构被修改的次数
     */
    transient int modCount;

    /**
     * HashMap 的扩容阈值，等于 capacity * loadfactor
     * 用于判断是否需要调整 HashMap 的容量，超过这个值则需要扩容
     */
    int threshold;

    /**
     * 哈希表加载因子
     */
    final float loadFactor;
```

在 HashMap 内部定义的几个变量，包括桶数组本身都是 transient 修饰的，这代表了他们无法被序列化，而HashMap 本身是实现了Serializable接口的，那么 HashMap 是如何序列化的呢？

查了一下源码发现，HashMap 内有两个用于序列化的函数 readObject(ObjectInputStream s) 和 writeObject（ObjectOutputStreams），通过这个函数将 table 序列化。



### 构造方法

有四个构造方法：

- HashMap()：构造一个空的 HashMap，初始容量为 16，默认加载因子为 0.75。
- HashMap(int initialCapacity)：构造一个空的 HashMap，指定初始容量，默认加载因子为 0.75。
- HashMap(int initialCapacity, float loadFactor)：构造一个空的 HashMap，指定初始容量和加载因子。
- HashMap(Map<? extends K, ? extends V> m) ：构造一个 HashMap 带指定的 Map。



HashMap() 源码：

```java
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }
```

构造一个空的 HashMap，默认加载因子为 0.75

```java
    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
```



HashMap(int initialCapacity, float loadFactor) 源码：

```java
    public HashMap(int initialCapacity) {
        // 使用默认的加载因子 0.75
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        // 是否超过最大容量， 2 的 30 次方
        if (initialCapacity > MAXIMUM_CAPACITY)  
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        this.loadFactor = loadFactor; // 设置加载因子
        this.threshold = tableSizeFor(initialCapacity); // 扩容临界值
    }
```

构造 HashMap 实例时数组 table 并未初始化，而是被延迟到插入新数据时再进行初始化，看 put 方法源码可知。



关于 「 threshold 」源码的解释：

```java
    /**
     * The next size value at which to resize (capacity * load factor).
     */
    // (The javadoc description is true upon serialization.
    // Additionally, if the table array has not been allocated, this
    // field holds the initial array capacity, or zero signifying
    // DEFAULT_INITIAL_CAPACITY.)
    int threshold;
```

threshold 是用来判断 HashMap 是否需要扩容的阈值，超过这个值则需要扩容。这个值等于 「 capacity * loadfactor 」。

javadoc 补充解释道，如果数组尚未初始化，则此字段为数组的初始容量，或 0 表示等于 DEFAULT_INITIAL_CAPACITY = 16。

```java
    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
```

在这构造方法中，通过 tableSizeFor(int cap) 方法计算 threshold 的初始值，该方法的功能是**返回一个大于等于且最接近 cap 的 2 的幂次方整数**，比如传入的 cap 等于 10，那么 threshold 等于 16。

使用无参构造函数构造时 threshold = 0。



### put 方法分析

HashMap 通过 put(K key, V value) 方法添加键值对，该方法源码如下：

```java
  public V put(K key, V value) {
        // 先调用 hash(key) 方法计算 key 的 hash 值即放在数组的位置，然后在调用 putVal 方法添加键值对。
        return putVal(hash(key), key, value, false, true);
    }

    /**
     * 实现了 Map.put 方法和相关的方法
     *
     * @param hash key 的 hash 值
     * @param key 要添加的键
     * @param value 要添加的值
     * @param onlyIfAbsent 如果为 true ，就不覆盖原来的 value
     * @param evict 用于 LinkedHashMap 中的尾部操作，这里没有实际意义
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        // 定义变量 tab 是将要操作的 Node 数组引用
        // p 表示 tab 上的某 Node 节点，n 为 tab 的长度，i 为 tab 的下标
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        // 如果 table 数组为 null，或者还没有元素时，则通过 resize() 方法进行初始化
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        // 通过（n - 1） & hash 计算出的值作为 tab 的下标 i，如果此位置元素为 null，则新建一个节点放入该位置
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            // 如果该位置上已存在元素，就说明碰撞了，那么就要开始处理碰撞。
          
            // e 即将插入的 Node 节点，k 为该位置上元素 p 的 key
            Node<K,V> e; K k;
            // 判断该位置上的 key 是否和待插入的 key 相等，如果是，则将 p 的引用赋值给将插入的节点 e
            // 即 HashMap 中已经存在了 key，于是插入操作就不需要了，只要把原来的 value 覆盖就可以了。
            // HashMap 中判断 key 是否相同是先判断 key 的 hash 是否相同，然后调用 equals 方法判断
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            // 如果 p 是红黑树节点，则调用红黑树的插入方法
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                // 下面处理 p 为链表节点的情形
              
                // 遍历链表，并统计链表长度
                for (int binCount = 0; ; ++binCount) {
                    // 如果遍历到末尾时，在尾部追加该元素结点。
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        // 如果链表的长度大于树化阈值，则将链表转为红黑树
                        // 因为插入后链表长度加 1，而 binCount 并不包含新节点，所以要将阈值减 1。
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    // 如果找到与我们待插入的元素具有相同 key 的结点，则停止遍历
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            // 待插入元素在　map 中已存在
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                // 判断是否要替换原来的值，如果原来值为空或 onlyIfAbsent 为 false 则替换
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                // 这个函数在 HashMap 中没任何操作，是个空函数，他存在主要是为了 LinkedHashMap 的一些后续处理工作。
                afterNodeAccess(e);
               // 返回的是被覆盖的 oldValue
                return oldValue;
            }
        }
      
       // 收尾工作，对 key 相同而覆盖 oldValue 的情况，在前面已经 return，不会执行这里，所以那一类情况不算数据结构变化，并不改变 modCount 值。
        ++modCount;
        // 前面新增加了一个元素要对 size 加 1，如果大于阈值则要进行扩容
        if (++size > threshold)
            resize();
       // 空函数，是用于 LinkedHashMap 的尾部操作，HashMap 中没有实际意义
        afterNodeInsertion(evict);
        // 新插入元素的情况，返回 null
        return null; 
    }
```



**HashMap 的键值对的添加过程总结：**

- 首先，通过 hash(Object key) 方法计算 key 的 hash 值，就是数组的位置；
- 然后再调用 putVal 方法添加键值对，首先，如果 table 数组等于 null 或为空，则先进行初始化，这里调用 resize() 方法；
- 判断该位置上是否存在元素，如果没有，则直接在此位置上放置元素，添加结束，如果键值对数量大于阈值则需要扩容；
- 如果该位置上存在元素，判断该位置上的元素的头结点的 key 是否和要添加的 key 相同，如果是，则再判断新值是否要替换原来的值；
- 如果不相同，判断该位置上的元素结构是红黑树还是链表；
- 如果是红黑树，调用红黑树的插入方法；
- 如果是链表，遍历链表，找到 key 相同的元素则替换，否则插入到链表尾部；
- 如果链表的长度大于等于树化阈值 8，则将链表转为红黑树；



#### hash(Object key) 方法

该方法是计算元素在数组中的索引位置。

```java
   // 计算 hash 值
   static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```

key 的 hash 值的计算是通过 hashCode() 的高16位异或低16位实现的：

`(h = k.hashCode()) ^ (h >>> 16)`

主要是从速度、功效、质量来考虑的，这么做可以在数组 table 的 length 比较小的时候，也能保证考虑到高低 Bit 都参与到 Hash 的计算中，同时不会有太大的开销。



#### 红黑树添加节点的实现

HashMap 在红黑树结构时使用 TreeNode 的 putTreeVal 方法添加键值对

 TODO



#### 链表树化过程

当链表长度大于 8 时转化为红黑树，使用 treeifyBin 方法实现

TODO



#### 红黑树链化过程

TODO



### 扩容机制：resize() 方法

当 HashMap 中的键值对数量超过阈值时，进行扩容。

在 HashMap 中，阈值大小为数组长度与负载因子的乘积「 threshold = capacity * loadfactor 」，数组的长度均是 2 的幂。

属性：

```java

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /**
     * The next size value at which to resize (capacity * load factor).
     */
    // (The javadoc description is true upon serialization.
    // Additionally, if the table array has not been allocated, this
    // field holds the initial array capacity, or zero signifying
    // DEFAULT_INITIAL_CAPACITY.)
    int threshold;

    /**
     * The load factor for the hash table.
     */
    final float loadFactor;
```



resize() 方法源码：

```java
    final Node<K,V>[] resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length; // 原数组容量
        int oldThr = threshold;  // 原阈值
        int newCap, newThr = 0;
        // 如果原数组容量大于 0，表示已经数组已初始化过
        if (oldCap > 0) {
            // 如果数组容量大于最大容量 2 的 30 次方，那么阈值等于 Integer.MAX_VALUE
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            // 扩容后的容量为原来的两倍
            // 如果新容量小于设定最大容量，并且原来的容量大于等于 16，则新阈值扩大到原来的两倍
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        // 后面的是数组未初始化过的情形
      
        // 如果原阈值大于 0，数组初始化容量等于阈值
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {   // 使用无参构造方法时，threshold = 0；
            // 数组容量等于默认初始容量 16
            newCap = DEFAULT_INITIAL_CAPACITY;
            // 阈值等于 16 * 0.75 = 12
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        // 新阈值为 0 时，按阈值计算公式进行计算
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        // 创建新的桶数组，桶数组的初始化也是在这里完成的
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        // 如果原数组不为空，遍历原数组
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    // 如果该位置只有一个元素，那么直接将该元素放到新数组上
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    // 以下是该位置存在多个元素的情况
                    // 如果元素结构是红黑树，调用红黑树的 split 方法
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        // 遍历链表
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
```



**总结下扩容过程：**

- 计算新数组的容量和新阈值，如果数组是已初始化的，新容量为原来的两倍，新阈值也为原来的两倍；
- 根据计算出的新容量创建新的数组，数组 table 也是在这里进行初始化的；
- 将键值对节点重新映射到新的桶数组里。如果节点是 TreeNode 类型，则需要拆分红黑树。如果是普通节点，则节点按原顺序进行分组。



### get 方法分析

HashMap 通过 get(Object key) 方法获取指定 key 对应的值，源码如下：

```java
   public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

     /**
     * Implements Map.get and related methods
     *
     * @param hash hash for key
     * @param key the key
     * @return the node, or null if none
     */
    final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        // 判断数组是否为空，长度是否大于 0，这个位置上是否存在该 key
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            // 判断第一个存在的节点的 key 是否和查询的 key 相等。如果相等，直接返回该节点。
            if (first.hash == hash && // always check first node
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                // 如果节点是红黑树结构，在根节点 first 上调用 getTreeNode 方法，在内部遍历红黑树节点，查看是否有匹配的 TreeNode。
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
               // 如果是链表，遍历链表，找到相同 key 的元素并返回
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }


```



**get 方法总结：**

- 判断数组是否为空，对应位置上是否存在该 key，如果为空或不存在直接返回 null；
- 判断索引处第一个节点的 key与传入 key 是否相等，如果相等直接返回；
- 如果不相等，判断链表是否是红黑树，如果是调用红黑树的 getTreeNode 方法，在内部遍历红黑树节点，查看是否有匹配的 TreeNode；
- 如果是链表，遍历链表，找到相同 key 的元素返回。







### 负载因子的作用？为什么默认是 0.75？

对于 HashMap 来说，负载因子是一个很重要的参数，该参数反应了 HashMap 桶数组的使用情况（假设键值对节点均匀分布在桶数组中）。通过调节负载因子，可使 HashMap 时间和空间复杂度上有不同的表现。当我们调低负载因子时，HashMap 所能容纳的键值对数量变少。扩容时，重新将键值对存储新的桶数组里，键的键之间产生的碰撞会下降，链表长度变短。此时，HashMap 的增删改查等操作的效率将会变高，这里是典型的拿空间换时间。相反，如果增加负载因子（负载因子可以大于1），HashMap 所能容纳的键值对数量变多，空间利用率高，但碰撞率也高。这意味着链表长度变长，效率也随之降低，这种情况是拿时间换空间。至于负载因子怎么调节，这个看使用场景了。一般情况下，我们用默认值就可以了。





### 参考

- [HashMap 源码详细分析(JDK1.8)](https://segmentfault.com/a/1190000012926722)

- [java 8 Hashmap深入解析 —— put get 方法源码](https://www.cnblogs.com/jzb-blog/p/6637823.html)