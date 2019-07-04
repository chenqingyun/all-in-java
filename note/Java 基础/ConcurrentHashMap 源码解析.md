java7: 分段哈希表  java8:哈希表+链表
    查找：CAS获取段和Entry[]  java8:CAS获取Node然后查找
    添加：段数是初始化的时候固定的， CAS获取对应的段， 然后lock住(重入锁),再进行操作 java8:锁Node
    扩容：段数不会进行扩容， 然后像普通hashmap进行扩容 java8:
    遍历：正常便利即可
    size():锁整个表 / 或者在不锁表的情况下遍历几遍进行对比