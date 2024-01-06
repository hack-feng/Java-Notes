# UML（统一建模语言） #

## 常用的 UML 图 ##

### 用例图 ###

 *  用于描述系统提供的系列功能，每个用例代表系统的一个功能模块
 *  包含用例、角色两种图元
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414634036.jpeg) 
    图 1 用例图

### 类图 ###

 *  表示系统中应该包含哪些实体，各实体之间如何关联
 *  类在类图上使用包含三个部分的矩形来描述：最上面的部分显示类的名称，中间部分包含类的属性，最下面的部分包含类的方法
 *  类之间的基本关系
    
     *  关联（包括聚合、组合）
        
         *  聚合：当某个实体聚合成另一个实体时，该实体还可以同时是另一个实体的部分
         *  组合：当某个实体组合成另一个实体时，该实体则**不能**同时是一个实体的部分
         *  关联使用一条实线来表示，**带箭头的实线**表示单向关联
     *  泛化
        
         *  泛化与继承是同一个概念，都是指子类是一种特殊的父类
         *  类与类之间的继承关系使用**带空心三角形的实线**表示
         *  类实现接口可视为一种特殊的继承，这种实现用**带空心三角形的虚线**表示
     *  依赖
        
         *  如果一个类的改动会导致另一个类的改动，则称两个类之间存在依赖
         *  依赖关系使用**带箭头的虚线**表示，其中箭头指向被依赖的实体
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414634635.jpeg) 
    图 2 类图

### 活动图 ###

 *  用于描述用例内部的活动或方法的流程
 *  开始状态、结束状态、活动、活动流、分支、分叉和汇合、泳道
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414635555.jpeg) 
    图 3 活动图

### 时序图 ###

 *  由活动者、对象、消息、生命线和控制焦点组成
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414636584.jpeg) 
    图 4 用户登录时序图

### 状态图 ###

 *  表示某个对象所处的不同状态和该类的状态转换信息
 *  初始状态、状态之间的转换、状态、判断点、一个或者多个终止点

### 组件图 ###

 *  包含组件、接口和 Port 等图元

### 部署图 ###


[d4a442f72e42182db820ac7eac8b497d.jpeg]: https://static.sitestack.cn/projects/sdky-java-note/d4a442f72e42182db820ac7eac8b497d.jpeg
[77f9acb609310704e09f14a5d0d541c9.jpeg]: https://static.sitestack.cn/projects/sdky-java-note/77f9acb609310704e09f14a5d0d541c9.jpeg
[bb006f3f21f6275360cc102967575db0.jpeg]: https://static.sitestack.cn/projects/sdky-java-note/bb006f3f21f6275360cc102967575db0.jpeg
[cddc62aca7e324fd73fd8d46163d95fa.jpeg]: https://static.sitestack.cn/projects/sdky-java-note/cddc62aca7e324fd73fd8d46163d95fa.jpeg