### Spring有几种事务管理机制？

> Spring提供了两种事务管理机制：编程式事务、声明式事务

#### 编程式事务

在代码中手动的管理事务的提交、回滚等操作，代码侵入性比较强

```java
try {
    //TODO something
     transactionManager.commit(status);
} catch (Exception e) {
    transactionManager.rollback(status);
    throw new InvoiceApplyException("异常失败");
}
```

#### 声明式事务(推荐)

基于AOP面向切面的，它将具体业务与事务处理部分解耦，代码侵入性很低，所以在实际开发中声明式事务用的比较多。

```java
@Transactional
@GetMapping("/test")
public String test() {
    int insert = cityInfoDictMapper.insert(cityInfoDict);
}
```