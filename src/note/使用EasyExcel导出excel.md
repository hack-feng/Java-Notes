## 功能背景

简单的说下这个功能的背景需求吧，有类似需求的可以复用，果然导入还没写完，导出的功能接踵而来，一块写了吧

* 实现excel导出（依旧废话...）
* 多个sheet页一起导出
* 第一个sheet页数据表头信息有两行
* 样式稍微美观，列宽可以自定义等
* 数据量稍微有些大（多个sheet页总量50w左右）

## 项目引入依赖

gradle:

~~~
compile "com.alibaba:easyexcel:3.1.0"
~~~

maven:

~~~
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.1.0</version>
</dependency>
~~~

注意： 3+版本的的easyexcel，使用poi 5+版本时，需要手动排除：poi-ooxml-schemas，例如：

~~~
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.1.0</version>
    <exclusions>
        <exclusion>
            <artifactId>poi-ooxml-schemas</artifactId>
            <groupId>org.apache.poi</groupId>
        </exclusion>
    </exclusions>
</dependency>
~~~

## 项目编码

如多个表格样式相同，可编写一个父类，将样式定义在父类上，子类继承父类即可。

* 定义经销商信息对象，代码如下：

~~~java
package org.cango.dealer.manage.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiaoxiaofeng
 * @date 2022/6/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
@HeadFontStyle(fontHeightInPoints = 12)
@ContentFontStyle(fontHeightInPoints = 11)
@ColumnWidth(20)
public class ExcelCompany {

    // -------------------- 基本信息 start -------------

    @ExcelProperty({"基本信息", "公司名称"})
    private String companyName;

    @ExcelProperty({"基本信息", "省份"})
    private String province;

    @ExcelProperty({"基本信息", "成立时间"})
    private Date startDate;

    @ExcelProperty({"基本信息", "企业状态"})
    private String entStatus;

    @ColumnWidth(30)
    @ExcelProperty({"基本信息", "博客地址"})
    private String csdnAddress;

    // ---------------- 基本信息 end ---------------------

    // ---------------- 经营信息 start ---------------------

    @ExcelProperty({"经营信息", "员工数"})
    private String employeeMaxCount;

    @ExcelProperty({"经营信息", "网站地址"})
    private String netAddress;

    @ExcelProperty({"经营信息", "所属区域省"})
    private String businessProvinceName;

    @ExcelProperty({"经营信息", "所属区域市"})
    private String businessCityName;

    @ExcelProperty({"经营信息", "所属区域区县"})
    private String businessAreaName;

    // ---------------- 经营信息 end ---------------------
}

~~~

* 定义联系人信息对象，代码如下：

~~~java
package org.cango.dealer.manage.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoxiaofeng
 * @date 2022/6/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
@HeadFontStyle(fontHeightInPoints = 12)
@ContentFontStyle(fontHeightInPoints = 11)
@ColumnWidth(20)
public class ExcelContact {
    @ExcelProperty("公司名称")
    private String companyName;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("身份证号码")
    private String idCard;

    @ExcelProperty("电话号码")
    private String mobile;

    @ExcelProperty("职位")
    private String contactPostName;
}
~~~

* 编写controller进行测试，代码如下：

~~~java
package org.cango.dealer.manage.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;
import org.cango.dealer.manage.model.excel.ExcelCompany;
import org.cango.dealer.manage.model.excel.ExcelContact;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangfuzeng
 * @date 2022/6/9
 */
@Slf4j
@RestController
public class ExportExcelController {

    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) {
        try (OutputStream out = response.getOutputStream()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("笑小枫测试导出", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            handleExcel(out);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void handleExcel(OutputStream out) {
        try (ExcelWriter excelWriter = EasyExcelFactory.write(out).build()) {
            WriteSheet dealerSheet = EasyExcelFactory.writerSheet(0, "经销商信息").head(ExcelCompany.class).build();
            WriteSheet contactSheet = EasyExcelFactory.writerSheet(1, "联系人").head(ExcelContact.class).build();
            excelWriter.write(getCompany(), dealerSheet);
            excelWriter.write(getContact(), contactSheet);
        }
    }

    private List<ExcelCompany> getCompany() {
        List<ExcelCompany> companyList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            companyList.add(ExcelCompany.builder()
                    .companyName("笑小枫公司" + i)
                    .province("上海市")
                    .businessProvinceName("山东省")
                    .businessCityName("临沂市")
                    .businessAreaName("河东区")
                    .entStatus("营业")
                    .netAddress("www.xiaoxiaofeng.site")
                    .csdnAddress("https://zhangfz.blog.csdn.net")
                    .employeeMaxCount("100")
                    .startDate(new Date())
                    .build());
        }
        return companyList;
    }

    private List<ExcelContact> getContact() {
        List<ExcelContact> contactList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            contactList.add(ExcelContact.builder()
                    .companyName("笑小枫公司" + i)
                    .name("笑小枫" + i)
                    .mobile("183000000000")
                    .idCard("371324199011111111")
                    .contactPostName("后端")
                    .build());
        }
        return contactList;
    }
}

~~~

## 测试结果

浏览器请求：http://localhost:8080/exportExcel

经销商sheet页信息：

![image-20220610102736604](http://file.xiaoxiaofeng.site/blog/image/image-20220610102736604.png)

联系人sheet页信息：

![image-20220610102750478](http://file.xiaoxiaofeng.site/blog/image/image-20220610102750478.png)



## 相关属性解读

![image-20220610104915216](http://file.xiaoxiaofeng.site/blog/image/image-20220610104915216.png)

### 注解

- `ExcelProperty` index 指定写到第几列，默认根据成员变量排序。`value`指定写入的名称，默认成员变量的名字，多个`value`可以参照快速开始中的复杂头
- `ExcelIgnore` 默认所有字段都会写入excel，这个注解会忽略这个字段
- `DateTimeFormat` 日期转换，将`Date`写到excel会调用这个注解。里面的`value`参照`java.text.SimpleDateFormat`
- `NumberFormat` 数字转换，用`Number`写excel会调用这个注解。里面的`value`参照`java.text.DecimalFormat`
- `ExcelIgnoreUnannotated` 默认不加`ExcelProperty` 的注解的都会参与读写，加了不会参与

### 参数

#### 通用参数

`WriteWorkbook`,`WriteSheet` ,`WriteTable`都会有的参数，如果为空，默认使用上级。

- `converter` 转换器，默认加载了很多转换器。也可以自定义。
- `writeHandler` 写的处理器。可以实现`WorkbookWriteHandler`,`SheetWriteHandler`,`RowWriteHandler`,`CellWriteHandler`，在写入excel的不同阶段会调用
- `relativeHeadRowIndex` 距离多少行后开始。也就是开头空几行
- `needHead` 是否导出头
- `head`  与`clazz`二选一。写入文件的头列表，建议使用class。
- `clazz` 与`head`二选一。写入文件的头对应的class，也可以使用注解。
- `autoTrim` 字符串、表头等数据自动trim

#### WriteWorkbook（理解成excel对象）参数

- `excelType` 当前excel的类型 默认`xlsx`
- `outputStream` 与`file`二选一。写入文件的流
- `file` 与`outputStream`二选一。写入的文件
- `templateInputStream` 模板的文件流
- `templateFile` 模板文件
- `autoCloseStream` 自动关闭流。
- `password` 写的时候是否需要使用密码
- `useDefaultStyle` 写的时候是否是使用默认头

#### WriteSheet（就是excel的一个Sheet）参数

- `sheetNo` 需要写入的编码。默认0
- `sheetName` 需要些的Sheet名称，默认同`sheetNo`

#### WriteTable（就把excel的一个Sheet,一块区域看一个table）参数

- `tableNo` 需要写入的编码。默认0

## 写在最后

本文只是用到部分功能，简单的做了一下总结，更多的功能，可以去官网查阅。

官方文档：https://www.yuque.com/easyexcel/doc/read