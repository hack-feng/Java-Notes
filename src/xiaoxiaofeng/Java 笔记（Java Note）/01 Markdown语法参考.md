# 标准 Markdown #

## 目录列表 Table of Contents（TOC） ##

\[TOC\]

## 段落 ##

这个文档自身就是使用 Markdown 编写的。Markdown 允许你通过编写易读、易写的富文本格式，然后很方便就可以转换成有效的 HTML。

## 标题 ##

Markdown 支持两种格式的标题：Settext 和 atx。

Setext-style 标题是用等于号“下划线”（作为一级标题）以及减号作为二级标题。

# 一级标题 #

## 二级标题 ##

atx-style 标题用 1 - 6 个 `#` 在一行的开始，对应于标题等级的 1 - 6 级（尾部的 `#` 是可选的）。

# 一级标题 #

## 二级标题 ##

###### 六级标题 ######

## 标签 ##

一般在文首输入 tags 添加标签，categories 添加分类。备注：分类具有层次性，标签无层次性。

tags:

 *  Markdown
 *  语言

categories:

 *  技术

## 待办事项 Todo 列表 ##

使用带有 \[ \] 或 \[x\]（未完成或已完成）项的列表语法撰写一个待办事项列表

 *  早起跑步
 *  发邮件

## 斜体或粗体 ##

*斜体* **粗体***斜体* **粗体**如果 \* 和 \_ 两边都有空白的话，它们就只会被当成普通的符号

## 网页链接 ##

内联方式（`title` 是可选的）：

This is an [example link][].

引用方式（`title` 是可选的）：

I get 10 times more traffic from [Google][] than from [Yahoo][] or [MSN][].

[MarkdownX 官方网站][MarkdownX]。之后，可以在文档的其他任意地方，定义这个链接。

## Email 链接 ##

作者的 email <spatblan@gmail.com> 链接.

## 图片 ##

内联方式（`title` 是可选的，表示鼠标悬停显示文字）：

 ![笑小枫-www.xiaoxiaofeng.com](https://sdky.gitee.io/path/img.jpg)  图 1 Title

引用方式（`title` 是可选的）：

 ![笑小枫-www.xiaoxiaofeng.com](https://sdky.gitee.io/url/to/img.jpg) 

## 列表 ##

行首插入 1 个 `*` 或 `+` 或 `-` 即可生成无序列表。行首插入 `1.`、`2.`、`3.`、……即可生成有序列表。注意：符号与文本之间必须要有 1 个空格。

有序列表：

 *  列表项 1
 *  列表项 2

无序列表：

1.  列表项 1
2.  列表项 2
3.  列表项 3
4.  列表项 4

列表项缩进两个空格就可以创建一个嵌套的列表：

 *  列表项 1
    
     *  嵌套的列表可以是有序的
     *  格式和正常的有序、无序列表没有差异
 *  列表项 2
    
     *  嵌套的列表可以是无序的
        
         *  这个嵌套的列表项有 4 个空格的缩进，因为它的父列表项自身就带有 2 个空格的缩进
         *  还允许更多层的嵌套
 *  列表项 3

## 引用 ##

> 段落前面添加 `>` 和空格，就能够形成引用段落。
> 
> > 这是嵌套的引用。

## 行内（内联）代码 ##

`内联代码` 使用反引号包含你也可以像 `这样` 转义反引号

## 代码块 ##

每行缩进 4 个空格或者 1 个 Tab：

``````````
这是代码块。
``````````

## 水平分割线 ##

三个或更多的 `*` 或 `-`：

--------------------

--------------------

## 强制换行 ##

在行尾输入两个空格：

这句话不会换行

这句话会换行

## 脚注（footnote） ##

hello1

> 1. hi ↩

# GitHub Flavored Markdown #

## 链接自动展示 ##

你可以直接输入链接地址，它可以直接识别这个链接。

http://markdownx.ryeeeeee.com

## 删除线 ##

通过两个波浪号将字符包含：

错误的文本

## 围栏式代码块 ##

``````````
标准的 Markdown 通过每行开头的 4 个空格将文本转换成代码块，而 GFM 支持围栏式代码块。只要将代码用 ``` 包含起来即可，不需要 4 个空格的缩进
``````````

## 表格 ##

这是个简单的表格：

| First Header | Second Header | Third Header |
| ------------ | ------------- | ------------ |
| Content Cell | Content Cell  | Content Cell |
| Content Cell | Content Cell  | Content Cell |

出于美观的考虑，可以把两端都包围起来：

| First Header | Second Header | Third Header |
| ------------ | ------------- | ------------ |
| Content Cell | Content Cell  | Content Cell |
| Content Cell | Content Cell  | Content Cell |

通过在标题分割行添加冒号 `:`，你可以定义表格单元的对其格式：向左靠齐，居中和向右靠齐：

| First Header | Second Header | Third Header |
| ------------ | ------------- | ------------ |
| Left         | Center        | Right        |
| Left         | Center        | Right        |

表格内使用 HTML 的 `<br>` 标签进行换行


[example link]: http://example.com/
[Google]: http://google.com/
[Yahoo]: http://search.yahoo.com/
[MSN]: http://search.msn.com/
[MarkdownX]: https://sdky.gitee.io/url/to/img.jpg
[alt text]: https://sdky.gitee.io/path/img.jpg