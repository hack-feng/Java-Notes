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