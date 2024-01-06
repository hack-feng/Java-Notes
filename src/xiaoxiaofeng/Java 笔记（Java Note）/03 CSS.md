 *  CSS（Cascading Style Sheet），层叠样式表，用于渲染 HTML 元素标签的样式

# CSS 的引入方式 #

``````````html
/* 使用内联样式：利用标签中 style 属性来改变每个标签的显示样式 */ 
  <p style="background-color:#FF0000; color:#FFFFFF;"> 
      p 标签段落内容 
  </p> 
  
  /* 使用内部样式定义：在 <head> 中使用 <style>，对多个标签进行统一修改 */ 
  <head> 
      <style type="text/css"> 
          p {color:#FF0000;} 
      </style> 
  </head> 
  
  /* 链接外部样式文件：在 <head> 中使用 <link/> 标签来引入外部样式文件 */ 
  /* rel 属性规定当前文档与被链接文档之间的关系；type 属性被链接文档的 MIME 类型；href 属性规定被链接文档的位置 */ 
  <link rel="stylesheet" type="text/css" href="mystyle.css" /> 
  
  /* 在 <style> 中使用 @import 导入外部样式文件 */ 
  <style type="text/css"> 
      @import url(mystyle.css); 
      div {color:#FF0000;} 
  </style>
``````````

# CSS 语法 #

``````````css
selector { 
      property1: value1; 
      property2: value2; 
  }
``````````

## 属性的值 ##

 *  inherit（继承父元素的值）
 *  预定义的值，如`left`、`right`、`none`
 *  长度和百分数，如`3em`、`10px`、`100%`（1 em 表示当前元素的字体尺寸）
 *  纯数字
 *  URL，如`url(bg-pattern.png)`
 *  CSS 颜色：预定义颜色关键字、十六进制数字、RGB、HSL、RGBA、HSLA（色相 hue、饱和度 saturation、亮度 lightness、透明度 alpha transparency）等格式表示的颜色值
 *  `!important`：提升指定样式规则的应用优先权

## 属性继承 ##

## 样式优先级 ##

 *  由上到下，由外到内，优先级由低到高
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414566226.png) 
    图 1 CSS优先级

# 选择器 #

 *  指定 CSS 要作用的标签
 *  简单选择器
    
     *  标签选择器 `tag`
     *  class 选择器 `.className`
     *  ID 选择器 `#id`
     *  属性选择器 `[att]`、`[att=val]`、`[att~=val]`
     *  伪类选择器 `:link`
 *  伪元素选择器 `::first-letter`、`::first-line`、`::before`、`::after`、`::selection`
 *  组合选择器
    
     *  后代选择器 `selector selector`
     *  子选择器 `selector>selector`
     *  兄弟选择器 `selector+selector`、`selector~selector`
     *  选择器分组 `selector,selector`

# CSS 盒子模型 #

 *  CSS 盒模型本质上是一个盒子，封装周围的 HTML 元素，包括：margin（外边距）- 清除边框外的区域，外边距是透明的border（边框）- 围绕在内边距和内容外的边框padding（内边距）- 清除内容周围的区域，内边距是透明的content（内容）- 盒子的内容，显示文本和图像
 *  总元素的宽度=宽度+左填充+右填充+左边框+右边框+左边距+右边距
 *  总元素的高度=高度+顶部填充+底部填充+上边框+下边框+上边距+下边距
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414566895.jpeg) 
    图 2 CSS盒子模型

``````````css
div { 
      background-color: lightgrey; 
      width: 300px; 
      border: 25px solid green; 
      padding: 25px; 
      margin: 25px; 
  }
``````````

# 常见属性 #

### 浏览器私有属性前缀 ###

 *  Chrome、Safari：`-webkit-`
 *  Firefox：`-moz`
 *  IE：`-ms-`
 *  Opera：`-o-`

### 字体 ###

 *  font-size:  |  ：文字的字体大小
 *  font-family: \[  |  \] \#：文字的字体系列（  = cursive | fantasy | monospace | serif | sans-serif）（可以使用 @font-face 规则定义字体）
 *  font-weight: normal | bold | bolder | lighter |  ：字体的粗细（normal 等价于 400、bold 等价于 700、100、200、300、400、500、600、700、800、900 ）
 *  font-style: normal | italic | oblique：文字的字体风格（是否斜体）
 *  line-height: normal |  |  |  ：行间的距离（行高）
 *  color：文字颜色
    

### 文本 ###

 *  text-align: left | right | center | justify ：文本水平对齐方式
 *  vertical-align: baseline | sub | super | top | text-top | middle | bottom | text-bottom |  |  ：元素的垂直对齐方式
 *  text-indent:  |  ：文本块首行的缩进
 *  white-space: normal | pre | nowrap | pre-wrap | pre-line：元素中的空白符的处理方式（pre 保留空格，wrap 保留换行）
 *  word-wrap: normal | break-word：当内容超过指定容器的边界时是否断行
 *  word-break: normal | keep-all | break-all：文本的字内换行行为
 *  word-spacing: normal |  |  ：单词间距（单词间的空白）
 *  text-shadow: none | \[  \{2,3\} &&  ? \]\#：文本阴影效果
 *  text-decoration: none | \[ underline || overline || line-through || blink \]：文本修饰
 *  text-overflow: clip | ellipsis：clip：当内联内容溢出块容器时，将溢出部分裁切掉；ellipsis： 当内联内容溢出块容器时，将溢出部分替换为（…）`text-overflow: ellipsis; overflow: hidden; white-space: nowrap;`
 *  text-transform：文本大小写（uppercase、lowercase、capitalize）
 *  text-indent：文本块首行的缩进
 *  letter-spacing：字符间距
 *  cursor: \[ \[ \]?,\]\* \[ auto | default | none | help | pointer| zoom-in | zoom-out | move：光标的类型
    

### 盒模型 ###

 *  width:  |  | auto：目标对象的宽度
 *  height：目标对象的高度
 *  padding：\[  |  \]\{1,4\}：内边距（填充）（顺序 TRBL：上 右 下 左）
 *  margin：\[  |  | auto \]\{1,4\}：外边距（顺序 TRBL：上 右 下 左）（margin 合并：毗邻元素、父元素与第一个/最后一个子元素）
 *  border:  ||  ||  ：边框（宽度 border-width、样式 border-style、颜色 border-color）
 *  border-radius: \[  |  \]\{1,4\} \[ / \[  |  \]\{1,4\} \]?：圆角边框（水平半径、垂直半径）
 *  overflow: visible | hidden | scroll | auto：内容溢出元素框时的处理方式
 *  box-sizing: content-box | border-box：设置 width、height 指定的区域，，默认 content-box
 *  box-shadow: none |  \[ ,  \]\*：  = inset? &&  \{2,4\} &&  ?：阴影
 *  outline：轮廓边框
    

### 背景 ###

 *  background
 *  background-color:  ：内容背景色，默认值：transparent（全透明）
 *  background-image:  \[ ,  \]\*：背景图像，值 `url(URL)` 或 none
 *  background-repeat:  \[ ,  \]\*：是否及如何重复背景图像，  = repeat-x | repeat-y | \[repeat | no-repeat | space | round\]\{1,2\}
 *  background-attachment:  \[ ,  \]\*：背景图像是否固定或者随着页面的其余部分滚动，  = fixed | scroll | local
 *  background-position:  \[ ,  \]\*：背景图像的开始位置，  = \[ left | center | right | top | bottom |  |  \] | \[ left | center | right |  |  \] \[ top | center | bottom |  |  \] | \[ center | \[ left | right \] \[  |  \]? \] && \[ center | \[ top | bottom \] \[  |  \]? \]
 *  background-origin: border-box | padding-box | content-box：设置背景图像开始位置的参考原点，默认 padding-box
 *  background-clip: border-box | padding-box | content-box：背景图像向外裁剪的区域，默认 border-box
 *  background-size: \[  |  | auto \]\{1,2\} | cover | contain：背景图像的尺寸大小
    

### 布局 ###

 *  display: inline | block | inline-block | none | table | flex：元素的显示方式
    
     *  block（块级元素）：默认宽度为父元素宽度，可设置宽高，换行显示
     *  inline（行内元素）：默认宽度为内容宽度，不可设置宽高，同行显示
     *  inline-block：默认宽度为内容宽度，可设置宽高，同行显示，整块换行
     *  flex（弹性元素）
 *  visibility: visible | hidden | collapse：元素是否可见
 *  position: static | relative | absolute | fixed：元素的定位方式
    
     *  relative（相对定位）：仍在文档流中，参照物为元素本身
     *  absolute（绝对定位）：默认宽度为内容宽度，脱离文档流，参照物为第一个定位祖先/根元素
     *  fixed（固定定位）：默认宽度为内容宽度，脱离文档流，参照物为视窗
 *  float: none | left | right：浮动，默认宽度为内容宽度，半脱离文档流（对元素，脱离文档流；对内容，在文档流），向指定方向一直移动，float 的元素在同一文档流
 *  clear: none | left | right | both：指定不允许有浮动对象的边，应用于后续元素、块级元素（清除浮动 `.clearfix:after{content:".";display:block;clear:both;height:0;overflow:hidden;visibility:hidden;}`）
 *  top：上外边距边界与其包含块上边界之间的偏移
 *  right：右外边距边界与其包含块右边界之间的偏移
 *  bottom：下外边距边界与其包含块下边界之间的偏移
 *  left：左外边距边界与其包含块左边界之间的偏移
 *  z-index：元素的堆叠顺序

#### flex ####

 *  flex container（弹性容器）；flex item（弹性元素）：在文档流中的子元素；main axis（主轴）；cross axis（副轴）
 *  方向
    
     *  flex-direction: row | row-reverse | column | column-reverse：flex item 在弹性容器中的排列方向，默认值 row
     *  flex-wrap: nowrap | wrap | wrap-reverse：弹性容器是否换行，默认值 nowrap
     *  flex-flow: <' flex-direction '> || <' flex-wrap '>：默认值 row nowrap
     *  order:  ：定义 flex item 的排列顺序，数值小的排在前面，默认值 0
 *  弹性
    
     *  flex-basis: main-size| ：设置 flex item 的初始宽/高
     *  flex-grow:  ：设置 flex item 的伸展因子，用来分配弹性容器的 **剩余空间**，默认值 0（flex-basis +flow-grow/sum(flow-grow)\* remain）
     *  flex-shrink:  ：设置 flex item 的收缩因子，默认值 1（flex-basis +flow-grow/sum(flow-grow)\* remain）
     *  flex: none | <' flex-grow '> <' flex-shrink >'? || <' flex-basis '>：默认值 0 1 main-size
 *  对齐
    
     *  justify-content: flex-start | flex-end | center | space-between | space-around：设置 main-axis 方向上的对齐方式
     *  align-items: flex-start | flex-end | center | baseline | stretch：设置 cross-axis 方向上对齐方式
     *  align-self: auto | flex-start | flex-end | center | baseline | stretch：设置单个 flex item 在 cross-axis 方向上对齐方式
     *  align-content: flex-start | flex-end | center | space-between | space-around | stretch：设置 cross-axis 方向上行对齐方式

### 变形 ###

 *  transform: none |  \+
 *  常见的 transform-function
    
     *  rotate( )：旋转
     *  rotate3d( ,  ,  ,  )：旋转
     *  translate( \[,  \]?)：移动
     *  translate3d( ,  ,  )：移动
     *  scalex( \[,  \]?)：缩放
     *  scale3d( ,  ,  )：缩放
     *  skew( \[,  \]?)：倾斜，Y 轴向 X 轴倾斜，X 轴向 Y 轴倾斜
 *  transform-origin: \[  |  | left | center | right \] \[  |  | top | center | bottom \]?：设置转换的原点，默认 50% 50%
 *  perspective: none |  ：指定观察者与「z=0」平面的距离
 *  perspective-origin: \[  |  | left | center | right \] \[  |  | top | center | bottom \]?：指定透视点的位置，，默认 50% 50%
 *  transform-style: flat | preserve-3d：指定某元素的子元素是（看起来）位于三维空间内，还是在该元素所在的平面内被扁平化
 *  backface-visibility: visible | hidden：指定元素背面面向用户时是否可见
    

### 过渡 ###

 *  transition
 *  transition-property: none |  \[ ,  \]\*：设置对象中的参与过渡的属性
 *  transition-duration: \[ , \]\*：设置对象过渡的持续时间
 *  transition-timing-function:  \[,  \]\*：设置对象中过渡的动画类型
 *  transition-delay: \[ , \]\*：设置对象延迟过渡的时间
    

### 动画 ###

 *  animation
 *  animation-name:  \[,  \]\*：设置对象所应用的动画名称
 *  animation-duration: \[,\]\*：设置对象动画的持续时间
 *  animation-timing-function:  \[,  \]\*：设置对象动画的过渡类型
 *  animation-iteration-count:  \[,  \]\*：设置对象动画的循环次数
 *  animation-direction:  \[,  \]\*：设置对象动画在循环中是否反向运动
 *  animation-play-state:  \[,  \]\*：设置对象动画的状态
 *  animation-delay: \[,\]\*：设置对象动画的延迟时间
 *  animation-fill-mode:  \[,  \]\*：设置对象动画时间之外的状态
 *  @keyframes  \{  \}：指定动画名称和动画效果的关键帧，  ：\[ \[ from | to |  \]\{ sRules \} \] \[ \[ , from | to |  \]\{ sRules \} \]\*

# 页面架构 #

## 居中布局 ##

``````````html
<div class="parent"> 
      <div class="child">DEMO</div> 
  </div>
``````````

 *  水平居中

``````````css
.parent { 
       text-align: center; 
   } 
   .child { 
       display: inline-block; 
   }
``````````

``````````css
.child { 
       display: table; 
       margin: 0 auto; 
   }
``````````

``````````css
.parent { 
       position: relative; 
   } 
   .child { 
       position: absolute; 
       left: 50%; 
       transform: translateX(-50%); 
   }
``````````

``````````css
.parent { 
       display: flex; 
       justify-content: center; 
   }
``````````

``````````css
.parent { 
       display: flex; 
   } 
   .child { 
       margin: 0 auto; 
   }
``````````

 *  垂直居中

``````````css
.parent { 
       display: table-cell; 
       vertical-align: middle; 
   }
``````````

``````````css
.parent { 
       position: relative; 
   } 
   .child { 
       position: absolute; 
       top: 50%; 
       transform: translateY(-50%); 
   }
``````````

``````````css
.parent { 
       display: flex; 
       align-items: center; 
   }
``````````

 *  居中

``````````css
.parent { 
       text-align: center; 
       display: table-cell; 
       vertical-align: middle; 
   } 
   .child { 
       display: inline-block; 
   }
``````````

``````````css
.parent { 
       position: relative; 
   } 
   .child { 
       position: absolute; 
       left: 50%; 
       top: 50%; 
       transform: translate(-50%,-50%); 
   }
``````````

``````````css
.parent { 
       display: flex; 
       justify-content: center; 
       align-items: center; 
   }
``````````

## 多列布局 ##

``````````html
<div class="parent"> 
      <div class="left"> 
          <p>left</p> 
      </div> 
      <div class="right"> 
          <p>right</p> 
          <p>right</p> 
      </div> 
  </div>
``````````

``````````html
<div class="parent"> 
      <div class="left"><p>left</p></div> 
      <div class="right-fix"> 
          <div class="right"> 
              <p>right</p><p>right</p> 
          </div> 
      </div> 
  </div>
``````````

 *  定宽与自适应

``````````css
.left { 
       float: left; 
       width: 100px; 
   } 
   .right { 
       margin-left: 120px; 
   }
``````````

``````````css
.left { 
       float: left; width: 100px; 
       position: relative; 
   } 
   .right-fix { 
       float: right; width: 100%; 
       margin-left: -100px; 
   } 
   .right { 
       margin-left: 120px; 
   }
``````````

``````````css
.left { 
       float: left; 
       width: 100px; 
       margin-right: 20px; 
   } 
   .right { 
       overflow: hidden; 
   }
``````````

``````````css
.parent { 
       display: table; width: 100%; 
       table-layout: fixed; 
   } 
   .left, .right { 
       display: table-cell; 
   } 
   .left { 
       width: 100px; 
       padding-right: 20px; 
   }
``````````

``````````css
.parent { 
       display: flex; 
   } 
   .left { 
       width: 100px; 
       margin-right: 20px; 
   } 
   .right { 
       flex: 1; 
   }
``````````

 *  不定宽与自适应

``````````css
.left { 
       float: left; 
       margin-right: 20px; 
   } 
   .right { 
       overflow: hidden; 
   } 
   .left p {width: 200px;}
``````````

``````````css
.parent { 
       display: table; width: 100%; 
   } 
   .left,.right { 
       display: table-cell; 
   } 
   .left { 
       width: 0.1%; 
       padding-right: 20px; 
   } 
   .left p{width: 200px;}
``````````

``````````css
.parent { 
       display: flex; 
   } 
   .left { 
       margin-right: 20px; 
   } 
   .right { 
       flex: 1; 
   } 
   .left p {width: 200px;}
``````````

 *  等分

``````````html
<div class="parent"> 
       <div class="column"><p>1</p></div> 
       <div class="column"><p>2</p></div> 
       <div class="column"><p>3</p></div> 
       <div class="column"><p>4</p></div> 
   </div>
``````````

``````````html
<div class="parent-fix"> 
       <div class="parent"> 
           <div class="column"><p>1</p></div> 
           <div class="column"><p>2</p></div> 
           <div class="column"><p>3</p></div> 
           <div class="column"><p>4</p></div> 
       </div> 
   </div>
``````````

``````````css
.parent { 
       margin-left: -20px; 
   } 
   .column { 
       float: left; 
       width: 25%; 
       padding-left: 20px; 
       box-sizing: border-box; 
   }
``````````

``````````css
.parent-fix { 
       margin-left: -20px; 
   } 
   .parent { 
       display: table; 
       width:100%; 
       table-layout: fixed; 
   } 
   .column { 
       display: table-cell; 
       padding-left: 20px; 
   }
``````````

``````````css
.parent { 
       display: flex; 
   } 
   .column { 
       flex: 1; 
   } 
   .column+.column { 
       margin-left: 20px; 
   }
``````````

 *  等高

``````````html
<div class="parent"> 
       <div class="left"> 
           <p>left</p> 
       </div> 
       <div class="right"> 
           <p>right</p> 
           <p>right</p> 
       </div> 
   </div>
``````````

``````````css
.parent { 
       display: table; width: 100%; 
       table-layout: fixed; 
   } 
   .left,.right { 
       display: table-cell; 
   } 
   .left { 
       width: 100px; 
       padding-right: 20px; 
   }
``````````

``````````css
.parent { 
       display: flex; 
   } 
   .left { 
       width: 100px; 
       margin-right: 20px; 
   } 
   .right { 
       flex: 1; 
   }
``````````

``````````css
.parent { 
       overflow: hidden; 
   } 
   .left,.right { 
       padding-bottom: 9999px; 
       margin-bottom: -9999px; 
   } 
   .left { 
       float: left; width: 100px; 
       margin-right: 20px; 
   } 
   .right { 
       overflow: hidden; 
   }
``````````

## 全屏布局 ##

``````````html
<body> 
      <div class="parent"> 
          <div class="top">top</div> 
          <div class="middle"> 
              <div class="left">left</div> 
              <div class="right"> 
                  <div class="inner">right</div> 
              </div> 
          </div> 
          <div class="bottom">bottom</div> 
      </div> 
  </body>
``````````

 ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414567393.png) 
图 3 全屏布局1

 *  position

``````````css
html, body, .parent { 
       margin: 0; 
       height: 100%; 
       overflow: hidden; 
   } 
  
   .top { 
       position: absolute; 
       top: 0; 
       left: 0; 
       right: 0; 
       height: 100px; 
       background: blue; 
   } 
  
   .left { 
       position: absolute; 
       left: 0; 
       top: 100px; 
       bottom: 50px; 
       width: 200px; 
       background: red; 
   } 
  
   .right { 
       position: absolute; 
       left: 200px; 
       top: 100px; 
       bottom: 50px; 
       right: 0; 
       background: pink; 
       overflow: auto; 
   } 
  
   .bottom { 
       position: absolute; 
       left: 0; 
       right: 0; 
       bottom: 0; 
       height: 50px; 
       background: black; 
   } 
  
   .right .inner {min-height: 1000px;}
``````````

 *  flex

``````````css
html, body, .parent { 
       margin: 0; 
       height: 100%; 
       overflow: hidden; 
   } 
  
   .parent { 
       display: flex; 
       flex-direction: column; 
   } 
  
   .top { 
       height: 100px; 
       background: blue; 
   } 
  
   .bottom { 
       height: 50px; 
       background: black; 
   } 
  
   .middle { 
       flex: 1; 
       display: flex; 
   } 
  
   .left { 
       width: 200px; 
       background: red; 
   } 
  
   .right { 
       flex: 1; 
       overflow: auto; 
       background: pink; 
   } 
  
   .right .inner {min-height: 1000px;}
``````````

 *  flex
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414567927.png) 

``````````css
  html, body, .parent { 
       margin: 0; 
       height: 100%; 
       overflow: hidden; 
   } 
  
   .parent { 
       display: flex; 
       flex-direction: column; 
   } 
  
   .top { 
       background: blue; 
   } 
  
   .bottom { 
       background: black; 
   } 
  
   .middle { 
       flex: 1; 
       display: flex; 
   } 
  
   .left { 
       background: red; 
   } 
  
   .right { 
       flex: 1; 
       overflow: auto; 
       background: pink; 
   } 
  
   .right .inner {min-height: 1000px;}
``````````

# 响应式 #

``````````html
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no" />
``````````

 *  @media: ：媒体查询

``````````css
  @media screen and (max-width:320px) { 
      /* 视窗宽度<=320px 时应用以下样式 */ 
  } 
  
  @media screen and (max-width:320px) { 
      /* 视窗宽度>=769px 时应用以下样式 */ 
  } 
  
  @media screen and (max-width:320px) and (max-width:1000px) { 
      /* 769px<=视窗宽度<=1000px 时应用以下样式 */ 
  }
``````````


[CSS]: https://static.sitestack.cn/projects/sdky-java-note/f79e62fc407a9c1a1ee28a0b9fd2174d.png
[CSS 1]: https://static.sitestack.cn/projects/sdky-java-note/e28ec9b0b368eccc924162c6db2cf0e5.jpeg
[1]: https://static.sitestack.cn/projects/sdky-java-note/6c1f860089076d756c0ea1c21551067c.png
[2]: https://static.sitestack.cn/projects/sdky-java-note/a68cc7e7c469152d3dc6629b4ec88a81.png