## 为什么写这么一篇文章呢？

遇到了个问题，同一天可以输入多个时间段，但是每个时间段的时间不能出现重叠。

纳尼，这不就是判断数据返回是否有重叠的变种嘛~

简单，开搞

## 数字范围是否重叠判断

这里以int类型为例了，其它的也都类似

**核心方法**

~~~java
    /**
     * @param start1 第一个数据开始时间
     * @param end1   第一个数据结束时间
     * @param start2 第二个数据开始时间
     * @param end2   第二个数据结束时间
     * @return true:发生重叠  false:未发生重叠
     */
    public static boolean isIntersect(int start1, int end1, int start2, int end2) {
        return Math.max(start1, start2) <= Math.min(end1, end2);
    }
~~~

4个参数`start1-end1`代表第一组范围，`start2-end2`代表第二组范围；

`Math.max(start1, start2) <= Math.min(end1, end2)`使用`Math.max`和`Math.min`可以无视第一组数据和第二组数据的顺序，只比较是否重叠，`<=` 代表`0-1`后面不能是`1-3`，只能是`2-3`；如果想要`1-3`的效果把`=`去掉即可，后面会有演示。

核心方法非常简单，那我们造一组伪数据测试一下吧。

**测试数据**

先来一组错误数据吧

~~~json
[
  {"startNum": 0, "endNum": 100},
  {"startNum": 100, "endNum": 500},
  {"startNum": 400, "endNum": 1000},
  {"startNum": 1001, "endNum": 9999}
]
~~~

**测试方法**

~~~java

    public static void main(String[] args) {
        String str = "[\n" +
                "  {\"startNum\": 0, \"endNum\": 100},\n" +
                "  {\"startNum\": 100, \"endNum\": 500},\n" +
                "  {\"startNum\": 500, \"endNum\": 1000},\n" +
                "  {\"startNum\": 1001, \"endNum\": 9999}\n" +
                "]";
        JSONArray array = JSON.parseArray(str);
        for (int i = 0; i < array.size(); i++) {
            for (int j = i + 1; j < array.size(); j++) {
                boolean isOk = isIntersect(
                        handleStr(array.getJSONObject(i).getString("startNum")),
                        handleStr(array.getJSONObject(i).getString("endNum")),
                        handleStr(array.getJSONObject(j).getString("startNum")),
                        handleStr(array.getJSONObject(j).getString("endNum")));
                if (isOk) {
                    System.out.println("no " + j + " and no " + (j + 1) + " is intersect");
                    System.out.println("or say, "
                            + array.getJSONObject(i).getString("startNum")
                            + " to " + array.getJSONObject(i).getString("endNum")
                            + " and " + array.getJSONObject(j).getString("startNum")
                            + " to " + array.getJSONObject(j).getString("endNum")
                            + " is intersect");

                    System.out.println("\n");
                }
            }
        }
    }
~~~

答案还是很清晰的哈~

![image-20240704103426928](https://image.xiaoxiaofeng.site/blog/2024/07/04/xxf-20240704103443.png?xxfjava)

数字范围效果完成了。数值类的都类似哈

## 时间范围是否重叠判断

时间有很多种，我们一种一种的来说，实现都一样，重要的是思想

首先来看一下下面这种格式，搞个错误数据吧

~~~json
  [
    {"startTime": "00:00", "endTime": "01:00"},
    {"startTime": "01:00", "endTime": "02:00"},
    {"startTime": "01:00", "endTime": "02:00"},
    {"startTime": "08:00", "endTime": "22:00"},
    {"startTime": "20:00", "endTime": "24:00"}
  ]
~~~

因为`01:00`是重叠的，所以我们把上面核心方法的`=`去掉。如下

~~~java
    /**
     * @param start1 第一个数据开始时间
     * @param end1   第一个数据结束时间
     * @param start2 第二个数据开始时间
     * @param end2   第二个数据结束时间
     * @return true:发生重叠  false:未发生重叠
     */
    public static boolean isIntersect(int start1, int end1, int start2, int end2) {
        return Math.max(start1, start2) < Math.min(end1, end2);
    }
~~~

接下来就很简单了，把时间处理成数据类型就可以了，下面看一下处理的方法

~~~java
    public static int handleStr(String str) {
        str = str.replace(":", "");
        return Integer.parseInt(str);
    }
~~~

来一起看一下运行结果吧

~~~java

    public static void main(String[] args) {
        String data = "  [\n" +
                "    {\"startTime\": \"00:00\", \"endTime\": \"01:00\"},\n" +
                "    {\"startTime\": \"01:00\", \"endTime\": \"02:00\"},\n" +
                "    {\"startTime\": \"01:00\", \"endTime\": \"02:00\"},\n" +
                "    {\"startTime\": \"08:00\", \"endTime\": \"22:00\"},\n" +
                "    {\"startTime\": \"20:00\", \"endTime\": \"24:00\"}\n" +
                "  ]";
        JSONArray array = JSON.parseArray(data);
        for (int i = 0; i < array.size(); i++) {
            for (int j = i + 1; j < array.size(); j++) {
                boolean isOk = isIntersect(
                        handleStr(array.getJSONObject(i).getString("startTime")),
                        handleStr(array.getJSONObject(i).getString("endTime")),
                        handleStr(array.getJSONObject(j).getString("startTime")),
                        handleStr(array.getJSONObject(j).getString("endTime")));
                if (isOk) {
                    System.out.println("no " + j + " and no " + (j + 1) + " is intersect");
                    System.out.println("or say, "
                            + array.getJSONObject(i).getString("startTime")
                            + " to " + array.getJSONObject(i).getString("endTime")
                            + " and " + array.getJSONObject(j).getString("startTime")
                            + " to " + array.getJSONObject(j).getString("endTime")
                            + " is intersect");

                    System.out.println("\n");
                }
            }
        }
    }
~~~

![image-20240704152655730](https://image.xiaoxiaofeng.site/blog/2024/07/04/xxf-20240704152655.png?xxfjava)

对于时间类型，下面格式如何比较

~~~json
  [
    {"startTime": "2024-07-04 00:00:00", "endTime": "2024-07-04 10:00:00"},
    {"startTime": "2024-07-04 10:00:00", "endTime": "2024-07-04 12:00:00"},
    {"startTime": "2024-07-04 12:00:00", "endTime": "2024-07-04 13:00:00"},
    {"startTime": "2024-07-04 12:00:00", "endTime": "2024-07-04 18:00:00"},
    {"startTime": "2024-07-04 17:00:00", "endTime": "2024-07-04 23:00:00"}
  ]
~~~

其实直接将时间转为时间戳来比较就可以了，完整代码如下：

~~~java
    public static void main(String[] args) {
        String data = "[\n" +
                "    {\"startTime\": \"2024-07-04 00:00:00\", \"endTime\": \"2024-07-04 10:00:00\"},\n" +
                "    {\"startTime\": \"2024-07-04 10:00:00\", \"endTime\": \"2024-07-04 12:00:00\"},\n" +
                "    {\"startTime\": \"2024-07-04 12:00:00\", \"endTime\": \"2024-07-04 13:00:00\"},\n" +
                "    {\"startTime\": \"2024-07-04 12:00:00\", \"endTime\": \"2024-07-04 18:00:00\"},\n" +
                "    {\"startTime\": \"2024-07-04 17:00:00\", \"endTime\": \"2024-07-04 23:00:00\"}\n" +
                "  ]";
        JSONArray array = JSON.parseArray(data);
        for (int i = 0; i < array.size(); i++) {
            for (int j = i + 1; j < array.size(); j++) {
                boolean isOk = isIntersect(
                        handleDate(array.getJSONObject(i).getString("startTime")),
                        handleDate(array.getJSONObject(i).getString("endTime")),
                        handleDate(array.getJSONObject(j).getString("startTime")),
                        handleDate(array.getJSONObject(j).getString("endTime")));
                if (isOk) {
                    System.out.println("no " + j + " and no " + (j + 1) + " is intersect");
                    System.out.println("or say, "
                            + array.getJSONObject(i).getString("startTime")
                            + " to " + array.getJSONObject(i).getString("endTime")
                            + " and " + array.getJSONObject(j).getString("startTime")
                            + " to " + array.getJSONObject(j).getString("endTime")
                            + " is intersect");

                    System.out.println("\n");
                }
            }
        }
    }

    /**
     * @param start1 第一个数据开始时间
     * @param end1   第一个数据结束时间
     * @param start2 第二个数据开始时间
     * @param end2   第二个数据结束时间
     * @return true:发生重叠  false:未发生重叠
     */
    public static boolean isIntersect(long start1, long end1, long start2, long end2) {
        return Math.max(start1, start2) < Math.min(end1, end2);
    }

    @SneakyThrows
    public static long handleDate(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.parse(str).getTime();
    }
~~~

看一下测试结果

![image-20240704154200906](https://image.xiaoxiaofeng.site/blog/2024/07/04/xxf-20240704154200.png?xxfjava)

## 总结

实际很简单，就是把需要校验的数据两两比较就可以了，总体思想就是把数据转为数值类型，然后进行比较就可以了。如果对你有帮助，记得点个关注哈~