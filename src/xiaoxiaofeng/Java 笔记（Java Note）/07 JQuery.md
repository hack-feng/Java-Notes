 *  JavaScript 框架，作用：优化 HTML 文档操作、事件处理、动画设计、Ajax 交互
 *  基础语法：`$(selector).action()`

# jQuery 核心函数 #

 *  `$(expression)`：根据表达式字符串（通常由 CSS 选择器组成，多个选择器用**逗号**分隔）来查找所有匹配的元素，返回一个类似数组的 jQuery 对象
 *  `$(html)`：用 HTML 标记字符串创建 DOM 元素，返回 jQuery
 *  `$(html, props)`：用 HTML 标记字符串创建 DOM 元素，同时设置一些属性、事件等，props 是一个形如 \{prop:value, prop2:value\} 的对象
 *  `$(elements)`：将一个或多个 DOM 元素包装为 jQuery 对象
 *  `$(fn)`：当 DOM 载入就绪可以查询及操纵时绑定一个要执行的函数

# jQuery 对象访问 #

 *  `size()`：jQuery 对象中元素的个数
 *  length：jQuery 对象中元素的个数
 *  selector：返回传给 $() 的原始选择器
 *  `get()`：返回由 jQuery 里包含的所有 DOM 元素组成的数组
 *  `get(index)`：返回 jQuery 里包含的第 index 个 DOM 元素，`$(this).get(0)` 与 `$(this)[0]` 等价
 *  `index()`：返回当前元素在**同辈**中的索引位置
 *  `first()`：获取第一个元素包装成的 jQuery 对象
 *  `last()`：获取最后一个元素包装成的 jQuery 对象
 *  `eq(index|-index)`：获取第 N 个元素包装成的 jQuery 对象
 *  `each(callback)`：遍历 jQuery 里包含的每个元素，同时指定遍历每个元素时执行的回调函数（回调函数的参数：index、item）

# 查找 #

 *  `children(expression)`：查找当前 jQuery 对象（实际是该对象包含的 DOM 对象）之内的所有子元素
 *  `closest(expression)`：从当前元素开始，逐级向上级元素匹配，并返回**最先匹配** expression 的元素
 *  `find(expression)`：找出当前元素中与指定 expression 匹配的所有后代元素
 *  `siblings([expression])`：查找当前 jQuery 对象包含的 DOM 对象前后的所有匹配 selector 选择器的兄弟元素

# 选择器 #

## 以 CSS 选择器访问 DOM 元素 ##

### 基本选择器 ###

 *  `#`idVal：根据给定的 ID 匹配一个元素，返回 Array
 *  tagName：根据给定的标签名匹配所有元素，返回 Array
 *  `.`className：根据给定的类匹配元素，返回 Array
 *  `selector1, selector2, selectorN`：将**每一个**选择器匹配到的元素合并后一起返回

### 层级选择器，返回 Array ###

 *  outer inner：在给定的祖先元素下匹配所有的**后代**元素
 *  parent > child：在给定的父元素下匹配所有的**子**元素
 *  prev + next：匹配所有**紧接**在 prev 元素后的 next 元素（同级）
 *  prev ~ siblings：匹配 prev 元素**之后的所有** siblings 元素（同级）

### 属性选择器 ###

 *  `tagName[attribute]`：匹配包含给定属性的元素
 *  `tagName[attribute=value]`：匹配给定的属性**是**某个特定值的元素
 *  `tagName[attribute!=value]`：匹配所有**不含有**指定的属性，或者属性不等于特定值的元素
 *  `tagName[attribute^=value]`：匹配给定的属性是以某些值**开始**的元素
 *  `tagName[attribute$=value]`：匹配给定的属性是以某些值**结尾**的元素
 *  `tagName[attribute=value]`*：匹配给定的属性是以\*包含*某些值的元素
 *  `tagName[attributeFilterl][attributeFilter2]…`：复合属性选择器，需要同时满足多个条件

## 选择器的附加限定词 ##

### 基本 ###

 *  `:first`：获取第一个元素
 *  `:last`：获取最后一个元素
 *  `:even`：匹配所有索引值为偶数的元素，从 0 开始计数
 *  `:odd`：匹配所有索引值为奇数的元素，从 0 开始计数

### 子元素 ###

 *  `:first-child`：匹配**父元素**下的第一个子元素
 *  `:last-child`：匹配**父元素**下的最后一个子元素
 *  `:nth-child(index)`：匹配其**父元素**下的第 index（从 1 开始）子元素，index 可以使用 even、odd、3n+1 等

### 内容 ###

 *  `:contains(text)`：匹配包含给定文本的元素
 *  `:has(selector)`：匹配含有选择器所匹配元素的元素

## 表单相关的选择器 ##

### 表单 ###

 *  `:input`：匹配所有 input、select、textarea 和 button 元素
 *  `:button`：匹配所有按钮
 *  `:radio`：匹配所有单选按钮
 *  `:checkbox`：匹配所有复选框
 *  `:password`：匹配所有密码框

### 表单对象属性 ###

 *  `:checked`：匹配所有选中的被选中元素（复选框、单选框等，不包括 select 中的 option）
 *  `:selected`：匹配所有选中的 option 元素
 *  `:enabled`：匹配所有可用元素
 *  `:disabled`：匹配所有不可用元素

# 操作元素内容的相关方法 #

 *  `val()`：获得第一个匹配元素的当前值
 *  `val(val)`：设置每一个匹配元素的值
 *  `html()`：取得第一个匹配元素的 **html 内容**
 *  `html(val)`：设置每一个匹配元素的 html 内容
 *  `text()`：取得第一个匹配元素的**文本内容**
 *  `text(val)`：设置所有匹配元素的文本内容

# 操作属性的相关方法 #

 *  `attr(name|properties|key, value|fn)`：可以操作原生属性以及自定义属性，获取元素的 checked、selected 或 disabled 等属性时，返回值是 checked、selected、disabled 或 undefined
 *  `prop(name|properties|key, value|fn)`：只能操作原生属性，获取元素的 checked、selected 或 disabled 等属性时，返回值是 true 或 false
 *  `data("xxx")`：获取属性名为 data-xxx 的属性值（注意：自定义的属性名应使用 H5 风格，`data("xxx")` 中的 xxx 必须全小写；如果该属性值为 JSON 格式字符串会自动转为对象，此时要注意 `data-xxx='…'` 应使用单引号）
 *  `prop(name)`：取得第一个匹配元素的属性值
 *  `prop(properties)`：将一个“名/值”形式的对象设置为所有匹配元素的属性
 *  `prop(name, value)`：为所有匹配的元素设置一个属性值
 *  `prop(name, function(index, attr))`：为所有匹配的元素设置一个计算的属性值，index 为当前元素的索引值，attr 为原先的属性值

# 操作 CSS 属性的相关方法 #

 *  `hasClass(class)`：判断当前的元素是否含有某个特定的类
 *  `addClass(class)`：为每个匹配的元素添加指定的类名
 *  `removeClass([class])`：从所有匹配的元素中删除全部或者指定的类
 *  `toggleClass(class)`：如果存在就删除一个类，如果不存在就添加一个类
 *  `toggleClass(class, switch)`：如果开关 switch 参数为 true 则加上对应的 class，否则就删除
 *  `css(name)`：访问第一个匹配元素的样式属性值
 *  `css(name, value)`：在所有匹配的元素中，设置**一个**样式属性的值
 *  `css(properties)`：把一个“名/值对”对象设置为所有匹配元素的样式属性，properties 是一个形如 \{keyl:vall, key2:val2. . . \} 的对象
 *  `height()`：返回 jQuery 对象里第一个匹配的元素的当前高度（以 px 为单位）
 *  `height(val)`：设置 jQuery 对象里所有元素的高度，val 的单位为 px
 *  `width()`：返回 jQuery 对象里第一个匹配的元素的当前宽度（以 px 为单位）
 *  `width(val)`: 设置 jQuery 对象里所有元素的宽度，val 的单位为 px
 *  `innerWidth()`：返回 jQuery 对象里第一个匹配的元素的内部宽度（content + padding）
 *  `outerWidth()`：返回 jQuery 对象里第一个匹配的元素的外部宽度（content + padding + border）
 *  `outerWidth(true)`：返回 jQuery 对象里第一个匹配的元素的外部宽度，包括页边距（content + padding + border + margin）

# 操作 DOM 节点的相关方法 #

 *  content 的类型可以为 String、Element、jQuery

## 在指定节点内插入新节点 ##

 *  `append(content)`：向每个匹配的元素内部追加 content
 *  `appendTo(expression)`：把所有匹配的元素追加到 expression 指定的元素集合中
 *  `prepend(content)`：向每个匹配的元素内部前置 content
 *  `prependTo(expression)`：把所有匹配的元素前置到 expression 指定的元素集合中

## 在指定节点外添加节点 ##

 *  `after(content)`：在每个匹配的元素之后插入 content
 *  `insertAfter(expression)`：把所有匹配的元素插入到 expression 指定的元素集合的后面
 *  `before(content)`：在每个匹配的元素之前插入 content
 *  `insertBefore(expression)`：把所有匹配的元素插入到 expression 指定的元素集合的前面

## 替换 ##

 *  `replaceWith(content)`：将所有匹配的元素替换**成** content
 *  `replaceAll(expression)`：用匹配的元素替换**掉**所有 expression 匹配到的元素

## 删除 ##

 *  `empty()`：删除匹配的元素集合中所有的**子节点**
 *  `remove()`：从 DOM 中删除当前元素，并且其它的比如绑定的事件、附加的数据等都会被移除
 *  `detach()`：从 DOM 中删除当前元素，但其它的比如绑定的事件、附加的数据等会保留下来

## 复制 ##

 *  `clone()`：复制所有匹配的元素，不包括其所有的事件处理，返回复制的 jQuery 对象
 *  `clone(true)`：复制所有匹配的元素，包括每个元素的所有的事件处理，返回复制的 jQuery 对象

# 绑定事件相关方法 #

 *  所谓事件命名空间，就是事件类型后面以点语法附加一个别名，以便引用事件。
 *  `bind(type, [data], fn)`：为每个匹配元素绑定事件处理函数，可多次绑定，返回当前 jQuery 对象
 *  `unbind([type], [fn])`：从每一个匹配的元素中删除所有绑定的事件，如果没有参数，则删除所有绑定的事件
 *  `hover(over, out)`：绑定鼠标移入及移出分别触发的函数
 *  `toggle(fn, fn2, …)`：每次点击都重复对这几个函数的轮番调用
 *  `type(fn)`：在每一个匹配元素的 type 事件中绑定一个处理函数，如 click、mouseover、mouseout、blur、submit 等
 *  `on(events, [selector], [data], fn(eventObject))`：为当前 jQuery 对象中匹配 selector 的所有**子元素**的一个或多个事件 events 绑定事件处理函数 fn，使用 on() 方法添加的事件处理程序适用于当前及未来的元素（比如由脚本创建的新元素）
 *  `off(events,[selector],[fn(eventObject)])`：从当前 jQuery 对象包含的 DOM 元素中删除绑定的事件处理函数；如果没有指定任何参数，则删除 jQuery 对象中所有 DOM 元素上的事件处理函数（可指定命名空间）；如果指定了 selector，则只删除匹配 selector 的 DOM 元素上的事件处理函数；如果指定了 fn，则只删除该事件处理函数 eventObject
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414592424.jpeg) 
    图 1 为特定事件绑定处理函数的方法

# Ajax 相关方法 #

 *  `$.ajax([options])`：通过 HTTP 请求加载远程数据，返回其创建的 XMLHttpRequest 对象
    
     *  参数：options 类型为 Object
     *  常用选项：
        
         *  async，指定是否时异步请求，默认 true
         *  type，指定请求方式，默认 GET
         *  url，指定发送请求的地址，默认当前页地址
         *  data，指定发送到服务器的数据，类型可为 Object、String
         *  contentType：发送信息至服务器时内容编码类型，默认值 "application/x-www-form-urlencoded"（提交的数据按照 key1=val1&key2=val2 的方式进行编码，key 和 val 都进行了 URL 转码）；"application/json"（提交的数据是序列化后的 JSON 字符串）
         *  dataType，指定预期服务器返回的数据类型，可用值：html、json、xml、text、jsonp、script（如果不指定，jQuery 将自动根据 HTTP 包 MIME 信息来智能判断）
         *  success，指定请求成功后的回调函数，参数：data、textStatus，服务器返回的数据
         *  error，指定请求失败时的回调函数，参数：XMLHttpRequest、textStatus（错误信息）、errorThrown
 *  `$.get(url, [data], [callback], [dataType])`：通过 GET 方式发送 Ajax 请求
 *  `$.getJSON(url, [data], [callback])`：通过 HTTP GET 请求载入 JSON 数据，可以通过使用 JSONP 形式的回调函数来加载**其它网域**的 JSON 数据，如 "myurl?callback=?"，jQuery 将自动替换 ? 为正确的回调函数名，并调用 js 解释器对返回的数据进行解析
 *  `$.post(url, [data], [callback], [dataType])`：通过 POST 方式发送 Ajax 请求
    
     *  参数：
        
         *  url，发送请求的地址
         *  data，待发送 Key/value 参数，类型为 Map
         *  callback，请求成功后的回调函数
         *  dataType，返回内容格式，可用值：xml、html、script、json、text
 *  `load(url, [data], [callback])`：向远程 url 发送异步请求，并直接将服务器响应插入当前 jQuery 对象匹配的 DOM 元素之内，参数：data 是一个形如 \{key1:val1, key2:val2 …\} 的 JavaScript 对象，代表发送请求的请求参数；callback 用来指定载入成功后的回调函数
 *  `$.param(obj)`：将 obj 参数（对象或数组）转换成查询字符串
 *  `serialize()`：将该 jQuery 对象包含的表单或表单控件转换成查询字符串， 形如 a=1&b=2&c=3&d=4&e=5
 *  `serializeArray()`：将该 jQuery 对象包含的表单或表单控件转换为一个数组，每个数组元素都是形如 \{name:fieldName, value:filedVal\} 的对象，其中 fieldName 是对应表单控件的 name 属性，fieldVal 是对应表单控件的 value 属性

``````````js
$.ajax({ 
     type: "POST", 
     url: "some.php", 
     data: "name=John&location=Boston", 
     /*xhrFields: { 
         withCredentials: true // 设置跨域请求时是否携带 cookie 
     },*/ 
     success: function(msg){ 
       alert( "Data Saved: " + msg ); 
     } 
  }); 
  $.get("test.cgi", { name: "John", time: "2pm" }, 
    function(data) { 
      alert("Data Loaded: " + data); 
  }); 
  
  // 禁用序列化表单元素数组或者对象 
  jQuery.ajaxSettings.traditional = true;
``````````

# 工具方法 #

 *  `$.trim(str)`：截断字符串前后的空白
 *  `$.parseJSON(string)`：将**符合 JSON 规范**的字符串解析成 JavaScript 对象或数组
 *  `$.parseXML(string)`：将符合 XML 规范的字符串解析成 XML 节点
 *  `$.extend([deep], target, object1 , [objectN])`：用于将 object1、objectN 的属性合并到 target 对象里。如果 target 里有和 object1、objectN 同名的属性，则 object1、objectN 的属性值将覆盖 target 的属性值；如果 target 不包含 object1、objectN 里所包含的属性值，则 object1、objectN 的属性值将会新增到 target 对象里。如果第一个参数设置为 true，则 jQuery 返回一个深层次的副本，递归地复制找到的任何对象，否则的话，副本会与原对象共享结构
 *  `$.each(object, callback)`：遍历 JavaScript 对象或数组，同时指定遍历每个元素时执行的 callback 函数（回调函数的参数：对象的属性名或数组的索引 index、对应的属性值或数组元素 val，如果想中途退出 `each()` 遍历，则让 callback 函数返回 false）
 *  `$.inArray(value, array, [fromIndex])`：返回 value 在 array 中出现的位置（从 0 开始），如果 array 中不包含 value 元素，则返回 -1
 *  `$.map(array, callback)`：将 array 数组转换到另一个数组，map 将会依次将 array 数组元素的值和索引传入 callback 函数（参数：val、index），每次传入 callback 函数的返回值将作为新数组的元素
 *  `$.grep(array, callback, [invert])`：用于对 array 数组进行筛选，grep 将会依次将 array 数组元素的值和索引传入 callback 函数（参数：val, index），如果 callback 函数返回 true 则保留该数组元素，否则删除数组元素；如果将 invert 指定为 true，则当 callback 函数返回 true 时，反而会删掉该数组元素
 *  `$.merge(first, second)`：将 first 和 second 两个数组的元素合并到新数组，不会删除重复值
 *  `$.unique(array)`：删除 array 数组中的重复值
 *  `$.type(obj)`：返回 obj 代表的类型：当 obj 不存在或为 undefined 时返回 "undefined"；当 obj 为 null 时返回"null”；当 obj 为 true 或 false 时返回 "boolean"；当 obj 为数值时返回 "number"；当 obj 为字符串时返 "string"；当 obj 为函数时返回 "function"；当 obj 为数组时返回 "array"；当 obj 为日期时返回 "date"；当 obj 为正则表达式时返回 "regexp"；其余返回 "object"

# 动画效果相关方法 #

 *  `show([speed,[easing],[fn]])`：将该 jQuery 对象里包含的隐藏的 DOM 元素**显示**出来
 *  `hide([speed,[easing],[fn]])`：将该 jQuery 对象里包含的显示的 DOM 元素**隐藏**起来
 *  `slideToggle([speed,[easing],[fn]])`：将该 jQuery 对象里包含的显示的 DOM 元素以“滑动”的方式隐藏或显示
 *  `fadeToggle([speed,[easing],[fn]])`：将该 jQuery 对象里包含的显示的 DOM 元素以淡入或淡出效果显示或隐藏

# 数据存储相关方法 #

 *  `data(name,value)`：向 jQuery 对象里存储 name:value 的数据对
 *  `data(object)`：向 jQuery 对象中一次存入多个 name:value 数据对，其中 object 是 形如 \{name:value…\} 的对象
 *  `data(name)`：获取 jQuery 对象里存储的 key 为 name 的数据
 *  `data()`：获取 jQuery 对象中存储的所有数据，返回一个 \{name:value…\} 形式的对象
 *  `removeData([name])`：删除 jQuery 对象里存储的 key 为 name 的数据

# jQuery 的回调支持 #

 *  `$.Callbacks()`：创建一个回调函数列表 Callbacks
 *  Callbacks 对象支持的方法
    
     *  `add(callbacks)`：将一个或多个回调函数添加到 Callbacks 对象中
     *  `fire(arguments)`：使用指定参数 arguments 激发该 Callbacks 对象中所有回调函数
     *  `remove(callbacks)`：从该 Callbacks 对象中删除一个或多个回调函数\`
     *  `empty()`：从该 Callbacks 对象中删除所有回调函数

# jQuery 的 Deferred 延迟对象 #

 *  `$.Deferred()`：定义一个 Deferred 对象
 *  Deferred 对象支持的方法
    
     *  `resolve(args)`：将 Deferred 对象状态转为 "resolved"，该 Deferred 对象上通过 done() 方法添加的函数将会被激发，args 作为参数传入 done() 方法添加的函数
     *  `reject(args)`：将 Deferred 对象状态转为 "rejected"，该 Deferred 对象上通过 fail() 方法添加的函数将会被激发，args 作为参数传入 fail() 方法添加的函数
     *  `state()`：返回该 Deferred 对象包含的异步操作所处的执行状态字符串：pending（任务执行中、未完成）、resolved（任务完成）、rejected（任务失败）
     *  `promise([target])`：返回 Deferred 对象对应的 Promise 对象（它相当于 Deferred 对象的副本，不允许开发者通过 Promise 对象修改 Deferred 对象的状态）。如果指定了 target 参数，在会该参数指定的对象上增加 Deferred 接口。从 jQuery 1.5 开始，所有 jQuery 对象都增加了 promise() 方法
     *  `done(doneCallbacks)`：指定该 Deferred 对象包含的异步操作执行成功后激发的回调函数
     *  `fail(doneCallbacks)`：指定该 Deferred 对象包含的异步操作执行失败后激发的回调函数
     *  `progress(progressCallbacks)`：指定该 Deferred 对象包含的异步操作执行过程中激发的回调函数
     *  `then(doneCallbacks, failCallbacks [, progressCallbacks])`：分别为 Deferred 对象包含的异步操作完成时、失败时、进行中指定一个或多个回调函数
     *  `always(alwaysCallbacks [, alwaysCallbacks])`：无论 Deferred 对象包含的异步操作执行成功还是执行失败，总会激发该方法指定的回调函数

# 扩展 jQuery #

 *  `$.fn.extend(object)`：为所有 jQuery 对象扩展新的方法，其中 object 是一个形如 \{name1: fn1, name2, fn2..\} 的对象，经过这种扩展之后，所有 jQuery 对象都可使用 name1、name2 等方法
 *  `$.extend(object)`：为 jQuery 命名空间扩展新的方法，其中 object 是一个形如 \{name1: fn1, name2, fn2..\} 的对象，经过这种扩展之后，jQuery 命名空间下就会增加 name1、name2 等方法


[cc250dd9d38c1c340c1969e5f86a9517.jpeg]: https://static.sitestack.cn/projects/sdky-java-note/cc250dd9d38c1c340c1969e5f86a9517.jpeg