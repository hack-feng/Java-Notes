## 1. 背景

最近要开始搞模型训练，已经安装好了anaconda，并创建了对应的python环境。

最新开始准备训练集数据，并对训练集进行标注，本文主要讲解了如果对训练集的数据进行标注。

之前标注工具是labelimg，labelimg 只支持矩形标注，为了更精准的多边形标准，开始使用labelme。

接下来详细讲解如果安装使用labelme。

## 2. labelme安装

直接直接使用 pip 命令安装labelme。

由于前面我们已经安装好了anaconda，新建了一个环境隔离，后续标统一放到这个环境上进行。

~~~
conda create -n ai-label python=3.10
conda activate ai-label
~~~

**前置条件：安装anaconda**

打开`Anaconda Prompt`，切换到我们对应的环境上，输入以下命令，还是从清华源上下载（不知道为啥配置了conda的清华源镜像地址没有生效，知道的小伙伴支援一下）

~~~
pip install pyqt5 -i https://pypi.tuna.tsinghua.edu.cn/simple
pip install labelme -i https://pypi.tuna.tsinghua.edu.cn/simple
~~~

![image-20250627135027090](https://image.xiaoxiaofeng.site/blog/image/image-20250627135027090.png?xiaoxiaofeng)

这样`labelme `我们就下载安装好了。

## 3. labelme使用

接下来输入`labelme `，回车。就会弹出`labelme `对应的界面。界面如下：

![image-20250627135152425](https://image.xiaoxiaofeng.site/blog/image/image-20250627135152425.png?xiaoxiaofeng)

可以更改文件输出路径、取消保存图像路径，设置自动保存等功能。

![image-20250709105906749](https://image.xiaoxiaofeng.site/blog/image/image-20250709105906749.png?xiaoxiaofeng)

可以在编辑里面选择我们需要标注的图形，更快的标注出我们需要的目标。

![image-20250709101528903](https://image.xiaoxiaofeng.site/blog/image/image-20250709101528903.png?xiaoxiaofeng)

点击打开目录，选择需要标注的文件夹就可以开始标注了。

![image-20250709101313277](https://image.xiaoxiaofeng.site/blog/image/image-20250709101313277.png?xiaoxiaofeng)

## 4. 格式转换

labelme默认输出的文件是json格式，如果仅需要目标检测需要转为YOLO（.txt）格式的可以使用下面工具类转换。

下面提供一个json2yolo的工具类，将其中的目录替换为你的目录即可。

~~~python

import os
import json
 
# labelme标注的json标签文件目录和保存生成的txt标签的文件夹
dir_json = r'D:/AI/license_plate/labels/'
dir_txt = r'D:/AI/license_plate/txt/'
# os.mkdir(dir_txt)
 
classes2id = {}
num = 0
jsons = os.listdir(dir_json)
for i in jsons:
    json_path = os.path.join(dir_json, i)
    with open(json_path, 'r', encoding="utf-8") as f:
        json_data = json.load(f)
        # print(json_data['shapes'])
        for j in json_data['shapes']:
            if j['label'] not in classes2id:
                classes2id[j['label']] = num
                num += 1
# 输出标签
print(classes2id)
 
 
 # 将json标签转换成txt标签
def json2txt(path_json, path_txt):  
    with open(path_json, 'r', encoding='utf-8') as path_json:
        jsonx = json.load(path_json)
        with open(path_txt, 'w+') as ftxt:
            shapes = jsonx['shapes']
            # 获取图片长和宽
            width = jsonx['imageWidth']
            height = jsonx['imageHeight']
            # print(shapes)
            cat=shapes[0]['label']
            cat=classes2id[cat]
 
            for shape in shapes:
                # 获取矩形框两个角点坐标
                x1 = shape['points'][0][0]
                y1 = shape['points'][0][1]
                x2 = shape['points'][1][0]
                y2 = shape['points'][1][1]
 
                # 转换为 YOLO 格式（归一化中心宽高）  
                dw = 1. / width
                dh = 1. / height
                x = dw * (x1 + x2) / 2
                y = dh * (y1 + y2) / 2
                w = dw * abs(x2 - x1)
                h = dh * abs(y2 - y1)
                yolo = f"{cat} {x} {y} {w} {h} \n"
                ftxt.writelines(yolo)
 
 
list_json = os.listdir(dir_json)
for cnt, json_name in enumerate(list_json):
    if os.path.splitext(json_name)[-1] == ".json":
        path_json = dir_json + json_name
        path_txt = dir_txt + json_name.replace('.json', '.txt')
        json2txt(path_json, path_txt)

~~~

**YOLO格式解读：**

YOLO格式的核心在于其标注文件规范，每个.txt文件对应一张图像的所有目标标注，每行表示一个目标实例。具体格式为：

~~~
<class_id> <x_center> <y_center> <width> <height>
~~~

* class_id：整数，表示目标类别索引，从0开始计数。例如0表示`plate`，1表示`car`等。应与数据集配置文件中names列表顺序一致。

* x_center/y_center：归一化后的边界框中心坐标，浮点数范围[0,1]。计算方式为：目标中心点x坐标/图像宽度，目标中心点y坐标/图像高度。

* width/height：归一化后的边界框宽度和高度，范围[0,1]。计算方式为：目标宽度/图像宽度，目标高度/图像高度。

方框坐标必须是归一化的xywh格式（从0到1），意思就是说，将*.txt的文档的五个值都用 0 到 1来表示 ，至于怎么从得到每个数值，就要靠下面这个来算了。

**那么是如何使得 坐标 归一化呢？**

如果方框以像素为单位，请将x_center和width除以图像宽度，将y_center和height除以图像高度。

## 5. 本文总结

本文主要介绍了Labelme标注工具的安装使用过程，如果只是训练识别，标注矩形框，使用labelimg也是一个不错的选择，如果需要标注多边形，那么labelme的作用就体现出来了。

## 写在最后

感谢你阅读到这里，我是**笑小枫**。💙

每一篇文章都是我用心整理和思考的结果，希望能对你有所帮助。如果觉得内容还不错，欢迎点赞、收藏、转发支持一下～

当然，技术这条路，一个人走可能会很快，但一群人走才能走得更远。如果你有任何问题、想法，或者单纯想交个朋友，都可以加我微信一起交流学习：

📱 **微信：q1150640979**

也欢迎常来我的小站坐坐：

🌐 **个人网站：[https://www.xiaoxiaofeng.com](https://www.xiaoxiaofeng.com/)**

我会在那里持续分享更多技术干货与成长心得。

**保持热爱，奔赴山海。我们下篇文章见～** 🚀

—— 笑小枫
