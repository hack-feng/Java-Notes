在看高性能MySQL第3版（4.1.7节）时，作者建议当存储IPv4地址时，应该使用32位的无符号整数（UNSIGNED INT）来存储IP地址，而不是使用字符串。但是没有给出具体原因。为了搞清楚这个原因，查了一些资料，记录下来。

相对字符串存储，使用无符号整数来存储有如下的好处：

 *  节省空间，不管是数据存储空间，还是索引存储空间
 *  便于使用范围查询（BETWEEN...AND），且效率更高

通常，在保存IPv4地址时，一个IPv4最小需要7个字符，最大需要15个字符，所以，使用VARCHAR(15)即可。MySQL在保存变长的字符串时，还需要额外的一个字节来保存此字符串的长度。而如果使用无符号整数来存储，只需要4个字节即可。另外还可以使用4个字段分别存储IPv4中的各部分，但是通常这不管是存储空间和查询效率应该都不是很高（可能有的场景适合使用这种方式存储）。

使用字符串和无符号整数来存储IP的具体性能分析及benchmark，[可以看这篇文章][Link 1]。


使用无符号整数来存储也有缺点：

 *  不便于阅读
 *  需要手动转换

对于转换来说，MySQL提供了相应的函数来把字符串格式的IP转换成整数INET\_ATON，以及把整数格式的IP转换成字符串的INET\_NTOA。如下所示：

```sql
mysql> select inet_aton('192.168.0.1');
+--------------------------+
| inet_aton('192.168.0.1') |
+--------------------------+
|               3232235521 |
+--------------------------+
1 row in set (0.00 sec)

mysql> select inet_ntoa(3232235521);
+-----------------------+
| inet_ntoa(3232235521) |
+-----------------------+
| 192.168.0.1           |
+-----------------------+
1 row in set (0.00 sec)
```

对于IPv6来说，使用VARBINARY同样可获得相同的好处，同时MySQL也提供了相应的转换函数，即INET6\_ATON和INET6\_NTOA。

对于转换字符串IPv4和数值类型，可以放在应用层，下面是使用java代码来对二者转换：

```java
package com.mikan;

/**
 * @author Mikan
 * @date 2015-09-22 10:59
 */
public class IpLongUtils {
    /**
     * 把字符串IP转换成long
     *
     * @param ipStr 字符串IP
     * @return IP对应的long值
     */
    public static long ip2Long(String ipStr) {
        String[] ip = ipStr.split("\\.");
        return (Long.valueOf(ip[0]) << 24) + (Long.valueOf(ip[1]) << 16)
                + (Long.valueOf(ip[2]) << 8) + Long.valueOf(ip[3]);
    }

    /**
     * 把IP的long值转换成字符串
     *
     * @param ipLong IP的long值
     * @return long值对应的字符串
     */
    public static String long2Ip(long ipLong) {
        StringBuilder ip = new StringBuilder();
        ip.append(ipLong >>> 24).append(".");
        ip.append((ipLong >>> 16) & 0xFF).append(".");
        ip.append((ipLong >>> 8) & 0xFF).append(".");
        ip.append(ipLong & 0xFF);
        return ip.toString();
    }

    public static void main(String[] args) {
        System.out.println(ip2Long("192.168.0.1"));
        System.out.println(long2Ip(3232235521L));
        System.out.println(ip2Long("10.0.0.1"));
    }
    
}
```

输出结果为：

```
3232235521
192.168.0.1
167772161
```



[Link 1]: http://bafford.com/2009/03/09/mysql-performance-benefits-of-storing-integer-ip-addresses/