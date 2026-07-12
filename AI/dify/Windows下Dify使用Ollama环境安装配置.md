> 大家好，我是笑小枫，在代码和人生之间，寻找优雅的解。

最近有朋友在问，如何在Windows系统上搭建一套AI应用开发环境。今天不造轮子，直接使用Dify进行快速搭建。

本文讲解Ollama、Docker Desktop和Dify的安装配置，以及Dify如何整合Ollama运行的模型。

## 先看效果展示

通过本文，你可以在本地去搭建一套Dify应用，并去创建你需要的AI应用。

![Dify智能问答界面](https://image.xiaoxiaofeng.site/blog/image/image-20260226104555187.png?xiaoxiaofeng)

是不是很酷？而且整个过程完全免费！让我们开始吧。

## 环境说明

说明：不使用本地Ollama跑模型，配置要求不高，可选择服务商接口调用。

**电脑环境**

* 系统：Windows 11 家庭中文版
* 处理器：i9-14900HX
* 显卡：RTX 5080 16G（跑本地模型需要）
* 网络：**需要科学上网**，可以私聊咨询（安装Docker容器需要）

**软件环境**

* Ollama：0.17.0
* Docker Desktop：4.62.0
* Dify：1.11.4
* DeepSeek-R1:8b
* bge-m3

---

## 安装Ollama

### 什么是Ollama？
简单来说，Ollama就是一个让你能在本地轻松运行大模型的工具，支持Llama、DeepSeek、Qwen等主流模型。

**如果使用服务商的接口，可以跳过这部分。**

### 安装步骤

**1. 下载安装程序**

打开浏览器，访问Ollama官网：https://ollama.com/download，下载Windows版本。

![Ollama官网下载界面](https://image.xiaoxiaofeng.site/blog/image/image-20260225181742041.png?xiaoxiaofeng)

**2. 快速安装**

双击下载好的安装程序，点击安装就行。

![Ollama安装界面](https://image.xiaoxiaofeng.site/blog/image/image-20260225181906247.png?xiaoxiaofeng)

**3. 安装结果验证**

打开命令行（Win+R，输入cmd），输入以下命令：

```bash
ollama --version
```

成功展示版本号，说明安装成功。

![验证Ollama安装](https://image.xiaoxiaofeng.site/blog/image/image-20260225181956844.png?xiaoxiaofeng)

**4. 验证服务是否启动**：
打开浏览器，访问 http://localhost:11434/，看到下图就说明Ollama正常运行：

![Ollama服务验证](https://image.xiaoxiaofeng.site/blog/image/Ollamasuccess.jpg?xiaoxiaofeng)

### 下载模型

安装完成后，使用系统菜单栏的搜索，直接搜索Ollama，找到程序，双击打开：

![Ollama界面](https://image.xiaoxiaofeng.site/blog/image/image-20260225182217146.png?xiaoxiaofeng)

**下载模型**：

- 可以直接点击界面上的模型下载
- 或者使用命令行下载，例如下载bge-m3的模型（知识库需要用到）：
```bash
ollama pull bge-m3
```

![命令行下载模型](https://image.xiaoxiaofeng.site/blog/image/image-20260225182311605.png?xiaoxiaofeng)

**查看下载的模型列表**

~~~
ollama list
~~~

**运行模型测试**：

```bash
ollama run deepseek-r1:8b
```

---

## 安装Docker Desktop

### 为什么要装Docker？
Dify官方推荐用Docker部署，这样可以避免各种环境配置问题。

### 安装步骤

**1. 下载Docker Desktop**

访问 https://www.docker.com/products/docker-desktop/ 下载Windows对应的版本：

![Docker Desktop下载](https://image.xiaoxiaofeng.site/blog/image/image-20260226094359555.png?xiaoxiaofeng)

**2. 开始安装**

双击安装包，等待加载完成：

![Docker安装加载](https://image.xiaoxiaofeng.site/blog/image/image-20260226094736162.png?xiaoxiaofeng)

点击OK继续：

![Docker安装确认](https://image.xiaoxiaofeng.site/blog/image/image-20260226094756511.png?xiaoxiaofeng)

**3. 重启电脑**

安装完成后，需要重启电脑：

![安装完成提示重启](https://image.xiaoxiaofeng.site/blog/image/image-20260226095027054.png?xiaoxiaofeng)

**4. 完成最后配置**

重启后打开Docker Desktop：

![Docker启动界面](https://image.xiaoxiaofeng.site/blog/image/image-20260226095302577.png?xiaoxiaofeng)

如果提示更新WSL，直接回车更新即可：

![WSL更新提示](https://image.xiaoxiaofeng.site/blog/image/image-20260226095319665.png?xiaoxiaofeng)

最后登录Docker账号（**需要科学上网**）：

![Docker登录界面](https://image.xiaoxiaofeng.site/blog/image/image-20260226095447413.png?xiaoxiaofeng)

---

## 安装Dify

### 什么是Dify？
Dify是一个开源的LLM应用开发平台，可以让你像搭积木一样构建AI应用，支持工作流编排、知识库管理、API集成等功能。

### 安装步骤

**1. 下载Dify源码**

如果有Git，直接克隆：
```bash
git clone https://github.com/langgenius/dify.git
```
没有Git的话，可以直接去GitHub下载ZIP包。

**2. 启动Dify**

进入docker目录并启动：
```bash
# 进入docker目录
cd dify/docker

# 复制环境配置（Windows用copy命令）
copy .env.example .env

# 启动Dify（下载镜像需要切换国内镜像源，或者科学上网）
docker-compose up -d

# 等待2-3分钟，查看服务状态
docker-compose ps  
```

看到所有的服务状态都是运行中，就成功了

![Dify服务状态](https://image.xiaoxiaofeng.site/blog/image/image-20260226103716495.png?xiaoxiaofeng)

**3. 初始化配置**

打开浏览器访问 http://localhost/，首次进入需要设置管理员账号（我这里的已经设置完了），然后进行登录。

![Dify初始化界面](https://image.xiaoxiaofeng.site/blog/image/image-20260226104438562.png?xiaoxiaofeng)

登录成功后，你就拥有了一个功能强大的AI应用开发平台！

---

## Dify集成Ollama

### 添加模型供应商

点击右上角头像 → 设置，如下图所示：

![Dify设置入口](https://image.xiaoxiaofeng.site/blog/image/image-20260226104847003.png?xiaoxiaofeng)

进入模型供应商页面，找到Ollama并点击"安装"（我这里已经安装过了）：

![添加Ollama供应商](https://image.xiaoxiaofeng.site/blog/image/dify1%20.jpg?xiaoxiaofeng)

### 添加LLM模型（以DeepSeek为例）

点击"添加模型"，**选择模型类型为"LLM"**，填写配置：

因为Ollama默认是localhost访问，Dify在docker，Ollama在本地，不能使用localhost。所以我们需要设置**OLLAMA_HOST**

~~~
setx OLLAMA_HOST "0.0.0.0:11434"
~~~

这里设置的是0.0.0.0，你也可以根据实际需要进行指定。

- 模型名称：deepseek-r1:8b（或其它下载的模型）
- 服务器URL：http://xxxx:11434 (xxxx替换成你的ip地址）
- 其他配置保持默认

![添加DeepSeek模型](https://image.xiaoxiaofeng.site/blog/image/webwxgetmsgimg.jpeg?xiaoxiaofeng)

### 添加Embedding模型（用于知识库）

再添加一个bge-m3（根据自己需求选择，这个对多语言支持比较好，我需要用到）模型，这次选择模型类型为"**Text Embedding**"：

![添加Embedding模型](https://image.xiaoxiaofeng.site/blog/image/dify222.jpg?xiaoxiaofeng)

如果配置的不对，Dify会报错，如果没有报错，那么恭喜，完成了整个环境的搭建。

## 测试效果

现在我们来创建一个应用测试一下！

进入"工作室" → 创建"Agent"：

![创建Agent](https://image.xiaoxiaofeng.site/blog/image/image-20260226111052067.png?xiaoxiaofeng)

选择deepseek-r1模型，输入问题测试：

![测试效果](https://image.xiaoxiaofeng.site/blog/image/image-20260226111346447.png?xiaoxiaofeng)

看到模型成功回答，说明整个环境配置成功啦！

---

## 写在最后

整个流程走下来，你是不是发现其实并不复杂？有了这套环境，你就可以：
- 搭建企业级智能客服
- 构建个人知识库问答系统
- 开发AI工作流应用
- 测试各种开源大模型

**下期预告**：我将深入讲解从零搭建一个完整的智能客服系统，包括知识库配置、工作流设计和API对接，敬请期待！

---

*关注我，获取更多AI实战教程👇*