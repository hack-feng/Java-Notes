## 功能背景

简单的说下这个功能的背景需求吧，有类似需求的可以复用

* 实现excel导入（废话...）
* 多个sheet页一起导入
* 第一个sheet页数据表头信息有两行，但只需根据第二行导入
* 如果报错，根据不同的sheet页返回多个List记录报错原因
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
~~~java
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

## Excel模板 

![image-20220607103913456](http://file.xiaoxiaofeng.site/blog/image/image-20220607103913456.png)

![image-20220607104050544](http://file.xiaoxiaofeng.site/blog/image/image-20220607104050544.png)

[点击下载模板（http://file.xiaoxiaofeng.site/blog/image/笑小枫测试导入.xls）](http://file.xiaoxiaofeng.site/blog/image/笑小枫测试导入.xls)

## 项目编码

* 定义通用的对象，只要用于记录行号

~~~java
package manage.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;

import java.util.Set;

/**
 * @author xiaoxiaofeng
 * @date 2022/5/30
 */
@Data
public class CommonExcel {
    /**
     * 行号
     */
    @ExcelIgnore
    private Integer rowIndex;

}
~~~

* 定义经销商信息对象，代码如下：

@ExcelProperty 对用的是excel的标题名称，如果不加@ExcelProperty，默认对应列号

~~~java
package manage.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.util.Date;

/**
 * @author xiaoxiaofeng
 * @date 2022/6/6
 */
@Data
public class ExcelCompany {

    // -------------------- 基本信息 start -------------

    @ExcelProperty("公司名称")
    private String companyName;

    @ExcelProperty("省份")
    private String province;

    @ExcelProperty("成立时间")
    private Date startDate;

    @ExcelProperty("企业状态")
    private String entStatus;

    @ExcelProperty("注册地址")
    private String registerAddress;

    // ---------------- 基本信息 end ---------------------

    // ---------------- 经营信息 start ---------------------

    @ExcelProperty("员工数")
    private String employeeMaxCount;

    @ExcelProperty("经营规模")
    private String newManageScaleName;

    @ExcelProperty("所属区域省")
    private String businessProvinceName;

    @ExcelProperty("所属区域市")
    private String businessCityName;

    @ExcelProperty("所属区域区县")
    private String businessAreaName;

    // ---------------- 经营信息 end ---------------------
}
~~~

* 定义联系人信息对象，代码如下：

~~~java
package manage.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author xiaoxiaofeng
 * @date 2022/6/6
 */
@Data
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

* 定义excel处理的监听器，代码如下：

~~~java
package manage.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import manage.model.excel.CommonExcel;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author xiaoxiaofeng
 * @date 2022/5/30
 */
@Slf4j
public class ImportExcelListener<T> implements ReadListener<T> {

    /**
     * 默认一次读取1000条，可根据实际业务和服务器调整
     */
    private static final int BATCH_COUNT = 1000;
    /**
     * Temporary storage of data
     */
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private final List<String> errorMsgList;

    /**
     * consumer
     */
    private final Consumer<List<T>> consumer;

    public ImportExcelListener(Consumer<List<T>> consumer, List<String> errorMsgList) {
        this.consumer = consumer;
        this.errorMsgList = errorMsgList;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        // 记录行号
        if (data instanceof CommonExcel) {
            ReadRowHolder readRowHolder = context.readRowHolder();
            ((CommonExcel) data).setRowIndex(readRowHolder.getRowIndex() + 1);
        }
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            consumer.accept(cachedDataList);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            consumer.accept(cachedDataList);
        }
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        // 如果是某一个单元格的转换异常 能获取到具体行号
        String errorMsg = String.format("%s， 第%d行解析异常", context.readSheetHolder().getReadSheet().getSheetName(),
                context.readRowHolder().getRowIndex() + 1);
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            errorMsg = String.format("第%d行，第%d列数据解析异常",
                    excelDataConvertException.getRowIndex() + 1,
                    excelDataConvertException.getColumnIndex() + 1);
            log.error("{}， 第{}行，第{}列解析异常，数据为:{}",
                    context.readSheetHolder().getReadSheet().getSheetName(),
                    excelDataConvertException.getRowIndex() + 1,
                    excelDataConvertException.getColumnIndex() + 1,
                    excelDataConvertException.getCause().getMessage());
        } else {
            log.error(errorMsg + exception.getMessage());
        }
        errorMsgList.add(errorMsg);
    }
}
~~~

* 编写controller进行测试，代码如下：

~~~
.readSheet(0)  读取哪个sheet页，默认从0开始

.head(ExcelCompany.class) 对应定义的sheet页对象，不同的sheet页使用对应的对象

.registerReadListener 使用的监听器，这里定义的时通用的，根据不同的业务逻辑，可以定义不同的监听器处理，如需特殊的返回处理，可以定义多个参数的构造器，在监听器里面处理返回

.headRowNumber(2) 标题行在第几行
~~~

~~~java
package manage.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import manage.model.excel.ExcelCompany;
import manage.model.excel.ExcelContact;
import manage.util.ImportExcelListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaoxiaofeng
 * @date 2022/6/6
 */
@Slf4j
@RestController
public class ImportExcelController {

    @PostMapping("/importExcel")
    public Map<String, List<String>> importExcel(@RequestParam(value = "file") MultipartFile file) {
        List<String> companyErrorList = new ArrayList<>();
        List<String> contactErrorList = new ArrayList<>();
        try (ExcelReader excelReader = EasyExcelFactory.read(file.getInputStream()).build()) {
            // 公司信息构造器
            ReadSheet dealerSheet = EasyExcelFactory
                    .readSheet(0)
                    .head(ExcelCompany.class)
                    .registerReadListener(new ImportExcelListener<ExcelCompany>(data -> {
                        // 处理你的业务逻辑，最好抽出一个方法单独处理逻辑
                        log.info("公司信息数据----------------------------------------------");
                        log.info("公司信息数据：" + JSON.toJSONString(data));
                        log.info("公司信息数据----------------------------------------------");
                    }, companyErrorList))
                    .headRowNumber(2)
                    .build();

            // 联系人信息构造器
            ReadSheet contactSheet = EasyExcelFactory
                    .readSheet(1)
                    .head(ExcelContact.class)
                    .registerReadListener(new ImportExcelListener<ExcelContact>(data -> {
                        // 处理你的业务逻辑，最好抽出一个方法单独处理逻辑
                        log.info("联系人信息数据------------------------------------------");
                        log.info("联系人信息数据：" + JSON.toJSONString(data));
                        log.info("联系人信息数据------------------------------------------");
                    }, contactErrorList))
                    .build();

            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(dealerSheet, contactSheet);
        } catch (IOException e) {
            log.error("处理excel失败，" + e.getMessage());
        }
        Map<String, List<String>> result = new HashMap<>(16);
        result.put("company", companyErrorList);
        result.put("contact", contactErrorList);
        log.info("导入excel完成，返回结果如下：" + JSON.toJSONString(result));
        return result;
    }
}

~~~

## 测试结果

通过postman进行调用，idea控制台打印结果如下：

![image-20220607104305169](http://file.xiaoxiaofeng.site/blog/image/image-20220607104305169.png)

postman返回的结果数据如下：

~~~json
{
    "msg": "",
    "obj": {
        "contact": [],
        "company": []
    },
    "result": "0000",
    "serverTime": 1654569757952
}
~~~

模拟一下，数据转换错误的场景，故意把时间写错，如下图：

![image-20220607104425421](http://file.xiaoxiaofeng.site/blog/image/image-20220607104425421.png)

通过postman进行调用，idea控制台打印结果如下：

![image-20220607104756885](http://file.xiaoxiaofeng.site/blog/image/image-20220607104756885.png)

postman返回的结果数据如下：

![image-20220607104903223](http://file.xiaoxiaofeng.site/blog/image/image-20220607104903223.png)

## 相关属性解读

### 注解

- `ExcelProperty` 指定当前字段对应excel中的那一列。可以根据名字或者Index去匹配。当然也可以不写，默认第一个字段就是index=0，以此类推。千万注意，要么全部不写，要么全部用index，要么全部用名字去匹配。千万别三个混着用，除非你非常了解源代码中三个混着用怎么去排序的。
- `ExcelIgnore` 默认所有字段都会和excel去匹配，加了这个注解会忽略该字段
- `DateTimeFormat` 日期转换，用`String`去接收excel日期格式的数据会调用这个注解。里面的`value`参照`java.text.SimpleDateFormat`
- `NumberFormat` 数字转换，用`String`去接收excel数字格式的数据会调用这个注解。里面的`value`参照`java.text.DecimalFormat`
- `ExcelIgnoreUnannotated` 默认不加`ExcelProperty` 的注解的都会参与读写，加了不会参与

### 参数

#### 通用参数

`ReadWorkbook`,`ReadSheet` 都会有的参数，如果为空，默认使用上级。

- `converter` 转换器，默认加载了很多转换器。也可以自定义。
- `readListener` 监听器，在读取数据的过程中会不断的调用监听器。
- `headRowNumber` 需要读的表格有几行头数据。默认有一行头，也就是认为第二行开始起为数据。
- `head`  与`clazz`二选一。读取文件头对应的列表，会根据列表匹配数据，建议使用class。
- `clazz` 与`head`二选一。读取文件的头对应的class，也可以使用注解。如果两个都不指定，则会读取全部数据。
- `autoTrim` 字符串、表头等数据自动trim
- `password` 读的时候是否需要使用密码

#### ReadWorkbook（理解成excel对象）参数

- `excelType` 当前excel的类型 默认会自动判断
- `inputStream` 与`file`二选一。读取文件的流，如果接收到的是流就只用，不用流建议使用`file`参数。因为使用了`inputStream` easyexcel会帮忙创建临时文件，最终还是`file`
- `file` 与`inputStream`二选一。读取文件的文件。
- `autoCloseStream` 自动关闭流。
- `readCache` 默认小于5M用 内存，超过5M会使用 `EhCache`,这里不建议使用这个参数。
- `useDefaultListener` `@since 2.1.4` 默认会加入`ModelBuildEventListener` 来帮忙转换成传入`class`的对象，设置成`false`后将不会协助转换对象，自定义的监听器会接收到`Map<Integer,CellData>`对象，如果还想继续接听到`class`对象，请调用`readListener`方法，加入自定义的`beforeListener`、 `ModelBuildEventListener`、 自定义的`afterListener`即可。

#### ReadSheet（就是excel的一个Sheet）参数

- `sheetNo` 需要读取Sheet的编码，建议使用这个来指定读取哪个Sheet
- `sheetName` 根据名字去匹配Sheet,excel 2003不支持根据名字去匹配

## 写在最后

本文只是用到部分功能，简单的做了一下总结，更多的功能，可以去官网查阅。

官方文档：https://www.yuque.com/easyexcel/doc/read

使用EasyExcel导出excel：
