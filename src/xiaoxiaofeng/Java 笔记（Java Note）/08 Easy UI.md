# 创建和配置 #

 *  使用步骤
    
     *  引入主题样式、图标样式和当前 EasyUI 版本对应的 jQuery 库、EasyUI 库
     *  创建 HTML 标签（需要使用 EasyUI 组件对应的标签）
     *  通过标签创建：在标签上，添加属性：`class="easyui-插件名" data-options="属性名:属性值，事件名:响应函数，…"`；或者通过 Javascript 创建：`$("#id").插件名({ });`
 *  所有的属性、事件都定义在 jQuery.fn.\{plugin\}.defaults 对象中
 *  所有方法都定义在 jQuery.fn.\{plugin\}.methods 对象中
 *  方法
    
     *  初始化方法：`$('selector').plugin({属性名:属性值, 事件名:响应函数, …});`
     *  调用方法：`$('selector').plugin('method', parameter);`
        
         *  selector 是 jQery 对象选择器
         *  plugin 是插件的名称
         *  method 是相应插件现有的方法名
         *  parameter 是参数对象，可以是一个对象、字符串等

# 布局 #

## panel（面板） ##

 *  通过 `<div>` 创建
    

### 属性 ###

 *  title：在面板头部显示的标题文本
*  iconCls：设置一个 16x16 图标的 CSS 类 ID 显示在面板左上角
*  width：设置面板宽度
*  height：设置面板高度
*  fit：当设置为 true 的时候面板大小将自适应父容器
*  border：定义是否显示面板边框
*  content：面板主体内容，内容可以是 `<iframe>` 标签
*  collapsible：定义是否显示可折叠按钮
*  minimizable：定义是否显示最小化按钮
*  maximizable：定义是否显示关闭按钮
*  closable：定义是否显示关闭按钮
*  tools：自定义工具菜单，可用值：1) 数组，每个元素都包含'iconCls'和'handler'属性；2) 指向工具菜单的选择器
*  closed：定义是否在初始化的时候关闭面板
*  href：从 URL 读取远程数据并且显示到面板
*  cache：如果为 true，在超链接载入时缓存面板内容
*  loadingMessage：在加载远程数据的时候在面板内显示一条消息

### 事件 ###

 *  onClose：在面板关闭之后触发
 *  onCollapse：在面板折叠之后触发
 *  onExpand：在面板展开之后触发

### 方法及方法参数 ###

 *  'panel'：返回面板对象
 *  'setTitle', title：设置面板头的标题文本

## tabs（选项卡） ##

 *  选项卡显示一批面板，但在同一个时间只会显示一个面板
 *  所用标签：

tab1

### 属性 ###

 *  width：选项卡容器宽度
 *  height：选项卡容器高度
 *  plain：设置为 true 时，将不显示控制面板背景
 *  fit：设置为 true 时，选项卡的大小将铺满它所在的容器
 *  border：设置为 true 时，显示选项卡容器边框
 *  toolPosition：选项卡位置。可用值：'top','bottom','left','right'
 *  tools：工具栏添加在选项卡面板头的左侧或右侧，可用的值有：1. 一个工具菜单数组，每个工具选项都和 linkbutton 相同；2. 一个指向 `<div/>`容器工具菜单的选择器
 *  tabWidth：标签条的宽度
 *  narrow：设置为 true 时，删除选项卡标题之间的空间
    

### 事件 ###

 *  onSelect: function(title, index)\{\} // 用户在选择一个选项卡面板的时候触发

### 方法及方法参数 ###

 *  'tabs'：返回所有选项卡面板
 *  'add', options：添加一个新选项卡面板
 *  'getSelected'：获取选择的选项卡面板
 *  'select', which：选择一个选项卡面板，'which'参数可以是选项卡面板的标题或者索引
 *  'exists', which：判断指定的面板是否存在

### 选项卡面板属性 ###

 *  选项卡面板属性与 panel 组件属性的定义类似
 *  closable：在设置为 true 的时候，选项卡面板将显示一个关闭按钮，在点击的时候会关闭选项卡面板

## accordion（分类） ##

 *  分类空间在同一时间只会显示一个面板，每个面板都内建支持展开和折叠功能，点击一个面板的标题将会展开或折叠面板主体
 *  所用标签：

content

## layout（布局） ##

 *  布局容器有 5 个区域：北、南、东、西和中间
 *  所用标签：

tab1

### 布局属性： ###

 *  fit：如果设置为 true，布局组件将自适应父容器

### 区域面板属性（与 panel 组件类似） ###

 *  region：定义布局面板位置，可用的值有：north, south, east, west, center
 *  border：为 true 时显示布局面板边框
 *  split：为 true 时用户可以通过分割栏改变面板大小

# linkbutton（按钮） #

 *  使用  按钮创建
    
 *  属性
    
     *  disabled：为 true 时禁用按钮
     *  plain：为 true 时显示简洁效果
     *  text：按钮文字
     *  iconCls：显示在按钮文字左侧的图标(16x16)
 *  事件
    
     *  onClick
 *  方法及方法参数
    
     
    
     *  'disable'：禁用按钮
     *  'enable'：启用按钮
        

# 表单 #

## form（表单） ##

 *  方法及方法参数
    
     *  'load', data：读取记录填充到表单中，数据参数可以是一个字符串或一个对象类型，如果是字符串则作为远程URL，否则作为本地记录
     *  'submit'：提交表单
     *  'clear'：清除表单数据
     *  'reset'：重置表单数据

``````````js
$('#ff').form('submit', { 
      url:..., 
      // 提交之前的回调函数 
      onSubmit: function(param){ 
          // 提交额外的参数 
          param.p1 = 'value1'; 
          param.p2 = 'value2'; 
      }, 
      // 提交成功后的回调函数 
      success: function(data){ 
          data = $.parseJSON(data); 
          if (data.success){ 
              alert(data.message) 
          } 
      } 
  });
``````````

## validatebox（验证框） ##

 *  验证规则：email、url、length\[0,100\]、remote\['[http://…/action.do','paramName][http_action.do_paramName]'\]
 *  属性
    
     *  required：定义为必填字段
     *  validType：定义字段验证类型，可用值有：1) 一个有效类型字符串运用一个验证规则；2) 一个有效类型数组运用多个验证规则
     *  missingMessage：当文本框未填写时出现的提示信息

## textbox（文本框） ##

 *  通过  创建文本框
 *  属性
    
     *  iconCls：在文本框显示背景图标
     *  prompt：在输入框显示提示消息
     *  value：默认值
     *  multiline：定义是否是多行文本框
 *  方法及方法参数
    
     *  'disable'：禁用组件
     *  'enable'：启用组件
     *  'setText', text：设置输入的文本
     *  'getText'：获取输入的文本
     *  'setValue,' value 设置组件的值
     *  'getValue'：获取组件的值

## combobox（下拉列表框） ##

### 属性 ###

 *  url：通过 URL 加载远程列表数据
 *  value：字段的默认值
 *  valueField：基础数据值名称绑定到该下拉列表框，默认值 value
 *  textField：基础数据字段名称绑定到该下拉列表框，默认值 text
 *  data：数据列表加载，参数类型 array
 *  queryParams：在向远程服务器请求数据的时候可以通过该属性像服务器端发送额外的参数，参数类型 object
 *  formatter：定义如何渲染行
 *  panelHeight：下拉面板高度，'auto'
 *  multiple：定义是否支持多选
 *  separator：在多选的时候使用何种分隔符进行分割
 *  editable：定义用户是否可以直接输入文本到字段中
 *  loadFilter: function(data) // 返回过滤后的数据并显示

### 方法及方法参数 ###

 *  'setText', text：设置输入的文本
 *  'getText'：获取输入的文本
 *  'setValue', value 设置组件的值
 *  'getValue'：获取组件的值
 *  'getValues'：获取组件值的数组
 *  'setValues', values：设置组件值的数组
 *  'reload', url：请求远程列表数据，通过 'url' 参数重写原始 URL 值
 *  'clear'：清除控件的值
 *  'select', data\[0\].value：设置默认选中第一项

## passwordbox（密码框） ##

## numberbox（数值输入框） ##

 *  min：允许的最小值
 *  max：允许的最大值

## datebox（日期输入框） ##

## datetimebox（日期时间输入框） ##

## filebox（文件框） ##

# 窗口 #

## window（窗口） ##

 *  一个浮动和可拖拽的面板
 *  属性扩展自 panel，新增的属性
    
     *  closed：定义是否在初始化的时候关闭面板
     *  shadow：如果设置为 true，在窗体显示的时候显示阴影
     *  draggable：定义是否能够拖拽窗口
     *  resizable：定义是否能够改变窗口大小
 *  事件完整继承自 panel
 *  方法扩展自 panel

## dialog（对话框窗口） ##

 *  一种特殊类型的窗口，在顶部有一个工具栏，在底部有一个按钮栏
 *  属性扩展自 panel(面板)，新增的属性：
    
     *  toolbar：设置对话框窗口顶部工具栏，可用值有：1) 一个数组，每一个工具栏中的工具属性都和 linkbutton 相同；2) 一个选择器指定工具栏
     *  buttons：对话框窗口底部按钮
 *  事件完全继承自 window
 *  方法扩展自 window

## messager（消息窗口） ##

 *  包含 alert(警告框), confirm(确认框), prompt(提示框), progress(进度框)等
 *  $.messager.show：消息窗口
 *  $.messager.alert：警告窗口，方法参数 title, msg, icon（可用值有：error,question,info,warning）, fn
 *  $.messager.confirm：确认消息窗口，方法参数 title, msg, fn(yes)
 *  $.messager.progress：进度消息窗体

# tree（树） #

 *  使用
    
     *  
     *  node 对象具备的属性：
        
         *  id：节点 ID
         *  text：显示节点文本
         *  iconCls:显示的节点图标
         *  children：一个节点数组声明了若干节点
         *  state：节点状态，'open' 或 'closed'，默认：'open'
         *  checked：表示该节点是否被选中
         *  attributes: 被添加到节点的自定义属性
     *  属性
        
         *  url：检索远程数据的 URL 地址
         *  formatter: function(node)\{\} // 定义如何渲染节点的文本
     *  事件
        
         *  onClick: function(node)\{\} // 在用户点击一个节点的时候触发
            

# datagrid（数据表格） #

 *  通过 标签创建
    
        |

## 属性 ##

 *  columns：列配置对象，类型 array
 *  frozenColumns
 *  fitColumns：自动展开/收缩列的大小，以适应网格的宽度，防止水平滚动，在设置 为 true 后，列配置中的 width 属性值则为比例
 *  toolbar：顶部工具栏，类型 array,selector
 *  striped：是否显示斑马线效果
 *  url：一个 URL 从远程站点请求数据
 *  pagination：如果为 true，则在数据表格控件底部显示分页工具栏，此时响应数据格式：\{"total":5,"rows":\[\{\}, \{\}, …\]\}，请求参数：page 表示查询第几页，rows 表示每页条数
 *  rownumbers：如果为 true，则显示一个行号列
 *  rownumberWidth：行号列宽度，默认 30
 *  singleSelect：如果为 true，则只允许选择一行
 *  pageSize：在设置分页属性的时候初始化页面大小，默认 10
 *  pageList：在设置分页属性的时候 初始化页面大小选择列表，默认 \[10,20,30,40,50\]
 *  queryParams：在请求远程数据的时候发送额外的参数
 *  showHeader：定义是否显示行头
 *  scrollbarSize：滚动条的宽度或高度

## 列属性 ##

 *  该元素也是一个数组对象，元素数组里面的元素是一个配置对象，用来定义每一个列字段
 *  title：列标题文本
 *  field：列字段名称（field 所使用的属性不能重复，无法进行输出格式化）
 *  width：列的宽度
 *  align：指明如何对齐列数据，可以使用的值有：'left','right','center'
 *  halign：指明如何对齐列标题
 *  sortable：如果为 true，则允许列使用排序
 *  hidden：如果为 true，则隐藏列
 *  checkbox：如果为 true，则显示复选框
 *  formatter: function(value, row, index)\{ \} // 单元格 formatter(格式化器)函数，参数：value 字段值、row 行对象、index 行索引

## 事件 ##

 *  事件继承自 panel，新增的事件
 *  onLoadSuccess: function(data)\{\} // 在数据加载成功的时候触发
 *  onClickRow: function(index, row)\{\} // 在用户点击一行的时候触发
 *  onDblClickRow: function(index, row)\{\} // 在用户双击一行的时候触发
 *  onSelect: function(index, row)\{\} // 在用户选择一行的时候触发
 *  onCheck: function(index, row)\{\} // 在用户勾选一行的时候触发

## 方法及方法参数 ##

 *  'load', param：加载和显示第一页的所有行
 *  'reload'：重载行，等同于 'load' 方法，但是它将保持在当前页
 *  'loadData', data：加载本地数据，旧的行将被移除
 *  'getData'：返回加载完毕后的数据
 *  'getRows'：返回当前页的所有行
 *  'getSelected'：返回第一个被选中的行或如果没有选中的行则返回 null
 *  'getSelections'：返回所有被选中的行，当没有记录被选中的时候将返回一个空数组
 *  'selectRow', index：选择一行，行索引从 0 开始
 *  'appendRow', row：追加一个新行
 *  'deleteRow', index：删除行


[http_action.do_paramName]: http://.../action.do','paramName