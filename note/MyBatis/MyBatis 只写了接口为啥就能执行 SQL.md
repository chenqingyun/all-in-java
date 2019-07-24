## MyBatis 只写了接口为啥就能执行 SQL

思路就是利用动态代理帮我们生成了接口的代理类，MapperProxy，在调用接口方法时，方法调用会被代理逻辑拦截，就是调用的 MapperProxy 对象的 invoke 方法，在代理逻辑中可根据接口全路径名和方法名获取到当前方法对应的 SQL，最后会使用 SqlSession 接口的方法使它能够执行查询。



[MyBatis原理概括](https://segmentfault.com/a/1190000015117926)

