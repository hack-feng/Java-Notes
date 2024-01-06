## 1. 功能背景
公司要对一个项目进行代码统计，这么多类，总不能让我一个一个数据，于是想到了Statistic插件。让我们一起看看Statistic插件怎么使用吧。

## 2. Statistic插件

首先需要知道Idea统计项目代码行数，主要是使用Statistic插件来统计，点击File->Settings，如下图所示：

![image-20230831164908348](https://image.xiaoxiaofeng.site/blog/2023/08/31/xxf-20230831164908.png?xxfjava)

进去Settings界面之后，点击Plugins，然后点击下方正中间的Marketplace，如下图所示：

![image-20230831165623377](https://image.xiaoxiaofeng.site/blog/2023/08/31/xxf-20230831165623.png?xxfjava)

搜索Statistic，选中之后，点击右侧的Install进行安装插件即可，安装完成点击Apply->Ok即可。

然后在左下方可以看到Statistic，若看不到则重启idea即可。

## 3. 统计代码量

点击下面任务栏的Statistic标签，如下图：

![image-20230831165817480](https://image.xiaoxiaofeng.site/blog/2023/08/31/xxf-20230831165817.png?xxfjava)

这个时候是没有统计的，如果需要统计的话，点击"Refresh"扫描项目代码，如下图：

![image-20230831165850829](https://image.xiaoxiaofeng.site/blog/2023/08/31/xxf-20230831165850.png?xxfjava)

Overview 参数说明(例如java)

- Count ： java文件的数量
- Size SUM ： java所有文件总占用硬盘大小
- Size MIN ： java文件的最小文件占用硬盘大小
- Size MAX ： java文件的最大文件占用硬盘大小
- Size AVG ： java文件的平均占用硬盘大小
- Lines ： java文件的总行数
- Lines MIN ： java文件的最小行数
- Lines MAX ： java文件的最大行数
- Lines AVG ： java文件的平均行数

上文中统计出来是全部代码，如果我们统计对应类型的代码，可以点击我们需要统计的标签，例如Java，可以看到对应对象的代码统计，如下图：

![image-20230831170047999](https://image.xiaoxiaofeng.site/blog/2023/08/31/xxf-20230831170048.png?xxfjava)

指定文件类型(如java)统计参数说明

- Total Lines ：代码总行数(包括注释，空行)
- Source Code Lines ：源代码行数(不包括注释，空行)
- Source Code Lines(%) ：源代码行数百分比(Source Code Lines/Total Lines)
- Comment Lines ：注释行数
- Comment Lines(%) ： 注释行数百分比(Comment Lines/Total Lines)
- Blank Lines : 空行数
- Blank Lines(%) : 空行百分比(Blank Lines/Total Lines)

可见，可对不同类型，从不同维度对工程项目的代码量进行统计，就可以统计工程中的代码行数。

## 4. 测试一下吧

我承认我就是来凑数的~

随便找个类吧，这里用`InitDataConfig.java`

~~~java
package com.maple.inputdb.config;

import com.maple.inputdb.bean.InitModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author 笑小枫 <https://xiaoxiaofeng.com/>
 * @date 2023/3/10
 */
@Slf4j
@Component
@AllArgsConstructor
public class InitDataConfig {

    private final DynamicDatasourceConfig dynamicDatasourceConfig;

    private final InitConfigProperties initConfigProperties;

    public void initData(InitModel initModel) {

        if (DbStatusSingleton.getInstance().getDbStatus()
                || Boolean.TRUE.equals(dynamicDatasourceConfig.checkDataSource())) {
            throw new RuntimeException("数据已完成初始化，请勿重复操作");
        }

        // 检查数据库连接是否正确
        checkConnection(initModel);

        if (!new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitUserName()).exists()) {
            File file = createFile(initConfigProperties.getInitFilePath(), initConfigProperties.getInitUserName());
            try (FileWriter out = new FileWriter(file);
                 BufferedWriter bw = new BufferedWriter(out)) {
                bw.write("userName=" + initModel.getSysUserName());
                bw.newLine();
                bw.write("password=" + initModel.getSysPassword());
                bw.flush();
            } catch (IOException e) {
                log.info("写入管理员信息文件失败", e);
                throw new RuntimeException("写入管理员信息文件失败，请重试");
            }
        }

        if (!new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitDbName()).exists()) {
            File file = createFile(initConfigProperties.getInitFilePath(), initConfigProperties.getInitDbName());
            try (FileWriter out = new FileWriter(file);
                 BufferedWriter bw = new BufferedWriter(out)) {

                bw.write(String.format("jdbcUrl=jdbc:mysql://%s:%s/%s?autoReconnect=true&autoReconnectForPools=true&useUnicode=true&characterEncoding=UTF-8",
                        initModel.getDatabaseHost(), initModel.getDatabasePort(), initModel.getDatabaseName()));
                bw.newLine();
                bw.write("username=" + initModel.getUser());
                bw.newLine();
                bw.write("password=" + initModel.getPassword());
                bw.newLine();
                bw.write("driverClassName=com.mysql.cj.jdbc.Driver");
                bw.flush();

            } catch (IOException e) {
                log.info("写入数据库文件失败", e);
                throw new RuntimeException("写入数据库文件失败，请重试");
            }
        }

        boolean isOk = dynamicDatasourceConfig.checkDataSource();
        if (!isOk) {
            throw new RuntimeException("初始化数据库信息失败，请检查配置是否正确");
        }
    }

    /**
     * 检查数据库连接是否正确
     *
     * @param initModel 连接信息
     */
    private void checkConnection(InitModel initModel) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s",
                    initModel.getDatabaseHost(), initModel.getDatabasePort(), initModel.getDatabaseName()), initModel.getUser(), initModel.getPassword());
            log.info("校验数据库连接成功，开始进行数据库配置" + conn.getCatalog());
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            log.info("校验数据库连接失败", e);
            throw new RuntimeException("初始化数据库信息失败，请检查配置是否正确：" + e.getMessage());
        }
    }

    private File createFile(String path, String fileName) {
        File pathFile = new File(path);
        if (pathFile.mkdirs()) {
            log.info(path + " is not exist, this is auto created.");
        }
        File file = new File(path + File.separator + fileName);
        try {
            if (!file.createNewFile()) {
                throw new RuntimeException(String.format("创建%s文件失败，请重试", fileName));
            }
        } catch (IOException e) {
            log.error(String.format("创建%s文件失败", fileName), e);
            throw new RuntimeException(String.format("创建%s文件失败，请重试", fileName));
        }
        return file;
    }

    public void deleteFile() {
        File sqlFile = new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitDbName());
        if (sqlFile.exists()) {
            log.info(sqlFile.getName() + " --- delete sql file result:" + sqlFile.delete());
        }

        File userFile = new File(initConfigProperties.getInitFilePath() + initConfigProperties.getInitUserName());
        if (userFile.exists()) {
            log.info(userFile.getName() + " --- delete user file result:" + userFile.delete());
        }

        dynamicDatasourceConfig.stopDataSource();

        // 数据初始化状态设为false
        DbStatusSingleton.getInstance().setDbStatus(false);
        log.info("初始化数据重置完成");
    }
}
~~~

当我们打开该文件，选择` Refresh on selection`，可以看到单个文件的统计情况。

![image-20230831171247624](https://image.xiaoxiaofeng.site/blog/2023/08/31/xxf-20230831171247.png?xxfjava)

嗯呢，学会了吧，就不水了，本文到此结束了

关注我吧，带你学会更多的小技巧。