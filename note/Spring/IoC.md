## IoC 

### 目录

- [什么是 IoC？](#什么是-ioc)
- [什么是 Spring 容器？](什么是-spring-容器)
- [IoC 实现原理](#ioc-实现原理)
- [Bean 的生命周期](#bean-的生命周期)
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



**ApplicationContext 和 BeanFactory 的区别？**

- BeanFactory 是 Spring 框架的基础设施，面向 Spring 本身，ApplicationContext 面向使用 Spring 框架的使用者，几乎所有的应用场合我们都直接使用 ApplicationContext 而非底层的 BeanFactory。
- BeanFactory 采取**延迟加载**，第一次调用 getBean() 方法时才会初始化 Bean，ApplicationContext 是加载完 applicationContext.xml 时，就创建具体的 Bean 对象。（**只对 BeanDefition 中描述为是单例的 bean，才进行饿汉式加载**）。



### IoC 实现原理

IoC 是一种思想，DI（Dependency Injection，依赖注入）是实现 IoC 思想的一种技术手段。

依赖注入就是对象之间的依赖关系依靠第三方容器在创建对象的时候进行设定，即由容器动态的将依赖关系主动注入到需要它们的对象中，而不是让调用者自己去获取依赖。



#### 创建 Spring容器

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



![image](https://user-images.githubusercontent.com/19634532/61205279-33b90180-a722-11e9-9a01-f23ebf3c497a.png)



原理？？？



IoC 有三种注入方式：

- 构造器注入：通过调用类的构造函数，将对象作为构造函数的参数传入。
- setter 注入：通过调用 set 方法传入对象。
- 接口注入：将调用类所有依赖注入的方法抽取到一个接口中，调用类通过实现该接口提供相应的注入方法。因为它需要被依赖的对象实现不必要的接口，带有侵入性。一般都不推荐这种方式。







### IoC 有什么好处？

IoC 的思想最核心的地方在于，资源不由使用资源的双方管理，而由不使用资源的第三方管理，这可以带来很多好处。

- 资源集中管理，实现资源的可配置和易管理。
- 降低了使用资源双方的依赖程度，也就是我们说的耦合度。

