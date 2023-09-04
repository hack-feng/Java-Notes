![13 Java IO流](https://image.xiaoxiaofeng.site/blog/2023/08/29/xxf-20230829105358.png?xxfjava)

# 13 Java IO流

## 1. 流
### 1.1 流的概念
### 1.2 IO流概述
### 1.3 流的三种分类方式
### 1.4 流的层次结构
## 2. 字节流
### 2.1 字节流写数据
### 2.2 字节流写数据的方式
### 2.3 字节流读取数据
### 2.4 字节流复制数据练习
### 2.5字节缓冲流
### 2.6 字节缓冲流复制数据练习
## 3. 字符流
### 3.1 字符流
#### 3.1.1 转换流出现的原因及思想
#### 3.1.2 转换流概述
#### 3.1.3 InputStreamReader读数据
#### 3.1.4 字符流复制文本文件
#### 3.1.5 转换流的简化写法
#### 3.1.6 FileReader
### 3.2 字符缓冲流
#### 3.2.1 BufferedWriter基本用法
#### 3.2.2 BufferedReader基本用法
#### 3.2.3 特殊功能
### 3.3 模拟记事本
## 4. File类
### 4.1 File类概述
### 4.2 构造方法
### 4.3 创建功能
### 4.4 删除功能
### 4.5 重命名功能
### 4.6 判断功能
### 4.7 获取功能
### 4.8 高级获取功能
### 4.9 文件过滤器
### 4.10 File练习
#### 4.10.1 文件名称过滤器的实现
#### 4.10.2 递归遍历目录下指定后缀名结尾的文件名称
#### 4.10.3 递归删除带内容的目录
#### 4.10.4 模拟文件管理器
## 5. NIO
### 5.1 Java NIO 概述
#### 5.1.1 Channel 和 Buffer 
#### 5.1.2 Selector 
### 5.2 Java NIO vs IO
#### 5.2.1 Java NIO和IO的主要区别 
#### 5.2.2 面向流与面向缓冲 
#### 5.2.3 阻塞与非阻塞IO 
#### 5.2.4 选择器（Selectors） 
#### 5.2.5 NIO和IO如何影响应用程序的设计 
#### 5.2.6 总结 
### 5.3 通道（Channel）
#### 5.3.1 Channel的实现 
#### 5.3.2 基本的 Channel 示例 
### 5.4 缓冲区（Buffer）
#### 5.4.1 Buffer的基本用法 
#### 5.4.2 Buffer的capacity、position、limit 
#### 5.4.3 Buffer的类型 
#### 5.4.4 Buffer的分配 
#### 5.4.5 向Buffer中写数据 
#### 5.4.6 从Buffer中读取数据 
### 5.5 分散Scatter和聚集Gather
#### 5.5.1 Scattering Reads 
#### 5.5.2 Gathering Writes 
### 5.6 通道之间的数据传输 
#### 5.6.1 transferFrom() 
#### 5.6.2 transferTo() 
### 5.7 选择器（Selector）
#### 5.7.1 为什么使用Selector? 
#### 5.7.2 Selector的创建 
#### 5.7.3 向Selector注册通道 
#### 5.7.4 SelectionKey 
#### 5.7.5 通过Selector选择通道 
#### 5.7.6 wakeUp() 
#### 5.7.7 close() 
#### 5.7.7 完整的示例 
### 5.8 文件通道
#### 5.8.1 打开FileChannel 
#### 5.8.2 从FileChannel读取数据 
#### 5.8.3 向FileChannel写数据 
#### 5.8.4 关闭FileChannel 
#### 5.8.5 FileChannel的size方法 
#### 5.8.6 FileChannel的truncate方法 
#### 5.8.7 FileChannel的force方法 
### 5.9 Socket 通道
#### 5.9.1 打开 SocketChannel 
#### 5.9.2 关闭 SocketChannel 
#### 5.9.3 从 SocketChannel 读取数据 
#### 5.9.4 写入 SocketChannel 
#### 5.9.5 非阻塞模式 
#### 5.9.6 非阻塞模式与选择器 
### 5.10 ServerSocket 通道
#### 5.10.1 打开 ServerSocketChannel 
#### 5.10.2 关闭 ServerSocketChannel 
#### 5.10.3 监听新进来的连接 
#### 5.10.4 非阻塞模式 
### 5.11 Datagram 通道
#### 5.11.1 打开 DatagramChannel 
#### 5.11.2 接收数据 
#### 5.11.3 发送数据 
#### 5.11.4 连接到特定的地址 
### 5.12 管道（Pipe）
#### 5.12.1 创建管道 
#### 5.12.2 向管道写数据 
#### 5.12.3 从管道读取数据 
### 5.13 总结
## 6. AIO
### 6.1 JDK7 AIO初体验
### 6.2 关于AIO的概念理解
### 6.3 JDK7 AIO初体验
### 6.4 AIO和NIO性能哪个好
### 6.5 使用J2SE进行服务器架构技术选型的变迁
#### 阶段1：一个连接一个线程
#### 阶段2：服务器端采用了线程池
#### 阶段3：采用非阻塞IO，多路复用技术，又有两种不同的方式
#### 阶段4：使用AIO技术
## 7. 序列化流
### 7.1 什么是java序列化，如何实现java序列化？
### 7.2 Serializable
### 7.3 序列化流ObjectOutputStream
### 7.4 反序列化流ObjectInputStream