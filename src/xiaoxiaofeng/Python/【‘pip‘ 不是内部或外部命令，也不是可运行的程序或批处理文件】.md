

# 一、【解决方法】（添加环境变量） #



> （1）打开 **此电脑 / 我的电脑 / 资源管理器**，点击左上角的 **属性** ，点击 **高级系统设置**
>
> （2）在 **系统属性** 界面中，选择下方的 **环境变量**
>
> （3）看 ...**的用户变量** ，点击变量 **PATH（或 path ）**，点击 **编辑**
>
> （4）在 **编辑环境变量** 界面，点击 **浏览** ，点击 **pip 所在的文件夹（pip.exe在python的安装目录里的Scripts里面）**，然后一定要**全都**点击 **确定**（编辑环境变量-->环境变量-->系统属性，要是没有都点击确定的话，会导致修改失败） |

 

【验证】按键盘上 **Win + R** , 输入 **cmd** ，输入 **pip** 或 **pip3**  查看是否成功。

 如果按照以上步骤做后，**还是**提示“pip不是内部或外部命令，也不是可运行的程序或批处理文件”的话，去python的安装目录看一下，找到 **Scripts** 文件夹，点击进去，看看有没有pip的字眼，如下图。**没有的话，**打开 **cmd** ，输入 **python -m ensurepip** 以下载安装pip.exe文件，然后再输入 pip 验证一下是否安装成功。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335342812.png)

# 二、【演示】（解决方法的详细版） #

## 1、打开 **系统属性**（高级）界面 ##

【方法一】

 直接用**电脑本身的搜索功能**，输入 **环境变量** ，点击 **编辑系统环境变量**

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2023/07/26/xxf-20230726094024.png?xxfjava)                                                                                                                                                                                                                                            
【方法二】

 打开 **此电脑 / 我的电脑 / 资源管理器** ，点击左上角的 **属性** ，点击 **高级系统设置**

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335344519.png)

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2023/07/26/xxf-20230726094024.png?xxfjava)

 以上得到：

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335345830.png) |


## 2、编辑用户变量——添加pip的完整路径 ##

### （1）点击 **环境变量** ###

在 系统属性 — 高级 界面 点击 **环境变量** 后，我们可以看到有 **...的用户变量** 和 **系统变量** 这两个（“ **...** ”：我用它 代指你电脑的名字，因为每个人的电脑不太一样，比如我的是 MAIBENBEN）。

我们**只需要编辑 ...的用户变量**，不需要修改 系统变量。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335346403.png)

### （2）点击 ...的用户变量 中的 **PATH / path** ，点击 **编辑** ###

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2023/07/26/xxf-20230726094025.png?xxfjava)

### （3）添加**pip的完整路径（文件所在的位置）** ###

在（2）后，我们进入了 **编辑环境变量** 的界面，这里应该有几行 **路径 / 地址 （用户自己安装的软件的某些程序文件所在的位置）**，这些不可以乱删，否则会导致软件运行时出现某些错误。所以，接下来我们要做的就是**添加** pip.exe 所在的位置 / 完整路径 / 地址 。

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335347511.png)

【注意】**以python3.10版本为例**，如果安装python时用的是**默认路径**的话，那么**pip**所在的位置是：C:\\Users\\用户名称\\AppData\\Local\\Programs\\Python\\Python310\\Scripts 

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335348064.png)

【方法一】新建

 ①提前**复制**好 pip 的完整路径

 打开 **此电脑 / 我的电脑 / 资源管理器** ，打开 **Python**的安装目录 ， 打开 **Scripts** 文件夹（这里我们可以看到**pip.exe等等**）。将 鼠标光标 移到上面 Scripts 上，按鼠标右键 点击 **复制地址** ，**或者** 点击下面第二个图中的 **绿色框框**中的空白处 然后**复制。**

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335348462.png)

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335349094.png)

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335349813.png)

 ②回到刚才的 **编辑环境变量** 的界面，点击 **新建**，粘贴**刚才复制的地址，** 点击 **确定**

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335350331.png)

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335350901.png) |
 【方法二】浏览

 点击 **浏览**，滚动鼠标找到 **python的安装目录里的 Scripts** ，点击**确定**，再点击**确定**

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335351493.png)

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335352095.png)

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335352617.png)                                                                                                                                                                                                                                                                                                                                                                                                                           

## 3、在 环境变量界面 点击 确定 ，然后 在 系统属性界面 点击 确定 ##

在（3）最后点击确定后关闭的知识编辑环境变量界面，此时环境变量还没真正保存修改！**在上述两个界面都点击确定之后，才会真正保存好刚才的修改！**

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335353194.png)

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2023/07/26/xxf-20230726094025.png?xxfjava)

## 4、验证 ##

键盘按 **Win + R** ，输入 **cmd** ，**确定**，然后再命令行输入 **pip** 或 **pip3** ，运行结果如下图则证明成功

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335354008.png)

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335354520.png)

试一下能否安装第三方库，成功！

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/7/26/xxf-1690335355101.png)
