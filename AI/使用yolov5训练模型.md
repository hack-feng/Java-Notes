## 前言

本文主要以是否戴口罩为案例，演示如何基于yolov5模型，训练我们自己需要的模型，因实现功能相对简单，我使用的训练集数据也就1000张图片左右，但每个训练集可能会包含多组数据。基本可以实现基础的识别功能，如果需要更为精确的结果，可以加大训练集的数量。

整体训练的流程相对简单，难的是怎么收集数据集，怎么针对细节调优。



本模型使用环境：python 3.10.18。使用conda管理环境，使用GPU训练。

因合并整体篇幅过长，所以依赖项单独拆分，整个流程博主边操作边整理，已亲身测试，当然不同的环境也可能出现不同的问题，如有问题可留言或加博主联系方式咨询。

相关文章链接如下：

[Anaconda3下载安装，管理Python环境，切换镜像源](https://zhangfz.blog.csdn.net/article/details/148865629)

[模型训练之数据标注-Labelimg的使用教程（yolo标注）](https://zhangfz.blog.csdn.net/article/details/149217172)

[模型训练之数据标注-Labelme的使用教程（yolo多边形，语义分割）](https://zhangfz.blog.csdn.net/article/details/149247792)

[Pytorch使用GPU训练全过程，包含安装CUDA、cuDNN、PyTorch](https://zhangfz.blog.csdn.net/article/details/149326299)

## 1. 相关资源下载

### 1.1 YOLOv5下载
~~~
https://github.com/ultralytics/yolov5
~~~

### 1.2 预训练权重下载
YOLOv5 提供了不同大小和速度的预训练模型，从小到大的顺序是：n， s， m， l， x。通常从 yolov5s.pt 开始，它在速度和精度之间有很好的平衡。

![image-20250825175843085](https://image.xiaoxiaofeng.site/blog/image/image-20250825175843085.png?xiaoxiaofeng)

~~~
https://github.com/ultralytics/yolov5/releases/tag/v5.0
~~~

下载后放到项目目录下： weights/yolov5s.pt，这个可以配置，也可不放到项目下

![image-20250825175935653](https://image.xiaoxiaofeng.site/blog/image/image-20250825175935653.png?xiaoxiaofeng)

### 1.3 安装依赖
使用Pycharm或vs code打开项目，在terminal中输入`pip install -r requirements.txt`安装必要的依赖包

可以使用国内镜像源加速，强烈建议

~~~
pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
~~~

## 2. 准备数据集

### 2.1 数据集格式结构
YOLOv5 要求数据集遵循特定的格式（YOLO 格式），数据集可以放在项目目录下，也可以放在其他地方，可以修改代码中的路径。

本文是放在`E:/mask_dataset`目录下

数据集格式结构

数据集目录应该如下所示：

~~~
    mask_dataset/
    ├── images/
    │   ├── train/
    │   │   ├── image1.jpg
    │   │   └── ...
    │   └── val/
    │       ├── image2.jpg
    │       └── ...
    └── labels/
        ├── train/
        │   ├── image1.txt
        │   └── ...
        └── val/
            ├── image2.txt
            └── ...
~~~

images/train/ 和 images/val/：分别存放训练集和验证集的图片（.jpg, .png 等）。

labels/train/ 和 labels/val/：分别存放与训练集和验证集图片对应的标签文件（.txt）。

每个标签文件与图片同名。

格式为：<class_id> <center_x> <center_y> <width> <height>

class_id：类别的索引（从 0 开始）。

![image-20250825180001385](https://image.xiaoxiaofeng.site/blog/image/image-20250825180001385.png?xiaoxiaofeng)

center_x, center_y, width, height： bounding box 的中心坐标和宽高，必须是归一化后的值（即除以图片的宽度和高度），范围在 0 到 1 之间。

> 示例： 对于一张 1000x800 的图片，一个中心点在 (500, 400)，宽高为 (200, 100) 的边界框，其归一化后的值为：
> center_x = 500 / 1000 = 0.5
> center_y = 400 / 800 = 0.5
> width = 200 / 1000 = 0.2
> height = 100 / 800 = 0.125
> 对应的标签文件内容为：0 0.5 0.5 0.2 0.125

### 2.2 数据集配置
创建 data/mask_data.yaml 文件

mask_data.yaml：数据集配置文件，包含以下内容：
~~~yaml
path: E:/mask_dataset # dataset root dir
train: train # train images (relative to 'path') 
val: val # val images (relative to 'path') 
test: 

# Classes
nc: 2
names:
  0: mask
  1: no_mask
~~~

## 3. 开始训练

使用 train.py 脚本进行训练。最关键的几个参数：

--weights：指定初始权重文件（yolov5s.pt）

--data：指定数据集配置文件路径（dataset.yaml）

--epochs：指定训练轮数

--img-size：指定输入图片的大小（通常是 640）

--batch-size：指定批次大小（根据你的 GPU 内存调整，-1 表示自动批大小）

--device：指定训练设备（0 代表第一块 GPU，cpu 代表使用 CPU）

训练直接找到train.py文件，我这里使用的是vscode，修改文件中的配置信息

![image-20250825175910752](https://image.xiaoxiaofeng.site/blog/image/image-20250825175910752.png?xiaoxiaofeng)

或者直接运行训练命令：

~~~bash
# 使用 GPU (单卡)
python train.py --img 640 --batch 16 --epochs 100 --data data/mask_data.yaml --weights weights/yolov5s.pt --device 0

# 使用 CPU (不推荐，速度极慢)
python train.py --img 640 --batch 16 --epochs 100 --data data/mask_data.yaml --weights weights/yolov5s.pt --device cpu

# 使用多卡GPU (例如 0号和1号GPU)
python train.py --img 640 --batch 64 --epochs 100 --data data/mask_data.yaml --weights weights/yolov5s.pt --device 0,1
训练开始后，日志和结果会保存在 runs/train/exp/ 目录（每次训练都会新建一个 expN 文件夹，如 exp2, exp3）。
~~~

在训练过程中，你可以看到资源使用的情况，根据对应的资源，尽可能地提升资源的利用率，从而提高训练的效率：

![image-20250825180112131](https://image.xiaoxiaofeng.site/blog/image/image-20250825180112131.png?xiaoxiaofeng)

验证集上的性能指标（mAP@0.5, mAP@0.5:0.95）。

![image-20250825180041264](https://image.xiaoxiaofeng.site/blog/image/image-20250825180041264.png?xiaoxiaofeng)

训练结束后，会得到最好的和最后一轮的模型权重（best.pt, last.pt）。

![image-20250825180211436](https://image.xiaoxiaofeng.site/blog/image/image-20250825180211436.png?xiaoxiaofeng)

## 4. 测试模型
测试模型使用 detect.py 脚本。最核心的参数是 --weights，指定模型权重文件。

具体调整如下：
1. 修改detect.py文件，将--weights参数改为训练好的模型权重文件路径

2. 调整--source，指定测试图片路径

3. 调整--data，指定数据集配置文件路径

4. 运行detect.py

![image-20250825180223502](https://image.xiaoxiaofeng.site/blog/image/image-20250825180223502.png?xiaoxiaofeng)

运行结果如下，测试图片来自百度，如有侵权，联系删除

![image-20250825180621644](https://image.xiaoxiaofeng.site/blog/image/image-20250825180621644.png?xiaoxiaofeng)

![image-20250825180529865](https://image.xiaoxiaofeng.site/blog/image/image-20250825180529865.png?xiaoxiaofeng)

![image-20250825180513628](https://image.xiaoxiaofeng.site/blog/image/image-20250825180513628.png?xiaoxiaofeng)

## 5. 写在最后

模型训练的成功，对数据集的质量要求还是很高，尽量使用不同场景的数据集。如果只是单一场景的数据集，训练出的结果可能只适用于单一场景。背景环境越复杂，对训练集的数据要求越高。

关于训练集的图片和标签，如果有需要的小伙伴可以添加我下方的联系方式获取。如有问题，可以留言。

## 

感谢你阅读到这里，我是**笑小枫**。💙

每一篇文章都是我用心整理和思考的结果，希望能对你有所帮助。如果觉得内容还不错，欢迎点赞、收藏、转发支持一下～

当然，技术这条路，一个人走可能会很快，但一群人走才能走得更远。如果你有任何问题、想法，或者单纯想交个朋友，都可以加我微信一起交流学习：

📱 **微信：q1150640979**

也欢迎常来我的小站坐坐：

🌐 **个人网站：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com/)**

我会在那里持续分享更多技术干货与成长心得。

**保持热爱，奔赴山海。我们下篇文章见～** 🚀

—— 笑小枫