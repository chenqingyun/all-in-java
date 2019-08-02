## equals 和 ==

首先看如下代码：
```java
public class EqualsDemo {
    public static void main(String[] args){
        int i = 3;
        int j = 3;

        System.out.println(i == j);//true

        String str1= "hello";
        String str2= "hello";

        //声明了一个引用类型的变量，还没有与任何对象关联。
        String str;

        //通过 new String("hello") 产生一个对象(也称类 String 的实例)
        //str 指向这个对象，str 即为这个对象的引用，str 存储的就是它指向的对象在内存中的地址
        str = new String("hello");

        String str3 = new String("hello");
        String str4 = str3;

        System.out.println(str.equals(str3));//true
        System.out.println(str == str3);//false

        System.out.println(str1.equals(str2));//true
        System.out.println(str1 == str2);//true

        System.out.println(str1.equals(str3));//true
        System.out.println(str1 == str3);//false

        System.out.println(str3.equals(str4));//true
        System.out.println(str3 == str4);//true
    }
}
```

先来说说``==``比较运算符

`` i == j `` 结果为 true,那我们可以理解为 ``==``比较的是变量的值是否相等，但是 ``str == str3``结果却是 false，为什么呢？

因为在 Java 中分两大数据类型，一种是基本数据类型，另一种是引用数据类型变量。基本数据类型就是指 ``byte，short，int，long，float，double，char,boolean ``这8种数据类型，引用数据类型就是那些对象，数组这些。``String`` 就是引用数据类型。

基本数据类型的变量直接存储的变量值，所以用``==``进行比较就是直接比较的变量的值，所以`` i == j `` 结果为 true，
而引用数据类型的变量存储的不是其本身的值，而是其指向的对象在内存中的地址值。
``str``和``str3``分别指向了两个不同的对象，这两个对象在内存中的地址值肯定是不同的，所以``str == str3``结果是 false；``str1`` 和 ``str2``指向的是同一个对象，``str3`` 和 ``str4``也是指向同一个对象，所以它们的比较结果都为 true。

所以可以得出个结论：``==``对于基本数据类型的变量,比较的是变量存储的值是否相等,而作用于引用类型的变量时，比较的是变量所指向的对象在内存中的地址值是否相等。

再来说 ``equals``
所有继承 Object 类的类都有``equals``方法，我们查看 jdk 源码，在 Object 类中，``euqals``方法是这样实现的：

```
public boolean equals(Object obj) {
        return (this == obj);
    }
```

所以，``equals`` 方法其实就是通过 ``==``运算符来比较两个变量的，就是比较两个变量所指向的对象是否相等。

但是``str.equals(str3)``结果却为 true,这两个变量分明是指向不同的对象啊，为什么结果是 true 呢？
再来看下 String 类中``euqals``方法的具体实现：

```
public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
```

哦，原来 String 类重写了``equals``方法，其是比较两个字符串变量存储的值是否相等，
所以上述代码中两个字符串变量通过``equals``方法比较的结果为 true。

# 结论：

## equals：
如果对``equals`` 方法进行重写，那么 ``equals`` 方法就是用于判断引用数据类型的变量的值否相等；
如果没有对 ``equals`` 方法进行重写，那么 ``equals`` 方法就是用于判断引用数据类型的变量所指向的对象的地址是否相等，即是否指向同一个对象

## ==
对于基本数据类型的变量,比较的是变量存储的值是否相等,
而作用于引用类型的变量时，比较的是变量所指向的对象在内存中的地址值是否相等。





