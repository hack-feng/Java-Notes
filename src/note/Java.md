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

### 自动设置结束时间为n个工作日后的日期
需求：
自动设置截止日期为工作日15天

>需要手动维护节假调休的工作日，以及非周末的节假日

>例如：

>   5月1号，周四为节假日，则需要维护为节假日

>  5月4号，周天需要调休工作，则需要维护为调休工作日
     

#### 1、定义表结构，维护节假日或工作日
~~~sql
CREATE TABLE `holiday_info` (
  `id` int(11) DEFAULT NULL COMMENT '主键id',
  `date_info` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日期信息',
  `date_type` int(1) DEFAULT NULL COMMENT '日期类型(0：节假日  1：工作日)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
~~~

#### 2、编写程序，实现功能
* java实现
~~~java
public class Test{
    public String getDate(int days){
        int flag = 0;
        String startDate = DateUtil.parseDateToString(null, new Date());
        // 查询休息日
        Set<String> countRest = holidayInfoDao.getCount(startDate, 1);
        // 查询工作日
        Set<String> countWork = holidayInfoDao.getCount(startDate, 0);

        while(flag <= days){
            flag ++;
            Date date = DateUtil.addDateByDays(new Date(),flag);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int w = cal.get(Calendar.DAY_OF_WEEK);
            // 周天和周六为休息日 周天为1，周六为7
            if (w == 1 || w == 7){
                // 判断周六和周天不是调休的工作日，则延后一天
                if(!countWork.contains(DateUtil.parseDateToString(null, date))){
                    days ++;
                }
            }else{
                // 判断周一和周五是节假日，则延后一天
                if(countRest.contains(DateUtil.parseDateToString(null, date))){
                    days ++;
                }
            }
        }
        String result = DateUtil.parseDateToString(null,DateUtil.addDateByDays(new Date(),days));
        System.out.println(result);
        return  result;
    }
}
~~~

* DateUtil.java日期处理类
~~~java
import com.ict.framework.common.utils.StringUtil;
import org.sqlite.date.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    //日期 转 str
    public static String parseDateToString(String format, Date date) {
        if (format == null) {
            format = "yyyy-MM-dd";
        }
       return DateFormatUtils.format(date, format);
    }

    public static Date addDateByDays(Date date, int Days) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, Days);
            date = calendar.getTime();
            return date;
    }
}
~~~

* mybatis查询sql
~~~sql
select date_info from holiday_info where date_type = #{type} and date_info > #{startDate} 
~~~

### 根据版本号设置展示的编号为为A-ZZ
>需求：   <br>
>版本号从1开始递增，页面展示为A-ZZ   <br>
>例如：  <br>
>version = 1 ,则 versionDisplay = A  <br>
>version = 27 ,则 versionDisplay = AA

具体代码实现如下：
~~~java
public class Test{
    public String getVersionDisplay(int version){
        if(version < 1){
            throw new RuntimeException("订单的版本号不能小于1");
        }
        if(version > 702){
            throw new RuntimeException("订单修改次数过多，请您创建新的订单");
        }
        String result;
        // A对应的ASCII编码为65
        // 因为第一位编号默认从1开始，version = 1时， first = A
        // first = first + version
        // 故first取值64
        int first = 64;
        int second = 65;
        
        // 如果version小于26，则只有一位编号；如果大于26，则有两位编号。
        if(version > 26){
            // 通过整除，得到第一位编号
            first = first + (version - 1)/26;
            // 通过取余，得到第二位编号
            second = second + (version - 1)%26;
            result = (char) first + "" + (char) second;
        }else{
            first = first + version;
            result = (char) first + "";
        }
        return result;
    }
}
~~~


### 数组，List，Set相互转化

* 数组转化为List
~~~
String[] strArray= new String[]{"Tom", "Bob", "Jane"};
 
List strList= Arrays.asList(strArray);
~~~

* 数组转Set
~~~
String[] strArray= new String[]{"Tom", "Bob", "Jane"};
 
Set<String> staffsSet = new HashSet<>(Arrays.asList(staffs));
 
staffsSet.add("Mary");
 
staffsSet.remove("Tom");
~~~

* List转Set
~~~
String[] staffs = new String[]{"Tom", "Bob", "Jane"};
 
List staffsList = Arrays.asList(staffs);
 
Set result = new HashSet(staffsList);
~~~

* set转List
~~~
String[] staffs = new String[]{"Tom", "Bob", "Jane"};
 
Set<String> staffsSet = new HashSet<>(Arrays.asList(staffs));
 
List<String> result = new ArrayList<>(staffsSet);
~~~

~~~
<mirror>
            <id>alimaven</id>
            <mirrorOf>central</mirrorOf>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
	</mirror> 
~~~


