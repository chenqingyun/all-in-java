## OutOfMemoryError 异常出现原因及解决方案
### 虚拟机栈

### Java 堆内存溢出
java.lang.OutOfMemoryError: Java heap space

「-Xms」 设置最小堆内存，「-Xmx」 设置最大堆内存，设置一样避免堆自动扩展
参数 「-XX:+HeapDumpOnOutOfMemoryError」 可以让虚拟机在出现内存溢出异常时 Dump 出当前的内存堆转储快照以便事后分析。

```
/**
 *
 * Java Heap OutOfMemoryError
 *
 * VM Args:-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 *
 */
public class HeapOOM {
    public static void main(String[] args) {
        List<Object> objects = new ArrayList<>();
        while (true){
            objects.add(new Object());
        }

    }
}
```

原因：
- 内存泄漏（Memory Leak）
- 堆内存设置太小


解决：


### 方法区
