![ec7033cd0ae6e308924584e8b59fe2b5.png][]

来源：https://www.cnblogs.com/zuge/p/7397255.html  

**DataGrip使用入门**

最近看到一款数据库客户端工具，DataGrip，是大名鼎鼎的JetBrains公司出品的，就是那个出品Intellij IDEA的公司。

DataGrip是一款数据库管理客户端工具，方便连接到数据库服务器，执行sql、创建表、创建索引以及导出数据等。之前试用的客户端工具是dbvisualizer，但是在试用了DataGrip以后，我就决定抛弃dbvisualizer。

我相信，当你第一眼看到DataGrip以后，会有一种惊艳的感觉，就好比你第一眼看到一个姑娘，就是那么一瞥，你对自己说，就是她了！废话不多说，来看看DataGrip的常用功能。

DataGrip下载链接如下https://www.jetbrains.com/datagrip/download。安装过程也很简单，双击安装，下一步，中间会让你选择主题，本人选择的是经典的Darcula，安装完成后，启动，界面如下

![b3faf7b8b8c35858bc83c76173a69f21.png][]

相信使用过IDEA的同学会感到很亲切。接下来管理数据库驱动。DataGrip支持主流的数据库，File->DataSource

![9036efdf3a23b8d6d00a09c6abe5c4bd.png][]

也可以在Database视图中展开绿色的+号，添加数据库连接

![cec91d934a5515f4e11b03f4d9a76067.png][]

选择需要连接的数据库类型

![1d6fef9e37beaf5c5c1d4fc342251e32.png][]

在面板中，左上部分列出了已经建立的数据库连接，点击各项，右侧会展示当前连接的配置信息，General面板中，可以配置数据库连接的信息，如主机、用户名、密码等，不同数据库配置信息不完全相同，填入数据库URL，注意，URL后有个选项，可以选择直接填入url，那么就不需要单独填主机名、端口等信息了。

Driver部分显示数据库驱动信息，如果还没有下载过驱动，底部会有个警告，提示缺少驱动

![6ef53e123a33a2eb9414fb2e87366251.png][]

点击Driver后的数据库类型，会跳转到驱动下载页面，点击download，下载完会显示驱动包

![e81750f0d44fbd90300769ccfa52a10b.png][]

![ca5c715a28e16ffd1d3279fc78e1273d.png][]

如果下载的驱动有问题，可以手动添加本地驱动包，在试用过程中，创建Oracle连接时，下载的驱动包就有问题，提示缺少class，点击右侧绿色的+号，选择本地下载好的jar包，通过右侧上下箭头，将导入的jar包移到最上位置就OK了

![7dd53f7611173fd66c9efd30ad6d24fd.png][]

点击Test Connection，查看配置是否正确，接下来就可以使用了。

打开DataGrip，选择File->Settings，当前面板显示了常用设置项

![3685d5d090d0548f8c08144faf4b5f4d.png][]

基本上默认设置就足够了，要更改设置也很简单，左侧菜单已经分类好了，第一项是数据库相关的配置，第二项是配置外观的，在这里可以修改主题，key map修改快捷键，editor配置编辑器相关设置，在这里可以修改编辑器字体，展开edit项，Editor->Color & Fonts->Font

![d5b2d338db9c33310f3ba9b98ee6bfb2.png][]

需要将当前主题保存一下，点击save as，起个名，选择重命名后的主题就能修改了，这里我选择习惯的Conurier New字体，大小为14号，点击右下角的apply，点击OK

![4cbb2988d87f1add12ae358da1b2ebbd.png][]

其他的没啥好设置的了。

接下来，我们来使用DataGrip完成数据库的常用操作，包括查询数据、修改数据，创建数据库、表等。

![6c274e5f96cfc842bc2ec727a3584bb9.png][]

左上区域显示了当前数据库连接，展开后会显示数据库表等信息，如果展开后没有任何信息，需要选中数据库连接，点击上面的旋转图标同步一下，下方有个More Schema选项，点击可以切换不同的schema。

右键选中的数据库连接，选择open console，就可以在右侧的控制台中书写sql语句了。

![8a0061da42d611406f6a35fbc1b35298.png][]

DataGrip的智能提示非常爽，无论是标准的sql关键字，还是表名、字段名，甚至数据库特定的字段，都能提示，不得不感叹这智能提示太强大了，Intellij IDEA的智能提示也是秒杀eclipse。  


写完sql语句后，可以选中，电子左上侧绿色箭头执行

![3075f9f43fb884b89d3138a16830b29f.png][]

也可以使用快捷键Ctrl+Enter，选中情况下，会直接执行该sql，未选中情况下，如果控制台中有多条sql，会提示你要执行哪条sql。之前习惯了dbvisualizer中的操作，dbvisualizer中光标停留在当前sql上(sql以分号结尾)，按下Ctrl+.快捷键会自动执行当前sql，其实DataGrip也能设置，在setting->Database-General中

![c71538fb6f6046101f047b5f9ffb862b.png][]

语句执行时默认是提示，改成smallest statement后，光标停留在当前语句时，按下Ctrl+Enter就会直接执行当前语句。

语句的执行结果在底部显示

![5869f851971d6ca46c26fe37c270c644.png][]

如果某列的宽度太窄，可以鼠标点击该列的任意一个，使用快捷键Ctrl+Shift+左右箭头可以调整宽度，如果要调整所有列的宽度，可以点击左上角红框部分，选择所有行，使用快捷键Ctrl+Shift+左右箭头调整

添加行、删除行也很方便，上部的+、-按钮能直接添加行或删除选中的行，编辑列同样也很方便，双击要修改的列，输入修改后的值，鼠标在其他部分点击就完成修改了

![15c84a844149460d89673e4ffc4081e0.png][]

有的时候我们要把某个字段置为null，不是空字符串""，DataGrip也提供了渐变的操作，直接在列上右键，选择set null

![10cdd3a154b1fbfbb72e8c18d2019d24.png][]

对于需要多窗口查看结果的，即希望查询结果在新的tab中展示，可以点击pin tab按钮，那新查询将不会再当前tab中展示，而是新打开一个tab

![9c25f7381b8813e3f07f29bcc92ca007.png][]

旁边的output控制台显示了执行sql的日志信息，能看到sql执行的时间等信息  


![6ab7c8361eebde92c7a55585639c5673.png][]

我就问这么吊的工具，还有谁！！！

要新建表也是相当简单、智能，选中数据库连接，点击绿色+号下选择table

![3a5eb464ca0dddc36f7829f38b2e7d83.png][]

在新打开的窗口中，可以填写表信息

![5863ba763881f48df88264d0a685ce0f.png][]

我就问你看到这个窗口兴奋不兴奋！！！

顶部可以填写表名、表注释，中间可以点击右侧绿色+号添加列，列类型type也是能自动补全，default右侧的消息框图标点击后能对列添加注释，旁边的几个tab可以设置索引及外键

所有这些操作的DDL都会直接在底部显示

![c8aa0e1c46ed58d19e61f6d3236f2c89.png][]

我就问你怕不怕？

表建完后，可以点击下图中的table图标，打开表查看视图

![544ad6e6dfdfd5d8939e01618e3a25bc.png][]

可以查看表的数据，也能查看DDL语句

这些基本功能的设计、体验，已经惊艳到我了，接下来就是数据的导出。

DataGrip的导出功能也是相当强大

选择需要导出数据的表，右键，Dump Data To File

![9c0006912a428479de5e3a11443ff632.png][]

即可以导出insert、update形式的sql语句，也能导出为html、csv、json格式的数据

也可以在查询结果视图中导出

![c3ada86d84de5457bcb70ba85baaab84.png][]

点击右上角下载图标，在弹出窗口中可以选择不同的导出方式，如sql insert、sql update、csv格式等

![e6144adaa970fc2216777e4ea78a2c49.png][]

如果是导出到csv格式，还能控制导出的格式

![af75a3e8a5cb1521b2f969e4186e907e.png][]

导出后用excel打开是这种结果  


![89f38119e722300dbe7f3788a219f563.png][]

除了能导出数据外，还能导入数据

选择表，右键->Import from File，选择要导入的文件

![6a151bdc582a2757c298ebae15513a99.png][]

注意，导出的时候如果勾选了左侧的两个header选项，导入的时候如果有header，也要勾选，不然会提示列个数不匹配

**1、关键字导航：**

当在datagrip的文本编辑区域编写sql时，按住键盘Ctrl键不放，同时鼠标移动到sql关键字上，比如表名、字段名称、或者是函数名上，鼠标会变成手型，关键字会变蓝，并加了下划线，点击，会自动定位到左侧对象树，并选中点击的对象

![1dc983c9dba9412cd99249a758b0570a.png][]

**2、快速导航到指定的表、视图、函数等：**

在datagrip中，使用Ctrl+N快捷键，弹出一个搜索框，输入需要导航的名称，回车即可

![71d9dc05768dcf6930e50f3e5fec8ea2.png][]

**3、全局搜索**

连续两次按下shift键，或者鼠标点击右上角的搜索图标，弹出搜索框，搜索任何你想搜索的东西

![69d787bf390d2659b5d3480c198dde2d.png][]

**4、结果集搜索**

在查询结果集视图区域点击鼠标，按下Ctrl+F快捷键，弹出搜索框，输入搜索内容，支持正则表达式、过滤结果。

![4924587de5ed9215eb0fb97f8004ae5e.png][]

**5、导航到关联数据**

表之间会有外检关联，查询的时候，能直接定位到关联数据，或者被关联数据，例如user1表有个外检字段classroom指向classroom表的主键id，在查询classroom表数据的时候，可以在id字段上右键，go to，referencing data

![9394675f65d4a71d102465db11a04958.png][]

选择要显示第一条数据还是显示所有数据

![32dea385f8f169ca2d1533373e1d8881.png][]

会自动打开关联表的数据

![e0e008a0b3de398b684d3beed9545301.png][]

相反，查询字表的数据时，也能自动定位到父表

**6、结果集数据过滤**

对于使用table edit（对象树中选中表，右键->table editor）打开的结果集，可以使用条件继续过滤结果集，如下图所示，可以在结果集左上角输入款中输入where条件过滤

![a23cb4128944774a3d366feb29e7d3a5.png][]

也可以对着需要过滤数据的列右键，filter by过滤

![f69f1691ce29a48e20c94cc54ad84536.png][]

**7、行转列**

对于字段比较多的表，查看数据要左右推动，可以切换成列显示，在结果集视图区域使用Ctrl+Q快捷键

![621ea4eeb19c1a9b468a1e46bbdddc86.png][]

**8、变量重命名**

鼠标点击需要重命名的变量，按下Shift+F6快捷键，弹出重命名对话框，输入新的名称

![053b46de0776df7a3cd75f49b3552aea.png][]

**9、自动检测无法解析的对象**

如果表名、字段名不存在，datagrip会自动提示，此时对着有问题的表名或字段名，按下Alt+Enter，会自动提示是否创建表或添加字段。

![dc32e80a6162f5cac7a00f2577c17019.png][]

**10、权限定字段名**

对于查询使用表别名的，而字段中没有使用别名前缀的，datagrip能自动添加前缀，鼠标停留在需要添加别名前缀的字段上，使用Alt+Enter快捷键

![719736889fd79e6d6676aff53a437b97.png][]

**11、\*通配符自动展开**

查询的时候我们会使用select 查询所有列，这是不好的习惯，datagrip能快速展开列，光标定位到后面，按下Alt+Enter快捷键

![bc7d7ec79648f7984ad0218a7ee6e9c4.png][]

**12、大写自动转换**

sql使用大写形式是个好的习惯，如果使用了小写，可以将光标停留在需要转换的字段或表名上，使用Ctrl+shift+U快捷键自动转换

**13、sql格式化**

选中需要格式化的sql代码，使用Ctrl+Alt+L快捷键

datagrip提供了一个功能强大的编辑器，实现了notpad++的列编辑模式

**14、多光标模式**

在编辑sql的时候，可能需要同时输入或同时删除一些字符，按下alt+shift，同时鼠标在不同的位置点击，会出现多个光标

![de9dae78e479e7b52b13132cd31b617b.png][]

**15、代码注释**

选中要注释的代码，按下Ctrl+/或Ctrl+shift+/快捷键，能注释代码，或取消注释

![a64ba5669ae1a3b8fa089388017e6e85.png][]

**16、列编辑**

按住键盘Alt键，同时按下鼠标左键拖动，能选择多列，拷贝黏贴等操作

![bdf2dd3e3b5848f2ff40266cbb7b14c5.png][]

**17、代码历史**

在文本编辑器中，邮件，local history，show history，可以查看使用过的sql历史

![46ee844b62f208b977e176ade9ca1a6c.png][]

**18、命令历史**

![b52c1b6931452c37cdaaf80ee35cd50d.png][]




[ec7033cd0ae6e308924584e8b59fe2b5.png]: https://img-blog.csdnimg.cn/img_convert/ec7033cd0ae6e308924584e8b59fe2b5.png
[b3faf7b8b8c35858bc83c76173a69f21.png]: https://img-blog.csdnimg.cn/img_convert/b3faf7b8b8c35858bc83c76173a69f21.png
[9036efdf3a23b8d6d00a09c6abe5c4bd.png]: https://img-blog.csdnimg.cn/img_convert/9036efdf3a23b8d6d00a09c6abe5c4bd.png
[cec91d934a5515f4e11b03f4d9a76067.png]: https://img-blog.csdnimg.cn/img_convert/cec91d934a5515f4e11b03f4d9a76067.png
[1d6fef9e37beaf5c5c1d4fc342251e32.png]: https://img-blog.csdnimg.cn/img_convert/1d6fef9e37beaf5c5c1d4fc342251e32.png
[6ef53e123a33a2eb9414fb2e87366251.png]: https://img-blog.csdnimg.cn/img_convert/6ef53e123a33a2eb9414fb2e87366251.png
[e81750f0d44fbd90300769ccfa52a10b.png]: https://img-blog.csdnimg.cn/img_convert/e81750f0d44fbd90300769ccfa52a10b.png
[ca5c715a28e16ffd1d3279fc78e1273d.png]: https://img-blog.csdnimg.cn/img_convert/ca5c715a28e16ffd1d3279fc78e1273d.png
[7dd53f7611173fd66c9efd30ad6d24fd.png]: https://img-blog.csdnimg.cn/img_convert/7dd53f7611173fd66c9efd30ad6d24fd.png
[3685d5d090d0548f8c08144faf4b5f4d.png]: https://img-blog.csdnimg.cn/img_convert/3685d5d090d0548f8c08144faf4b5f4d.png
[d5b2d338db9c33310f3ba9b98ee6bfb2.png]: https://img-blog.csdnimg.cn/img_convert/d5b2d338db9c33310f3ba9b98ee6bfb2.png
[4cbb2988d87f1add12ae358da1b2ebbd.png]: https://img-blog.csdnimg.cn/img_convert/4cbb2988d87f1add12ae358da1b2ebbd.png
[6c274e5f96cfc842bc2ec727a3584bb9.png]: https://img-blog.csdnimg.cn/img_convert/6c274e5f96cfc842bc2ec727a3584bb9.png
[8a0061da42d611406f6a35fbc1b35298.png]: https://img-blog.csdnimg.cn/img_convert/8a0061da42d611406f6a35fbc1b35298.png
[3075f9f43fb884b89d3138a16830b29f.png]: https://img-blog.csdnimg.cn/img_convert/3075f9f43fb884b89d3138a16830b29f.png
[c71538fb6f6046101f047b5f9ffb862b.png]: https://img-blog.csdnimg.cn/img_convert/c71538fb6f6046101f047b5f9ffb862b.png
[5869f851971d6ca46c26fe37c270c644.png]: https://img-blog.csdnimg.cn/img_convert/5869f851971d6ca46c26fe37c270c644.png
[15c84a844149460d89673e4ffc4081e0.png]: https://img-blog.csdnimg.cn/img_convert/15c84a844149460d89673e4ffc4081e0.png
[10cdd3a154b1fbfbb72e8c18d2019d24.png]: https://img-blog.csdnimg.cn/img_convert/10cdd3a154b1fbfbb72e8c18d2019d24.png
[9c25f7381b8813e3f07f29bcc92ca007.png]: https://img-blog.csdnimg.cn/img_convert/9c25f7381b8813e3f07f29bcc92ca007.png
[6ab7c8361eebde92c7a55585639c5673.png]: https://img-blog.csdnimg.cn/img_convert/6ab7c8361eebde92c7a55585639c5673.png
[3a5eb464ca0dddc36f7829f38b2e7d83.png]: https://img-blog.csdnimg.cn/img_convert/3a5eb464ca0dddc36f7829f38b2e7d83.png
[5863ba763881f48df88264d0a685ce0f.png]: https://img-blog.csdnimg.cn/img_convert/5863ba763881f48df88264d0a685ce0f.png
[c8aa0e1c46ed58d19e61f6d3236f2c89.png]: https://img-blog.csdnimg.cn/img_convert/c8aa0e1c46ed58d19e61f6d3236f2c89.png
[544ad6e6dfdfd5d8939e01618e3a25bc.png]: https://img-blog.csdnimg.cn/img_convert/544ad6e6dfdfd5d8939e01618e3a25bc.png
[9c0006912a428479de5e3a11443ff632.png]: https://img-blog.csdnimg.cn/img_convert/9c0006912a428479de5e3a11443ff632.png
[c3ada86d84de5457bcb70ba85baaab84.png]: https://img-blog.csdnimg.cn/img_convert/c3ada86d84de5457bcb70ba85baaab84.png
[e6144adaa970fc2216777e4ea78a2c49.png]: https://img-blog.csdnimg.cn/img_convert/e6144adaa970fc2216777e4ea78a2c49.png
[af75a3e8a5cb1521b2f969e4186e907e.png]: https://img-blog.csdnimg.cn/img_convert/af75a3e8a5cb1521b2f969e4186e907e.png
[89f38119e722300dbe7f3788a219f563.png]: https://img-blog.csdnimg.cn/img_convert/89f38119e722300dbe7f3788a219f563.png
[6a151bdc582a2757c298ebae15513a99.png]: https://img-blog.csdnimg.cn/img_convert/6a151bdc582a2757c298ebae15513a99.png
[1dc983c9dba9412cd99249a758b0570a.png]: https://img-blog.csdnimg.cn/img_convert/1dc983c9dba9412cd99249a758b0570a.png
[71d9dc05768dcf6930e50f3e5fec8ea2.png]: https://img-blog.csdnimg.cn/img_convert/71d9dc05768dcf6930e50f3e5fec8ea2.png
[69d787bf390d2659b5d3480c198dde2d.png]: https://img-blog.csdnimg.cn/img_convert/69d787bf390d2659b5d3480c198dde2d.png
[4924587de5ed9215eb0fb97f8004ae5e.png]: https://img-blog.csdnimg.cn/img_convert/4924587de5ed9215eb0fb97f8004ae5e.png
[9394675f65d4a71d102465db11a04958.png]: https://img-blog.csdnimg.cn/img_convert/9394675f65d4a71d102465db11a04958.png
[32dea385f8f169ca2d1533373e1d8881.png]: https://img-blog.csdnimg.cn/img_convert/32dea385f8f169ca2d1533373e1d8881.png
[e0e008a0b3de398b684d3beed9545301.png]: https://img-blog.csdnimg.cn/img_convert/e0e008a0b3de398b684d3beed9545301.png
[a23cb4128944774a3d366feb29e7d3a5.png]: https://img-blog.csdnimg.cn/img_convert/a23cb4128944774a3d366feb29e7d3a5.png
[f69f1691ce29a48e20c94cc54ad84536.png]: https://img-blog.csdnimg.cn/img_convert/f69f1691ce29a48e20c94cc54ad84536.png
[621ea4eeb19c1a9b468a1e46bbdddc86.png]: https://img-blog.csdnimg.cn/img_convert/621ea4eeb19c1a9b468a1e46bbdddc86.png
[053b46de0776df7a3cd75f49b3552aea.png]: https://img-blog.csdnimg.cn/img_convert/053b46de0776df7a3cd75f49b3552aea.png
[dc32e80a6162f5cac7a00f2577c17019.png]: https://img-blog.csdnimg.cn/img_convert/dc32e80a6162f5cac7a00f2577c17019.png
[719736889fd79e6d6676aff53a437b97.png]: https://img-blog.csdnimg.cn/img_convert/719736889fd79e6d6676aff53a437b97.png
[bc7d7ec79648f7984ad0218a7ee6e9c4.png]: https://img-blog.csdnimg.cn/img_convert/bc7d7ec79648f7984ad0218a7ee6e9c4.png
[de9dae78e479e7b52b13132cd31b617b.png]: https://img-blog.csdnimg.cn/img_convert/de9dae78e479e7b52b13132cd31b617b.png
[a64ba5669ae1a3b8fa089388017e6e85.png]: https://img-blog.csdnimg.cn/img_convert/a64ba5669ae1a3b8fa089388017e6e85.png
[bdf2dd3e3b5848f2ff40266cbb7b14c5.png]: https://img-blog.csdnimg.cn/img_convert/bdf2dd3e3b5848f2ff40266cbb7b14c5.png
[46ee844b62f208b977e176ade9ca1a6c.png]: https://img-blog.csdnimg.cn/img_convert/46ee844b62f208b977e176ade9ca1a6c.png
[b52c1b6931452c37cdaaf80ee35cd50d.png]: https://img-blog.csdnimg.cn/img_convert/b52c1b6931452c37cdaaf80ee35cd50d.png

public static void main(String[] args) {
        String test = "[b52c1b6931452c37cdaaf80ee35cd50d.png]: https://img-blog.csdnimg.cn/img_convert/b52c1b6931452c37cdaaf80ee35cd50d.png\n" +
                "[1b320a029c8583e0f666d39856f3c33f.png]: https://img-blog.csdnimg.cn/img_convert/1b320a029c8583e0f666d39856f3c33f.png";
        
        String[] array = test.split("\n");
        System.out.println(array[1]);
        
    }