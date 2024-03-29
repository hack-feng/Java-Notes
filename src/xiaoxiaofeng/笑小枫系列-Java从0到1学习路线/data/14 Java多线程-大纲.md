![14 Java多线程](https://image.xiaoxiaofeng.site/blog/2023/09/04/xxf-20230904094425.png?xxfjava)

# 14 Java多线程

## 1. 多线程概述
### 1.1 多线程引入
### 1.2 多线程概述
#### 1.2.1 什么是进程？
#### 1.2.2 多进程有什么意义呢?
#### 1.2.3 什么是线程?
#### 1.2.4 多线程有什么意义呢?
#### 1.2.5 线程与进程的关系
#### 1.2.6 进程与线程的区别
#### 1.2.7 什么是并行、并发呢？
### 1.3 Java程序运行原理
## 2. 多线程的实现方案
### 2.1 多线程的实现方案一：继承Thread类，重写run()方法
#### 2.1.2 为什么要重写run()方法？
#### 2.1.3 启动线程使用的是那个方法
#### 2.1.4 线程能不能多次启动
#### 2.1.5 run()和start()方法的区别
#### 2.1.6 Thread类的基本获取和设置方法
### 2.2 多线程的实现方案二：实现Runnable接口
### 2.3 多线程程序实现方案三：实现Callable接口
### 2.4 匿名内部类方式使用多线程
### 2.5 单线程和多线程的运行流程
## 3. 线程调度和线程控制
### 3.1 线程调度
#### 3.1.1 线程有两种调度模型
#### 3.1.2 如何设置和获取线程优先级
### 3.2 线程控制
#### 线程休眠
#### 线程插队
#### 线程让步
## 4. 线程的生命周期
### 4.1 线程的状态
### 4.2 线程的生命周期图
## 5. 线程安全问题
### 5.1 判断一个程序是否会有线程安全问题的标准
### 5.2 如何解决多线程安全问题呢?
#### 5.2.1 解决线程安全问题实现1：同步代码块，格式如下
### 5.3 解决线程安全问题实现2：同步方法   
#### 5.3.1 线程安全与非线程安全的类
#### 5.3.2 JDK5中Lock锁的使用
#### synchronized和lock的用法区别
#### synchronized和lock性能区别
## 6. 死锁
#### 产生死锁的原因主要是
#### 产生死锁的四个必要条件
#### 死锁的解除与预防
## 7. 线程间通信
## 8. 线程组
## 9. 线程池
### 9.1 什么是线程池？
### 9.2 为什么用线程池？
### 9.3 Executor类的继承结构
### 9.4 ThreadPoolExecutor
#### 9.4.1 构造方法
### 9.5 任务提交给线程池之后的处理策略
### 9.6 阻塞队列的介绍
#### 9.6.1 BlockingQueue
#### 9.6.2 排队策略
#### 直接提交
#### 无界队列
#### 有界队列
#### 9.6.3 BlockingQueue
#### 9.6.4 BlockingDeque
#### 9.6.5 ArrayBlockingQueue
#### 9.6.6 LinkedBlockingQueue
#### 9.6.7 SynchronousQueue
#### 9.6.8 DelayQueue
#### 9.6.9 PriorityBlockingQueue
#### 9.6.10 生产者消费者
### 9.7 线程池工具类Executors
### 9.8 线程池
### 9.9 线程池的简单使用
## 10. 定时器的使用
### Timer定时类
### TimerTask
## 11. 线程运行架构
## 12. 多线程总结
### 12.1 三种多选实现方案
### 12.2 实现Runnable好处
### 12.3 线程间的通信
### 12.4 wait、sleep区别
### 12.5 常用方法
### 12.6 线程的生命周期