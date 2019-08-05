## Spring IoC 

### 目录

- [什么是 IoC？](#什么是-ioc)
- [什么是 Spring 容器？](#什么是-spring-容器)
- [ApplicationContext 和 BeanFactory 的区别？](#applicationContext-和-beanFactory-的区别)
- [IoC 实现原理](#ioc-实现原理)
  - [创建 Spring 容器](#创建-spring-容器)
  - [装配 Bean](#装配-bean)
- [Bean 的生命周期](#bean-的生命周期)
- [BeanPostProcessor](#BeanPostProcessor)
- [Bean 的作用域](#bean-的作用域)
- [Spring 中的单例 Bean 是线程安全的吗](#spring-中的单例-bean-是线程安全的吗)
- [IoC 有什么好处？](ioc-有什么好处)



### 什么是 IoC？

IoC （Inverse of Control，控制反转），它是一种思想。它的思路是将对象的控制权交给一个容器，在 Spring 中就是 Spring IoC 容器，由 Spring 容器统一控制对象的创建、销毁、协调等，而不是在调用者内部进行对象的创建或管理。

**谁控制谁？**

在传统的开发模式下，我们都是采用直接 new 一个对象的方式来创建对象，是资源的使用者主动去控制对象资源，使用 IoC 容器后，则由 IoC 容器借由 Bean 配置来进行控制。

**控制什么？**

控制对象的生命周期和关系。



**什么是反转？**

原来是资源使用者主动在内部控制依赖的对象，此为正转，有了 IoC 容器后，由容器来帮忙创建及注入依赖对象，资源使用者只是被动的接受依赖对象，此为反转。哪些方面反转了？依赖对象的获取被反转了。



首先要理解什么 Spring 容器。

### 什么是 Spring 容器？

Spring 容器是 Spring 框架的核心，其实就是一个 Bean 工厂，存储 Spring Bean 对象，Bean 的实例化，获取，销毁等都是由这个 Bean 工厂进行管理的。（Bean 就是被 Spring 容器创建和管理的 Java 对象）

Spring 容器通过 DI 管理构成应用的组件，通过一个配置文件描述 Bean 与 Bean 之间的依赖关系，利用 Java 的反射机制实例化 Bean 并建立 Bean 之间的依赖关系。

**Spring 容器可以分为两种类型：**

- **Bean 工厂**：由 org.springframework.beans.factory.BeanFactory 接口定义，最简单的容器，提供基本的 DI 支持。
- **应用上下文**：由 org.springframework.context.ApplicationContext 接口定义，基于 BeanFactory 构建，并提供应用框架级别的服务。



#### ApplicationContext 和 BeanFactory 的区别？

- BeanFactory 是 Spring 框架的基础设施，面向 Spring 本身，ApplicationContext 面向使用 Spring 框架的使用者，几乎所有的应用场合我们都直接使用 ApplicationContext 而非底层的 BeanFactory。
- BeanFactory 采取**延迟加载**，第一次调用 getBean() 方法时才会初始化 Bean，ApplicationContext 是加载完 applicationContext.xml 时，就创建具体的 Bean 对象。（**只对 BeanDefition 中描述为是单例的 bean，才进行饿汉式加载**）。



### IoC 实现原理

IoC 是一种思想，DI（Dependency Injection，依赖注入）是实现 IoC 思想的一种技术手段。

依赖注入就是对象之间的依赖关系依靠第三方容器在创建对象的时候进行设定，即由容器动态的将依赖关系主动注入到需要它们的对象中，而不是让调用者自己去获取依赖。



#### 创建 Spring 容器

Spring 容器的创建是通过 ApplicationContext 的实现类加载 applicationContext.xml 配置文件完成的。

ApplicationContext 接口常用的实现类：

- ClassPathXmlApplicationContext：默认从类路径加载配置文件。
- FileSystemXmlApplicationContext：默认从文件系统下加载配置文件。
- AnnotationConfigApplicationContext：当我们使用注解配置容器对象时，需要使用此类来创建 Spring 容器。它用来**读取注解**。





**web 应用中初始化容器**

我们平时的应用都是 web 应用，要重点理解在 web 应用中如何对容器进行初始化。

1. 在 web.xml 文件中配置 Spring 的监听器「 ContextLoaderListener 监听器 」，并配置 ContextConfigLocation 参数，即配置 Spring 配置文件路径；
2. web 容器启动后加载 web.xml，加载 ContextLoaderLinstener 监听器，该监听器实现 ServletContextListener 接口。
3. 触发 ContextLoaderLinstener 的 contextInitialized() 方法 ，内部调用 initWebApplicationContext() 方法创建 WebApplicationContext 实例，即创建了 Spring 容器。
4. 在 initWebApplicationContext() 方法中，Spring 容器初始化 Bean 实例是在 configureAndRefreshWebApplicationContext() 方法中，最后真正初始化调用的是 **refresh()** 方法。



总的来说，**Spring 容器的创建就是通过在 web.xml 配置的 Spring 监听器在 Web 容器启动时加载 Spring 配置文件，得到 WebApplicationContext 实例，然后就是初始化 Bean 实例**。



**注意：**

ServletContextListener：在服务器启动时创建，在服务器正常关闭时销毁。



java 应用中创建容器（了解）

   ```java
           ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
   
   ```

   绝大部分项目都是 web 应用，所以需要使用第二种方式来创建 IoC 容器。



#### 装配 Bean

Spring 提供了几种 Bean 装配机制：

- 隐式的基于注解的自动装配。
- 在 XML 进行显示配置。
- 在 Java 类中显示配置。
- 基于 Groovy DSL 的配置。



##### 基于注解的自动装配

- 在 Java 类上添加注解，如 @Service、@Controller，@Component；

- 在 Spring 配置文件中，声明 context 命名空间，指定要扫描的包；

- 使用 @Autowried 进行自动注入。

  > IoC 有三种注入方式：
  >
  > - 构造器注入：通过调用类的构造函数，将对象作为构造函数的参数传入。
  > - setter 注入：通过调用 set 方法传入对象。
  > - 接口注入：将调用类所有依赖注入的方法抽取到一个接口中，调用类通过实现该接口提供相应的注入方法。因为它需要被依赖的对象实现不必要的接口，带有侵入性。一般都不推荐这种方式。





**内部实现：**

整个过程在 AbstractApplicationContext 的 **refresh()** 方法中完成。

1. ResourceLoader 从存储介质中加载 Spring 配置信息，并使用 Resource 表示这个配置文件的资源
2. BeanDefinitionReader 读取 Resource 所指向的配置文件资源，然后解析配置文件。配置文件中每一个<bean> 解析成一个 BeanDefinition 对象，并保存到 BeanDefinitionRegistry 中；
3. 容器扫描 BeanDefinitionRegistry 中的 BeanDefinition，使用反射机制自动识别出实现  BeanFactoryPostProcessor 接口的 Bean ，然后对 BeanDefinition 进行加工处理；
4. 调用 InstantiationStrategy 进行 Bean 实例化的工作；
5. 使用 BeanWrapper 以反射机制操作 Bean，完成 Bean 属性的注入工作；
6. 利用容器中注册的 Bean 后处理器（实现 BeanPostProcessor 接口的Bean）对已经完成属性设置工作的 Bean 进行后续加工，直接装配出一个准备就绪的 Bean。
7. 单例 Bean 缓存池：Spring 在 DefaultSingletonBeanRegistry 类中提供了一个用于缓存单实例 Bean 的缓存器，它是一个用 HashMap 实现的缓存器，单实例的 Bean 以 beanName 为键保存在这个HashMap 中。



![image](https://user-images.githubusercontent.com/19634532/61205279-33b90180-a722-11e9-9a01-f23ebf3c497a.png)

**总的，简单来说：加载 Spring 配置信息；然后解析配置文件，配置文件中每一个<bean> 解析成一个 BeanDefinition 对象，并保存到 Bean 定义注册表中；容器扫描注册表中的 BeanDefinition 对象并加工处理，然后实例化 Bean，通过反射机制完成 Bean 属性的注入工作，利用实现了 BeanPostProcessor 的 Bean 后置处理器对 Bean 进行后续加工，最后装配出准备就绪的 Bean 实例；将 Bean 实例放到 Spring 容器中提供的一个 HashMap 实现的 Bean 的缓存器，以 beanName 为键保存在这个HashMap 中。**



[Spring IOC原理总结](https://zhuanlan.zhihu.com/p/29344811?utm_source=wechat_session&utm_medium=social&utm_oi=803254883036831744)





### Bean 的生命周期

普通的 Java 对象，通过 new 进行 Bean 实例化，然后该 Bean 对象就可以使用了，一旦该 Bean 对象不再使用，就会被 Java 虚拟机进行垃圾回收。



Spring 容器中的 Bean 的生命周期由 Spring 容器控制，其生命周期如下：

![image](https://user-images.githubusercontent.com/19634532/61228443-e310cb00-a758-11e9-8b47-774300eb13db.png)



singleton 类型的 Bean 如上生命周期，prototype 类型的 Bean完成实例化之后就由调用方去管理后续流程了，IoC容器不再管理。



**ApplicationContext中Bean的生命周期**

1. 容器根据 Bean 的定义信息进行实例化。会对 scope 为 singleton 且非懒加载的 bean 进行实例化；
2. 使用依赖注入，Spring 按照 Bean 定义信息配置 Bean 的所有属性；
3. 如果 Bean 实现了 BeanNameAware 接口，工厂调用 Bean 的 setBeanName() 方法传递 Bean 的 id；
4. 如果实现了 BeanFactoryAware 接口，工厂调用 setBeanFactory() 方法传入工厂自身；
5. 如果实现了 ApplicationContextAware 接口，会调用该接口的 setApplicationContext() 方法，传入该 Bean 的 ApplicationContext，这样该 Bean 就获得了自己所在的 ApplicationContext；
6. 如果 Bean 实现了 BeanPostProcessor 接口，则调用 postProcessBeforeInitialization() 方法；
7. 如果 Bean 实现了 InitializingBean 接口，则会回调该接口的 afterPropertiesSet() 方法；
8. 如果 Bean 制定了 init-method 方法，就会调用 init-method 方法；
9. 如果 Bean 实现了 BeanPostProcessor 接口，则调用 postProcessAfterInitialization() 方法；
10. 现在 Bean 已经可以使用了。
    - scope 为 singleton 的 Bean 缓存在 Spring IOC 容器中。
    - scope 为 prototype 的 Bean 生命周期交给客户端。
11. 销毁。
    - 如果 Bean 实现了DisposableBean接口，destory() 方法将会被调用；
    - 如果配置了destory-method 方法，就调用这个方法。



**BeanFactory 和 ApplicationContext 不同之处：**

1. BeanFactory 容器中不会调用 ApplicationContext 接口的 setApplicationContext() 方法。
2. BeanFactory 中 BeanPostProcessor 接口必须自己通过代码手动注册。
3. BeanFactory 容器启动的时候，不会去实例化所有 Bean，包括所有 scope 为 singleton 且非懒加载的 Bean 也是一样，而是在调用的时候去实例化。



### 后置处理器：BeanPostProcessor

BeanPostProcessor 是 Spring IOC 容器给我们提供的一个扩展接口

```java
public interface BeanPostProcessor {
    // bean 初始化方法调用前被调用
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    // bean 初始化方法调用后被调用
    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
```



Spring 容器通过 BeanPostProcessor 给了我们一个机会对 Spring 管理的 bean 进行再加工。比如：我们可以修改 bean 的属性，可以给 bean 生成一个动态代理实例等等。一些 Spring AOP 的底层处理也是通过实现BeanPostProcessor 来执行代理包装逻辑的。



BeanFactory 和 ApplicationContext 对待 bean 后置处理器稍有不同。

- ApplicationContext 会自动检测在配置文件中实现了 BeanPostProcessor 接口的所有 bean，并把它们注册为后置处理器，然后在容器创建 bean 的适当时候调用它，因此部署一个后置处理器同部署其他的 bean 并没有什么区别。
- 使用 BeanFactory 实现的时候，bean 后置处理器必须通过代码显式地去注册。



应用场景：

**如果我们需要在 Spring 容器完成Bean的实例化、配置和其他的初始化前后添加一些自己的逻辑处理，我们就可以定义一个或者多个 BeanPostProcessor 接口的实现，然后注册到容器中。**

[Spring探秘，妙用BeanPostProcessor](https://zhuanlan.zhihu.com/p/29750396)



### Bean 的作用域

- 单例（ Singleton ）：在整个应用中，只创建一个 Bean 实例。
- 原型（ Prototype）：每次注入或者通过 Spring 应用上下文获取的时候，都会创建一个 Bean 实例。
- 会话（ Session ）：在 Web 应用中，为每个会话创建一个 Bean 实例。
- 请求（Request）：在 Web 应用中，为每个请求创建一个 Bean 实例。

默认作用域是 Singleton 的。

如果选择其他作用域，要使用 **@Scope** 注解

```java
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServiceImpl{

}
```



### Spring 中的单例 Bean 是线程安全的吗

Spring 并没有对单例 Bean 进行任何多线程的封装处理。关于单例 Bean 的线程安全和并发问题需要开发者自行解决。但实际上，大部分 Spring Bean 并没有可变的状态（比如 Service 类和 DAO 类），所以在某种程度上，Spring 的单例 Bean 是线程安全的。如果你的 Bean 有多种状态（比如 View Model 对象），就需要自行保证线程安全。

最容易的解决办法就是将多态 Bean 的作用域由「 singleton 」变更为「 prototype 」。





### IoC 有什么好处？

IoC 的思想最核心的地方在于，资源不由使用资源的双方管理，而由不使用资源的第三方管理，这可以带来很多好处。

- 资源集中管理，实现资源的可配置和易管理。
- 降低了使用资源双方的依赖程度，也就是我们说的耦合度。