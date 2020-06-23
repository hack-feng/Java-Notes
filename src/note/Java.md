### 

设置3个系统变量，参数如下：
~~~
* 变量名：JAVA_HOME
* 变量值：D:\Program Files\Java\jdk-12.0.2        // 要根据自己的实际路径配置
* 变量名：CLASSPATH
* 变量值：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;         //记得前面有个"."
* 变量名：Path
* 变量值：%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;
~~~

###SpringBoot JUnit测试

~~~java
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoControllerTest {

    private Log logger= LogFactory.getLog(DemoControllerTest.class);

    @Autowired
    private IDemoService demoService;
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @Before
    public void setupMockMvc(){
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象
    }

    // 直接调用service
    @Test
    public void getList() {
        logger.info(demoService.list());

    }

    // GET请求
    @Test
    public void get() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/demo/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
//                .param() // 传参
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    // POST请求
    @Test
    public void add() throws Exception {
        Demo demo = new Demo();
        demo.setName("测试数据");
        String result = mvc.perform(MockMvcRequestBuilders.post("/demo")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(JSONObject.toJSONBytes(demo))
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getContentAsString();
        logger.info(result);
    }
}
~~~

###BeanUtils.copyProperties使用（对象copy）
BeanUtils提供对Java反射和自省API的包装。其主要目的是利用反射机制对JavaBean的属性进行处理。
我们知道，一个JavaBean通常包含了大量的属性，很多情况下，对JavaBean的处理导致大量get/set代码堆积，增加了代码长度和阅读代码的难度。

注意：属性复制，不同jar中的方法，用法不一样。 <br>
1、package org.springframework.beans;  <br>
BeanUtils.copyProperties(A,B); <br>
是A中的值付给B

2、package org.apache.commons.beanutils; <br>
BeanUtils.copyProperties(A,B); <br>
是B中的值付给A


### 反射遍历实体类信息

~~~java
public class DemoControllerTest {
    public void reflect(Object a){
        // 获取所有列信息
        Field[] field = a.getClass().getDeclaredFields();
        
        for (Field field1 : field) {
            // 获取属性的名字
            String name = field1.getName();    
            // 获取属性的类型
            String type = field1.getGenericType().toString();    
            // 将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1); 
            field1.setAccessible(true);
            
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.lang.Double")) {
                // 调用getter方法获取属性值
                Method m = a.getClass().getMethod("get" + name);
                Double v = (Double) m.invoke(a);
                if (v != null) {
                    System.out.println(v);
                }
                // 调用setter方法赋属性值
                Method m2 = a.getClass().getMethod("set" + name, Double.class);
                m2.invoke(a, 20d);
            }
        }
    }
}
~~~

~~~
request获取请求信息

1、request.getContextPath()；获取项目名称

2、request.getScheme()；获取请求使用的协议名

3、request.getProtocol()；获取请求协议版本

4、request.getServerName()；获取请求URL上的主机名（内网未转发时，一般为项目部署服务器主机ip）

5、request.getServerPort()；获取请求URL上的端口号

6、request.getLocalAddr()；获取最终接收请求的主机地址

7、request.getLocalName()；获取最终接收请求的主机名

8、request.getLocalName()；获取最终接收请求的主机

9、request.getLocalPort()；获取最终接收请求的端口

10、request.getMethod()；获取请求的方法

11、request.getRequestURI()；获取请求URL从端口到请求参数中间的部分

12、request.getServletPath()；获取请求URL中访问servlet的那部分路径

13、request.getServletContext().getRealPath('/')；获取给定虚拟路径在服务端的真实路径

14、request.getSession().getServletContext().getRealPath('/')；获取给定虚拟路径在服务端的真实路径

15、request.getRemoteAddr()；获取发送请求的客户端地址（如果经过Apache等转发，则不是真实的DCN网地址）；

16、request.getRemoteHost()；获取发送请求的客户端主机名

17、request.getRemotePort()；获取发送请求的客户端端口
~~~

### 没法使用Spring自动注入时，手动注入Service等资源
~~~
BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
RedisService redisService = (RedisService) factory.getBean("redisService");
~~~

### JAVA 半角全角相互转换
~~~
/**
 * 半角转全角
 * @param input String.
 * @return 全角字符串.
 */
public static String ToSBC(String input) {
         char c[] = input.toCharArray();
         for (int i = 0; i < c.length; i++) {
           if (c[i] == ' ') {
             c[i] = '\u3000';
           } else if (c[i] < '\177') {
             c[i] = (char) (c[i] + 65248);

           }
         }
         return new String(c);
}

/**
 * 全角转半角
 * @param input String.
 * @return 半角字符串
 */
public static String ToDBC(String input) {
    

         char c[] = input.toCharArray();
         for (int i = 0; i < c.length; i++) {
           if (c[i] == '\u3000') {
             c[i] = ' ';
           } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
             c[i] = (char) (c[i] - 65248);

           }
         }
    String returnString = new String(c);
    
         return returnString;
}
~~~

### 计算明天的日期
~~~java
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
 
public class test {
 
    public static void main(String[] args) {
        Format f = new SimpleDateFormat("yyyy-MM-dd");
 
        Date today = new Date();
        System.out.println("今天是:" + f.format(today));
 
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
 
        Date tomorrow = c.getTime();
        System.out.println("明天是:" + f.format(tomorrow));
    }
}
~~~

### SimpleDateFormat显示毫秒

~~~
SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmssSSS");
~~~
