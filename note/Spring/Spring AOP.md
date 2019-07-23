## Spring AOP



### 什么是 AOP

AOP（ Aspect Orient Programming ），面向切面编程



### AOP 实现原理

使用 「 动态代理 」技术

- 基于 JDK 的动态代理

- 基于 CGLib 的动态代理

如果目标类没有实现接口，那么 Spring AOP 会选择使用 CGLib 来动态代理目标类。

**为什么需要两种代理机制？**

因为 JDK 只提供接口的代理，而不支持类的代理



#### JDK 动态代理

通过反射来接收被代理的类，并且要求被代理的类必须实现 InvocationHandler 接口。

JDK 动态代理的核心是 java.lang.reflect 包中的两个类：InvocationHandler 接口和 Proxy 类。可以通过实现 InvocationHandler 接口定义横切逻辑，并通过反射机制调用目标类的代码，动态的将横切逻辑和业务逻辑编织在一起。通过 Proxy 类动态创建目标类的代理对象。



#### CGLib 动态代理

CGLib（Code Generation Library），是一个代码生成的类库，可以在**运行时**动态的生成某个类的子类。

CGLib 采用底层的字节码技术，可以为一个类创建子类，在子类中采用方法拦截的技术拦截所有父类方法的调用并顺势织入横切逻辑。

**注意，CGLib 是通过继承的方式做的动态代理，因此如果某个类被标记为 final ，那么它是无法使用 CGLib 做动态代理的。**



两种动态代理方式总结：

**JDK 动态代理（Dynamic Proxy）**

- 基于标准 JDK 的动态代理功能。
- 只针对实现了接口的业务对象。

**CGLib 动态代理**

- 通过动态地对目标对象进行子类化来实现 AOP 代理。
- 需要指定 @EnableAspectJAutoProxy(proxyTargetClass = true) 来强制使用。
- 当业务对象没有实现任何接口的时候默认会选择 CGLib。





### 实现流程

1. Spring 加载自动代理器 AnnotationAwareAspectJAutoProxyCreator，当作一个系统组件；
2. 当一个 bean 加载到Spring中时，会触发自动代理器中的 bean 后置处理；
3. bean 后置处理，会先扫描 bean 中所有的 Advisor；
4. 然后用这些 Adviosr 和其他参数构建 ProxyFactory；
5. ProxyFactory 会根据配置和目标对象的类型寻找代理的方式（JDK动态代理或CGLIG代理）；
6. 然后代理出来的对象放回 context 中，完成 Spring AOP 代理。


