# Dify进阶实操：用API自动化维护知识库中的动态商品信息

之前给大家分享了如何使用Dify搭建智能客服，今天来聊聊通过Dify提供的API接口，去实现知识库的自动化维护。

**本文的实操策略**：建议在Dify手动创建一个空白的知识库，然后上传一个文档，后续通过api接口的方式去维护文档内的文档块。

开始之前，先同步下`知识库`、`文档`、`文档块`的概念

**知识库**就是一个仓库，可以成**文件夹**，里面可以有很多文件（文档）。

![image-20260227150252582](https://image.xiaoxiaofeng.site/blog/image/image-20260227150252582.png?xiaoxiaofeng)

**文档**就是知识库中的一个个**文件**，可以是各种格式。

![image-20260227145832607](https://image.xiaoxiaofeng.site/blog/image/image-20260227145832607.png?xiaoxiaofeng)

**文档块**就是文档中**一段段的信息**。

![image-20260227145822077](https://image.xiaoxiaofeng.site/blog/image/image-20260227145822077.png?xiaoxiaofeng)

## 1. 创建API密钥

我们先进入已有的知识库（或者新建一个空白库），在左下角找到**服务 API**，在这里创建一个API的密钥。

![image-20260227134526968](https://image.xiaoxiaofeng.site/blog/image/image-20260227134526968.png?xiaoxiaofeng)

保存好这个密钥，后续所有接口调用都需要携带。

## 2. 创建知识库

首先，先在Dify页面创建一个名为`xiaoxiaofeng`的空知识库。

![image-20260227135126176](https://image.xiaoxiaofeng.site/blog/image/image-20260227135126176.png?xiaoxiaofeng)

接下来需要通过`/v1/datasets`接口获取这个知识库的ID。

```
GET http://192.168.50.193/v1/datasets?page=1&limit=20&keyword=xiaoxiaofeng
Authorization: Bearer 你创建的密钥
```

返回数据格式如下，JSON中`id`字段，就是我们后续操作中需要用到的知识库ID。

![image-20260227140106598](https://image.xiaoxiaofeng.site/blog/image/image-20260227140106598.png?xiaoxiaofeng)



## 3. 创建文档

可以通过页面上传一个空文件占位，也可以通过接口创建。这里演示通过接口的方式创建

官方文档提供了文本和文件两种方式，我选择了文本方式![image-20260227140757204](https://image.xiaoxiaofeng.site/blog/image/image-20260227140757204.png?xiaoxiaofeng)

**创建文档的示例代码：**

```
POST http://192.168.50.193/v1/datasets/{dataset_id}/document/create-by-text
Authorization: Bearer 你创建的密钥
Content-Type: application/json

{
  "name": "product",
  "text": "我是笑小枫，感谢大家的关注",
  "indexing_technique": "high_quality",
  "doc_form": "text_model",
  "doc_language": "中文",
  "embedding_model": "bge-m3",
  "embedding_model_provider": "langgenius/ollama/ollama",
  "retrieval_model": {
    "search_method": "hybrid_search",
    "reranking_enable": false,
    "top_k": 5,
    "score_threshold_enabled": false,
    "score_threshold": 0.5,
    "weights": {
      "weight_type": "customized",
      "keyword_setting": {
        "keyword_weight": 0.3
      },
      "vector_setting": {
        "vector_weight": 0.7,
        "embedding_model_name": "",
        "embedding_provider_name": ""
      }
    }
  }
}
```

响应结果会返回文档ID，这个ID后面创建文档块需要用

![image-20260227142737559](https://image.xiaoxiaofeng.site/blog/image/image-20260227142737559.png?xiaoxiaofeng)

创建成功后，在Dify页面就能看到这个文档了

![image-20260227142858391](https://image.xiaoxiaofeng.site/blog/image/image-20260227142858391.png?xiaoxiaofeng)

### 几个容易踩坑的配置项

**indexing_technique：索引方式**

这里选择了高质量模式（high_quality），需要配置embedding模型

![image-20260227141831858](https://image.xiaoxiaofeng.site/blog/image/image-20260227141831858.png?xiaoxiaofeng)

**embedding_model_provider：模型提供商**

这个字段不能直接填"ollama"，需要填的是提供商标识。可以通过调用可用模型接口进行获取

![image-20260227143214956](https://image.xiaoxiaofeng.site/blog/image/image-20260227143214956.png?xiaoxiaofeng)

接口返回结果示例如下：

![image-20260227143311165](https://image.xiaoxiaofeng.site/blog/image/image-20260227143311165.png?xiaoxiaofeng)

**retrieval_model：检索模型配置**

对应页面上的这块配置区域

![image-20260227142000596](https://image.xiaoxiaofeng.site/blog/image/image-20260227142000596.png?xiaoxiaofeng)

**注意**：文档上`weights`字段是`number<float> | null`类型，实际中，混合检索模式下的结构如下：

```json
"weights": {
    "weight_type": "customized",
    "keyword_setting": {
        "keyword_weight": 0.3
    },
    "vector_setting": {
        "vector_weight": 0.7,
        "embedding_model_name": "",
        "embedding_provider_name": ""
    }
}
```

保存时字段类型如果报错，可以先手动创建一个知识库，然后调用`/v1/datasets`接口查询，可以看到数据结构

![image-20260227142454074](https://image.xiaoxiaofeng.site/blog/image/image-20260227142454074.png?xiaoxiaofeng)

## 4. 创建文档块

文档块接口相对简单

![image-20260227143451199](https://image.xiaoxiaofeng.site/blog/image/image-20260227143451199.png?xiaoxiaofeng)

**我的示例：**

```
POST http://192.168.50.193/v1/datasets/{dataset_id}/documents/{document_id}/segments
Authorization: Bearer 你创建的密钥
Content-Type: application/json

{
  "segments": [
    {
      "content": "我的微信公众号是笑小枫，感谢大家关注",
      "keywords": [
        "微信公众号", "笑小枫"
      ]
    }
   ]
}
```

返回结果：

![image-20260227144210400](https://image.xiaoxiaofeng.site/blog/image/image-20260227144210400.png?xiaoxiaofeng)

返回数据中的`id`要存下来，和我们的数据做关联，这个ID是后续修改、删除该条数据的标识。

现在回到Dify页面，就可以看到我们刚才添加的数据了

![image-20260227144421023](https://image.xiaoxiaofeng.site/blog/image/image-20260227144421023.png?xiaoxiaofeng)

## 5. 召回测试

添加完成后，可以在召回测试中验证一下效果

![image-20260227144608423](https://image.xiaoxiaofeng.site/blog/image/image-20260227144608423.png?xiaoxiaofeng)

## 写在最后

LLM应用的难点，提示词占一半，知识库召回占一半。

针对产品数据，向量数据库里的数据怎么存，才能得到我们想要的结果，这件事我还在不断摸索中。

也欢迎各位读者，我们一起交流。

---

*本文涉及的接口版本为Dify 1.11.4，不同版本可能有细微差异，以实际版本为准。*