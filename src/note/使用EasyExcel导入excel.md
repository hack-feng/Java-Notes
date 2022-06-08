官方文档：https://www.yuque.com/easyexcel/doc/read

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

    private static final int BATCH_COUNT = 500;
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
            // 公司信息
            ReadSheet dealerSheet = EasyExcelFactory
                    .readSheet(0)
                    .head(ExcelCompany.class)
                    .registerReadListener(new ImportExcelListener<ExcelCompany>(data -> {
                        // 处理你的业务逻辑，最好抽出一个方法单独处理逻辑
                        log.info("公司信息数据--------------------------------------------------------------");
                        log.info("公司信息数据：" + JSON.toJSONString(data));
                        log.info("公司信息数据--------------------------------------------------------------");
                    }, companyErrorList))
                    .headRowNumber(2)
                    .build();

            // 联系人信息
            ReadSheet contactSheet = EasyExcelFactory
                    .readSheet(1)
                    .head(ExcelContact.class)
                    .registerReadListener(new ImportExcelListener<ExcelContact>(data -> {
                        // 处理你的业务逻辑，最好抽出一个方法单独处理逻辑
                        log.info("联系人信息数据--------------------------------------------------------------");
                        log.info("联系人信息数据：" + JSON.toJSONString(data));
                        log.info("联系人信息数据--------------------------------------------------------------");
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



