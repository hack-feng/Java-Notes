## 序

本文主要讲述一下Java18的新特性

## 版本号

```apache
java -version
openjdk version "18" 2022-03-22
OpenJDK Runtime Environment (build 18+36-2087)
OpenJDK 64-Bit Server VM (build 18+36-2087, mixed mode, sharing)
```

> 从version信息可以看出是build 18+36

## 特性列表

### [JEP 400: UTF-8 by Default](https://link.segmentfault.com/?enc=rVg%2FoM3YgCu7SGtfkQG6qQ%3D%3D.7rVewc%2Fa%2BgU9GDQBSp%2F64sHaHtfFfxmo2pGnfHkActt%2ByBxRskYa8E%2F87N6S7%2ByV)

> java18以前Charset.defaultCharset()是根据操作系统、user locale等来决定的，导致不同操作系统的默认charset是不一样，这次统一改为了UTF-8
> java18要统一为UTF-8则需要`-Dfile.encoding=UTF-8`来设置
> 如果还想沿用以前的判断方式则可以通过`-Dfile.encoding=COMPAT`来设置

### [JEP 408: Simple Web Server](https://link.segmentfault.com/?enc=f7uWDrbrO2897kXKYnAmpg%3D%3D.YHRLXYydMP1LyZpjWs%2BfI%2FD2sVfDwoToEiLPdbeW84lh%2BZDDZFU0XCPk%2BXC9HuBk)

> 提供了一个类似python的SimpleHTTPServer(`python -m SimpleHTTPServer [port]`)的开箱即用的HTTP文件服务器
> 可以通过`jwebserver -p 9000`启动

```apache
jwebserver -p 9000
Binding to loopback by default. For all interfaces use "-b 0.0.0.0" or "-b ::".
Serving /tmp and subdirectories on 127.0.0.1 port 9000
URL http://127.0.0.1:9000/
```

> 也可以在代码里定制并启动

```axapta
jshell> var server = SimpleFileServer.createFileServer(new InetSocketAddress(8080),
   ...> Path.of("/some/path"), OutputLevel.VERBOSE);
jshell> server.start()
```

### [JEP 413: Code Snippets in Java API Documentation](https://link.segmentfault.com/?enc=gtJDe8maZAlw8fChnoBe8w%3D%3D.oSCU3gczbqY5SW3td2di4dK9iV8jct82gvPUoz%2FuhnI67IxiKEwsU3u2kguhRL%2FD)

> 以前要在通过javadoc展示代码可以使用@code如下

```dust
<pre>{@code
    lines of source code
}</pre>
```

> 但是它的缺点就是得用pre包装，导致该片段不能包含html标签，而且缩进不太灵活
>
> 而这次给javaDoc引入了@snippet标签，无需对html标签再进行转义

```php
/**
 * The following code shows how to use {@code Optional.isPresent}:
 * {@snippet :
 * if (v.isPresent()) {
 *     System.out.println("v: " + v.get());
 * }
 * }
 */
```

> 也可以直接引用源代码，避免javadoc的代码与实际代码脱节

```elixir
/**
 * The following code shows how to use {@code Optional.isPresent}:
 * {@snippet file="ShowOptional.java" region="example"}
 */
```

### [JEP 416: Reimplement Core Reflection with Method Handles](https://link.segmentfault.com/?enc=O%2FowlOYPsmNbNUP8eyQtjA%3D%3D.t4qtKN7bDFZNsFkKgt%2F6D3arzaozFFBctieWzfdCdnZUB5jul7bgZ68kbQLxWvJb)

> 通过Method Handles重新实现java.lang.reflect.Method, Constructor及Field来替代字节码生成的Method::invoke, Constructor::newInstance, Field::get, and Field::set的实现
> 方便支持Project Valhalla，为以后减少扩展成本

### [JEP 417: Vector API (Third Incubator)](https://link.segmentfault.com/?enc=k0GTfDy%2BdGctawsHlGjl5g%3D%3D.CLEEXNQKFsSMO%2FoBFhOVQEz1uXmog%2FsJQNcHxdpn%2Fos5p0Nto489SDDvSZ1kcUxq)

> JDK16引入了[JEP 338: Vector API (Incubator)](https://link.segmentfault.com/?enc=h1BrkcC54WtGSDLP04zhPw%3D%3D.jn%2BtujofN0Yl7vtapXQ0u6wD7R%2B%2F5L9hiirgxdLvR1Pa3jA4iWiAedBE5A8zsvOb)提供了jdk.incubator.vector来用于矢量计算
> JDK17进行改进并作为第二轮的incubator[JEP 414: Vector API (Second Incubator)](https://link.segmentfault.com/?enc=VMQmhkQr9wweBIbqv%2BfVcA%3D%3D.HIwtKbFTUKcyYtmhm83ZXVfDuvC2lhI%2BtdAK7CY0dNaEXx4%2FXDw6Px0wC%2BKqT8Gk)
> JDK18进行改进并作为第三轮的incubator

### [JEP 418: Internet-Address Resolution SPI](https://link.segmentfault.com/?enc=Tp6EeMK9VjmtSAO6n%2F4rWA%3D%3D.U9h1rJyaqL4B6IxQy5D5uAjckTWXoU0Y46xGITJzO9l22f6MP%2F10PNnSEBQNeGLF)

> 给解析网络地址提供了SPI，即java.net.spi包的InetAddressResolverProvider
> 方便给project loom做准备(目前InetAddress的API会阻塞在系统调用)，也方便定制化及testing

### [JEP 419: Foreign Function & Memory API (Second Incubator)](https://link.segmentfault.com/?enc=Lohi%2FNxXm8kivvjnNUYxNQ%3D%3D.NL%2Bf1%2BfRJnOFlBW0LgHozv7UXi1F1qEgodOr0Op6X7GNhHYBGTboOJgBRTKAc1Hh)

> JDK14的[JEP 370: Foreign-Memory Access API (Incubator)](https://link.segmentfault.com/?enc=pA%2BtlQ39dIqqHTBcRE5sQA%3D%3D.0T%2B6K1Lx%2FVChfBNFpPKAJnjN90%2B2DEkDBmdwddgyFpqPRgOUGXXz9lZ1nSrMnpOv)引入了Foreign-Memory Access API作为incubator
> JDK15的[JEP 383: Foreign-Memory Access API (Second Incubator)](https://link.segmentfault.com/?enc=fp8eq219E3MEPtIDEXbAcA%3D%3D.9NULR4lV%2B76cdMYrsuBnRBFDUv7VwC743sNcJHZOvoFqLWy62CLmTlWU%2FB9lJZiE)Foreign-Memory Access API作为第二轮incubator
> JDK16的[JEP 393: Foreign-Memory Access API (Third Incubator)](https://link.segmentfault.com/?enc=rXVPEdU7%2BGS11rr3Bhfcew%3D%3D.FHMgZYG2ibOq9db6P3bhI31shWw9ZA%2BQxubMPu7amtNa7LdQ4NVnQJerlVrDqdyW)作为第三轮，它引入了Foreign Linker API
> JDK17引入[JEP 412: Foreign Function & Memory API (Incubator)](https://link.segmentfault.com/?enc=rvAJG4Xks4%2F7MRpPPg8Mxg%3D%3D.Dw%2F4%2FhAHgDlhnwUbvvL8Pt06%2BvvX3pDDsmwhI3WCShl29GBdCsxcTfaOGPf6Qb%2B8)作为第一轮incubator
> JDK18则作为第二轮的incubator

### [JEP 420: Pattern Matching for switch (Second Preview)](https://link.segmentfault.com/?enc=H4GAnp0QjLm36H8JkokvhA%3D%3D.jHbkXm7%2FRdJCrTxakk43Mc%2F%2FGoTdY7kVR1mnF4GGvbPGmtSi72DcMdBWM6EVOe0u)

> instanceof的模式匹配在JDK14作为preview，在JDK15作为第二轮的preview，在JDK16转正

```livescript
static String formatterPatternSwitch(Object o) {
    return switch (o) {
        case Integer i -> String.format("int %d", i);
        case Long l    -> String.format("long %d", l);
        case Double d  -> String.format("double %f", d);
        case String s  -> String.format("String %s", s);
        default        -> o.toString();
    };
}
```

> JDK17引入[JEP 406: Pattern Matching for switch (Preview)](https://link.segmentfault.com/?enc=Q0%2BL4fiwziMC7RXtG3bY1g%3D%3D.cHtjW83OMGcuFHOBpC853Xti7Z5c7DQtS81RyN5VvJjgMRp45WBG%2B%2Bbl200QZx%2Fc)
> JDK18则作为第二轮的preview

### [JEP 421: Deprecate Finalization for Removal](https://link.segmentfault.com/?enc=pJjGpOtvIloHPD8BURUd4Q%3D%3D.YQBUKwIPuc7Lc8B1mvSaixfP60fFf4paa95yrtIqQn82Zd%2FZUArPXzvkrmMtypuC)

> 废弃finalize方法方便后续移除

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 18 Release Notes](https://link.segmentfault.com/?enc=NNe4blWbVp5OogKPLA3LZA%3D%3D.A%2FiO6uBltK3KL6lZZSjtFylSdQ2yVaPk3iDv%2F4S5x00qY1U0bAtrgRMC3%2FP%2FvLS%2B)，这里举几个例子。

### 添加项

- SerialGC、ParallelGC、ZGC支持String Deduplication

  > 可使用`-XX:+UseStringDeduplication`开启

- Map from an Element to its JavaFileObject

  > 新增`Elements.getFileObjectOf(Element)`来映射为JavaFileObject

- Configurable Card Table Card Size[JDK-8272773](https://link.segmentfault.com/?enc=BlTqNISx1nYYIQJBd%2Bk6ew%3D%3D.CnwQpb0BzQPbtg17NBT5nP8cFRG91PlD9PbgrW0taAPCY5DA%2FFXh3qpa7%2Bj7pybhcitiU2aUZvxyIOOCsj55dw%3D%3D)

  > 可以使用`-XX:GCCardSizeInBytes`来设置card table大小

- Allow G1 Heap Regions up to 512MB[JDK-8275056](https://link.segmentfault.com/?enc=PweMsRBQS0vacN7BqsNrDg%3D%3D.D9ROeaL33DObYaWuRDHCq369eYRttswLnNRbn98P5E3nQOpfd%2BFZ8uyj8pV7X5NbjvhFMc5%2FFJzyusPxohY%2Bkg%3D%3D)

  > 允许G1的heap regions的最大值从之前的32MB到512MB

- JDK Flight Recorder Event for Finalization[JDK-8266936](https://link.segmentfault.com/?enc=zkUs9XR8u5l3stT%2Fvhd%2Few%3D%3D.Q%2FMsv5j5j%2BYxM4giRUFzokR4cGsck9WUEQ4FdYHpknZgUNEzKM0EqP9WUbD7YVjPsjwFEU9TAI1EmrcWDE%2F1YQ%3D%3D)

  > 新增jdk.FinalizerStatistics

### 移除项

- Removal of Google's GlobalSign Root Certificate[JDK-8225083](https://link.segmentfault.com/?enc=4EYnDnB8RzJ2y4Jtz%2Fytzw%3D%3D.UlnHI1DFchdSDMbNkWRHtGP59PJlqWAAY3KZvqI6ygF%2BrvIrF%2BKiTs%2FCpuhqtx20ZpJQBm3KIYWJplrSrawebQ%3D%3D)

  > 移除了google的GlobalSign根证书

- Removal of Empty finalize() Methods in java.desktop Module[JDK-8273102](https://link.segmentfault.com/?enc=FIeOIY9cAlfwkJlyUOLFeA%3D%3D.j8x5qhm8bFHY1Q3Gwh0Gt4ebjIiM0CXUx1WyUXC8dlPNuQ%2FfV0ADZ1RU9nIyheJ6J5j9Jv3Cn9XtXsaWeJupRQ%3D%3D)

  > 移除java.desktop模块里头的空finalize()方法

- Removal of impl.prefix JDK System Property Usage From InetAddress[JDK-8274227](https://link.segmentfault.com/?enc=A7jg5cfbctaydhlrS4585g%3D%3D.hLTYvZQH%2F%2FmxMU6nAwldaE7HSKRYoaqU9V4Xs%2BfZ0eRjbT%2FFz82USnsTZRck5YPPg%2FYGRt3rX7dYnGQXm44oBQ%3D%3D)

  > 移除impl.prefix属性，转而使用InetAddressResolver这个spi

- Removal of Support for Pre JDK 1.4 DatagramSocketImpl Implementations[JDK-8260428](https://link.segmentfault.com/?enc=GmOdQ16k7KMxksysHw05Lg%3D%3D.ZwVVAQ52Qb5zfPtZoeKjra%2BFtsXmtcXbJv1qzMQQNIhvKe9SS9hE6CpKqdQbhs1ER9SmMWxyG2uZd3Rv6d6Tcw%3D%3D)

  > 移除jdk1.4之前的DatagramSocketImpl

- Removal of Legacy PlainSocketImpl and PlainDatagramSocketImpl Implementations[JDK-8253119](https://link.segmentfault.com/?enc=FsezQ2Dl7xqrmRpLSeifDg%3D%3D.fzjy7S2I%2B8VFjBy4SIOVmo3wc10BC1dX1ZhSx3ibVyllS%2Bfh7j6d4kWhKix6A09fm6OzBchP8HXLChvC6zp7Qg%3D%3D)

  > 移除java.net.SocketImpl及java.net.DatagramSocketImpl的老实现PlainSocketImpl、PlainDatagramSocketImpl
  > `jdk.net.usePlainDatagramSocketImpl`属性也一并移除

### 废弃项

完整列表见[deprecated-list](https://link.segmentfault.com/?enc=%2BW7TxwSjq%2FRtdKWHGkRh7A%3D%3D.RA7zGph1oW%2BbrFJK%2BBYWj1DEpL6vz4%2F28Ax77lF8SslKBuK6MN1iYoN3ncibeUK4AiC8ICTVRB0wSlhG6Och6G53LIfWkRwu2Wyb2STo6iU%3D)

- Deprecated Subject::doAs for Removal[JDK-8267108](https://link.segmentfault.com/?enc=i0w48fHgy%2BsPY7yWzxl5Ew%3D%3D.Zze0TqS52%2B%2F%2FccAm9NP7sOzJ6txydgnSuGtOjjfJXnqw4NrW16e8txQWOA30eFftLBdLVbHgxnTNOnzDP6VXqQ%3D%3D)

  > 废弃`javax.security.auth.Subject::doAs`为移除做准备

- Deprecated sun.misc.Unsafe Methods That Return Offsets[JDK-8277863](https://link.segmentfault.com/?enc=JKtKPSmPe%2B%2F4k3q8AiJuig%3D%3D.qrQfRgsZqVws6AOiZskmi83oNiPJ%2BuGNT5IyloScduw1LmrvVeXG5a38sRyeqYJxNnucJdu%2FtUmX%2F4fdEHu50w%3D%3D)

  > sun.misc.Unsafe中objectFieldOffset, staticFieldOffset, staticFieldBase方法被废弃

- Terminally Deprecated Thread.stop[JDK-8277861](https://link.segmentfault.com/?enc=s9Qt3U3F2acqz47i07FV1w%3D%3D.2dpwk7bc5IbtFIK5CiQc%2FI3yhftjvQ%2Fagk9HYeckGOm1F26FZ0VCpvTGPZNUdMy0yKP78hLEo%2FUitdxSGx6yqQ%3D%3D)

  > 废弃Thread.stop为后续移除做准备

- Obsoleted Product Options -XX:G1RSetRegionEntries and -XX:G1RSetSparseRegionEntries[JDK-8017163](https://link.segmentfault.com/?enc=uu7XtH5x5bg2TdpLqumOKw%3D%3D.dg%2BXEStVJJvZxF70BVzQ%2F3oRzcpjoO2dAt0OfnJJgrJJvkuehfHf6G2ScjgWhNfFDxnLWJ0f8r6mLMrnt4lgtQ%3D%3D)

  > 废弃`-XX:G1RSetRegionEntries`及`-XX:G1RSetSparseRegionEntries`

### 已知问题

- Extended Delay Before JDK Executable Installer Starts From Network Drive[JDK-8274002](https://link.segmentfault.com/?enc=0GW02gZdhydetBDI0%2BH1Rw%3D%3D.SqEj9ZdMY21O7FMevuGe21i1NtdyvJ7LZBM2hSiePsOZabQh8Aj8rM0HytaFwLUb5UNFMNcM1sNWCOydICKmYw%3D%3D)

  > 在 Windows 11 和 Windows Server 2022 上，从映射的网络驱动器启动时，临时安装文件的提取可能会有些缓慢。安装程序仍然可以工作，但可能会有暂时的延迟。

## 小结

Java18主要有如下几个特性

- [JEP 400: UTF-8 by Default](https://link.segmentfault.com/?enc=yRCJwHVM83y9wHtzCi6Wrw%3D%3D.OiPwABFelWtT5RBFf3MHsaqNWG90SWFlJq6EF1K2UBrO0%2BrhFb7YoYx%2Fd2Vghw4P)
- [JEP 408: Simple Web Server](https://link.segmentfault.com/?enc=OMqyUmFD4%2F77SZxUudhFjg%3D%3D.u8NVfXtuC8kxoMXzm2k60m6%2FCtyhAYs467nnTGIk96IhNw5o9QMjLGSoX2J4xHsZ)
- [JEP 413: Code Snippets in Java API Documentation](https://link.segmentfault.com/?enc=nCtmg06Tj1ewNIRqxYhRAA%3D%3D.y8d0p7aYeRDc19XH8yx4ztw9oNC1v6lP%2B6Z0%2BkZbndILHmx38FMW878nv9QwkBFN)
- [JEP 416: Reimplement Core Reflection with Method Handles](https://link.segmentfault.com/?enc=fpI3b56x7U4fvlm5GIy3tg%3D%3D.WIMX0k6YoAL54pcJBA5xF%2FlVE%2F6cT%2FdUF%2FMDDIGXuIITfs163g5Og46FrSvJYU6c)
- [JEP 417: Vector API (Third Incubator)](https://link.segmentfault.com/?enc=pD9rQmtTcQNbbW0JSaArvg%3D%3D.LHEI4gk5Cx55USYvTJXhn%2FNkHdvfY2O%2FmVzkE9TBgA137eI1Frl%2BkYgpzdLTp8YS)
- [JEP 418: Internet-Address Resolution SPI](https://link.segmentfault.com/?enc=VtCC3mZoGsSWMaoBooJoeg%3D%3D.kvxqS44e094M4MurssZBRJ%2FOBpRJgzMu9bOaJPCP0HZfvau9yVHmKI8CCxRK6qcM)
- [JEP 419: Foreign Function & Memory API (Second Incubator)](https://link.segmentfault.com/?enc=8GnraXnYB1yKHbfoaFLSmw%3D%3D.Vg%2FAIUi5Mn4L1S7XZPG7rG9KsTJHu1iSqnLK8prRq%2FqVdN5fTcMdfg8iQ3SdXafm)
- [JEP 420: Pattern Matching for switch (Second Preview)](https://link.segmentfault.com/?enc=ICkgVeU8A%2Fo%2B94PjKO3ZnQ%3D%3D.R0xybkFC5OZQP1S3mjFC%2BJJcSvaEccd3cynh0mc9rPVBkNFsNv29x1lbzlI363UN)
- [JEP 421: Deprecate Finalization for Removal](https://link.segmentfault.com/?enc=WdsH2IZrF0zjywdYmw0JUg%3D%3D.DFDcAkSA6ikGZLPkS9I2q8fRhTzb1VchwbEDgdM4s26oWLO%2Bn3NxYuh93GSyoVD7)

## doc

- [JDK 18 Features](https://link.segmentfault.com/?enc=Ng2VwB5iS4QRCTrPBz0c%2FA%3D%3D.dRxBk%2F3xqdjKVY%2BqhcGVZEnhYipMVXA00%2Fr%2F550GAeuFPQiy2X5t5HCnu3qQF43R)
- [JDK 18 Release Notes](https://link.segmentfault.com/?enc=1lLpVEtIbyNo2NFIbsPiTw%3D%3D.RJF99Ao27WehYeXFpGee8rv9QO6gy%2F5UUyhThc1YQ1bG9X7hYFVUUOgn5qLk2EOS)
- [Consolidated JDK 18 Release Notes](https://link.segmentfault.com/?enc=iRte9BGcPX%2FP0%2Bw5E0LcPw%3D%3D.4wXBMZXKxs7U8aL9837%2FqmPKtMWpdij4Kx4wHqhGrdtyh5xL%2BmqdEs2npdogRY3uVj041NNMTHe4GVhRsMI002yiPyoEzNmw8n1Le%2Fv4D54%3D)
- [Java SE 18 deprecated-list](https://link.segmentfault.com/?enc=LllQ2wfv3ANnMdEZOxvpVw%3D%3D.ecy0ntoDtXw7w3dF6DjvqD3YxI0jLrk0mjP%2BCOpTqCQHvYU0z4gxYBPoIoCf3CS2IwEBuV%2BDkZ311%2FI%2BAk9xOAhjqPmVrdCd2e8WcceR%2Bm8%3D)
- [The Arrival of Java 18](https://link.segmentfault.com/?enc=rH3zJh3XosVLq1aiJqkwPw%3D%3D.cNjxeCpVSQ890CATPFIQ9R2gWonQANWO3P9%2BuEb4%2Fqzdwl7y7pUMDaTE%2BJGkMCmClcw8SHLtoaBgN1nCom5Yyw%3D%3D)
- [JDK 18 G1/Parallel/Serial GC changes](https://link.segmentfault.com/?enc=hzyTmpGKHpJXjAD64D%2BmHg%3D%3D.Eo3qw%2BsnliPuiYPlrSoHzwcKlmziF5iDjUdR%2FaF47UfcaZXKvrxXLnAMy4pLmERf4PE%2FXIMwy%2B8HkHUkZIRmACEttVB0hvmmsPJqeoH6vSw%3D)