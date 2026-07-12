![1051b4ec-ad3a-45b2-b669-b62d7463121e](https://image.xiaoxiaofeng.site/blog/image/1051b4ec-ad3a-45b2-b669-b62d7463121e.png?xiaoxiaofeng)

# CC Switch 保姆使用教程：Codex 多模型一键切换

## 写在前面

我平时用 Codex 写代码，订阅的是火山方舟的 coding plan。

痛点很明确：**一个套餐里塞了一堆模型**——doubao、DeepSeek、Kimi、各种 thinking 版本……想用哪个得去改配置文件

来回切几次，人都麻了。

后来用上了 **CC Switch**，点点鼠标就能切，舒服了。

本文以火山方舟 coding plan 为例，演示 Windows 下用 CC Switch 管理 Codex 多模型配置。

> **环境说明**
> 操作系统：Windows 11
> 工具：Codex CLI + CC Switch
> API 来源：火山方舟 coding plan（仅作示例）

---

## 一、CC Switch 是什么

一句话：**一个管理 AI 编程 CLI 配置的桌面工具**。

![image-20260702162504645](https://image.xiaoxiaofeng.site/blog/image/image-20260702162504645.png?xiaoxiaofeng)

支持 Codex、Claude Code、Gemini CLI 等多个 CLI，可以保存多套配置，**一键切换**，不用再手动改配置文件。

官方文档：https://ccswitch.io/zh/docs

---

## 二、下载安装

### 2.1 下载

去 GitHub Releases 页面：

> https://github.com/farion1231/cc-switch/releases

Windows 下载 `.exe` 安装包。

![image-20260702163858368](https://image.xiaoxiaofeng.site/blog/image/image-20260702163858368.png?xiaoxiaofeng)

### 2.2 安装

双击安装包，一路下一步。

> **提示**：第一次打开如果被 SmartScreen 拦截，点「更多信息」→「仍要运行」即可。

![image-20260702163950859](https://image.xiaoxiaofeng.site/blog/image/image-20260702163950859.png?xiaoxiaofeng)

装完打开，主界面长这样，因为我的Claude Code已经配置了火山方舟的Coding Plan，所以多一个。

![image-20260702164338911](https://image.xiaoxiaofeng.site/blog/image/image-20260702164338911.png?xiaoxiaofeng)

---

## 三、配置 Codex

### 3.1 切到 Codex 模式

CC Switch 顶部选择 **Codex**，下图中红框的图标。

![image-20260702164927887](https://image.xiaoxiaofeng.site/blog/image/image-20260702164927887.png?xiaoxiaofeng)

### 3.2 新增配置

点击加号新增的按钮，这里以火山方舟 coding plan 为例，选择火山Agentplan，略微一改和coding plan一样适用。

![image-20260702165042730](https://image.xiaoxiaofeng.site/blog/image/image-20260702165042730.png?xiaoxiaofeng)

关键字段：

* **API 请求地址**：`https://ark.cn-beijing.volces.com/api/coding/v3`
* **API Key**：火山方舟控制台拿到的 key
* **模型**：填对应的模型 ID，如 `glm-5.2`

![image-20260702170535667](https://image.xiaoxiaofeng.site/blog/image/image-20260702170535667.png?xiaoxiaofeng)

> **注意**：模型 ID 一定要填**model-name**，去火山方舟控制台里复制，别手打，容易错。

### 3.3 把套餐里的模型都加进去

火山方舟 coding plan 里能用好几个模型，**每个模型建一条供应商配置**，codex是默认模型，不支持一个里面配置多个模型去切换，不知道为啥。

不要获取模型列表，那些不一定在我们的套餐里面。

可以去控制台查看适配的模型

![image-20260702171423317](https://image.xiaoxiaofeng.site/blog/image/image-20260702171423317.png?xiaoxiaofeng)

比如我加了这几条：

* glm-5.2
* doubao-seed-2.0-code

![image-20260702174506848](https://image.xiaoxiaofeng.site/blog/image/image-20260702174506848.png?xiaoxiaofeng)

> **💡 小贴士**：配置名直接写模型名，一眼就知道是哪个。

创建完一个直接点击复制，填入对应的模型就行了。切换的时候直接点击【启用】，然后需要**重启codex才生效**。

![image-20260702180917581](https://image.xiaoxiaofeng.site/blog/image/image-20260702180917581.png?xiaoxiaofeng)

---

## 四、测试一下

打开我们的codex，左下角的设置可以看到通过API密钥登录

![image-20260702181342519](https://image.xiaoxiaofeng.site/blog/image/image-20260702181342519.png?xiaoxiaofeng)

但是我的自定义模型一直是空的，不知道为啥。

![image-20260702181411560](https://image.xiaoxiaofeng.site/blog/image/image-20260702181411560.png?xiaoxiaofeng)

![image-20260702181726287](https://image.xiaoxiaofeng.site/blog/image/image-20260702181726287.png?xiaoxiaofeng)

但我的conding plan实际已经跑量了~但我也不确定当前使用的什么模型。。。这点体验贼差。知道怎么搞的佬指教下！

## 五、常见问题

**Q1：切了模型 Codex 还是旧的？**
A：终端没重开。关掉重开一个。

**Q2：API Key 在哪拿？**
A：火山方舟控制台 → API Key 管理，没有新建一个。

**Q3：会泄露 Key 吗？**
A：配置存本地，不上传。前提是从官方 GitHub 下载。

---

## 本文小结

CC Switch 配 Codex，**把改配置文件这事儿变成了点鼠标**，对火山方舟 coding plan 这种一揽子多模型的场景特别合适。

没用过的小伙伴速速去试~

> 项目地址：https://github.com/farion1231/cc-switch
> 官方文档：https://ccswitch.io/zh/docs

博主也是边学习边输出，有问题欢迎指出，也欢迎加好友交流。

微信：**ai-xiaoxiaofeng**

个人网站：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
