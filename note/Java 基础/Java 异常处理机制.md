## Java 异常处理机制

目录

- [Java 异常分类](#java-异常分类)
- [异常处理](#异常处理)
  - [finally 和 return 谁先执行？](#finally-和-return-谁先执行？)
- [如何优雅的处理异常](#如何优雅的处理异常)
  - [附：阿里巴巴异常处理规范](#附：阿里巴巴异常处理规范)

- [try-cath 对性能的影响](#try-cath-对性能的影响)

### Java 异常分类

所有异常类都是继承至 java.lang 包下的 Throwable 类。

以下，Java 异常层次结构图，列举了常见的几种异常类：

<div align="center"><img src="https://upload-images.jianshu.io/upload_images/3297676-fbb20e337e8a4def.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240" width= "800px"></div>



- Error：描述 Java 运行时系统的内部错误和资源耗尽错误，无法人工处理。

- RuntimeException：由程序错误导致的异常，发生该错误，说明代码有问题。

- 其他异常：程序本身没有问题。



异常又可分为检查型异常（CheckedException）和不检查型异常（UncheckedException）：

**UncheckedException**：派生于 Error 类和 RuntimeException 类的异常都是 UncheckedException。

**CheckedException**：其他异常。是编译器检查的一部分，必须在代码里显示的进行捕获处理。使用 try-catch 处理或 throws 抛出。



### 异常处理

使用 `try-catch-finally` 捕获处理异常，在方法内部可以使用 throw 语句将异常对象抛出。

代码示例：

```java
	public void exceptionTest() {
        try {
            // do something
        } catch (RuntimeException e) {
            // do something
            throw new ExceptionA();
        } finally {
            // do something
        }
    }
```



在方法上使用 throws 关键字声明 Checked 异常，将异常向外抛出，表示此方法不处理异常，而交给方法调用方进行处理。

代码示例：

```java
	public void exceptionTest() throws FileNotFoundException {
        // do something
    }
```



#### finally 和 return 谁先执行？

try 语句块中含有 return 语句，是先执行 return 语句，只是 return 的结果被暂存，待 finally 代码块执行完了再将之前暂存的结果返回。如果 finally 子句中有 return 语句，那么 finally 子句的返回值会覆盖 try 里面的返回结果。可以写一段代码验证一下。

```java 
   public int exceptionTest() {
        try {
            return 2;
        } catch (RuntimeException e) {
            // do something
            throw new ExceptionA();
        } finally {
            return 3;
        }
    }
```

以上返回值为 3。

**再次抛出异常的推荐处理方式：异常链**

代码示例：

```java
	public static void exceptionTest() {
        try {
            // do something
        } catch (ExceptionA a) {
            ExceptionB b = new ExceptionB("errorMessage");
            b.initCause(a);
            throw b;
        }
    }
```

建议使用这种包装技术，这样可以让用户抛出子系统中的异常， 而不会丢失原始异常的细节。

> intCause 方法最多可以调用一次。 它通常从构造函数中调用，或者在创建 throwable 之后立即调用



### 如何优雅的处理异常

- 如无必要，勿用异常，只在异常情况下使用异常机制。
- 尽量不要捕获类似 Exception 这样的通用异常，而是应该捕获特定异常。
- 不要生吞（swallow）异常，不做任何处理。
- Throw early, catch late 原则：
  - 提早抛出：检查错误时，尽早抛出错误异常。
  - 延迟捕获：底层逻辑代码异常只管抛出，到高层再统一处理；没法处理的异常不要捕获，抛到上层再处理。



#### 附：阿里巴巴异常处理规范

- Java 类库中定义的可以通过预检查方式规避的 RuntimeException 异常不应该通过 catch 的方式来处理，比如:NullPointerException，IndexOutOfBoundsException 等等。 
- catch 时分清稳定代码和非稳定代码，稳定代码指的是无论如何不会出错的代码。 对于非稳定代码的 catch 尽可能进行区分异常类型，再做对应的异常处理。 说明：对大段代码进行 try-catch，使程序无法根据不同的异常做出正确的应激反应，也不利于定位问题，这是一种不负责任的表现。 
- 不要捕获了却什么都不处理而抛弃之，如果不想处理它，请将该异常抛给它的调用者。最外层的业务使用者，必须处理异常，将其转化为用户可以理解的内容。 
- 有 try 块放到了事务代码中，catch 异常后，如果需要回滚事务，一定要注意手动回滚事务。 
- finally 块必须对资源对象、流对象进行关闭，有异常也要做 try-catch。  JDK7 及以上，可以使用 try-with-resources 方式。 
- 不要在 finally 块中使用 return。
- 捕获异常与抛异常，必须是完全匹配，或者捕获异常是抛异常的父类 。
- 方法的返回值可以为 null，不强制返回空集合，或者空对象等，必须添加注释充分 说明什么情况下会返回 null 值。
- 防止 NPE，是程序员的基本修养。
- 应使用有业务含义的自定义异常。



### try-cath 对性能的影响

- 如果不发生异常，性能几乎是不受影响的，与没有 try-cath 的代码一样。
- 一旦异常发生，进入 catch 代码块里，创建异常对象、收集栈信息等操作就比较影响性能。

详细分析：[从 JVM 视角分析 try...catch...性能](<https://juejin.im/post/5b65a8f2f265da0fa00a399a>)


