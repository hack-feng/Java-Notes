### Transactional有几种作用域？

**作用于类**
  当把@Transactional 注解放在类上时，表示所有该类的public方法都配置相同的事务属性信息。

**作用于方法**
  当类配置了@Transactional，方法也配置了@Transactional，方法的事务会覆盖类的事务配置信息。

**作用于接口**
  不推荐这种使用方法，因为一旦标注在Interface上并且配置了Spring AOP 使用CGLib动态代理，将会导致@Transactional注解失效