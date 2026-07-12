# Claude Code 下载安装与使用指南：让 AI 成为你的编程搭档

## 写在前面

最近 AI 编程工具越来越火，从AI Coding到Vibe Coding，发展速度快得让人目不暇接。

说实话，我体验过不少 AI 编程助手，但 Claude Code 给我的感觉更好一些——**它是一个真正能「干活」的 AI 程序员**。

话不多说，开干！

> **环境说明**
> 操作系统：Windows 11 / macOS / Linux 均可
> 前置依赖：Node.js 16+（npm 安装方式）
> Claude Code 版本：写作时最新版
>
> 我这里使用Windows 11进行演示

---

## 一、什么是 Claude Code

Claude Code 是 Anthropic 推出的一款**命令行 AI 编程工具**，它可以直接在你的终端中运行，深度理解你的代码库，帮助你完成代码编写、调试、重构、代码审查等一系列开发任务。

与传统的代码补全工具不同，Claude Code 是一个**代理式（Agentic）编程助手**——它不仅仅是补全代码，而是可以像一名真正的开发者一样，理解你的需求，自主规划任务，调用各种工具（读写文件、执行命令、搜索代码等），一步步完成复杂的开发工作。

简单来说，你可以把它理解为：**一个住在你终端里的 AI 程序员**。

![Claude Code 概念图](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-01.png?xiaoxiaofeng)

---

## 二、Claude Code 能做什么

在开始安装之前，我们先来看看 Claude Code 到底能帮我们干什么：

* **代码生成与重构**：用自然语言描述需求，Claude Code 帮你生成完整代码
* **代码审查**：自动审查代码变更，发现潜在 bug 和优化点
* **调试辅助**：分析错误日志，定位问题根因
* **项目初始化**：一键生成 CLAUDE.md 项目文档
* **自动化工作流**：通过 Hooks 和 Slash Commands 实现自定义自动化
* **多工具协同**：内置文件读写、命令执行、网页搜索等工具，并支持 MCP 协议扩展
* **Git 工作树隔离**：在隔离的工作空间中安全地进行代码修改

可以看到，它的能力覆盖了开发的方方面面，从写代码到查 bug，从项目初始化到自动化流程，基本上一站式搞定了。

![Claude Code 功能概览](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-02.png?xiaoxiaofeng)

---

## 三、下载与安装

Claude Code 目前支持 Windows、macOS 和 Linux 三大平台，提供多种安装方式。我个人推荐 npm 安装，最省心。

### 3.1 方式一：npm 安装（推荐）

这是最通用的安装方式，前提是你的电脑已经安装了 Node.js 16 及以上版本。

```bash
# 全局安装 Claude Code
npm install -g @anthropic-ai/claude-code

# 验证安装是否成功
claude --version
```

如果你还没有安装 Node.js，可以先去 [Node.js 官网](https://nodejs.org/) 下载安装，无脑一路下一步就好。

![npm 安装 Claude Code](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-03.png?xiaoxiaofeng)

### 3.2 方式二：Homebrew 安装（macOS / Linux）

如果你用的是 macOS 或 Linux，用 Homebrew 也很方便：

```bash
# 添加 Anthropic 的 tap
brew tap anthropics/tap

# 安装 Claude Code
brew install claude-code

# 验证安装
claude --version
```

![Homebrew 安装 Claude Code](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-04.png?xiaoxiaofeng)

### 3.3 方式三：pip 安装

如果你更习惯 Python 生态，pip 也能装：

```bash
# 通过 pip 安装
pip install anthropic-claude-code

# 验证安装
claude --version
```

### 3.4 方式四：直接下载二进制文件

你也可以从 [Claude Code GitHub Releases](https://github.com/anthropics/claude-code/releases) 页面直接下载对应平台的二进制文件，解压后把可执行文件所在目录加到系统 PATH 里就能用了。

![直接下载二进制文件](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-05.png?xiaoxiaofeng)

### 3.5 Windows 用户特别说明

Windows 用户推荐用 npm 方式安装，装好后在 PowerShell 或 CMD 中直接敲 `claude` 就行。如果遇到执行策略问题，以管理员身份打开 PowerShell 执行：

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

---

## 四、初始化配置

安装完成后，还需要简单配置一下才能愉快地使用。

### 4.1 获取 API Key

Claude Code 需要 Anthropic 的 API Key 才能工作。获取步骤如下：

1. 访问 [Anthropic Console](https://console.anthropic.com/)
2. 注册或登录你的 Anthropic 账号
3. 在 API Keys 页面创建一个新的 API Key
4. 复制并妥善保存你的 API Key

![获取 API Key](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-06.png?xiaoxiaofeng)

> **注意**：API Key 只会显示一次，一定要保存好！丢了就只能重新生成了。

### 4.2 配置 API Key

有几种配置方式，选你喜欢的就行：

**【方式一】环境变量（推荐）**

```bash
# Linux / macOS
export ANTHROPIC_API_KEY=your-api-key-here

# Windows PowerShell
$env:ANTHROPIC_API_KEY = "your-api-key-here"
```

建议把环境变量加到你的 shell 配置文件里（比如 `.bashrc`、`.zshrc` 或 Windows 系统环境变量），这样每次打开终端都会自动生效，懒人必备。

**【方式二】配置文件**

Claude Code 的配置文件在 `~/.claude/settings.json`，直接在里面设置：

```json
{
  "apiKey": "your-api-key-here",
  "model": "claude-sonnet-4-6"
}
```

### 4.3 首次启动

配置好 API Key 后，终端里敲 `claude` 就能启动交互式会话了：

```bash
claude
```

首次启动时，Claude Code 会引导你完成基础设置，包括选择默认模型、配置权限之类的，跟着提示走就行。

![首次启动 Claude Code](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-07.png?xiaoxiaofeng)

---

## 五、基本使用

### 5.1 在项目里开始对话

Claude Code 最核心的用法就是在你的项目目录里启动它：

```bash
# 进入你的项目目录
cd my-project

# 启动 Claude Code
claude
```

启动后，你就可以用自然语言跟它对话了。它会自动读取你项目里的文件结构，理解你的代码库，主打的就是一个字，**快**！

![Claude Code 交互界面](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-08.png?xiaoxiaofeng)

### 5.2 常用场景示例

光说理论没意思，咱们直接上手看几个常见场景：

**场景一：让 Claude Code 帮你写代码**

```
帮我写一个用户登录接口，使用 JWT 认证，包含登录、刷新 Token、登出三个功能。
```

**场景二：代码审查**

```
帮我审查一下最近修改的代码，看看有没有潜在的问题。
```

**场景三：调试错误**

```
我的应用启动时报了这个错：Error: connect ECONNREFUSED 127.0.0.1:5432，帮我分析一下原因。
```

**场景四：重构代码**

```
帮我把这个 UserService 类拆分一下，现在它太臃肿了，单一职责原则。
```

说实话，这几个场景基本覆盖了日常开发 80% 的需求，效果还是相当不错的。

![Claude Code 使用示例](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-09.png?xiaoxiaofeng)

### 5.3 Slash Commands（斜杠命令）

Claude Code 内置了一系列斜杠命令，快速执行常见操作：

| 命令 | 说明 |
|------|------|
| `/init` | 为当前项目初始化 CLAUDE.md 文档 |
| `/clear` | 清除当前对话历史 |
| `/compact` | 压缩上下文窗口，释放空间 |
| `/review` | 审查当前代码变更 |
| `/run` | 启动并测试你的应用 |
| `/config` | 打开配置编辑器 |
| `/help` | 查看帮助信息 |

![Slash Commands 使用](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-10.png?xiaoxiaofeng)

---

## 六、进阶功能

### 6.1 CLAUDE.md —— 项目的「说明书」

CLAUDE.md 是 Claude Code 里一个非常重要的概念。它是一个放在项目根目录下的 Markdown 文件，用来告诉 Claude Code 关于你项目的关键信息：

* 项目是做什么的
* 技术栈是什么
* 代码规范和约定
* 常用的命令
* 项目的目录结构说明

你可以手动创建 CLAUDE.md，也可以用 `/init` 命令让 Claude Code 自动分析项目生成，懒人福音有没有。

```markdown
# CLAUDE.md

## 项目简介
这是一个基于 Spring Boot 的电商后端服务。

## 技术栈
- Java 17
- Spring Boot 3.x
- MyBatis Plus
- MySQL 8.0
- Redis

## 编码规范
- 使用 Lombok 简化代码
- Controller 层不做业务逻辑
- 统一使用 Result<T> 作为返回格式
```

有了 CLAUDE.md，Claude Code 理解你项目的时候会更加精准，强烈建议搞一个。

![CLAUDE.md 示例](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-11.png?xiaoxiaofeng)

### 6.2 Hooks —— 自动化工作流

Hooks 让你可以在 Claude Code 的特定事件前后自动执行脚本，比如代码提交前自动跑 lint 和测试：

```json
{
  "hooks": {
    "pre-commit": "npm run lint && npm run test",
    "post-command": "echo '任务执行完毕'"
  }
}
```

常见的 Hook 事件包括：命令执行前/后、文件修改前/后、会话开始/结束等，灵活度很高。

### 6.3 MCP 协议 —— 扩展工具能力

MCP（Model Context Protocol）是 Anthropic 推出的开放协议，允许 Claude Code 连接外部的工具服务器。通过 MCP，你可以让 Claude Code 访问数据库、调用第三方 API、操作浏览器等等，**大大扩展了它的能力边界**。

配置 MCP 服务器示例：

```json
{
  "mcpServers": {
    "my-database": {
      "command": "npx",
      "args": ["-y", "@anthropic/mcp-server-postgres"],
      "env": {
        "DATABASE_URL": "postgresql://localhost/mydb"
      }
    }
  }
}
```

![MCP 协议架构图](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-12.png?xiaoxiaofeng)

### 6.4 Worktree —— 安全的代码隔离

当 Claude Code 需要大规模修改代码时，它可以自动创建 Git Worktree——一个独立的工作目录。这样做的好处是：

* 修改不会影响你当前的工作区
* 可以安全地尝试激进的改动
* 如果结果不满意，直接删除 Worktree 就行，不会留下任何痕迹

```bash
# Claude Code 会自动执行类似以下操作
git worktree add ../my-project-feature feature-branch
```

---

## 七、权限管理

Claude Code 在执行文件读写、命令运行等操作时，会根据你的权限配置来决定是直接执行还是需要你确认。

权限配置在 `~/.claude/settings.json` 中：

```json
{
  "permissions": {
    "allow": [
      "Read(*)",
      "Bash(npm:*)",
      "Bash(git:*)"
    ],
    "deny": [
      "Read(/etc/passwd)",
      "Bash(rm:*)"
    ]
  }
}
```

权限规则支持通配符，你可以灵活地控制 Claude Code 能做什么、不能做什么，安全性还是有保障的。

![权限管理配置](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-13.png?xiaoxiaofeng)

---

## 八、IDE 集成

除了命令行，Claude Code 还支持集成到主流 IDE 中。

### 8.1 VS Code

在 VS Code 扩展市场搜索 "Claude Code" 就能安装官方扩展。装好之后你可以在 VS Code 里直接使用 Claude Code 的聊天界面、内联代码建议，一键执行命令，体验很丝滑。

![VS Code 集成](https://image.xiaoxiaofeng.site/blog/image/image-claude-code-14.png?xiaoxiaofeng)

### 8.2 JetBrains IDE

在 JetBrains 插件市场搜索 "Claude Code" 安装官方插件，支持 IntelliJ IDEA、PyCharm、WebStorm 等主流 JetBrains IDE。Java 开发者的福音～

---

## 九、模型选择与费用

Claude Code 支持多种 Claude 模型，可以根据任务复杂度和预算来选：

| 模型 | 特点 | 适用场景 |
|------|------|----------|
| **Claude Haiku** | 速度最快，成本最低 | 简单的代码补全、格式化、注释生成 |
| **Claude Sonnet** | 速度与能力均衡 | 日常编码、代码审查、重构 |
| **Claude Opus** | 能力最强 | 复杂架构设计、疑难 bug 调试、大型重构 |

在配置文件中设置默认模型：

```json
{
  "model": "claude-sonnet-4-6"
}
```

> **提示**：Claude Code 采用按量付费模式，具体价格请以 [Anthropic 官方定价页](https://www.anthropic.com/pricing) 为准。

---

## 十、常见问题

**Q1：Claude Code 和 GitHub Copilot 有什么区别？**

Copilot 主要是代码补全工具，在你编码时提供内联建议。Claude Code 则是一个完整的代理式 AI 程序员，它能理解整个项目上下文，自主规划和执行复杂的多步骤任务。两者定位不太一样。

**Q2：Claude Code 支持中文吗？**

支持。你可以用中文跟 Claude Code 对话，它也能理解中文注释和文档，完全没问题。

**Q3：Claude Code 能处理多大的项目？**

Claude Code 可以处理大型项目，它智能地管理上下文窗口，只加载相关的代码到上下文中。对于特别大的项目，建议写好 CLAUDE.md 来帮它更好地理解项目结构。

**Q4：国内用户怎么用？**

国内用户可以直接使用（Anthropic API 目前在国内可以访问），但如果遇到网络不稳定的情况，可以考虑用代理或第三方 API 中转服务。

---

## 写在最后

感谢你阅读到这里，我是**笑小枫**。💙

Claude Code 整体体验下来，给我的感觉就是——**它不再只是一个被动的代码补全工具，而是一个能主动理解、规划、执行的 AI 编程搭档**。从代码生成到审查调试，从项目初始化到自动化工作流，基本覆盖了日常开发的全流程。

当然，它也不是完美的，比如网络问题对国内用户偶尔会有影响，大项目的上下文理解有时候也需要 CLAUDE.md 来辅助。但瑕不掩瑜，**它绝对值得你花几分钟装起来试一试**。

每一篇文章都是我用心整理和思考的结果，希望能对你有所帮助。如果觉得内容还不错，欢迎点赞、收藏、转发支持一下～

当然，技术这条路，一个人走可能会很快，但一群人走才能走得更远。如果你有任何问题、想法，或者单纯想交个朋友，都可以加我微信一起交流学习：

📱 **微信：q1150640979**

也欢迎常来我的小站坐坐：

🌐 **个人网站：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com/)**

我会在那里持续分享更多技术干货与成长心得。

**保持热爱，奔赴山海。我们下篇文章见～** 🚀

—— 笑小枫

---

> 本文写于 2026 年 6 月，Claude Code 版本可能已有更新，请以官方文档为准。
