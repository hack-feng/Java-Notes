# 实战教程：手把手教你用Dify搭建智能客服系统，零代码也能轻松搞定！

上一篇文章我们完成了Dify+Ollama的环境搭建（没看过的朋友可以点击文末链接回顾）

今天就来点实战干货——**如何从零搭建一个基于本地知识库的智能客服系统**。

先说说我这个客服系统的能力：

1. 用户选择地区+输入问题
2. 自动识别用户意图（LLM）
3. 检测用户所在地区
4. 根据意图和地区检索对应知识库
5. 基于知识库生成精准答案
6. 最终返回给用户

整个流程使用Dify实现起来，真的超！级！简！单！

整体流程大致如下：

![deepseek_mermaid_20260226_2f990c](https://image.xiaoxiaofeng.site/blog/image/deepseek_mermaid_20260226_2f990c.png?xiaoxiaofeng)

---

## 搭建知识库

### 1. 创建知识库

进入Dify后台，点击"知识库"→"创建知识库"：

![创建知识库](https://image.xiaoxiaofeng.site/blog/image/image-20260226133204090.png?xiaoxiaofeng)

### 2. 导入数据

这里我选择通过文件导入，支持txt、pdf、docx等多种格式。如果你的知识库经常变动，后期可以用接口维护（这个我们后面单独讲）。

![导入文件](https://image.xiaoxiaofeng.site/blog/image/image-20260226134937568.png?xiaoxiaofeng)

**小技巧**：也可以同步在线文档，比如Notion、Confluence等，根据实际需求选择即可。

### 3. 关键配置

添加文件后点击下一步，这里有三个关键设置：

**① 索引方式**：建议选择"高质量"，RAG检索更精准（虽然会消耗更多Token，但效果值得）

**② Embedding模型**：选择之前Ollama安装的**bge-m3**，因为我这里需要多语言，大家可以根据自己选择。

![Embedding配置](https://image.xiaoxiaofeng.site/blog/image/image-20260226134607395.png?xiaoxiaofeng)

**③ 检索设置**：建议使用Dify都推荐的"混合检索"，Top K和Score阈值根据需求调整

![检索设置](https://image.xiaoxiaofeng.site/blog/image/image-20260226134207476.png?xiaoxiaofeng)

### 4. 等待处理

点击下一步，系统会自动处理文档，等待片刻即可：

![知识库处理中](https://image.xiaoxiaofeng.site/blog/image/image-20260226135101591.png?xiaoxiaofeng)

我这里根据不同的地区创建了多个知识，同时考虑到通用知识库比较固定，可以文本维护；产品知识库比较容易发生改变，后续可以使用接口方式维护。

---

## 搭建智能客服

### 1. 选择工作流类型

进入"工作室"，选择"Chatflow"，这个类型比较适合使用知识库的智能客服场景;

点击后进入创建页面，当然我们也可以选择模板去创建。

![创建Chatflow](https://image.xiaoxiaofeng.site/blog/image/image-20260226135344269.png?xiaoxiaofeng)

### 2. 整体设计思路

整个搭建过程就是创建流程节点——根据自己的需要添加对应节点即可，比较直观，后面维护也比较方便。

![工作流节点](https://image.xiaoxiaofeng.site/blog/image/image-20260226135631579.png?xiaoxiaofeng)

我用到的主要节点：LLM节点、知识检索节点和条件分支节点。

![完整工作流](https://image.xiaoxiaofeng.site/blog/image/image-20260226135802623.png?xiaoxiaofeng)

### 3. 关键节点配置详解

#### 关键点1：添加地区参数

因为我的需求需要用户带地区属性，所以在用户输入的节点添加一个`region_code`字段。

![添加地区参数](https://image.xiaoxiaofeng.site/blog/image/image-20260226140105189.png?xiaoxiaofeng)

#### 关键点2：意图分析配置

意图分析有几个点需要注意，下面我们一起看一下。

![意图分析节点](https://image.xiaoxiaofeng.site/blog/image/image-20260226140220750.png?xiaoxiaofeng)

**模型配置**：

温度设置：因为意图分析，我需要得到的结果比较固定，所以温度这里设置的比较低。

推理模式：**模型的思考模式默认没有打开，但是默认会走思考模式。** 因为意图分析我需要快速得到结论，不需要深度思考，这里打开思考模式，选择False关闭(有点矛盾)。

![模型参数配置](https://image.xiaoxiaofeng.site/blog/image/image-20260226140405701.png?xiaoxiaofeng)
![关闭思考模式](https://image.xiaoxiaofeng.site/blog/image/image-20260226140647148.png?xiaoxiaofeng)

**SYSTEM提示词**：可以自己写完再找模型优化下，这是和LLM交互的核心，尽可能的简化、明确。：

```
你是一个分类器。

规则：
1 → 购车需求（买车、寻车、车型、配置）
2 → 信息咨询（公司、流程、政策、如何购买）
3 → 合作意向（合作、渠道、代理、项目）
99 → 其他

输出要求：仅输出1个数字，无空格、无标点、无解释。

用户：{{#sys.query#}}
```

**结构化输出**：意图的输出的变量，这里直接得到结果就行，启用结构化输出，并用枚举限制结果。

![结构化输出](https://image.xiaoxiaofeng.site/blog/image/image-20260226141048603.png?xiaoxiaofeng)

#### 关键点3：分支流程设计

**意图分支**：根据LLM的意图输出结果，进入不同的分支节点。

![意图分支](https://image.xiaoxiaofeng.site/blog/image/image-20260226141259446.png?xiaoxiaofeng)

**地区分支**：根据用户最初选择的地区，进入对应的地区流程分支。

![地区分支](https://image.xiaoxiaofeng.site/blog/image/image-20260226141420621.png?xiaoxiaofeng)

#### 关键点4：知识库检索

为每个分支对应的知识库检索节点，添加我们配置的知识库信息。

![知识库检索](https://image.xiaoxiaofeng.site/blog/image/image-20260226141420621.png?xiaoxiaofeng)

**关键设置**：调用LLM生成结果的时候，需要将检索到的知识库文档放入上下文变量中，模型基于知识库的结果进行回复。

![设置上下文](https://image.xiaoxiaofeng.site/blog/image/image-20260226141602929.png?xiaoxiaofeng)

#### 关键点5：最终回复节点

因为所有分支最后都会汇总到一个回复节点上，所以我们需要把每个分支的返回结果都加上。

![回复节点配置](https://image.xiaoxiaofeng.site/blog/image/image-20260226141721038.png?xiaoxiaofeng)

### 4. 调试与发布

配置完成后，先点击右上角的"预览"按钮进行调试，确保每个分支都能正常的工作：

![预览调试](https://image.xiaoxiaofeng.site/blog/image/image-20260226142301963.png?xiaoxiaofeng)

测试没有问题后，点击"发布"，智能客服就可以使用了。

---

## 多场景测试验证

### 1. 直接运行测试

选择运行，会打开一个类似于智能体的网页。进入对话界面，选择地区、输入问题，查看响应效果：

![平台测试](https://image.xiaoxiaofeng.site/blog/image/image-20260226142512436.png?xiaoxiaofeng)

### 2. 嵌入网站

选择嵌入模式，复制嵌入代码后，粘贴到网站HTML中，就能拥有一个智能客服浮窗。

![网站嵌入](https://image.xiaoxiaofeng.site/blog/image/image-20260226141848156.png?xiaoxiaofeng)

### 3. API调用

点击"访问API"，可以配置API密钥，并根据接口访问文档通过接口进行交互，这个应该是我们比较常用的模式，可以完美的把整个流程嵌入到我们的实际业务中。

![API文档](https://image.xiaoxiaofeng.site/blog/image/image-20260226142617799.png?xiaoxiaofeng)

**调用示例**：

```python
POST http://192.168.50.193/v1/chat-messages
Authorization: Bearer app-xxxxxxxxxxxxxxxxxxxxx
Content-Type: application/json

{
  "user": "笑小枫",
  "response_mode":"streaming",
  "conversation_id":"",
  "files":[],
  "query":"我想买一辆奥迪A6L",
  "inputs":{"region_code":"zh"},
  "parent_message_id":null
}
```

成功返回的结果如下：

![API调用成功](https://image.xiaoxiaofeng.site/blog/image/image-20260226150401241.png?xiaoxiaofeng)

---

## 文末福利

**下期预告**：Dify进阶：如何用接口动态维护知识库，告别手动更新

如果你想第一时间收到更新，记得**关注+星标**，不会错过任何干货！

**遇到问题怎么办？**

- 在评论区留言，我会一一回复
- 私信我你的问题
- 加入读者群交流

---

**如果本文对你有帮助，欢迎点赞、在看、转发三连支持！**