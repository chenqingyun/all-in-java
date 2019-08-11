## CopyOnWriteArrayList

copy 出一个容器（副本），再往新的容器里添加这个新的数据，最后把新的容器的引用地址赋值给了之前那个旧的容器地址。这样做的好处是我们可以对 CopyOnWrite 容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以 CopyOnWrite 容器也是一种读写分离的思想，读和写不同的容器。



CopyOnWriteArrayList 添加元素时中使用 ReentrantLock 加锁，保证线程安全。

读的时候不需要加锁，如果读的时候有多个线程正在向 ArrayList 添加数据，读还是会读到旧的数据，因为写的时候不会锁住旧的 ArrayList。



缺点：

- **内存占用**：两个数组同时在内存中，如果实际应用中，数据比较多，而且比较大的情况下，占用内存会比较大。
- **数据一致性**：CopyOnWrite 容器只能保证数据的最终一致性，不能保证数据的实时一致性。所以如果你希望写入的的数据，马上能读到，请不要使用 CopyOnWrite 容器。



优点：

- 解决多线程的并发问题。



CopyOnWrite 的应用场景

CopyOnWrite 并发容器用于读多写少的并发场景



[聊聊并发-Java中的Copy-On-Write容器](http://ifeve.com/java-copy-on-write/)