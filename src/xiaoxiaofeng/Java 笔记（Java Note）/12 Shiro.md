 *  Java 安全框架，提供了认证、授权、加密和会话管理等功能
 *  Shiro 的三个核心组件：Subject、**SecurityManager**、Realms

# 相关概念 #

 *  Subject：主体，与应用交互的“用户”，可以是人、第三方服务等
 *  Principal：身份信息，主体（subject）进行身份认证的标识，标识必须具有唯一性，如用户名、手机号、邮箱地址等，一个主体可以有多个身份，但是必须有一个主身份（Primary Principal）
 *  Credential：凭证信息，如密码、证书等
 *  Realm：安全实体数据源，用于获取安全实体，由用户提供，可以有 1 个或多个
 *  SecurityManager：安全管理器，所有具体的交互都通过 SecurityManager 进行控制，它管理着所有 Subject、且负责进行认证、授权及会话、缓存的管理
 *  Authenticator：认证器，负责主体认证，可以自定义实现
 *  Authorizer：授权器，或者访问控制器，用来决定主体是否有权限进行相应的操作，即控制着用户能访问应用中的哪些功能
 *  SessionManager：用来管理主体与应用之间交互的数据；可以实现分布式的会话管理
 *  SessionDAO：Session 数据访问对象，用于会话的 CRUD，
 *  CacheManager：缓存控制器，用来管理如用户、角色、权限等的缓存
 *  Cryptography：密码模块，Shiro 提供的加密组件，用于如密码加密/解密
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414420590.jpeg) 
    图 1 Shiro的核心架构

# Shiro 认证 authenticate #

 *  认证就是验证用户身份的过程
 *  在认证过程中，用户需要提交实体信息（Principals）和凭证信息（Credentials）以检验用户是否合法，如“用户名/密码”

## 认证流程 ##

 *  调用 subject.login 方法进行登录，其会自动委托给 securityManager.login 方法进行登录
 *  securityManager 通过 Authenticator（认证器）进行认证
 *  Authenticator 的实现 ModularRealmAuthenticator 调用 realm 从 ini 配置文件取用户真实的账号和密码，这里使用的是 IniRealm（shiro 自带，相当于数据源）
 *  IniRealm 先根据 token 中的账号去 ini 配置文件中找该账号，如果找不到则给 ModularRealmAuthenticator 返回 null，如果找到则匹配密码，匹配密码成功则认证通过
 *  最后调用 Subject.logout 进行退出操作
 *  Shiro 的认证过程最终会交由 Realm 执行，这时会调用 Realm 的 `getAuthenticationInfo(token)` 方法，该方法主要执行以下操作：
    
     *  检查提交的进行认证的令牌信息
     *  据令牌信息从数据源（通常为数据库）中获取用户信息
     *  对用户信息进行匹配验证
     *  验证通过将返回一个封装了用户信息的 AuthenticationInfo 实例
     *  验证失败则抛出 AuthenticationException 异常信息

## 认证实现 ##

 *  收集实体/凭据信息`UsernamePasswordToken token = new UsernamePasswordToken(username, password);``token.setRememberMe(true);`
 *  提交实体/凭据信息：通过 SecurityUtils 工具类，获取当前的用户，然后通过调用 login 方法提交认证`Subject currentUser = SecurityUtils.getSubject();``currentUser.login(token);`
 *  认证处理
    
     *  如果 login 方法执行完毕且没有抛出任何异常信息，用户认证通过；如果 login 方法执行过程中抛出异常，认证失败
     *  身份认证失败可以捕获 AuthenticationException 或其子类，常见的如：DisabledAccountException（禁用的帐号）、LockedAccountException（锁定的帐号）、UnknownAccountException（错误的帐号）、ExcessiveAttemptsException（登录失败次数过多）、IncorrectCredentialsException（错误的凭证）、ExpiredCredentialsException（过期的凭证）等
     *  认证通过后在应用程序任意地方调用 `SecurityUtils.getSubject()` 都可以获取到当前认证通过的用户实例，使用 `subject.isAuthenticated()` 判断用户是否已验证都将返回 true

## 登出操作 ##

 *  通过调用 `subject.logout()` 来删除登录信息，当执行完登出操作后，Session 信息将被清空，subject 将被视作为匿名用户

# Shiro 授权 authorize #

 *  授权即访问控制，将判断用户在应用程序中对资源是否拥有相应的访问权限
 *  将权限分配给某个角色（角色，一个权限的集合），然后将这个角色关联一个或多个用户
 *  权限表达式：`资源类型：操作：资源 ID`，如 `user:edit:123`，表示可编辑 id 为 123 的用户数据

## 授权流程 ##

 *  首先调用 `Subject.isPermitted/hasRole` 接口，其会委托给 SecurityManager，而 SecurityManager 接着会委托给 Authorizer
 *  Authorizer 是真正的授权者，如果我们调用如 `isPermitted("user:view")`，其首先会通过 PermissionResolver 把字符串转换成相应的 Permission 实例
 *  在进行授权之前，其会调用相应的 Realm 获取 Subject 相应的角色/权限用于匹配传入的角色/权限
 *  Authorizer 会判断 Realm 的角色/权限是否和传入的匹配，如果有多个 Realm，会委托给 ModularRealmAuthorizer 进行循环判断，如果匹配如 `isPermitted/hasRole` 会返回 true，否则返回 false 表示授权失败

## 授权实现 ##

### 基于编码的授权实现 ###

 *  Subject 接口中相关验证方法`hasRole(String roleName)`：当用户拥有指定角色时，返回 true`hasRoles(List<String> roleNames)`：按照列表顺序返回相应的一个 boolean 值数组`hasAllRoles(Collection<String> roleNames)`：如果用户拥有所有指定角色时，返回 true`checkRole(String roleName)`：断言用户是否拥有指定角色`checkRoles(Collection<String> roleNames)`：断言用户是否拥有所有指定角色`checkRoles(String… roleNames)`：对上一方法的方法重载`isPermitted(Permission p)`：Subject 拥有制定权限时，返回 treu`isPermitted(List<Permission> perms)`：返回对应权限的 boolean 数组`isPermittedAll(Collection<Permission> perms)`：Subject 拥有所有制定权限时，返回 true`checkPermission(Permission p)`：断言用户是否拥有制定权限`checkPermission(String perm)`：断言用户是否拥有制定权限`checkPermissions(Collection<Permission> perms)`：断言用户是否拥有所有指定权限`checkPermissions(String… perms)`：断言用户是否拥有所有指定权限

### 基于注解的授权实现 ###

 *  可修饰类或方法
 *  @RequiresPermissions("权限表达式")：当前用户需拥有制定权限
 *  @RequiresRoles：当前用户需拥有制定角色
 *  @RequiresAuthentication：当前用户需是经过认证的用户
 *  @RequiresGuest：当前用户需为“guest”用户
 *  @ RequiresUser：当前用户需为已认证用户或已记住用户

### 基于 JSP 标签的授权实现 ###

 *  引入标签库：<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
 *  常用标签：未认证（包含未记住）的用户：认证通过或已记住的用户：已认证通过的用户，不包含已记住的用户：未认证通过用户：验证当前用户是否属于以下任意一个角色：验证当前用户是否属于该角色：当用户不属于该角色时验证通过：验证当前用户是否拥有指定权限：当前用户没有指定权限时，验证通过：输出当前用户信息：显示用户身份中的属性值

# Shiro 默认拦截器 #

 *  默认的拦截器会自动注册，可以直接在 ini 配置文件中通过 "拦截器名.属性" 设置其属性
 *  注：anon，authcBasic，auchc，user 是认证过滤器，perms，roles，ssl，rest，port 是授权过滤器
 *  anon: 匿名拦截器，即不需要登录即可访问，一般用于静态资源过滤，示例 `"/static/**=anon"`
 *  authc:表示需要认证才能使用，示例 `"/**=authc"`，主要属性：
    
     *  usernameParam：表单提交的用户名参数名
     *  passwordParam：表单提交的密码参数名
     *  rememberMeParam：表单提交的密码参数名
     *  loginUrl：登录表单地址
     *  successUrl：登录成功后的默认重定向地址
     *  failureKeyAttribute：登录失败后错误信息存储 key
 *  authcBasic：HTTP 身份验证拦截器，主要属性：applicationName：弹出登录框显示的信息
 *  roles：角色授权拦截器，验证用户是否拥有资源角色；示例 `"/admin/**=roles[admin]"`
 *  perms：权限授权拦截器，验证用户是否拥有资源权限；示例 `"/user/create=perms["user:create"]"`
 *  user：用户拦截器，用户已经身份验证/记住我登录的都可；示例 `"/index=user"`
 *  logout：退出拦截器，主要属性：redirectUrl：退出成功后重定向的地址，示例 `"/logout=logout"`
 *  port：端口拦截器，主要属性：port：可以通过的端口，示例 `"/test= port[80]"` ，如果用户访问该页面是非 80，将自动将请求端口改为 80 并重定向到该 80 端口，其他路径/参数等都一样
 *  rest：风格拦截器，自动根据请求方法构建权限字符串（GET=read, POST=create, PUT=update, DELETE=delete, HEAD=read, TRACE=read, OPTIONS=read, MKCOL=create）构建权限字符串，示例 `"/users=rest[user]"` ，会自动拼出 `"user:read, user:create, user:update, user:delete"` 权限字符串进行权限匹配（所有都得匹配，isPermittedAll）
 *  ssl：SSL 拦截器，只有请求协议是 https 才能通过，否则自动跳转会 https 端口（443），其他和 port 拦截器一样

# 自定义 Realm #

 *  自定义 Realm 继承 AuthorizingRealm，重写 getName()、doGetAuthenticationInfo()、doGetAuthorizationInfo() 方法

## 认证实现 ##

 *  `doGetAuthenticationInfo()` 方法：通过 AuthenticationToken 获取里面的 principal，如用户名，通过查询数据库是否存在该 principal，找不到返回 null，找到，返回 simpleAuthenticationInfo，里面包含 principal、credential、credentialsSalt、realmName

``````````java
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException { 
      UsernamePasswordToken token = (UsernamePasswordToken) authcToken; 
      User user = accountManager.findUserByUserName(token.getUsername()); 
      if (user != null) { 
          return new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName()); 
      } else { 
          return null; 
      } 
  }
``````````

## 授权实现 ##

 *  `doGetAuthorizationInfo()` 方法：从 principals 中获取 primaryPrincipal，根据 primaryPrincipal 从数据库中查询角色数据 roles、权限数据 permissions，并添加到 simpleAuthorizationInfo 中，再返回

``````````java
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) { 
      String userName = (String) principals.fromRealm(getName()).iterator().next(); 
      User user = accountManager.findUserByUserName(userName); 
      if (user != null) { 
          SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(); 
          for (Group group : user.getGroupList()) { 
              info.addStringPermissions(group.getPermissionList()); 
          } 
          return info; 
      } else { 
          return null; 
      } 
  }
``````````

# Shiro 配置说明 #


[Shiro]: https://static.sitestack.cn/projects/sdky-java-note/549b92b566d3c1b688b0a4954813326b.jpeg