## 笑小枫💕

> 欢迎来到笑小枫的世界，喜欢的朋友关注一下我呦，大伙的支持，就是我坚持写下去的动力。
>
> 微信公众号：笑小枫
>
> 笑小枫个人博客：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com)
>
> CSDN：[https://zhangfz.blog.csdn.net](https://zhangfz.blog.csdn.net)

## 前言

本地安装mysql见系列的另一篇文章文章[【笑小枫的按步照搬系列】本地安装Mysql数据库](https://www.xiaoxiaofeng.com/archives/mysql8)

这里不做过多的讲解，本文主要讲解安装完mysql后，如何使用可视化工具远程连接操作mysql数据库。

使用可视化工具连接mysql，市面上常见的一般是使用Navicat和DataGrip，本文主要介绍的是DataGrip的安装使用介绍。

关于Navicat的，见本系列的另一篇文章[【笑小枫的按步照搬系列】数据库可视化工具Navicat安装及破解](https://www.xiaoxiaofeng.com/archives/navicat)


## DataGrip使用入门

最近看到一款数据库客户端工具，DataGrip，是大名鼎鼎的JetBrains公司出品的，就是那个出品Intellij IDEA的公司。

DataGrip是一款数据库管理客户端工具，方便连接到数据库服务器，执行sql、创建表、创建索引以及导出数据等。之前试用的客户端工具是dbvisualizer，但是在试用了DataGrip以后，我就决定抛弃dbvisualizer。

我相信，当你第一眼看到DataGrip以后，会有一种惊艳的感觉，就好比你第一眼看到一个姑娘，就是那么一瞥，你对自己说，就是她了！废话不多说，来看看DataGrip的常用功能。

DataGrip下载链接如下https://www.jetbrains.com/datagrip/download。安装过程也很简单，双击安装，下一步，中间会让你选择主题，本人选择的是经典的Darcula，安装完成后，启动，界面如下

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614813842.png)

相信使用过IDEA的同学会感到很亲切。接下来管理数据库驱动。DataGrip支持主流的数据库，File->DataSource

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614814522.png)

也可以在Database视图中展开绿色的+号，添加数据库连接

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614814982.png)

选择需要连接的数据库类型

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614815593.png)

在面板中，左上部分列出了已经建立的数据库连接，点击各项，右侧会展示当前连接的配置信息，General面板中，可以配置数据库连接的信息，如主机、用户名、密码等，不同数据库配置信息不完全相同，填入数据库URL，注意，URL后有个选项，可以选择直接填入url，那么就不需要单独填主机名、端口等信息了。

Driver部分显示数据库驱动信息，如果还没有下载过驱动，底部会有个警告，提示缺少驱动

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614816118.png)

点击Driver后的数据库类型，会跳转到驱动下载页面，点击download，下载完会显示驱动包

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614816630.png)

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614817203.png)

如果下载的驱动有问题，可以手动添加本地驱动包，在试用过程中，创建Oracle连接时，下载的驱动包就有问题，提示缺少class，点击右侧绿色的+号，选择本地下载好的jar包，通过右侧上下箭头，将导入的jar包移到最上位置就OK了

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614817710.png)

点击Test Connection，查看配置是否正确，接下来就可以使用了。

打开DataGrip，选择File->Settings，当前面板显示了常用设置项

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614818203.png)

基本上默认设置就足够了，要更改设置也很简单，左侧菜单已经分类好了，第一项是数据库相关的配置，第二项是配置外观的，在这里可以修改主题，key map修改快捷键，editor配置编辑器相关设置，在这里可以修改编辑器字体，展开edit项，Editor->Color & Fonts->Font

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614818706.png)

需要将当前主题保存一下，点击save as，起个名，选择重命名后的主题就能修改了，这里我选择习惯的Conurier New字体，大小为14号，点击右下角的apply，点击OK

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614819249.png)

其他的没啥好设置的了。

接下来，我们来使用DataGrip完成数据库的常用操作，包括查询数据、修改数据，创建数据库、表等。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614819824.png)

左上区域显示了当前数据库连接，展开后会显示数据库表等信息，如果展开后没有任何信息，需要选中数据库连接，点击上面的旋转图标同步一下，下方有个More Schema选项，点击可以切换不同的schema。

右键选中的数据库连接，选择open console，就可以在右侧的控制台中书写sql语句了。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614820461.png)

DataGrip的智能提示非常爽，无论是标准的sql关键字，还是表名、字段名，甚至数据库特定的字段，都能提示，不得不感叹这智能提示太强大了，Intellij IDEA的智能提示也是秒杀eclipse。

写完sql语句后，可以选中，电子左上侧绿色箭头执行

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614820941.png)

也可以使用快捷键Ctrl+Enter，选中情况下，会直接执行该sql，未选中情况下，如果控制台中有多条sql，会提示你要执行哪条sql。之前习惯了dbvisualizer中的操作，dbvisualizer中光标停留在当前sql上(sql以分号结尾)，按下Ctrl+.快捷键会自动执行当前sql，其实DataGrip也能设置，在setting->Database-General中

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614821456.png)

语句执行时默认是提示，改成smallest statement后，光标停留在当前语句时，按下Ctrl+Enter就会直接执行当前语句。

语句的执行结果在底部显示

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614821992.png)

如果某列的宽度太窄，可以鼠标点击该列的任意一个，使用快捷键Ctrl+Shift+左右箭头可以调整宽度，如果要调整所有列的宽度，可以点击左上角红框部分，选择所有行，使用快捷键Ctrl+Shift+左右箭头调整

添加行、删除行也很方便，上部的+、-按钮能直接添加行或删除选中的行，编辑列同样也很方便，双击要修改的列，输入修改后的值，鼠标在其他部分点击就完成修改了

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614822500.png)

有的时候我们要把某个字段置为null，不是空字符串""，DataGrip也提供了渐变的操作，直接在列上右键，选择set null

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614822993.png)

对于需要多窗口查看结果的，即希望查询结果在新的tab中展示，可以点击pin tab按钮，那新查询将不会再当前tab中展示，而是新打开一个tab

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614823505.png)

旁边的output控制台显示了执行sql的日志信息，能看到sql执行的时间等信息

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614824095.png)

我就问这么吊的工具，还有谁！！！

要新建表也是相当简单、智能，选中数据库连接，点击绿色+号下选择table

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614824618.png)

在新打开的窗口中，可以填写表信息

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614825059.png)

我就问你看到这个窗口兴奋不兴奋！！！

顶部可以填写表名、表注释，中间可以点击右侧绿色+号添加列，列类型type也是能自动补全，default右侧的消息框图标点击后能对列添加注释，旁边的几个tab可以设置索引及外键

所有这些操作的DDL都会直接在底部显示

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614825584.png)

我就问你怕不怕？

表建完后，可以点击下图中的table图标，打开表查看视图

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614826116.png)

可以查看表的数据，也能查看DDL语句

这些基本功能的设计、体验，已经惊艳到我了，接下来就是数据的导出。

DataGrip的导出功能也是相当强大

选择需要导出数据的表，右键，Dump Data To File

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614826688.png)

即可以导出insert、update形式的sql语句，也能导出为html、csv、json格式的数据

也可以在查询结果视图中导出

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614827151.png)

点击右上角下载图标，在弹出窗口中可以选择不同的导出方式，如sql insert、sql update、csv格式等

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614827613.png)

如果是导出到csv格式，还能控制导出的格式

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614828062.png)

导出后用excel打开是这种结果

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614828566.png)

除了能导出数据外，还能导入数据

选择表，右键->Import from File，选择要导入的文件

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614829041.png)

注意，导出的时候如果勾选了左侧的两个header选项，导入的时候如果有header，也要勾选，不然会提示列个数不匹配

## 1、关键字导航

当在datagrip的文本编辑区域编写sql时，按住键盘Ctrl键不放，同时鼠标移动到sql关键字上，比如表名、字段名称、或者是函数名上，鼠标会变成手型，关键字会变蓝，并加了下划线，点击，会自动定位到左侧对象树，并选中点击的对象

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614829709.png)

##  2、快速导航到指定的表、视图、函数等

在datagrip中，使用Ctrl+N快捷键，弹出一个搜索框，输入需要导航的名称，回车即可

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614830281.png)

## 3、全局搜索

连续两次按下shift键，或者鼠标点击右上角的搜索图标，弹出搜索框，搜索任何你想搜索的东西

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614830790.png)

## 4、结果集搜索

在查询结果集视图区域点击鼠标，按下Ctrl+F快捷键，弹出搜索框，输入搜索内容，支持正则表达式、过滤结果。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614831220.png)

## 5、导航到关联数据

表之间会有外检关联，查询的时候，能直接定位到关联数据，或者被关联数据，例如user1表有个外检字段classroom指向classroom表的主键id，在查询classroom表数据的时候，可以在id字段上右键，go to，referencing data

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614831671.png)

选择要显示第一条数据还是显示所有数据

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614832282.png)

会自动打开关联表的数据

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614832708.png)

相反，查询字表的数据时，也能自动定位到父表

## 6、结果集数据过滤

对于使用table edit（对象树中选中表，右键->table editor）打开的结果集，可以使用条件继续过滤结果集，如下图所示，可以在结果集左上角输入款中输入where条件过滤

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614833193.png)

也可以对着需要过滤数据的列右键，filter by过滤

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614833764.png)

## 7、行转列

对于字段比较多的表，查看数据要左右推动，可以切换成列显示，在结果集视图区域使用Ctrl+Q快捷键

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614834325.png)

## 8、变量重命名

鼠标点击需要重命名的变量，按下Shift+F6快捷键，弹出重命名对话框，输入新的名称

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614834849.png)

## 9、自动检测无法解析的对象

如果表名、字段名不存在，datagrip会自动提示，此时对着有问题的表名或字段名，按下Alt+Enter，会自动提示是否创建表或添加字段。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614835426.png)

## 10、权限定字段名

对于查询使用表别名的，而字段中没有使用别名前缀的，datagrip能自动添加前缀，鼠标停留在需要添加别名前缀的字段上，使用Alt+Enter快捷键

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614835860.png)

## 11、\*通配符自动展开

查询的时候我们会使用select 查询所有列，这是不好的习惯，datagrip能快速展开列，光标定位到后面，按下Alt+Enter快捷键

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614836396.png)

## 12、大写自动转换

sql使用大写形式是个好的习惯，如果使用了小写，可以将光标停留在需要转换的字段或表名上，使用Ctrl+shift+U快捷键自动转换

## 13、sql格式化 

选中需要格式化的sql代码，使用Ctrl+Alt+L快捷键

datagrip提供了一个功能强大的编辑器，实现了notpad++的列编辑模式

## 14、多光标模式

在编辑sql的时候，可能需要同时输入或同时删除一些字符，按下alt+shift，同时鼠标在不同的位置点击，会出现多个光标

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614836886.png)

## 15、代码注释 

选中要注释的代码，按下Ctrl+/或Ctrl+shift+/快捷键，能注释代码，或取消注释

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614837351.png)

## 16、列编辑

按住键盘Alt键，同时按下鼠标左键拖动，能选择多列，拷贝黏贴等操作

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614837764.png)

## 17、代码历史

在文本编辑器中，邮件，local history，show history，可以查看使用过的sql历史

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614838198.png)

## 18、命令历史

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2022/11/28/xxf-1669614838711.png)
