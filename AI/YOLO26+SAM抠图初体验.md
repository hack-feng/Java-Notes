## 背景

最近项目需要针对车辆图片进行更换背景，因为之前的车牌训练使用的yolo11，开始的计划使用yolo26-seg的直接进行图像分割。实验下来，整体的效果太差，边缘处不够精细，于是引入了facebook开源的SAM2.1模型。整体来说，效果很不错，图片上的分割还是很强。

> PS. [SAM3](https://github.com/facebookresearch/sam3)已经出来了，可以根据语义之前去选中物体，感觉可以省略掉yolo圈图的步骤。目前没有找到对应的pt文件，所以本文还是使用的SAM2.1。

![image-20260521140330688](https://image.xiaoxiaofeng.site/blog/image/image-20260521140330688.png?xiaoxiaofeng)

## 环境介绍

- 笔记本电脑
- 无GPU，32G CPU
- 操作系统：Windows

**相关模型地址**：

![image-20260521140212596](https://image.xiaoxiaofeng.site/blog/image/image-20260521140212596.png?xiaoxiaofeng)

[yolo26](https://github.com/ultralytics/ultralytics)

[mobile_sam](https://github.com/ultralytics/assets/releases)

[sam2](https://github.com/facebookresearch/segment-anything)

## 实战体验

本文主要针对使用yolo26-seg、yolo26s+mobile_sam、yolo26s+sam2.1b、yolo26s+sam2.1l 三个方案进行处理时间和效果进行对比。

针对不同的模型我们一起来看一下代码吧。

### 方案一：yolo26-seg方案

详细代码如下，让AI给注释的很详细了，不单独介绍了。

~~~python
import cv2
import numpy as np
from ultralytics import YOLO
from pathlib import Path

# 1. 加载YOLO分割模型
model = YOLO("weights/yolo26n-seg.pt")

# 2. 读取图像并进行推理
img_path = "images/test3.jpg"
results = model.predict(source=img_path, conf=0.25)

# 3. 遍历检测结果
for r in results:
    img = np.copy(r.orig_img)  # 原始图像 (H, W, 3)
    img_name = Path(r.path).stem
    
    # 用于存储处理后的车辆图像，以便后续拼接
    processed_vehicle_imgs = []
    
    # 检查是否有掩码
    if r.masks is None:
        print("未检测到掩码")
        continue

    # 4. 遍历每个检测到的对象
    # 使用 r.boxes 的长度作为循环次数，确保索引一致
    num_detections = len(r.boxes)
    
    for i in range(num_detections):
        # 获取类别标签
        cls_id = int(r.boxes.cls[i])
        label = r.names[cls_id]
        
        # 只处理车辆类别
        vehicle_classes = ['car', 'truck', 'bus', 'motorcycle']
        if label not in vehicle_classes:
            continue
        
        print(f"发现目标: {label}")

        # 5. 创建二值掩码
        b_mask = np.zeros(img.shape[:2], np.uint8)
        
        # 获取当前对象的掩码轮廓
        # r.masks.xy 是一个列表，每个元素对应一个检测框的掩码点集
        if r.masks.xy is None or i >= len(r.masks.xy):
            continue
            
        contour = r.masks.xy[i].astype(np.int32).reshape(-1, 1, 2)
        cv2.drawContours(b_mask, [contour], -1, (255, 255, 255), cv2.FILLED)
        
        # 6. 方案B：提取车辆（透明背景，需保存为PNG）
        # 创建 RGBA 图像
        b, g, r_channel = cv2.split(img)
        rgba = cv2.merge([b, g, r_channel, b_mask])
        
        # 7. 裁剪到车辆边界框
        x1, y1, x2, y2 = r.boxes.xyxy[i].cpu().numpy().astype(np.int32)
        
        # 确保坐标不越界
        h, w = img.shape[:2]
        x1, y1 = max(0, x1), max(0, y1)
        x2, y2 = min(w, x2), min(h, y2)
        
        # 避免裁剪区域为空
        if x2 <= x1 or y2 <= y1:
            continue

        # 裁剪透明背景车辆
        car_transparent_cropped = rgba[y1:y2, x1:x2]
        
        # 单独保存透明背景的 PNG
        cv2.imwrite(f"{img_name}_{label}_transparent.png", car_transparent_cropped)
        
        # --- 为了拼接显示，生成白底车辆图 ---
        white_bg = np.full((y2-y1, x2-x1, 3), 255, dtype=np.uint8)
        mask_crop = b_mask[y1:y2, x1:x2]
        
        # 归一化掩码
        mask_norm = mask_crop.astype(np.float32) / 255.0
        mask_norm_3ch = np.stack([mask_norm]*3, axis=-1)
        
        # 混合前景和白底
        # 注意: rgba[:,:,:3] 是 BGR 通道
        foreground_crop = rgba[:,:,:3][y1:y2, x1:x2].astype(np.float32)
        car_white_bg_cropped = (foreground_crop * mask_norm_3ch + 
                                white_bg.astype(np.float32) * (1 - mask_norm_3ch)).astype(np.uint8)
        
        processed_vehicle_imgs.append(car_white_bg_cropped)

    # --- 拼接逻辑 ---
    if processed_vehicle_imgs:
        # 取第一辆检测到的车辆进行拼接演示
        vehicle_img = processed_vehicle_imgs[0]
        
        # 调整高度一致：以原图高度为准
        target_h = img.shape[0]
        h_v, w_v = vehicle_img.shape[:2]
        
        if h_v > 0:
            # 缩放车辆图以匹配原图高度 (保持宽高比)
            scale = target_h / h_v
            new_w_v = int(w_v * scale)
            vehicle_img_resized = cv2.resize(vehicle_img, (new_w_v, target_h))
            
            # 水平拼接: [原图] | [抠图结果]
            combined = np.hstack([img, vehicle_img_resized])
            
            # 保存拼接结果
            save_path_combined = f"{img_name}_combined_result.jpg"
            cv2.imwrite(save_path_combined, combined)
            print(f"拼接结果已保存: {save_path_combined}")
    else:
        print("未检测到符合条件的车辆")

print("处理完成！")
~~~

### 方案二：yolo26s+sam

SAM使用的代码都是同一套，只需要把`SAM_MODEL_PATH = "weights/mobile_sam.pt"`换成对应的模型即可。

~~~python
import cv2
import numpy as np
from ultralytics import YOLO, SAM
from pathlib import Path

# ========== 1. 配置参数 ==========
YOLO_MODEL_PATH = "weights/yolo26s.pt"   # YOLO 检测模型
SAM_MODEL_PATH = "weights/mobile_sam.pt"   # SAM 2 模型路径
IMAGE_PATH = "images/test1.jpg"
CONF_THRESHOLD = 0.25
VEHICLE_CLASSES = ['car', 'truck']       # YOLO 需要检测的类别

# ========== 2. 加载模型 ==========
print("正在加载 YOLO 模型...")
yolo_model = YOLO(YOLO_MODEL_PATH)

print("正在加载 SAM 模型...")
sam_model = SAM(SAM_MODEL_PATH)

# ========== 3. YOLO 检测获取边界框 ==========
print(f"正在检测图像: {IMAGE_PATH}")
yolo_results = yolo_model.predict(source=IMAGE_PATH, conf=CONF_THRESHOLD)

for r in yolo_results:
    img = r.orig_img.copy()
    img_name = Path(r.path).stem
    
    if r.boxes is None or len(r.boxes) == 0:
        print("未检测到任何目标")
        continue

    # 收集所有车辆类别的边界框
    vehicle_boxes = []
    vehicle_labels = []
    
    for i, cls_id in enumerate(r.boxes.cls):
        label = r.names[int(cls_id)]
        if label in VEHICLE_CLASSES:
            box = r.boxes.xyxy[i].cpu().numpy()
            vehicle_boxes.append(box)
            vehicle_labels.append(label)

    if not vehicle_boxes:
        print("未检测到指定车辆类别")
        continue

    # ========== 4. SAM 分割 ==========
    print(f"发现 {len(vehicle_boxes)} 个车辆目标，正在进行 SAM 分割...")
    boxes_np = np.array(vehicle_boxes)
    
    # 用于存储处理好的车辆图像，以便最后拼接
    processed_crops = [] 

    try:
        sam_results = sam_model(IMAGE_PATH, bboxes=boxes_np)
        
        if isinstance(sam_results, list) and len(sam_results) > 0:
            sam_result = sam_results[0]
        else:
            sam_result = sam_results

        if sam_result.masks is None:
            print("SAM 未生成掩码")
            continue
            
        masks_data = sam_result.masks.data.cpu().numpy()
        
        # ========== 5. 处理并保存结果 ==========
        for idx, mask_np in enumerate(masks_data):
            label = vehicle_labels[idx]
            
            # 将浮点型掩码转换为 uint8 (0-255)
            mask_uint8 = (mask_np * 255).astype(np.uint8)
            
            # 获取对应的边界框用于裁剪
            x1, y1, x2, y2 = map(int, vehicle_boxes[idx])
            
            # --- 1. 保存透明背景 PNG (原始需求) ---
            b, g, r_ch = cv2.split(img)
            rgba = cv2.merge([b, g, r_ch, mask_uint8])
            save_path_trans = f"{img_name}_{label}_sam_transparent.png"
            # 注意：这里保存的是裁剪后的透明图
            cv2.imwrite(save_path_trans, rgba[y1:y2, x1:x2])
            
            # --- 2. 准备用于拼接的图像 (白底) ---
            # 提取裁剪区域的原始像素
            crop_img = img[y1:y2, x1:x2]
            crop_mask = mask_uint8[y1:y2, x1:x2]
            
            # 创建白底
            white_bg = np.full_like(crop_img, 255)
            
            # 归一化掩码
            mask_norm = crop_mask.astype(np.float32) / 255.0
            mask_3ch = np.stack([mask_norm] * 3, axis=-1)
            
            # 混合：前景 * 掩码 + 白底 * (1 - 掩码)
            blended_crop = (crop_img.astype(np.float32) * mask_3ch + 
                            white_bg.astype(np.float32) * (1 - mask_3ch)).astype(np.uint8)
            
            # 保存到列表，用于后续拼接
            processed_crops.append(blended_crop)
            
            # 单独保存白底 JPG (可选)
            save_path_white = f"{img_name}_{label}_sam_white_bg.jpg"
            cv2.imwrite(save_path_white, blended_crop)

        # ========== 6. 合并输出 (原图 + 抠图) ==========
        if processed_crops:
            # 策略：将所有抠出的车辆水平拼接到原图右侧
            # 为了美观，我们将所有车辆图调整到相同的高度（例如原图高度的 50% 或固定高度）
            
            target_height = img.shape[0] # 让拼接图高度与原图一致
            
            resized_crops = []
            for crop in processed_crops:
                h, w = crop.shape[:2]
                if h == 0: continue
                # 计算缩放比例，保持宽高比
                scale = target_height / h
                new_w = int(w * scale)
                new_h = target_height
                resized = cv2.resize(crop, (new_w, new_h))
                resized_crops.append(resized)
            
            if resized_crops:
                # 将所有车辆图水平拼接在一起
                if len(resized_crops) > 1:
                    combined_crops = np.hstack(resized_crops)
                else:
                    combined_crops = resized_crops[0]
                
                # 将原图与拼接后的车辆图水平拼接
                # [ 原图 ] | [ 车辆1 | 车辆2 ... ]
                final_result = np.hstack([img, combined_crops])
                
                save_path_combined = f"{img_name}_combined_sam_result.jpg"
                cv2.imwrite(save_path_combined, final_result)
                print(f"合并结果已保存: {save_path_combined}")

    except Exception as e:
        print(f"SAM 处理出错: {e}")
        import traceback
        traceback.print_exc()

print("全部处理完成！")
~~~

## 效果对比

我们从4张照片就行对比，包括室内环境、室外环境、不同车型、不同颜色等多个维度进行选图。

![image-20260521133735577](https://image.xiaoxiaofeng.site/blog/image/image-20260521133735577.png?xiaoxiaofeng)

### 对比1：室内白色轿车

这张图可以说是4张图里面最为明显的一张图了，因为图片拼接的缘故，边上的细节可能展示的不是很清楚，yolo26-seg、yolo26s+mobile_sam的边缘处理锯齿很严重，yolo26s+sam2.1b基本抠出了车辆图片，但yolo26s+sam2.1l 的更为精准。

![image-20260521133540219](https://image.xiaoxiaofeng.site/blog/image/image-20260521133540219.png?xiaoxiaofeng)

可以看一下这张放大的细节图

![image-20260521134239862](https://image.xiaoxiaofeng.site/blog/image/image-20260521134239862.png?xiaoxiaofeng)

### 对比2：室外跑车

![image-20260521132333519](https://image.xiaoxiaofeng.site/blog/image/image-20260521132333519.png?xiaoxiaofeng)

### 对比3：室内深色SUV

![image-20260521132642752](https://image.xiaoxiaofeng.site/blog/image/image-20260521132642752.png?xiaoxiaofeng)

### 对比4

![image-20260521132824028](https://image.xiaoxiaofeng.site/blog/image/image-20260521132824028.png?xiaoxiaofeng)

### 耗时对比

| 方案                  | 总耗时(ms) | 相对速度 |
| :-------------------- | :--------- | :------- |
| YOLOv11n-seg          | 493.29     | 基准(1x) |
| YOLOv11s + mobile_sam | 1291.70    | 约2.6x   |
| YOLOv11s + SAM2.1b    | 3704.63    | 约7.5x   |
| YOLOv11s + SAM2.1l    | 8221.29    | 约16.7x  |

yolo26n-seg

![image-20260521112121789](https://image.xiaoxiaofeng.site/blog/image/image-20260521112121789.png?xiaoxiaofeng)

yolo26s+mobile_sam

![image-20260521113121699](https://image.xiaoxiaofeng.site/blog/image/image-20260521113121699.png?xiaoxiaofeng)

yolo26s+sam2.1b

![image-20260521113315878](https://image.xiaoxiaofeng.site/blog/image/image-20260521113315878.png?xiaoxiaofeng)

yolo26s+sam2.1l

![image-20260521113809077](https://image.xiaoxiaofeng.site/blog/image/image-20260521113809077.png?xiaoxiaofeng)

## 整体评价

### 1. 效果排名

**SAM2.1l > SAM2.1b > mobile_sam > YOLOv11n-seg**

SAM2.1系列在边缘精细度上具有明显优势，特别是Large版本，对于车窗、车灯、车身曲线等细节的保留最为完整。YOLO原生分割在速度上有优势，但边缘质量难以满足精细化换背景的需求。

### 2. 速度与精度的权衡

| 场景                         | 推荐方案              | 理由                        |
| :--------------------------- | :-------------------- | :-------------------------- |
| 实时处理/高吞吐量/不要求质量 | YOLOv11n-seg          | 速度最快(493ms)，精度可接受 |
| 移动端/资源受限              | YOLOv11s + mobile_sam | 平衡方案，1.3s耗时可控      |
| 服务端/高质量要求            | YOLOv11s + SAM2.1b    | 3.7s换取显著质量提升        |
| 追求极致效果                 | YOLOv11s + SAM2.1l    | 8.2s耗时，但效果最佳        |

### 3. 工程建议

- **推荐组合**：YOLOv11s + SAM2.1b。在CPU环境耗时(3.7秒)和效果之间取得了最佳平衡，能够满足大多数商业场景下的换背景需求。
- **优化方向**：
  - 可考虑使用YOLOv11n替代YOLOv11s进行目标检测，进一步降低检测耗时
  - 对于批量处理，可利用SAM的批量推理能力提升整体效率
  - 若后续SAM3的PT文件发布，可直接使用语义选择物体，省略YOLO环节
- **硬件建议**：SAM2.1系列对计算资源要求较高，建议在GPU环境下部署，CPU环境下Large版本8秒的耗时在生产环境中可能成为瓶颈。
- **背景处理**：把图像抠出来是最简单，难点在于后面的添加背景，如何处理光线、阴影、图片位置、交接处羽化等等问题。后续有研究了再继续输出文章吧~

## 总结

本文到这里就结束了，我们通过实际代码和效果对比，详细展示了YOLO原生分割与YOLO+SAM组合方案在车辆图片分割任务上的表现差异。希望能为正在做类似图像分割任务的小伙伴提供一些参考。

如果觉得本文对你有帮助，欢迎点赞、收藏、关注，后续还会带来更多AI实战内容~

**我是笑小枫，喜欢的朋友记得点个关注哟~**