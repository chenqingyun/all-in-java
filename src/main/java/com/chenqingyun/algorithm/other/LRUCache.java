package com.chenqingyun.algorithm.other;

import java.util.HashMap;

/**
 * @author chenqingyun
 * @date 2019-06-04
 *
 * LRU 算法的 Java 实现，基于 HashMap + 双向链表
 */
public class LRUCache {
    class DoubleLinkedList {
        String key;
        Object value;
        DoubleLinkedList pre;
        DoubleLinkedList next;
    }

    private HashMap<String, DoubleLinkedList> cache = new HashMap<>();
    private int count;
    private int capacity;
    private DoubleLinkedList head;
    private DoubleLinkedList tail;

    public LRUCache(int capacity) {
        this.count = 0;
        this.capacity = capacity;
        head = new DoubleLinkedList();
        tail = new DoubleLinkedList();
        head.pre = null;
        head.next = tail;
        tail.next = null;
        tail.pre = head;
    }

    public void set(String key, Object value) {
        DoubleLinkedList node = cache.get(key);
        if (node == null) {
            // 不存在添加至头部
            DoubleLinkedList newNode = new DoubleLinkedList();
            newNode.key = key;
            newNode.value = value;

            this.cache.put(key, newNode);
            // 添加节点
            addNode(newNode);
            count++;
            if (count > capacity) {
                // 超出容量
                // 取出并删除末尾的节点
                DoubleLinkedList tail = this.popTail();
                this.cache.remove(tail.key);
                count--;
            }
        } else {
            // 移动到头部
            node.value = value;
            this.moveToHead(node);
        }
    }

    private void moveToHead(DoubleLinkedList node) {
        this.removeNode(node);
        this.addNode(node);
    }

    private void addNode(DoubleLinkedList node) {
        node.pre = head;
        node.next = head.next;

        head.next.pre = node;
        head.next = node;
    }

    private DoubleLinkedList popTail() {
        DoubleLinkedList node = tail.pre;
        // 删除节点
        removeNode(node);
        return node;
    }

    private void removeNode(DoubleLinkedList node) {
        DoubleLinkedList pre = node.pre;
        DoubleLinkedList next = node.next;
        pre.next = next;
        next.pre = pre;
    }
}
