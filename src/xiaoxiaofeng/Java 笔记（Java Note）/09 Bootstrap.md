 *  响应式设计（Reponsive Web Design，简称 RWD）指的是不同的设备（电脑、平板、手机）访问相同的页面的时候，得到不同的页面视图，而得到的视图是适应当前屏幕的

# Bootstrap CSS #

## 栅格系统（Grid System） ##

 *  class 前缀：`.col-xs-`*、`.col-sm-`*、`.col-md-`*、`.col-lg-`*，其中 \* 范围是从 1 到 12
 *  偏移列，使用 `.col-md-offset-*` 类，这些类会把一个列的左外边距（margin）增加  *列，其中*  范围是从 1 到 11

``````````html
<div class="container"> 
      <div class="row"> 
          <div class="col-*-*">...</div> 
          <div class="col-*-*">...</div> 
      </div> 
      <div class="row">...</div> 
  </div>
``````````

## 排版 ##

``````````html
<small>本行内容是在标签内</small><br> 
  <strong>本行内容是在标签内</strong><br> 
  <em>本行内容是在标签内，并呈现为斜体</em><br> 
  <p class="text-left">向左对齐文本</p> 
  <p class="text-center">居中对齐文本</p> 
  <p class="text-right">向右对齐文本</p> 
  <p class="text-muted">本行内容是减弱的</p> 
  <p class="text-primary">本行内容带有一个 primary class</p> 
  <p class="text-success">本行内容带有一个 success class</p> 
  <p class="text-info">本行内容带有一个 info class</p> 
  <p class="text-warning">本行内容带有一个 warning class</p> 
  <p class="text-danger">本行内容带有一个 danger class</p>
``````````

## 代码 ##

 *  `<code>` 同一行代码片段
 *  `<pre>` 多行代码

## 表格 ##

 *  `<table>` 类
    
     - `.table` 为任意 `<table>` 添加基本样式 (只有横向分隔线)
     - `.table-striped` 在 `<tbody>` 内添加斑马线形式的条纹
     - `.table-bordered` 为所有表格的单元格添加边框
     - `.table-hover` 在 `<tbody>` 内的任一行启用鼠标悬停状态
     - `.table-condensed` 让表格更加紧凑
     
*  `<tr>`, `<th>` 和 `<td>` 类

     - `.active` 将悬停的颜色应用在行或者单元格上
     - `.success` 表示成功的操作
     - `.info` 表示信息变化的操作
     - `.warning` 表示一个警告的操作
     - `.danger` 表示一个危险的操作

## 表单 ##

### 垂直排列的表单（默认） ###

 *  创建基本表单的步骤
    
     *  向父 元素添加 `role="form"`。
     *  把 和控件放在一个 `<div class="form-group">` 中
     *  向所有的文本元素 、 和  添加 class ="form-control"（设置宽度属性为 width: 100%;）

### 内联表单 ###

 *  向  标签添加 class `.form-inline`
    

### 水平排列的表单 ###

 *  向父  元素添加 class `.form-horizontal`，向标签添加 class `.control-label`
    

``````````html
<form class="form-horizontal" role="form">
    <div class="form-group">
        <label class="col-sm-2 control-label">Email</label>
        <div class="col-sm-10">
            <p class="form-control-static">email@example.com</p>
        </div>
    </div>
    <div class="form-group">
        <label for="inputPassword" class="col-sm-2 control-label">密码</label>
        <div class="col-sm-10 has-error">
            <input type="password" class="form-control" id="inputPassword" placeholder="请输入密码">
        </div>
    </div>
</form>
``````````

### 支持的表单控件 ###

 *  Bootstrap 支持最常见的表单控件，主要是 input、textarea、checkbox、radio 和 select

### 表单控件状态 ###

 *  验证状态：Bootstrap 包含了错误、警告和成功消息的验证样式。只需要对父元素简单地添加适当的 class（`.has-warning`、`.has-error` 或 `.has-success`）即可使用验证状态

## 按钮 ##

 *  以下样式可用于 , , 或  元素上
    
     *  `.btn` 为按钮添加基本样式
     *  `.btn-default` 默认/标准按钮（灰色）
     *  `.btn-primary` 原始按钮样式（未被操作）（蓝色）
     *  `.btn-success` 表示成功的动作（绿色）
     *  `.btn-info` 该样式可用于要弹出信息的按钮（浅蓝色）
     *  `.btn-warning` 表示需要谨慎操作的按钮（黄色）
     *  `.btn-danger` 表示一个危险动作的按钮操作（红色）
     *  `.btn-link` 让按钮看起来像个链接（仍然保留按钮行为）
     *  `.btn-lg` 制作一个大按钮
     *  `.btn-sm` 制作一个小按钮
     *  `.btn-xs` 制作一个超小按钮
     *  `.btn-block` 块级按钮（拉伸至父元素100%的宽度）
     *  `.active` 激活状态
     *  `.disabled` 禁用状态
        

## 图片 ##

 *  `.img-rounded` 为图片添加圆角
 *  `.img-circle` 将图片变为圆形
 *  `.img-thumbnail` 缩略图功能
 *  `.img-responsive` 图片响应式

## 辅助类 ##

 *  文本颜色：`.text-muted`、`.text-primary`、`.text-success`、`.text-info`、`.text-warning`、`.text-danger`
 *  背景颜色：`.bg-primary`、`.bg-success`、`.bg-info`、`.bg-warning`、`.bg-danger`

# Bootstrap 布局组件 #

``````````html
<!-- 字体图标（Glyphicons）-->
<span class="glyphicon glyphicon-search" style="color: blue; font-size: 60px;"></span> 搜索

<!-- 进度条 -->
<div class="progress progress-striped active">
    <div class="progress-bar " role="progressbar" aria-valuemin="0" aria-valuemax="100" style="width: 45%;">
        45%
    </div>
</div>

<!-- 标签 -->
<span class="label label-default">默认标签</span>
<span class="label label-primary">主要标签</span>
<span class="label label-success">成功标签</span>
<span class="label label-info">信息标签</span>
<span class="label label-warning">警告标签</span>
``````````

# Bootstrap 插件 #

``````````js
// 初始化为默认行为
$("#myModal").modal()
 // 初始化为不支持键盘
$("#myModal").modal({ keyboard: false })
// 初始化并立即调用 show
$("#myModal").modal('show')
$('#myModal').on('show.bs.modal', function () {
  // 执行一些动作...
})
``````````

## 模态框（Modal）插件 ##

 *  `$('#identifier').modal(options);`
 *  参数backdrop: boolean 或字符串 'static' // 指定一个静态的背景，当用户点击模态框外部时不会关闭模态框keyboard: true // 键盘上的 esc 键被按下时关闭模态框show: true // 当初始化时显示模态框remote: path // 使用 jQuery.load 方法，为模态框的主体注入内容
 *  方法'toggle'：手动切换模态框'show'：手动打开模态框'hide'：手动隐藏模态框
 *  事件'show.bs.modal' 在调用 show 方法后触发'shown.bs.modal' 当模态框对用户可见时触发'hide.bs.modal' 当调用 hide 实例方法时触发'hidden.bs.modal' 当模态框完全对用户隐藏时触发

``````````html
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">模态框标题</h4>
            </div>
            <div class="modal-body">
                模态框内容
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>
``````````

## 弹出框（Popover）插件 ##

 *  `$('#identifier').popover(options);`
 *  参数html：是否向弹出框插入 HTML，默认值：falsetitle：弹出框标题content：弹出框内容，string | element | functiontrigger：定义如何触发弹出框：click| hover | focus | manual，可以传递多个触发器，每个触发器之间用空格分隔placement：规定如何定位弹出框：top | bottom | left | right | auto

# Bootstrap - jQuery - Plugin #

## string ##

``````````html
// "ASP is old, ASP.net is new, ASP long lives"
"{0} is old, {1} is new, {0} long lives".format("ASP", "ASP.net")
``````````

## dialog（对话框窗口） ##

``````````html
<button class="btn btn-primary" onclick="run()">Run</button>
<div id="loginwrap">
    <form id="loginform" role="form">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="username" class="form-control"
                   id="username">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control"
                   id="password">
        </div>
    </form>
</div>

<script>
    $("#loginwrap").dialog({
        title: "Login",
        backdrop: "static", // with backdrop or keyboard
        autoOpen: false, // without auto open
        dialogClass: "modal-sm", // Optional sizes: "modal-lg" or "modal-sm"
        onClose: function () { // Override onClose event
            $(this).dialog("destroy");
        },
        buttons: [
            {
                text: "Close",
                'class': "btn-primary",
                click: function () {
                    $(this).dialog("close");
                }
            },
            {
                text: "Login",
                'class': "btn-success",
                click: function () {
                    /*your login handler*/
                }
            }
        ]
    });

    function run() {
        $("#loginwrap").dialog("open");
        // Rut the element to the old place, remove the overlay from DOM.
        // $("#loginwrap").dialog("destroy");
    }
</script>
``````````

## messager（消息窗口） ##

``````````js
$.messager.alert("Title", "This is message!");

$.messager.model = {
    ok:{ text: "确定", classed: 'btn-success' },
    cancel: { text: "取消", classed: 'btn-danger' }
};
$.messager.confirm("title", "This is message!", function() {
    alert("you closed it")
});

$.messager.popup("This is message!");
``````````

## datagrid（数据表格） ##

## tree（树） ##