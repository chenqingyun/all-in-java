## Java 异常处理机制

### Java 异常类层次结构

Throwable

- Error
- Exception
  - RuntimeException
    - ArrayIndexOfOutOfBoundsException
    - NullPointerException
  - 其他：IOException



Unchecked 异常：Error 和 RuntimeException。

Checked 异常：其他。需要 try-catch 处理或 throws 抛出



### 异常处理

try-catch-finally



如果 try 语句块中有 return 语句，先执行 return 语句，但是返回值暂存，待 finally 代码块执行结束再返回值。如果 finally 代码块中也哟 return 语句，则 finally 代码块中的返回值会替换 try 语句块中的返回值。

### 如何优雅的处理异常