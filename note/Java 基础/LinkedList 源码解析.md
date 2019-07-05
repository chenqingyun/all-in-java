## LinkedList 源码解析

### 目录

- [概述](#概述)
- [底层数据结构](#底层数据结构)
- [构造方法](#构造方法)
- [add 方法分析](#add-方法分析)
- [addAll 方法分析](#addAll-方法分析)
- [remove 方法分析](#remove-方法分析)
- [get 方法分析](#get-方法分析)
- [set 方法分析](#set-方法分析)
- [clear 方法分析](#clear-方法分析)
- [与 ArrayList 的比较](#与-arraylist-的比较)



> 本文源码基于 JDK 1.8

### 概述



LinkedList 的继承结构：

```java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
```

LinkedList 继承了 AbstractSequentialList ，实现了 List 和 Deque 接口，是一个双向链表，可以当做队列，堆栈，或者是双端队列。



LinkedList 的特点：有序，元素可重复，可存储 null 元素，查询慢，增删快，线程不安全。



### 底层数据结构

LinkedList 底层数据结构是一个双向链表，链表上 Node 节点代码如下：

```java
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
```

Node 是在 LinkedList 里定义的一个静态内部类，它表示链表每个节点的结构，包括一个数据域 item，一个后置指针 next，一个前置指针 prev。



在 LinkedList 中也定义了两个变量分别指向链表中的第一个和最后一个结点

LinkedList 属性：

```java
    // 链表的节点个数，即当前存储元素的个数    
    transient int size = 0;

    // 头结点
    transient Node<E> first;

    // 尾节点
    transient Node<E> last;
```



### 构造方法

LinkedList 有两个构造方法：

```java
    // 构造一个空的列表
    public LinkedList() {
    }

    // 构造一个包含指定集合的元素的列表
    public LinkedList(Collection<? extends E> c) {
        this();
        addAll(c);
    }
```



### add 方法分析

LinkedList 有两个 add 操作的方法，一个是 add(E e) 方法，向链表末尾添加元素，一个是 add(int index, E element) 方法，在指定位置插入元素。

看下 add (E e) 方法源码：

```java
  public boolean add(E e) {
        linkLast(e);
        return true;
    }

    void linkLast(E e) {
        final Node<E> l = last;
        // 构造新节点，当前链表的最后一个节点为该新节点的前驱节点
        final Node<E> newNode = new Node<>(l, e, null);
        // 新节点作为尾节点
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;
    }
```

add (E e) 方法每次都会把新增节点放在链表的最后的一位，所以链表的添加性能可以看成 O(1) 操作。



add(int index, E element) 方法源码：

```java
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    Node<E> node(int index) {
        // 如果 index 小于当前 size 的一半，就从首节点开始遍历查找
        if (index < (size >> 1)) {
            Node<E> x = first;
            // 依次向后遍历 index 次，将结果返回
            for (int i = 0; i < index; i++)
                x = x.next; 
            return x;
        } else {
            // 否则，就从末尾节点向前遍历
            Node<E> x = last;
            // 先后遍历获取到节点并返回
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    /**
     * Inserts element e before non-null Node succ.
     */
    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;
        final Node<E> pred = succ.prev;
        // 构造新节点，index 位置上的节点为新节点的后继节点，它的前驱节点为新节点的前驱节点
        final Node<E> newNode = new Node<>(pred, e, succ);
        // 设置旧节点的前驱节点为新节点
        succ.prev = newNode;
        // 如果旧节点不存在节点，那么说明该旧节点是头结点，则将新节点替换为头结点
        if (pred == null)
            first = newNode;
        else
            // 设置前驱节点的后继节点为新节点
            pred.next = newNode;
        size++;
        modCount++;
    }
```

add(int index, E element) 向指定位置插入元素。

- 如果 index 是最后一个元素位置，则调用 linkLast(E e)  方法添加元素到链表末尾；

- 否则，调用 node(int index) 方法折半查找，如果 index 小于size的一半，就从头开始向后遍历查询，否则就从后向前遍历查询找到该 index 上的节点；然后通过 linkBefore(E e, Node<E> succ) 方法将新添加元素节点放到 index 位置上。

该添加操作的时间复杂度 O(n)。



### addAll 方法分析

addAll 操作也有两个方法，addAll(Collection<? extends E> c) 方法直接向链表末尾添加指定集合，addAll(int index, Collection<? extends E> c) 方法向指定位置添加集合。

addAll 方法源码：

```java
  public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        // 检查 index 是否合法
        checkPositionIndex(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;
       
        // 临时的 index 节点的前驱节点和后继节点
        Node<E> pred, succ;
        // 说明是向尾部插入
        if (index == size) {
            succ = null; // 后继节点为最后一个节点，值为 null
            pred = last; // 现在前驱节点变为最后一个节点
        } else {
            succ = node(index); // 找到 index 位置上的节点作为后继节点
            pred = succ.prev; // index 节点的前驱节点赋值当前的前驱节点
        }
        
        // 遍历对象数组
        for (Object o : a) {
            @SuppressWarnings("unchecked") E e = (E) o;
            // 构造新节点
            Node<E> newNode = new Node<>(pred, e, null);
            // 如果当前前驱节点为空，那么第一个新节点就是头节点
            if (pred == null)
                first = newNode;
            else
                pred.next = newNode;
          
            pred = newNode; // 当前的节点赋值为临时的前驱节点
        }
      
        // 如果临时的 null，说明是尾部插入，那么尾部节点的前驱节点，就会被赋值成最后节点
        if (succ == null) {
            last = pred;
        } else {
            pred.next = succ;
            succ.prev = pred;
        }

        size += numNew;
        modCount++;
        return true;
    }
```



### remove 方法分析

删除元素的操作有三个方法：

- remove() 方法，删除链表第一个元素
- remove(int index)  删除制定位置元素
- remove(Object o)  删除指定元素

remove()  方法源码：

```java
  public E remove() {
        return removeFirst();
    }

    public E removeFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return unlinkFirst(f);
    }

    private E unlinkFirst(Node<E> f) {
        // assert f == first && f != null;
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.next = null; // help GC
        first = next;
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        modCount++;
        return element;
    }
```



remove(int index)  和 remove(Object o)  方法源码：

```java
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    E unlink(Node<E> x) {
        // assert x != null;
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        modCount++;
        return element;
    }
```

remove(int index)  方法根据 index 移除，调用了 node(int index) 方法来查找需要移除的节点，remove(Object o) 方法根据元素来移除的时候，则是进行了整个链表的遍历，然后再卸载节点。



链表的删除操作还有 removeLast 方法，删除最后一个元素。

链表基于首尾节点的删除可以看成是 O(1) 操作，而非首尾的删除最坏的情况下能够达到 O(n) 操作，因为要遍历查询指定节点，所以性能较差。



### get 方法分析

get 方法有三个，分别是 get(int index)，getFirst()，getLast()：

```java
  public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

   public E getFirst() {
        final Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.item;
    }

    public E getLast() {
        final Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.item;
    }
```

get(int index) 方法里面调用 node( int index) 方法折半查找元素，前面分析过了，这个操作是 O(n) 的。

getFirst()，getLast() 方法是 O(1) 的。



### set 方法分析

```java
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }
```

set 方法依旧是调用的 node( int index) 方法，所以链表在指定位置更新数据，性能也一般。



### clear 方法分析

```java
    public void clear() {
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
        modCount++;
    }
```

所有的节点的数据置为 null。



### 与 ArrayList 的比较

[ArrayList 源码解析](https://github.com/chenqingyun/all-in-java/blob/master/note/Java%20%E5%9F%BA%E7%A1%80/ArrayList%20%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90.md)

- ArrayList 底层是数组，LinkedList 底层是一个双向链表，链表在内存中不是一块连续的地址，而是用多少就会申请多少，所以它比 ArrayList 更加节省空间。
- LinkedList 的首末位的添加删除操作非常快，为 O(1)，但是查询和遍历操作比较耗时，为 O(n)，ArrayList 查询快，增删慢。
- 两者都是有序，元素可重复，可存储 null 元素，线程不安全的。