# BOM #

 *  BOM（Browser Object Model），浏览器对象模型，操作浏览器对象的 API

## window 对象 ##

*  表示浏览器中打开的窗口
*  如果文档包含框架（`<frame>` 或 `<iframe>` 标签），浏览器会为 HTML 文档创建一个 window 对象，并为每个框架创建一个额外的 window 对象
*  所有 JavaScript 全局对象、函数以及变量均自动成为 window 对象的成员
*  常用属性： opener：返回对创建此窗口的窗口的引用
   parent：返回父窗口
   top：返回最顶层的父窗口
   navigator：对 Navigator 对象的只读引用
   frames：返回窗口中所有命名的框架
   location：用于窗口或框架的 Location 对象
   history：对 History 对象的只读引用
   document：对 Document 对象的只读引用
   screen：对 Screen 对象的只读引用
*  常用方法
   `alert(message)`：消息框
   `confirm(message)`：确认框，返回一个 boolean 值
   `prompt(msg, defaultText)`：输入框，返回用户输入的内容
   `open(url, name, specs, replace)`：打开新窗口
   `close()`：关闭当前窗口
   `setTimeout(function, time)`：设置在 time 毫秒后**异步**调用 fn 函数，返回 timer
   `setInterval(function, time)`：设置每隔 time 毫秒**异步**调用 fn 函数，返回 timer
   `requestAnimationFrame(function)`：设置浏览器在下次重绘之前调用指定的回调函数更新动画（回调函数执行次数通常是每秒 60 次） `clearTimeout(timer)`：清除由 setTimeout 创建的定时器
   `clearInterval(timer)`：清除由 setInterval 创建的定时器
   `getComputedStyle(element)`：获取 element 对象的 CSS 样式声明对象

    > `window.chrome.loadTimes()` 

## navigator 对象 ##

 *  浏览器信息
 *  userAgent：返回由客户机发送服务器的 user-agent 头部的值
 *  platform：返回运行浏览器的操作系统平台
 *  cookieEnabled：返回指明浏览器中是否启用 cookie 的布尔值
 *  language：返回当前的浏览器语言

## location 对象 ##

 *  浏览器定位和导航
 *  href：文档所在地址的 URL 地址
 *  protocol
 *  hostname
 *  port
 *  host
 *  pathname
 *  search
 *  hash：文档所在地址的 URL 的锚部分（从 \# 号开始的部分）
 *  `reload()`：重新载入当前文档
 *  `assign(url)`：载入新的 URL，记录浏览历史
 *  `replace(url)`：载入新的 URL，不记录浏览历史
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414579380.png) 
    图 1 URL构成

## history 对象 ##

 *  浏览器当前窗口的浏览历史
 *  length：返回浏览器历史列表中的 URL 数量
 *  `back()`：加载 history 列表中的前一个 URL
 *  `forward()`：加载 history 列表中的下一个 URL
 *  `go(number|url)`：加载 history 列表中的某个具体页面

## screen 对象 ##

 *  屏幕信息

## 存储对象 ##

 *  localStorage 用于长久保存整个网站的数据，保存的数据没有过期时间，直到手动去除
 *  sessionStorage 用于临时保存同一窗口（或标签页）的数据，在关闭窗口或标签页之后将会删除这些数据
 *  属性length：返回存储对象中包含多少条数据
 *  方法`getItem(keyname)`：返回指定键的值`key(n)`：返回存储对象中第 n 个键的名称`setItem(keyname, value)`：添加键和值，如果对应的值存在，则更新该键对应的值`removeItem(keyname)`：移除键`clear()`：清除存储对象中所有的键

## document 对象 ##

 *  HTML 文档的根节点
 *  属性
    
     *  links、forms、images
     *  title、URL、domain、documentElement、body、bgColor、fgColor、readyState（文档的加载状态：loading、interactive、complete）
     *  cookie
 *  方法
     `write()`：在页面中输出文本
     `writeln()`：在页面中输出文本，输完后换行
     `getElementById(idVal)`：返回文档中 id 属性值为 idVal 的 HTML 元素
     `createElement(tagName)`：创建 Tag 标签对应的节点

# DOM #

 *  DOM（Document Object Model），文档对象模型，操作文档对象的 API
 *  在 HTML DOM 中，每一个元素都是节点：文档节点（document 对象）、元素节点、属性节点、文本节点、注释节点
    
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414580069.png) 
    图 2 DOM\_Tree模型

## 节点对象 ##

 *  对于 HTML 页面而言，浏览器会将页面中的"空白"也当成文本节点
*  属性
   childNodes：返回当前节点的子节点的节点列表（包括文本节点）
   children：返回当前节点的子节点的节点列表（不包括文本节点）
   firstChild、lastChild、parentNode
   previousSibling：返回节点之前紧跟的同级节点
   nextSibling：返回节点之后紧跟的同级节点
*  方法
   `hasChildNodes()`：判断当前节点是否有子节点
   `appendChild(newNode)`：将 newNode 添加成当前节点的最后一个子节点
   `insertBefore(newNode, refNode)`：在 refNode 节点之前插入 newNode 节点
   `removeChild(node)`：删除并返回 node 子节点
   `replaceChild(newChild, oldChild)`：将 oldChild 节点替换成 newChild 节点
   `getElementsByName(name)`：返回带有指定名称的对象组成的数组
   `getElementsByTagName(tagName)`：返回带有指定标签名的对象组成的数组
   `querySelector(selectors)`：返回文档中与指定选择器或选择器组匹配的第一个 html 元素，如果找不到匹配项，则返回 null
   `querySelectorAll(selectors)`：返回与指定的选择器组匹配的文档中的元素列表，返回的对象是 NodeList

## 元素对象 ##

 * 元素可包含属性、其他元素或文本

*  属性

   |           | 元素节点 | 属性节点 | 文本节点 |
   | --------- | -------- | -------- | -------- |
   | nodeType  | 1        | 2        | 3        |
   | nodeName  | 标签名   | 属性名   | #text    |
   | nodeValue | null     | 属性值   | 文本内容 |

*  方法
   `getAttributeNode(name)`：获取的属性节点
   `getAttribute()`：获取指定元素的属性值（获取到的是属性值字符串）
   `setAttribute(name, value)`：设置或者改变指定属性并指定值

## HTML 元素 ##

 *  常用属性（获取到的是属性值转换过的实用对象）
    innerHTML：获取或设置 HTML 元素内部的代码内容
    innerText、textContent：获取或设置 HTML 元素的文本内容，即该元素在页面上呈现的内容
    value：获取或设置**表单控件**的呈现内容
    id：获取或设置 HTML 元素的 id 属性值
    className：获取或设置 HTML 元素的 class 属性值
    style.CSS 属性名、style.cssText：获取或设置 HTML 元素的**内联** CSS 样式，如 style["background-color"] 或 style.backgroundColor
    dataset：获取 HTML 元素上 `data-*` 属性集，`data-*` 属性可用于在元素上保存数据

## 其它对象的属性及方法 ##

 *  表单
    name、autocomplete、action、method、elements
    `reset()`：重设表单，将所有表单域的值设置为初始值，可重置元素：input, keygen, output, select, textarea，触发表单 reset 事件（元素重置时不再触发元素上的 change 和 input 事件）
    `submit()`：提交表单
    `checkValidity()`
*  列表框、下拉菜单：selectedOptions（返回列表框中所有选中选项组成的数组）、value、selectedIndex
*  列表框、下拉菜单的选项：selected
*  单选框、复选框：checked

# 事件驱动编程 #

 *  核心对象：事件源（各种 DOM 元素）、事件名称、事件响应函数、事件对象 event

## 事件流 ##

 *  事件传递的过程：capture phase、target phase、bubble phase

## 事件注册、取消与触发 ##

 *  对 DOM 元素绑定事件处理函数
 *  将事件处理函数绑定到 HTML 元素属性时，可以为函数传入 this、event 等参数
 *  方式
    
     *  绑定到 HTML 元素的属性
     *  window.onload 后，绑定到 DOM 对象属性，如 `元素.onclick=响应函数引用`
     *  window.onload 后，调用事件绑定方法，对元素添加事件类型、响应函数

``````````js
// 事件注册 
    var addEvent = document.addEventListener ? 
        function(element, eventType, listener, useCapture) { 
            element.addEventListener(eventType, listener, useCapture); // W3C 
        } : 
        function(element, eventType, listener, useCapture) { 
            element.attachEvent('on' + eventType, listener); // IE 
        }; 
    
    // 取消事件注册 
    var delEvent = document.removeEventListener ? 
        function(element, eventType, listener, useCapture) { 
            element.removeEventListener(eventType, listener, useCapture); // W3C 
        } : 
        function(element, eventType, listener, useCapture) { 
        element.detachEvent('on' + eventType, listener); // IE 
        }; 
    
    // 事件触发 
    element.dispatchEvent(eventType); // W3C 
    element.fireEvent('on' + eventType); // IE
``````````

## 事件对象 event ##

 *  属性
    target（W3C）、srcElement（IE）：事件源
    type：事件类型
    currentTarget
*  阻止事件传播 `event.stopPropagation()`（W3C）
   `event.cancelBubble = true`（IE）
   `event.stopImmediatePropagation()`（W3C）
*  阻止默认行为 `Event.preventDefault()`（W3C）
   `Event.returnValue = false`（IE）

## 事件类型 ##

 *  **事件名称**由**事件类型**前加一个“on”前缀构成

### 常见的事件类型 ###

 *  [事件参考][Link 1]
 *  资源事件：load（资源及其依赖资源已完成加载）、beforeunload（资源将要被卸载时）、unload（文档或一个依赖资源正在被卸载）、error（资源加载失败时）、abort（正在加载资源已经被中止时）
*  焦点事件：focus（元素获得焦点）、blur（元素失去焦点）
*  表单事件：submit（点击提交按钮）、reset（点击重置按钮时）、invalid（表单元素验证不通过）
*  值变化事件：input（<input>, <select> 或 <textarea> 的元素值被改变）、readystatechange（readyState 的值发生改变）
*  存储事件：change（<input>, <select> 或 <textarea> 的元素值被改变，只在用户提交更改时触发）
*  视图事件：resize（窗口或框架被重新调整大小）、scroll（页面发生滚动）
*  键盘事件：keydown（按下任意按键）、keypress（除 Shift, Fn, CapsLock 外任意键被按住）、keyup（释放任意按键）
*  鼠标事件：click、dbclick、mouseenter、mouseleave、contextmenu（右键菜单显示前）
     ![笑小枫-www.xiaoxiaofeng.com](https://image.xiaoxiaofeng.site/spider/2023/4/25/xxf-1682414580693.png) 
     图 3 事件类型

``````````js
$(function () { 
        // 按回车键登陆 
        $(document).keyup(function (event) { 
            if ((event.keyCode || event.which) === 13) { 
                $("#loginForm").submit(); 
            } 
        }); 
    });
``````````


[URL]: https://static.sitestack.cn/projects/sdky-java-note/91e5dcfdc3a9c6d796419ff88330f99b.png
[DOM_Tree]: https://static.sitestack.cn/projects/sdky-java-note/e9332b676493944916d9a74108e55564.png
[Link 1]: https://developer.mozilla.org/zh-CN/docs/Web/Events
[90cfe28121d236384cde85e962d37a18.png]: https://static.sitestack.cn/projects/sdky-java-note/90cfe28121d236384cde85e962d37a18.png