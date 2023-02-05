## 笑小枫💕

> 欢迎来到笑小枫的世界，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)
>



## 一、前言 ##

最近新买了一台电脑，电脑空荡荡的，啥都得重头装，记录一下 Git 的安装过程，温习温习。  


## 二、Git 的安装 ##

### 2.1 Git 的下载 ###

这个就需要去 Git 官网下载对应系统的软件了，下载地址为 [git-scm.com](https://git-scm.com/)或者[gitforwindows.org](https://gitforwindows.org/)
`上面的 git-scm 是 Git 的官方，里面有不同系统不同平台的安装包和源代码，而 gitforwindows.org 里只有 windows 系统的安装包`  

  


### 2.2 Git 的安装 ###

我下载的版本是 `Git-2.31.1-64-bit.exe` `Git-2.35.1.2-64-bit.exe`，接下来我们就对这个版本进行安装工作。  



#### 2.2.1 使用许可声明 ####

双击下载后的 `Git-2.31.1-64-bit.exe` `Git-2.35.1.2-64-bit.exe`，开始安装，这个界面主要展示了 GPL 第 2 版协议1的内容，点击 \[next\] 到第二步。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328868310.png)

  


#### 2.2.2 选择安装目录 ####

可点击 “Browse…” 更换目录，也可直接在方框里面改，我一般直接将 “C” 改为 “D”，这样就直接安装在 D 盘里了。点击 \[next\] 到第三步。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328869892.png)

 


#### 2.2.3 选择安装组件 ####

图中这些英文都比较简单，我已经把大概意思翻译出来了，大家根据自己的需要选择勾选。点击 \[next\] 到第四步。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328870540.png)  
`注：最后一个选项打勾的话，需要下载 Windows Terminal 配合 Git Bash使用`，如图：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328871088.png)

  


#### 2.2.4 选择开始菜单文件夹 ####

方框内 Git 可改为其他名字，也可点击 “`Browse...`” 选择其他文件夹或者给"`Don't create a Start Menu folder`" 打勾不要文件夹，点击 \[next\] 到第五步。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328871946.png)

安装成功后在开始菜单里的图如下：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328872500.png)

  


#### 2.2.5 选择 Git 默认编辑器 ####

Git 安装程序里面内置了 10 种编辑器供你挑选，比如 `Atom`、`Notepad`、`Notepad++`、`Sublime Text`、`Visual Studio Code`、`Vim` 等等，默认的是 Vim ，选择 Vim 后可以直接进行到下一步，但是 Vim 是纯命令行，操作有点难度，需要学习。如果选其他编辑器，则还需要去其官网安装后才能进行下一步。  


下图为默认编辑器 `Vim`.可直接点击 \[next\] 到第六步。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328873575.png)

如果你不想用 `Vim` 当默认编辑器，换一个，比如 `Notepad++` ，那么你者需要点击下面的蓝色字体 " Notepad++ " 去其官网下载安装好才能进行下一步 \[next\].  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328874253.png)

安装后还要配置在`我的电脑->属性->高级系统设置->高级->环境变量->系统变量->Path->编辑添加` Notepad++ 的安装地址，如 **`C:\Program Files\notepad++`**.  
这样才能在 Git Bash 里面直接调用 Notepad++.

    $ notepad++ 文件名.后缀  //在 git bash 调用 notepad++ 打开文件

新手建议使用 `Notepad++` 、`Sublime Text`，这两个比 Windows 自带的记事本功能多太多了。点击 \[next\] 到第六步。

 


#### 2.2.6 决定初始化新项目(仓库)的主干名字 ####

第一种是让 Git 自己选择，名字是 `master` ，但是未来也有可能会改为其他名字；第二种是我们自行决定，默认是 `main`，当然，你也可以改为其他的名字。一般默认第一种，点击 \[next\] 到第七步。

`注： 第二个选项下面有个 NEW！ ，说很多团队已经重命名他们的默认主干名为 main . 这是因为2020 年非裔男子乔治·弗洛伊德因白人警察暴力执法惨死而掀起的 Black Lives Matter(黑人的命也是命)运动，很多人认为 master 不尊重黑人，呼吁改为 main.`

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328874925.png)

  


#### 2.2.7 调整你的 path 环境变量 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328875586.png)

翻译如下：

    Use Git from Git Bash only 
    This is the most cautious choice as your PATH will not be modified at all. You w only be able to use the Git command line tools from Git Bash.
    仅从 Git Bash 使用 Git
    这是最谨慎的选择，因为您的 PATH 根本不会被修改。您将只能使用 Git Bash 中的 Git 命令行工具。
    Git from the command line and also from 3rd-party software
    (Recommended) This option adds only some minimal Git wrappers to your PATH to avoid cluttering your environment with optional Unix tools.
    You will be able to use Git from Git Bash, the Command Prompt and the Windov PowerShell as well as any third-party software looking for Git in PATH.
    从命令行以及第三方软件进行 Git
    （推荐）此选项仅将一些最小的 Git 包装器添加到PATH中，以避免使用可选的 Unix 工具使环境混乱。
    您将能够使用 Git Bash 中的 Git，命令提示符和 Windov PowerShell 以及在 PATH 中寻找 Git 的任何第三方软件。
    Use Git and optional Unix tools from the Command Prompt 
    Both Git and the optional Unix tools will be added to your PATH.
    Warning: This will override Windows tools like "find"and "sort". Only use this option if you understand the implications.
    使用命令提示符中的 Git 和可选的 Unix 工具
    Git 和可选的 Unix 工具都将添加到您的 PATH 中。
    警告：这将覆盖 Windows 工具，例如 "find" and "sort". 仅在了解其含义后使用此选项。

第一种是`仅从 Git Bash 使用 Git`。这个的意思就是你只能通过 Git 安装后的 Git Bash 来使用 Git ，其他的什么命令提示符啊等第三方软件都不行。

第二种是`从命令行以及第三方软件进行 Git`。这个就是在第一种基础上进行第三方支持，你将能够从 `Git Bash`，`命令提示符(cmd)` 和 `Windows PowerShell` 以及`可以从 Windows 系统环境变量中寻找 Git` 的任何第三方软件中使用 Git。推荐使用这个。

第三种是`从命令提示符使用 Git 和可选的 Unix 工具`。选择这种将覆盖 Windows 工具，如 “ find 和 sort ”。只有在了解其含义后才使用此选项。一句话，适合比较懂的人折腾。

 


#### 2.2.8 选择 SSH 执行文件 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328876413.png)  
翻译如下：
~~~
    Use bundled OpenSSH 
    This uses ssh. exe that comes with Git.
    使用捆绑的 OpenSSH
    这使用的 ssh.exe 是 Git 自带的 
    Use (Tortoise) Plink 
    To use PuTTY, specify the path to an existing copy of (Tortoise) Plink.exe
    Set ssh. variant for Tortoise Plink 
    使用 TortoisePlink (注，这是一个软件)
    要使用 PuTTY，请指定 TortoisePlink.exe 的现有副本的路径
    为 TortoisePlink 设置 ssh.variant
    
    Use external OpenSSH 
    NEW! This uses an external ssh. exe. Git will not install its own OpenSSH
    (and related) binaries but use them as found on the PATH.
    使用外部 OpenSSH
    新！这使用外部 ssh.exe 文件。 
    Git 不会安装自己的 OpenSSH（和相关）二进制文件，而是使用在环境变量 PATH 中找到的它们。
~~~

`注：这是一个新功能，我 2021-4-17 安装的 2.31.1 版本并没有这个选项，先按默认的来吧，先填个坑，有机会再补充`

 


#### 2.2.9 选择HTTPS后端传输 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328877096.png)

翻译如下：
~~~
    use the OpenSSL library 
    Server certificates will be validated using the ca-bundle. crt file.
    使用 OpenSSL 库
    服务器证书将使用 ca-bundle.crt 文件进行验证。
    	
    Use the native Windows Secure Channel library 
    Server certificates will be validated using Windows Certificate Stores.
    This option also allows you to use your company's internal Root CA certificates distributed e.g. via Active Directory Domain Services.
    使用本机 Windows 安全通道库
    服务器证书将使用 Windows 证书存储进行验证。
    此选项还允许您使用公司内部分发的内部根 CA 证书，例如通过 Active Directory 域服务。
~~~

这两种选项有什么区别呢？

来自[https://stackoverflow.com/questions/62456484/whats-the-difference-between-openssl-and-the-native-windows-secure-channel-libr](https_stackoverflow.com_questions_62456484_whats-the-difference-between-openssl-and-the-native-windows-secure-channel-libr)

> 如果在具有企业管理证书的组织中使用 Git，则将需要使用安全通道。如果你仅使用 Git 来访问公共存储库（例如 GitHub ），或者你的组织不管理自己的证书，那么使用 SSL 后端（它们只是同一协议的不同实现）就可以了。

也就是说，作为普通用户，只是用 Git 来访问 Github、GitLab 等网站，选择前者就行了。点击 \[next\] 到第十步。

  


#### 2.2.10 配置行尾符号转换 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328877702.png)
~~~
    Checkout Windows-style, commit Unix-style line endings 
    Git will convert LF to CRLF when checking out text files. 
    When committing text files, CRLF will be converted to LF. For cross-platform projects, this is the recommended setting on Windows("core. autocrif"is set to "true").
    签出 Windows 样式，提交 Unix 样式的行结尾
    Git 签出文本文件时，会将 LF 转换为 CRLF。
    提交文本文件时，CRLF 将转换为 LF。
    对于跨平台项目，这是 Windows 上的建议设置（"core.autocrif" 设置为 "true"）。
    
    Checkout as-is, commit Unix-style line endings 
    Git will not perform any conversion when checking out text files. 
    When committing text files, CRLF will be converted to LF. For cross-platform projects, this is the recommended setting on Unix("core.autocrif" is set to "input").
    按原样签出，提交 Unix 样式的行结尾
    Git 在签出文本文件时不会执行任何转换。提交文本文件时，CRLF 将转换为 LF。
    对于跨平台项目，这是在 Unix 上的建议设置（"core.autocrif" 设置为 "input"）。
    
    Checkout as-is, commit as-is 
    Git will not perform any conversions when checking out or committing text files. 
    Choosing this option is not recommended for cross-platform projects("core. autocrif"is set to "false").
    按原样签出，按原样提交
    Git 在签出或提交文本文件时不会执行任何转换。
    不建议跨平台项目选择此选项（"core.autocrif" 设置为 "false"）。
~~~
这三种选择分别是：  
`签出 Windows 样式，提交 Unix 样式的行结尾。`  
`按原样签出，提交Unix样式的行结尾。`  
`按原样签出，按原样提交。`  


那 Windows 样式和 Unix 样式到底有什么区别呢？

引用 《[GitHub 入门与实践][GitHub]》 第 50 页内容2

> GitHub 中公开的代码大部分都是以 Mac 或 Linux 中的 LF（Line Feed）换行。然而，由于 Windows 中是以 CRLF（Carriage Return+ Line Feed）换行的，所以在非对应的编辑器中将不能正常显示。  
>   
> Git 可以通过设置自动转换这些换行符。使用 Windows 环境的各位，请选择推荐的 “Checkout Windows-style，commit Unix-style line endings” 选项。换行符在签出时会自动转换为 CRLF，在提交时则会自动转换为 LF .

上面说 Mac 、Linux、Unix 的 Line Feed ，翻译过来就是换行符，用 “\\n” 表示，换行符 “\\n” 的 ASCII 值为10；  
Windows 的是 Carriage Return+ Line Feed（回车+换行），用 “\\r\\n” 表示，回车符 “\\r” 的 ASCII 值为13；

这上下两者是不一样的。  
所以这就需要转换了，至于为什么选第一项？  
这还用问吗？`我们现在的教程就是介绍怎么安装 Windows 版 Git，肯定选第一项啦。`

至于 “回车”（carriage return）和 “换行”（line feed）这两个概念的来历和区别？  
引用一下 [阮一峰老师博客的部分内容][Link 1]

> 在计算机还没有出现之前，有一种叫做电传打字机（Teletype Model 33）的玩意，每秒钟可以打 10 个字符。但是它有一个问题，就是打字机打完一行换行的时候，要用去 0.2 秒，正好可以打两个字符。要是在这 0.2 秒里面，又有新的字符传过来，那么这个字符将丢失。  
> 于是，研制人员想了个办法解决这个问题，就是在每行后面加两个表示结束的字符。一个叫做 "回车"，告诉打字机把打印头定位在左边界；另一个叫做 "换行"，告诉打字机把纸向下移一行。

更多资料参考：

1.  [腾讯云 - 换行符 ‘\\n’ 和 回车符 ‘\\r’ 的区别？](https://cloud.tencent.com/developer/article/1353286)
2.  [知乎 - 为什么会用 \\r\\n 两个字符表示换行？](https://www.zhihu.com/question/29326647)
3.  [Stackoverflow - What are carriage return, linefeed, and form feed?](https://stackoverflow.com/questions/3091524/what-are-carriage-return-linefeed-and-form-feed)

点击 \[next\] 到第十一步。

  


#### 2.2.11 配置终端模拟器以与 Git Bash 一起使用 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328878339.png)
~~~
    Use MinTTY(the default terminal of MSYS2) 
    Git Bash will use MinTTY as terminal emulator, which sports a resizable window
    non-rectangular selections and a Unicode font.Windows console programs(such
    as interactive Python) must be launched via 'winpty' to work in MinTTY.
    使用 MinTTY（MSYS2的默认终端）
    Git Bash 将使用 MinTTY 作为终端仿真器，该仿真器具有可调整大小的窗口非矩形选择和 Unicode 字体。
    Windows 控制台程序（例如交互式 Python）必须通过 "winpty" 启动才能在 MinTTY 中运行。
    
    Use Windows' default console 
    window Git will use the default console window of Windows("cmd.exe"), which works v
    with Win32 console programs such as interactive Python or node. js, but has a
    very limited default scroll-back,needs to be configured to use a Unicode font in 
    order to display non-ASCII characters correctly, and prior to Windows 10 its 
    window was not freely resizable and it only allowed rectangular text selections.<br>
    使用 Windows 的默认控制台窗口
    Git 将使用 Windows 的默认控制台窗口（"cmd.exe"），该窗口可与 Win32 控制台程序（例如交互式Python 或 
    node.js）一起使用，但默认回滚非常有限，需要将其配置为使用 Unicode 字体才能正确显示非 ASCII 字符，并且在 
    Windows 10 之前，其窗口不可随意调整大小，并且仅允许选择矩形文本。
~~~
建议选择第一种，MinTTY 3功能比 cmd 多，cmd 只不过 比 MinTTY 更适合处理 Windows 的一些接口问题，这个对 Git 用处不大，除此之外 Windows 的默认控制台窗口（`cmd`）有很多劣势，比如 cmd 具有非常有限的默认历史记录回滚堆栈和糟糕的字体编码等等。  
相比之下，MinTTY 具有可调整大小的窗口和其他有用的可配置选项，可以通过右键单击的工具栏来打开它们 git-bash 。点击 \[next\] 到第十二步。

  


#### 2.2.12 选择默认的 “git pull” 行为 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328878924.png)
~~~
    ODefault(fast-forward or merge)
    This is the standard behavior ofgit pull": fast-forward the current branch to 
    the fetched branch when possible, otherwise create a merge commit.
    默认（快进或合并）
    这是 "git pull" 的标准行为：在可能的情况下将 当前分支 快进到 获取的分支，否则创建合并提交。
    
    ORebase Rebase the current branch onto the fetched branch. If there are no local 
    commits to rebase, this is equivalent to a fast-forward.
    变基将当前分支变基到获取的分支上。如果没有本地提交要变基，则等同于快进。
    
    Oonly ever fast-forward 
    Fast-forward to the fetched branch. Fail if that is not possible.
    只能快进快进到获取的分支。如果不可能，则失败。
~~~
“git pull” 是什么意思呢？  
git pull 就是获取最新的远程仓库分支到本地，并与本地分支合并

上面给了三个 “git pull” 的行为：  
第一个是 `merge`  
第二个是 `rebase`  
第三个是 `直接获取`

第一种 `git pull = git fetch + git merge`  
第二种 `git pull = git fetch + git rebase`  
第三种 `git pull = git fetch` ？(这个没试过，纯属猜测

一般默认选择第一项，`git rebase` 绝大部分程序员都用不好或者不懂，而且风险很大，但是很多会用的人也很推崇，但是用不好就是灾难。

git pull 只是拉取远程分支并与本地分支合并，而 git fetch 只是拉取远程分支，怎么合并，选择 merge 还是 rebase ，可以再做选择。


#### 2.2.13 选择一个凭证帮助程序 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328879566.png)

翻译如下：
~~~
    Git Credential Manager 
    Use the cross-platform Git Credential Manager.
    See more information about the future of Git Credential Manager here.
    Git 凭证管理
    使用跨平台的 Git  凭证管理。
    在此处查看有关 Git 凭证管理未来的更多信息。
    
    None 
    Do not use a credential helper.
    不使用凭证助手。
~~~
一共两个选项：  
`Git 凭证管理`  
`不使用凭证助手`

第一个选项是提供`登录凭证`帮助的，Git 有时需要用户的凭据才能执行操作；例如，可能需要输入`用户名`和`密码`才能通过 HTTP 访问远程存储库（GitHub，GItLab 等等）。

登录图如下(属于第一个选项的，老图了)，来自[https://segmentfault.com/q/1010000011171685](https://segmentfault.com/q/1010000011171685)  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328880371.png)


点击 \[next\] 进到十四步。




#### 2.2.14 配置额外的选项 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328881012.png)

翻译如下：
~~~
    Enable file system caching 
    File system data will be read in bulk and cached in memory for certain operations("core.fscache" is set to "true"). 
    This provides a significant performance boost.
    启用文件系统缓存
    将批量读取文件系统数据并将其缓存在内存中以进行某些操作（"core.fscache” 设置为 "true"）。
    这可以显着提高性能。


    
    Enable symbolic links 
    Enable symbolic links(requires the SeCreateSymbolicLink permission).
    Please note that existing repositories are unaffected by this setting.
    启用符号链接
    启用符号链接（需要SeCreateSymbolicLink权限）。
    请注意，现有存储库不受此设置的影响。
~~~
有两个选项：  
`启用文件系统缓存`  
`启用符号链接`

`启用文件系统缓存`就是将批量读取文件系统数据并将其缓存在内存中以进行某些操作，可以显著提升性能。这个选项默认开启。  
`启用符号链接` ，符号链接是一类特殊的文件， 其包含有一条以绝对路径或者相对路径的形式指向其它文件或者目录的`引用`，类似于 Windows 的快捷方式，不完全等同 类Unix（如 Linux） 下的 符号链接。因为该功能的支持需要一些[条件][Link 2]，所以默认不开启。  


点击 \[next\] 到第十五步。

 


#### 2.2.15 配置实验性选项 ####

`截图忘截了，我太难了，谷歌找张图顶替一下`  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328881795.png)

翻译如下：
~~~
    Enable experimental support for pseudo consoles.
    (NEW!) This allows running native console programs like Node or Python in a Git Bash window without using winpty, 
    but it still has known bugs.
    启用对伪控制台的实验性支持。
    (新功能!) 这允许在不使用 winpty 的情况下在 Git Bash 窗口中运行诸如 Node 或 Python 之类的本机控制台程序，
    但是它仍然存在已知的 bug。
    
    Enable experimental built-in file system monitor
    (NEW!) Automatically run a built-in file system watcher, to speed up common operations such as ' git status', ' git add', ' git commit', etc in worktrees containing many files.
    启用实验性内置文件系统监视器
    （新！）自动运行内置文件系统监视器，以加快包含许多文件的工作树中的常见操作，例如 'git status'、'git add'、'git commit' 等.
~~~
这是实验性功能，可能会有一些小错误之类的，建议不用开启。  
点击 \[install\] 进行安装。  

`安装成功`  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328882627.png)

  


### 2.3 Git 的功能介绍 ###

这是安装成功后开始菜单里面的图。  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328883211.png)  
有 `Git Bash`、`Git CMD`、`Git FAQs`、`Git GUI`、`Git Release Note`，下面我们就分别介绍一下这几个。



#### 2.3.1 Git Bash ####

`Git Bash` 是基于CMD的，在CMD的基础上增添一些新的命令与功能，平时主要用这个，功能很丰富，长这样：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328883826.png)

  


#### 2.3.2 Git CMD ####

`Git CMD` 不能说和 cmd 完全一样，只能说一模一样，功能少得可怜，两者如下图：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328884323.png)

   


#### 2.3.3 Git FAQs ####

`Git FAQs` 就是 Git Frequently Asked Questions（常问问题），访问地址：[https://github.com/git-for-windows/git/wiki/FAQ][https_github.com_git-for-windows_git_wiki_FAQ]

  


#### 2.3.4 Git GUI ####

`Git GUI` 就是 Git 的图形化界面，如下图：  
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/13/xxf-1668328884920.png)  
可以通过它快速`创建新仓库（项目），克隆存在的仓库（项目），打开存在的仓库（仓库）`。




#### 2.3.4 Git Release Note ####

`Git Release Note` 就是版本说明，增加了什么功能，修复了什么 bug 之类的。


## Git常用操作的命令

### 常用
~~~
$ git remote add origin git@github.com:yeszao/dofiler.git         # 配置远程git版本库
$ git pull origin master                                          # 下载代码及快速合并 
$ git push origin master                                          # 上传代码及快速合并
$ git fetch origin                                                # 从远程库获取代码

$ git branch                                                      # 显示所有分支
$ git checkout master                                             # 切换到master分支
$ git checkout -b dev                                             # 创建并切换到dev分支
$ git commit -m "first version"                                   # 提交
$ git branch -u 远程分支 本地分支                                    # 将本地dev和远程仓库的origin/dev连接起来
$ git branch -u origin/dev dev                                    # 将本地dev和远程仓库的origin/dev连接起来
$ git status                                                      # 查看状态
$ git log                                                         # 查看提交历史

$ git config --global core.editor vim                             # 设置默认编辑器为vim（git默认用nano）
$ git config core.ignorecase false                                # 设置大小写敏感
$ git config --global user.name "YOUR NAME"                       # 设置用户名
$ git config --global user.email "YOUR EMAIL ADDRESS"             # 设置邮箱
~~~

### 本地生成ssh keys命令
```ssh-keygen -t rsa```


### 别名Alias
~~~
$ git config --global alias.br="branch"                 # 创建/查看本地分支
$ git config --global alias.co="checkout"               # 切换分支
$ git config --global alias.cb="checkout -b"            # 创建并切换到新分支
$ git config --global alias.cm="commit -m"              # 提交
$ git config --global alias.st="status"                 # 查看状态
$ git config --global alias.pullm="pull origin master"  # 拉取分支
$ git config --global alias.pushm="push origin master"  # 提交分支
$ git config --global alias.log="git log --oneline --graph --decorate --color=always" # 单行、分颜色显示记录
$ git config --global alias.logg="git log --graph --all --format=format:'%C(bold blue)%h%C(reset) - %C(bold green)(%ar)%C(reset) %C(white)%s%C(reset) %C(bold white)— %an%C(reset)%C(bold yellow)%d%C(reset)' --abbrev-commit --date=relative" # 复杂显示
~~~

### 创建版本库
~~~
$ git clone <url>                 # 克隆远程版本库
$ git init                        # 初始化本地版本库
~~~

### 修改和提交
~~~
$ git status                      # 查看状态
$ git diff                        # 查看变更内容
$ git add .                       # 跟踪所有改动过的文件
$ git add <file>                  # 跟踪指定的文件
$ git mv <old> <new>              # 文件改名
$ git rm <file>                   # 删除文件
$ git rm --cached <file>          # 停止跟踪文件但不删除
$ git commit -m “commit message”  # 提交所有更新过的文件
$ git commit --amend              # 修改最后一次提交
~~~

### 查看提交历史
~~~
$ git log                         # 查看提交历史
$ git log -p <file>               # 查看指定文件的提交历史
$ git blame <file>                # 以列表方式查看指定文件的提交历史
~~~

### 撤消
~~~
$ git reset --hard HEAD           # 撤消工作目录中所有未提交文件的修改内容
$ git reset --hard <version>      # 撤销到某个特定版本
$ git checkout HEAD <file>        # 撤消指定的未提交文件的修改内容
$ git checkout -- <file>          # 同上一个命令
$ git revert <commit>             # 撤消指定的提交
~~~

### 撤销commit
~~~
git reset --soft HEAD~1

## 如果进行了2次commit，想都撤回，可以使用：
git reset --soft HEAD~2

## ... 以此类推
~~~

git config --unset http.proxy
git config --unset https.proxy

### 撤销add
~~~
## 全部撤销
git reset HEAD

## 指定文件撤销 git reset HEAD <file>，文件名可通过git status命令获取。
git reset HEAD src/main/java/wang/leisure/gitpractice/FirstClass.java
~~~

### 分支与标签
~~~
$ git branch                      # 显示所有本地分支
$ git checkout <branch/tag>       # 切换到指定分支或标签
$ git branch <new-branch>         # 创建新分支
$ git branch -d <branch>          # 删除本地分支
$ git tag                         # 列出所有本地标签
$ git tag <tagname>               # 基于最新提交创建标签
$ git tag -a "v1.0" -m "一些说明"  # -a指定标签名称，-m指定标签说明
$ git tag -d <tagname>            # 删除标签

$ git checkout dev                # 合并特定的commit到dev分支上
$ git cherry-pick 62ecb3
~~~

### 合并与衍合
~~~
$ git merge <branch>              # 合并指定分支到当前分支
$ git merge --abort               # 取消当前合并，重建合并前状态
$ git merge dev -Xtheirs          # 以合并dev分支到当前分支，有冲突则以dev分支为准
$ git rebase <branch>             # 衍合指定分支到当前分支
~~~

### 远程操作
~~~
$ git remote -v                   # 查看远程版本库信息
$ git remote show <remote>        # 查看指定远程版本库信息
$ git remote add <remote> <url>   # 添加远程版本库
$ git remote remove <remote>      # 删除指定的远程版本库
$ git fetch <remote>              # 从远程库获取代码
$ git pull <remote> <branch>      # 下载代码及快速合并
$ git push <remote> <branch>      # 上传代码及快速合并
$ git push <remote> :<branch/tag-name> # 删除远程分支或标签
$ git push --tags                 # 上传所有标签
~~~

### 打包
~~~
$ git archive --format=zip --output ../file.zip master    # 将master分支打包成file.zip文件，保存在上一级目录
$ git archive --format=zip --output ../v1.2.zip v1.2      # 打包v1.2标签的文件，保存在上一级目录v1.2.zip文件中
$ git archive --format=zip v1.2 > ../v1.2.zip             # 作用同上一条命令
~~~
* git打包命令会自动忽略.gitignore中指定的目录和文件，以及.git目录。

### 全局和局部配置
* 全局配置保存在：$Home/.gitconfig
* 本地仓库配置保存在：.git/config

### 远程与本地合并
~~~
$ git init                              # 初始化本地代码仓
$ git add .                             # 添加本地代码
$ git commit -m "add local source"      # 提交本地代码
$ git pull origin master                # 下载远程代码
$ git merge master                      # 合并master分支
$ git push -u origin master             # 上传代码
~~~

### GitLab安装使用

#### 配置yum源

~~~
vim /etc/yum.repos.d/gitlab-ce.repo
~~~

将以下内容复制到上述打开的文件中`/etc/yum.repos.d/gitlab-ce.repo`
~~~
[gitlab-ce]
name=Gitlab CE Repository
baseurl=https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/yum/el$releasever/
gpgcheck=0
enabled=1
~~~

#### 更新本地yum缓存
~~~
yum makecache
~~~
