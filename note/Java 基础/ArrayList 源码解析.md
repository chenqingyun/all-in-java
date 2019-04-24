ArrayList

> 基于 Java 8 源码

ArrayList 继承关系图：

<div><img src="https://upload-images.jianshu.io/upload_images/3297676-c222aa2756dab872.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width= "600px"></div>

属性

<div><img src="https://upload-images.jianshu.io/upload_images/3297676-4ef000553dbad747.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width= "600px"></div>

初始容量大小

ArrayList 无参构造函数源码：

<div><img src="https://upload-images.jianshu.io/upload_images/3297676-fd51a026603ef1ad.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width= "600px"></div>

该构造函数构造一个初始容量为 10 的空列表。

elementData 是 Object 类型的数组，可知 ArrayList 底层实现是数组。

<div><img src="https://upload-images.jianshu.io/upload_images/3297676-4564d034d4b5674e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width= "600px"></div>

可以通过该构造函数构造具有指定初始容量的空列表。

建议在创建一个 ArrayList 实例时设定容量大小，后面讲到扩容机制会提到。



扩容机制

add 方法源码：



