## Typora+PicGo+阿里云oss搭建图床

**问题**：使用Typora写文章时，图片只能存放在本地，且图片位置移动会导致图片无法正常加载。

**解决办法**：将图片上传至云端保存，实现无论何时都能正常加载图片。

这里以阿里云OSS做演示，其他云也类似，PicGo目前已经支持大多主流的云空间。

### 第一步：安装PicGo ###

**下载地址**：[https://www.xiaoxiaofeng.com/resource/23](https://www.xiaoxiaofeng.com/resource/23)

#### 启动界面 ####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2024/07/03/xxf-20240703140241.png?xxfjava)

#### PicGo设置 ####

打开“PicGo设置”—“设置Server”，点击“确定”

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2024/07/03/xxf-20240703140241.png?xxfjava)

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2024/7/3/xxf-1719986205213.png)

### 第二步：配置Typora ###

**下载地址**：[https://www.xiaoxiaofeng.com/resource/22](https://www.xiaoxiaofeng.com/resource/22)

**启动界面**

![image-20240703140324525](https://image.xiaoxiaofeng.site/blog/2024/07/03/xxf-20240703140324.png?xxfjava)

**配置Typora**

打开Typora“文件”—“偏好设置”—“图像”，选择PicGo(app)

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2024/7/3/xxf-1719986206154.png)

### 第三步：阿里云OSS搭建图床 ###

#### 开通阿里云对象存储 ####

开通阿里云对象存储[https://www.aliyun.com/product/oss][https_www.aliyun.com_product_oss]，注册账号后进入对象存储OSS—“管理控制台”

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2024/7/3/xxf-1719986206592.png)

#### 购买套餐 ####

自己根据自己的需求购买套餐，我这里是购买的40G—1年资源包，一年9元。

#### 创建Bucket ####

进入管理控制台后，点击右侧的“创建Bucket”

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2024/7/3/xxf-1719986207054.png)

##### Bucket具体配置 #####

 *  名字：不能有大写字母
 *  地域：根据自己所在地域就进选择
 *  存储类型：标准存储
 *  读写权限：建议这里选择 **公开读**（这样别人通过链接也可以访问你的图片）

##### 创建AcessKey #####

Bucket创建成功后，在个人头像处，弹出的框中选择**AccessKey管理**
![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2024/07/03/xxf-20240703140241.jpeg?xxfjava)

##### 创建一个AccessKey #####

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2024/7/3/xxf-1719986208211.png)

创建完成后，建议选择 **AccessKey下载CSV文件**，因为这里的AccessKeyID和AccessKeySecret**非常重要非常重要！**

### 第四步：配置PicGo ###

##### 打开PicGo设置—阿里云OSS #####

我们在这个界面配置图床

![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/blog/2024/07/03/xxf-20240703140242.png?xxfjava)

 *  KeyID：这里填你刚刚创建的AccessKeyID
 *  KeySecret：这里填你刚刚创建的AccessKeySecret
 *  存储空间名：创建的Bucket名字
 *  存储区域：存储区域的地址节点，**注意这里不要直接复制中文！**
    
     *  打开刚刚**创建的Bucket**，点击**概览**
     *  ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2024/7/3/xxf-1719986208994.png)
 *  后面两项可以不填。

### 第五步：测试 ###

到这里我们的图床就配置完成了，可以在Typora中随意粘贴图片，会发现图片自动上传到云端了。
