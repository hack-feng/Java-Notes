# XML 概述 #

 *  XML（eXtensible Markup Language），可扩展标记语言
 *  用途：传输数据或存储数据
 *  XML 文档中的元素形成了一棵文档树，这棵树从根部开始，并扩展到树的最底端
 *  所有元素均可拥有子元素，相同层级上的子元素成为同胞（兄弟或姐妹）
 *  所有元素均可拥有文本内容和属性
 *  元数据（有关数据的数据）应当存储为属性，而数据本身应当存储为元素
 *  XML 约束约束在文档中允许出现的元素名称、属性及元素出现的顺序等等类型：XML DTD、XML Schema

## XML 语法规则 ##

 *  所有标签必须闭合
 *  XML 标签对大小写敏感
 *  XML 必须正确地嵌套
 *  XML 文档必须有且仅有一个根元素，该元素是所有其他元素的父元素
 *  XML 元素可以拥有属性（名称/值的对），其属性值须加引号
 *  XML 中 5 个预定义的实体引用：< > & ' " （只有字符 "<" 和 "&" 确实是非法的）
 *  XML 中的注释
 *  XML 以换行符（LF）存储换行
 *  必须保证内容编码与文件本身编码相同

``````````xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?> 
  <!-- 根元素是 <contacts> --> 
  <!-- 文档中的所有 <linkman> 元素都被包含在 <contacts> 中 --> 
  <!-- <book> 元素有 4 个子元素：<name>、<email>、<address>、<group> --> 
  <contacts> 
      <linkman id="1"> 
          <name>Sdky</name> 
          <email>sdky@qq.com</email> 
          <address>广州</address> 
          <group>Java学院</group> 
      </linkman> 
      <linkman id="2"> 
          <name>ZJL</name> 
          <email>zjl@qq.com</email> 
          <address>广州</address> 
          <group>Java学院</group> 
      </linkman> 
  </contacts>
``````````

# XML 解析 #

 *  在 XML 或 HTML 中，一切皆节点（Node），包括：
    
     *  Document：xml 文档
     *  Element ：带有标签的元素
     *  Attribute：标签上的属性
     *  Text：元素之间的文本内容

## DOM 操作 ##

 *  DOM（Document Object Model）：文档对象模型，把文档中的成员描述成一个个对象
 *  特点
    
     *  在加载的时候，一次性的把整个 XML 文档加载进内存，在内存中形成一棵树（Document 对象）
     *  使用代码操作 Document，其实操作的是内存中的DOM 树，和本地磁盘中的 XML 文件没有直接关系
     *  增删改操作完之后，都需要做**同步操作**，才能把内存中的数据更新到 XML 文件
     *  对 XML 内容 的增删改查（CRUD）的操作简单，但是性能比较低下（线性解析）
 *  缺点：若 XML 文件过大，可能造成内存溢出

### DocumentBuilder 类 ###

 *  通过工厂对象创建 DocumentBuilder 对象`DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();`
 *  创建 Document 对象的实例方法
    
     *  `Document parse(File f)`：将给定文件的内容解析为一个 XML 文档，并且返回一个 Document 对象
     *  `Document newDocument()`：获取 Document 对象的一个新实例来生成一个 DOM 树

### Transformer 类（源树转结果树） ###

 *  通过工厂创建 Transformer 对象`Transformer transformer = TransformerFactory.newInstance().newTransformer();`
 *  同步操作方法：`void transform(Source xmlSource, Result outputTarget)`：将 XML Source 转换为 Result
 *  DOMSource 类（Source 的实现类，充当转换 Source 树的持有者）构造器：DOMSource(Node n)，以 DOM 树为节点创建输入源
 *  StreamResult 类（Result 的实现类，充当转换结果树的持有者）构造器：StreamResult(File f)，从 File 构造输出源

## 在 org.w3c.dom 包下 ##

### Node 接口，子接口：Document、Element、Attr、Text ###

 *  `String getTextContent()`：获取此节点的文本内容
 *  `void setTextContent(String textContent)`：设置此节点的文本内容
 *  `Node getParentNode()`：获取此节点的父节点
 *  `Node appendChild(Node newChild)`：在此节点的子节点列表末尾添加一个子节点
 *  `Node removeChild(Node oldChild)`：移除此节点中指定的子节点

### Document 接口（xml 文档） ###

 *  `Element getDocumentElement()`：获取文档的根元素
 *  `Element getElementById(String elementId)`：获取 ID 属性类型的属性为指定值的元素对象
 *  `NodeList getElementsByTagName(String tagname)`：获取整个文档中指定名称的所有元素形成的节点列表
 *  `Element createElement(String tagName)`：在文档中创建指定名称的元素

### Element 接口（带有标签的元素） ###

 *  `NodeList getElementsByTagName(String name)`：获取指定名称的所有子元素形成的子节点列表
 *  `String getAttribute(String name)`：获取元素中指定名称的属性值
 *  `void setAttribute(String name, String value)`：设置元素的属性（名称和值）

### NodeList 接口 ###

 *  `Node item(int index)`：返回节点列表中的第 index 个节点（可能需要强转）
 *  `int getLength()`：节点列表中的节点数

## DOM4J ##

 *  导入 dom4j.jar

``````````java
// 获取文档对象 
  SAXReader reader = new SAXReader(); 
  Document doc = reader.read("resources/contacts.xml"); 
  // 获取根元素 
  Element root = doc.getRootElement(); 
  
  // 获取所有子元素列表 
  List<Element> elements = root.elements("linkman"); 
  for (Element ele : elements) { 
      // 获取指定属性，再获取该属性的属性值 
      String value1 = ele.attribute("id").getValue(); 
      // 获取元素中指定属性的属性值 
      String value2 = ele.attributeValue("id"); 
      // 获取指定元素，再获取该元素中的文本信息 
      String name = ele.element("name").getText(); 
      // 获取元素中的文本信息 
      String name = ele.elementText("name"); 
  } 
  
  // 添加元素并添加属性 
  Element linkmanEle = root.addElement("linkman").addAttribute("id", "1"); 
  // 添加元素并设置值 
  linkmanEle.addElement("name").setText("xxx"); 
  
  // 同步操作 
  OutputFormat format = OutputFormat.createPrettyPrint(); 
  XMLWriter writer = new XMLWriter(new FileWriter("resources/output.xml"), format); 
  writer.write(doc); 
  writer.close(); // 必须关闭流
``````````