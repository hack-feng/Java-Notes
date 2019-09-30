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
