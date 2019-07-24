## SpringMVC

### 目录

- [SpringMVC 处理请求的过程](#springMVC-处理请求的过程)
- [SpringMVC 的工作原理](#springMVC-的工作原理)



### SpringMVC 处理请求的过程

1. 客户端发出 HTTP 请求，web 服务器接收到请求，如果匹配 DispatcherServlet 的请求映射路径（web.xml  中指定），则将请求转交给 DispatcherServlet 处理。
2. DipatcherServlet 接收到这个请求之后将根据请求的信息（包括 URL、HTTP 方法、请求报文头、请求参数、Cookie 等）以及 HandlerMapping 的配置找到处理请求的处理器（Handler）。 
3. 当 DispatcherServlet 根据 HandlerMapping 找到对应的 Handler，将处理权交给 Handler（Handler将具体的处理进行封装），再由具体的 HandlerAdapter 对 Handler 进行具体的调用。 
4. Handler 处理完业务逻辑后将返回一个 ModelAndView 对象给 DispatcherServlet，ModelAndView 包含了视图逻辑名和模型数据信息。
5. ModelAndView 包含的是一个「 逻辑视图名 」而非真正的视图对象，DispatcherSevlet 借由 ViewResolver将逻辑视图名转化为真正的视图 View。 
6. 当得到真正的视图对象 View 时，DispatcherServlet 就使用这个 View 对 ModelAndView 的模型数据进行视图渲染。
7. 最终客户端得到的信息可能是一个 HTML 页面，或者是 XML 或 JSON 串，或者是图片或一个 PDF 文档等不同的媒体形式。



![image](https://user-images.githubusercontent.com/19634532/61796763-a760ba00-ae58-11e9-81b5-00fb5beb8d9c.png)



### SpringMVC 的工作原理

需要回答以下三个问题：

- DispatcherServlet 框架如何截获特定的 HTTP 请求并交由 SpringMVC 框架处理？
- 位于 Web 层的 Spring 容器（WebApplicationContext）如何与位于业务层的 Spring 容器（ApplicationContext）建立关联，以使 Web 层的 Bean 可以调用到业务层的 Bean？
- 如何初始化 SpringMVC 的各个组件，并装配到 DispatcherServlet 中？



**1. 配置 DispatcherServlet，截获特定的 URL 请求**

在 web.xml 中配置 Servlet，并通过<servlet-mapping>指定其处理的 URL。



**2. 多个 Spring 容器之间可以设置父子级关系，Web 层的 Spring 容器将作为业务层的 Spring 容器的子容器**

即 Web 层容器可以引用到业务层容器的 Bean，而业务层容器却访问不到 Web 层容器的 Bean。



**3. 在 DispatcherServlet 的 initStrategies() 方法中完成 SpringMVC 的各个组件的初始化和装配**

```java
protected void initStrategies(ApplicationContext context) {
        this.initMultipartResolver(context); // 初始化上传文件解析器
        this.initLocaleResolver(context); // 初始化本地化解析器
        this.initThemeResolver(context); // 初始化主题解析器
        this.initHandlerMappings(context); // 初始化处理器映射器
        this.initHandlerAdapters(context); // 初始化处理器适配器
        this.initHandlerExceptionResolvers(context); // 初始化处理器异常解析器
        this.initRequestToViewNameTranslator(context); // 初始化请求到视图翻译器
        this.initViewResolvers(context); // 初始化视图解析器
        this.initFlashMapManager(context); 
    }
```

initStrategies() 方法将在 WebApplicationContext 初始化后自动执行，此时 Spring 上下文的 Bean 已经初始化完成。改方法的工作原理：通过反射机制查找并装配 Spring 容器中用户显示定义的组件 Bean，如果找不到，则装配默认的组件实例。



