- HTML（Hyper Text Markup Language），超文本标记语言
- XHTML（extensible Hyper Text Markup Language），扩展的超文本标记语言
- [MDN HTML 文档(opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/HTML)

# HTML 5 的语法变化

1. 标签不再区分大小写
2. 元素可以省略结束标签
3. 允许省略属性值的属性
   HTML 5 中允许省略属性值的属性：checked、readonly、disabled、selected、multiple、required 等
4. 允许属性值不使用引号

# HTML 元素

- HTML 文档声明：<!DOCTYPE html>
- 根标签`<html>`
- 开始标签、属性、元素内容、结束标签
- 空标签（没有内容的元素），没有闭合标签
  - 在 HTML 中，空标签没有结束标签
  - 在 XHTML 中，空标签必须在开始标签中被关闭（在右尖括号前加上反斜杠）
  - 如：<input />、<img />、<isindex />、<area />、<base />、<basefont />、<bgsound />、<col />、<embed />、<frame />、<keygen />、<link />、<meta />、<nextid />、<param />、<plaintext />、<spacer />、<wbr />、<!DOCTYPE html>、<!-- -->

# HTML 属性

- [HTML 元素参考(opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element)
- 属性值应该始终被包括在引号内
- 核心属性
  - id：定义元素的唯一id
  - class：定义元素的类名（classname）
  - style：规定元素的行内样式（inline style）
  - title：规定元素的额外信息，可在工具提示中显示（在鼠标移到元素上时显示一段工具提示文本）
  - hidden：为 true 时，通知浏览器不显示该组件

![HTML5标签集合](https://image.xiaoxiaofeng.site/blog/2023/04/25/xxf-20230425181333.jpeg?xxfjava)

# 文档头部 `<head>`

- 在 `<head>` 元素中可以插入脚本（scripts）、样式文件（CSS）、及各种 meta 信息

- `<head>`，定义头部标签

  -`<title>`，定义文档标题

  - `<meta>` (opens new window)

    ，基本的元数据，通常以 名称/值 对出现，属性：

    - [name (opens new window)](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/meta/name)，把 content 属性关联到一个名称，属性值：application-name、author、description、generator、keywords、referrer（控制所有从该文档发出的 HTTP 请求中 `Referer` 的内容，如 `no-referrer-when-downgrade`）
    - http-equiv，把 content 属性关联到 HTTP 头部，值：content-type、default-style、refresh
    - content，必需属性，定义与 http-equiv 或 name 属性相关的元信息
    - charset，文档的字符编码，`<meta charset="UTF-8">`

  - `<link>`：定义文档与外部资源之间的关系，属性：rel（必需属性，当前文档与被链接文档之间的关系，如 stylesheet、icon）、type（被链接文档的 MIME 类型）、href（被链接文档的位置）

  - `<style>`，用于引入样式定义，必需属性：type

  - `<script>`，用于加载脚本文件，属性 type：指示脚本的 MIME 类型，默认值是 "text/javascript"；async 属性：仅适用于外部脚本，规定脚本将被异步执行，属性值 "async"；defer 属性：规定是否对脚本执行进行延迟，直到页面加载为止，属性值 "defer"

```html
<script type="text/javascript">
    function goPage(num) {
        // 找到表单，给 currentPage 设值，然后提交
        var form1 = document.getElementById("form1");
        form1.currentPage.value  = num;
        form1.submit();
    }
</script>
```

# 块

## 块级元素（block element）

- 总是在**新行**上开始
- 高度，行高以及外边距和内边距都可控制
- 宽度缺省是它的容器的 100%，除非设定一个宽度
- 可以容纳内联元素和其它块级元素
- 常见块级元素：`<h1>、<div>、<p>、<ul>、<table>、<form>、<pre>`

## 内联元素（inline element）

- 和其它元素都在一行上
- 高、行高及外边距和内边距不可改变
- 宽度就是它的文字或图片的宽度，不可改变
- 内联元素只能容纳文本或者其它内联元素
- 常见内联元素：`<span>、<b>、<strong>、<td>、<a>、<img>、<input>、<select>、<textarea>、<label>`

## 布局

- `<div>`，块级元素，用于组合其他 HTML 元素的容器
- `<span>`，内联元素，用于组合文档中的内联元素（默认不会换行）

# 文档章节

- `<body>、<header>、<nav>、<asside>、<article>、<section>、<footer>、<hx>`

![主体结构标签](https://sdky.gitee.io/assets/img/主体结构标签.84d49c59.jpg)

# 基本标签

- `<h1>` 至 `<h6>`，标题
- `<p>`，段落，会自动在其前后创建一些空白
- `<br />`，换行，空标签
- `<hr />`，水平线，空标签
- `<!-- 注释内容 -->` ，注释

# 文本格式化

-  `<font>`，定义字体颜色、大小、字体，属性：color、size、face
- `<b>`，定义粗体文本
- `<strong>`，定义粗体文本
- `<i>`，定义斜体字
- `<em>`，定义强调文本，实际效果与斜体文本差不多
- `<small>`，定义小号字体文本
- `<sub>`，定义下标文本
- `<sup>`，定义上标文本
- `<tt>`，定义等宽文本
- `<ins>`，定义插入字
- `<del>`，定义删除字
- `<mark>`，定义带有记号（高亮）的文本
- `<cite>`，定义作品的标题
- `<q>`，定义一个短的引用，在引用的周围插入引号
- `<bdo>`，定义文本显示方向，属性 dir 的值：ltr（左到右）、rtl（右到左）
- `<pre>`，预格式文本，会保留空格、回车、Tab 键和其它格式字符
- `<address>`，地址
- 注意：HTML 代码中的所有连续的空行和换行被显示为一个空格

# 超链接 `<a>`

- 作用
  1. 创建指向另一个文档的链接
  2. 创建一个文档内部的锚点
  3. 链接到 Email 地址
- 属性
  - href，其属性的值可以是任何有效文档的相对或绝对 URL，包括片段标识符和 JavaScript 代码段
    1. \# 包含了一个位置信息，默认的锚是 #top 也就是网页的上端，在页面很长的时候会使用 # 来定位页面的具体位置，格式为：#id属性、#<a> 标签的 name 属性
    2. 定义一个死链接 `href="javascript:void(0);"`
    3. 链接到 Email 地址 `href="mailto:admin@gmail.com"`
    4. 拨号 `href="tel:13612345678"`
  - target，被链接的文档在何处显示，值： _self（自身框架）、 _blank（新窗口）、_parent（父框架）、_top（整个窗口）、*framename* （在指定的框架中打开被链接文档）
  - download，指定下载文件的名称（值为空时，则为目标默认文件名）

#  图片 `<img />`

- 空标签
- 属性
  - alt，必需属性，定义有关图形的短的描述
  - src，必需属性，指定图片文件所在的位置
  - height，定义图像的高度
  - width，定义图像的宽度
  - usemap，将图像定义为图像映射（当用户单击其中某一个区域时，将被链接到不同的文档中）

# 列表

- `<ul>`，定义无序列表
  - `<li>`，定义列表项目
- `<ol>`，定义有序列表，属性 type、start
  - `<li>`，定义列表项目
- `<dl>`，自定义列表
  - `<dt>`，定义标题列表项
  - `<dd>`，定义普通列表项

# 表格 `<table>`

- 属性：border="1"（边框宽度）、cellpadding="0"、cellspacing="0"、width（表格宽度）
- `<caption>`，表格标题
- `<thead>` ，表头
- `<tfoot>`，表格页脚
- `<tbody>`，表格主体
  - `<tr>`，表格行，属性：align（值：left、center、right）、bgcolor
    - `<th>`，表头单元格，文本显示为粗体且居中
    - `<td>`，标准单元格，默认显示为正常字体且左对齐，属性包括：colspan（规定单元格可横跨的列数）、rowspan（规定单元格可横跨的行数）、height、width

# 表单和输入 `<form>`

- 属性：action（必需属性，指定表单被提交的地址）、method、enctype（定义对表单数据进行编码的方式，值：application/x-www-form-urlencoded、multipart/form-data、text/plain）、name、target（规定在何处打开 action 属性的 URL）、autocomplete（表单是否启用自动完成功能）、novalidate（当提交表单时不对其进行验证）
- 表单控件的 name 属性指定请求参数名，value 属性指定请求参数值
- 没有 name 属性的表单控件不会生成请求参数
- 如果多个表单控件有相同的 name 属性，则多个表单控件只生成一个请求参数，只是该参数有多个值
- 表单控件可以使用 form 属性指定所属的一个或多个表单
- `<fieldset>`，将表单内的相关元素分组，用 <legend> 标签定义分组的标题
- <input />，输入域，空标签，属性包括：
  - type，定义输入类型，其属性值：text、password、radio（单选框）、checkbox（复选框）、file（文件上传域）、hidden（隐藏域）、image、submit（提交按钮）、reset、button、search、email、number、url、date、time、datetime、datetime-local
  - name，input 元素的名称
  - value，input 元素的值
  - checked，预先选定复选框或单选按钮
  - disabled，禁用此 input 元素
  - required，在提交表单之前必须输入（不能为空）
  - accept，规定通过**文件上传**进行提交的文件类型，可用值：image/*，audio/*、vidio/*，不带“;”的 MIME type，以“.”开始的文件后缀名（有多种类型时用","分隔）
  - multiple，允许一个以上的值
  - placeholder，提供一种提示，描述输入域所期待的值
  - maxlength，指定文本框中所允许输入的最大字符数
  - readonly，指定该文本框内的值不允许用户修改
  - min、max、step：这 3 个属性只对数值类型（type="number"）、日期类型的 <input> 元素有效，这 3 个属性控制该表单控件的值必须在 min~max 之间，并符合 step 步长
  - pattern，规定输入字段的值的模式或格式，可用 title 属性来描述模式
  - autocomplete：规定输入字段是否应该启用自动完成功能，属性值：on、off
- `<select>`，下拉菜单或列表框，属性：name、multiple（是否允许多选）、size（可见选项的数目）（一旦指定了 multiple 或 size 属性，`<select>` 标签就会自动生成列表框）
  - `<optgroup>`，定义选项组，属性：label、disabled
  - `<option>`，菜单项或列表项，属性：value、selected（是否处于被选中状态）、disabled、
- `<textarea>`，多行文本域，属性：cols（宽度）、rows（高度）、wrap（规定在表单提交时文本区域中的文本是如何换行的，默认值为 soft，文本不换行，值为 hard，文本换行）
- `<button>`，按钮，属性：name、value、type（属性值：submit、button、reset）、disabled
  - 注意：如果在 `<form>` 中使用了`<button>`，其 type 属性的默认值：IE 浏览器中是 "button"，而 W3C 浏览器中是 "submit"
- `<label>`，为 input 元素定义标注（当用户选择该标签时，浏览器就会自动将焦点转到和标签相关的表单控件上），for 属性规定 label 与哪个表单元素（hidden 除外）绑定（其属性值应设置为相关元素的 **id 属性**的值），如果没有指定 for 属性，则为第一个子孙可关联元素

# 框架

- `<frameset>`，定义框架集，属性：rows、clos
- `<frame />`，定义框架集的窗口或框架，scrolling、noresize
- `<noframes>`，定义针对不支持框架的用户的替代内容，子标签 <body>
- `<iframe>`，定义内联框架，属性：src（指定该 iframe 装载的页面的 url ）、align、height、width、frameborder等

# 音频与视频

## HTML 标签

- `<audio>`
- `<video>`
- 属性：
  - src：必须属性，音视频文件的 URL
  - controls：是否向用户显示控件。
  - autoplay：音视频在就绪后是否马上播放
  - preload：可取值为“none”（默认值）、“metadata”、“auto”，音频在页面加载时进行加载，并预备播放（如果使用“autoplay”，则忽略该属性）
  - loop：当音视频结束时是否重新开始播放

## Audio/Video DOM 对象

- 控制多媒体播放 `load()`：加载媒体内容
  `play()`：开始播放
  `pause()`：暂停播放
  playbackRate：播放速度
  currentTime：播放进度
  volume：音量
  muted：静音
- 查询多媒体状态 paused：是否暂停
  seeking：是否正在跳转
  ended：是否播放完成
  duration：媒体时长
  initialTime：媒体开始时间
- 多媒体相关事件 loadstart：开始请求媒内容
  loadmetadata：媒体元数据已经加载完成
  canplay：加载了一些内容，可以开始播放
  play：调用 play()，或设置了 autoplay
  waiting：缓冲数据不够，播放暂停
  playing：正在播放

# 字符实体

- ` `（不换行空格，其占据宽度受字体影响）、` `（其占据的宽度正好是 1/2 个中文宽度，且基本上不受字体影响）、` `（其占据的宽度正好是 1 个中文宽度，且基本上不受字体影响）

![HTML字符实体](https://image.xiaoxiaofeng.site/blog/2023/04/25/xxf-20230425181333.jpeg?xxfjava)