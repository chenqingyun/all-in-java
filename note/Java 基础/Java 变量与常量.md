## Java 变量与常量

### 目录

- 变量

  - 局部变量
  - 成员变量
  - 静态变量

- 常量

  - 静态常量

  

### 变量
- 变量名必须以字母开头由字母或数字构成
- 变量名要有意义
- 声明一个变量后必须要对其初始化
- 变量的声明尽可能的靠近变量第一次使用的地方



变量可分为：

- 局部变量
- 成员变量（实例变量）
- 类变量（静态变量）



#### 局部变量
- 局部变量声明在方法，构造方法或者语句块中，作用域就在其之中
- 访问修饰符不能用于声明局部变量
- 局部变量没有默认值，必须初始化
- 形式参数是局部变量
- 局部变量的数据存储在栈内存中，栈内存中的局部变量随着方法的消失而消失



#### 成员变量（实例变量）
- 定义在类的内部，方法或者语句块的外部
- 访问修饰符可以用于声明实例变量
- 当一个对象被实例化后，每个实例变量的值就跟着确定
- 实例变量在对象创建时创建，在对象被销毁的时候销毁
- 实例变量可以声明在使用前或者使用后
- 通常情况下将实例变量设为私有，并提供公开的方法用于访问实例变量
- 实例变量有默认值，数值型为0，布尔型为 false，引用类型为 null
- 实例变量存储在堆中的对象里面，由垃圾回收器回收



#### 类变量（静态变量）
- 关键字 ``static`` 声明，经常被声明为类常量，很少单独使用 ``static`` 声明变量
- 无论实例化多少个对象，该类变量只有一个
- 静态变量存储在静态存储区
- 静态变量在程序开始时创建，程序结束时销毁



### 常量
- 关键字 ``final`` 定义常量
- 不可改变
- 常量名全部大写
- 增强程序的可读性和可为维护性
- 作为对象属性，在对象创建的时候被初始化，存在堆里；在方法体或签名上声明，存在栈中。



#### 类常量
- 使用关键字 ``static final`` 定义
- 类常量的定义位于 main 方法的外部
- 类属性，作为类信息在类被加载时被存在静态的方法区

