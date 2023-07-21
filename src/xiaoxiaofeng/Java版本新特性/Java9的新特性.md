## 序

本文主要讲述一下Java9的新特性

## 特性列表

完整的特性详见[JDK 9 features](https://link.segmentfault.com/?enc=XONClFJ7coMDAZUA6y2DBw%3D%3D.yVJeceXp%2FayvP%2FVJcZWu0ezlpYGqDdjFPgJ85V48ht8TO1K1z0k0L%2Bela5l0wUAj)，这里列几个相对重要的：

### 模块系统JPMS(`重磅`)

> 相关的规范及JEP:

- [Java Platform Module System (JSR 376)](https://link.segmentfault.com/?enc=6cq2pinEsETKV0LaUtybeA%3D%3D.AIOCCYSEVTFEJeZkfggL6ueWq8mCRCBTqrhdKVWV8Ju%2Fm1kGs0Y3z2jki646Nkg9)
- [JEP 261: Module System](https://link.segmentfault.com/?enc=obSK2ok2CwxcpbAshGfkhQ%3D%3D.efih%2BA803BfFSG6Phb9BnAjc2Y9Gbcac%2FRGsiUtMQppxAZAfQNoEehrcDn2RRopE)
- [JEP 200: The Modular JDK](https://link.segmentfault.com/?enc=VWfBzBtj%2Br%2BhJ%2FthuKzn1g%3D%3D.Wwkju37eT2u%2BMGbaem8AEDgjqiWLx5KFJddO2Le7jtbNQBVdn2En24CF5lqOSfKS)
- [JEP 201: Modular Source Code](https://link.segmentfault.com/?enc=1asnDaRq0yt1WxSMF14Yjw%3D%3D.wFC38Bbx0Jx8JySemK9SwpriKc1It14vh7va94NMpRTQwNtJcEu%2FcPoCpPxxyFrk)
- [JEP 282: jlink: The Java Linker](https://link.segmentfault.com/?enc=fVGWCR%2B6R4a2MFxKy8e51Q%3D%3D.ZTvEiRto1Rb%2FrKgZxm1DOhJ1l8X3LdFAkIPC8VCDfPcCEMujFHlxHTPhjQB8GzC5)
- [JEP 220: Modular Run-Time Images](https://link.segmentfault.com/?enc=VuZJ14atm%2FV2nDf2z6MZ3A%3D%3D.EjnTlz7RFtjlpq0TNnP%2FUz8qpvsW7RBgwar%2F22M9RCYDpTzHEQBYbc0KPFDRMNcn)
- [JEP 260: Encapsulate Most Internal APIs](https://link.segmentfault.com/?enc=QgIwDGEV9wz6P2L%2FN%2FIoaA%3D%3D.%2FG0poINZh69rNoiBzwtdQE5YOWSsYQMUzumqbjAolWeAm%2B3vjyV%2B53jmoUxasm4O)

> 相关解读

- [java9系列(三)模块系统精要](https://segmentfault.com/a/1190000013357446)
- [java9 opens与exports的区别](https://segmentfault.com/a/1190000013409571)
- [java9迁移注意事项](https://segmentfault.com/a/1190000013398709)
- [java9 module相关选项解析](https://segmentfault.com/a/1190000013440386)

### G1成为默认垃圾回收器

> 相关JEP:

- [JEP 248: Make G1 the Default Garbage Collector](https://link.segmentfault.com/?enc=epXDpPpDKI3hGOfPvxkbYA%3D%3D.tNhozaXiyCxGlKjRiOPMpxcJfkptIriyzxmUCsFc3hNstkFm1omxp6xyKXz7uNTc)
- [JEP 291: Deprecate the Concurrent Mark Sweep (CMS) Garbage Collector](https://link.segmentfault.com/?enc=Q9c5UCR8C9JQWDO02FOqAQ%3D%3D.cNHaxShp7OG7MPXUmtTilBFeN1%2FBAWJclmM7WQGYfxTyxcq7RR8PkM1t%2Btb3hbLp)
- [JEP 278: Additional Tests for Humongous Objects in G1](https://link.segmentfault.com/?enc=Mmkdl%2FNQqK2tm8vQ1yTyZQ%3D%3D.2mZ5QprGG3Swi%2Fkotercp57Wz%2BGfbVvVVDZ0WBKuGHeBknbIJQLJNM0FZWqqaXU9)

> 相关解读

- [java9系列(九)Make G1 the Default Garbage Collector](https://segmentfault.com/a/1190000013615459)

### Unified JVM/GC Logging

> 相关JEP:

- [JEP 158: Unified JVM Logging](https://link.segmentfault.com/?enc=s%2Fujrfchj%2B%2FtuwgwlmPaWQ%3D%3D.XeVjEm6LesMdYNMMr2tNZIOhPNS7pIEwA3PpWItXrzamDA9LXhV8V0eHqnTsYuOQ)
- [JEP 264: Platform Logging API and Service](https://link.segmentfault.com/?enc=alAXhFWg1zPiwg8jaZuAnw%3D%3D.Wk50PkEln63r19ddoRzFKkpx9xcyJZZxJ%2BSvDa%2BXidEgvJJYihUDEckipTCWI7UC)
- [JEP 271: Unified GC Logging](https://link.segmentfault.com/?enc=QJnODt4SqZEAgxa%2BS7w91Q%3D%3D.nIXI%2BGfJt2tNH8xb%2FAVIoXrJ6jrPfT4Vl5oj8hJgWrh81sqemLvlK0grvyiigZR9)

> 相关解读

- [java9 gc log参数迁移](https://segmentfault.com/a/1190000013475524)

### HTTP/2 Client(Incubator)

支持HTTP2，同时改进httpclient的api，支持异步模式。

> 相关JEP

- [JEP 110: HTTP/2 Client (Incubator)](https://link.segmentfault.com/?enc=AyRJWZFv6BF310zVW2IjEA%3D%3D.HbC16oni0Nco2x9r1UHjQv2%2FE3xCulZ92ZRV7ctf56WLJPc1XnSQKqUB6ZQePuvd)

> 相关解读

- [java9系列(六)HTTP/2 Client (Incubator)](https://segmentfault.com/a/1190000013518969)

### jshell: The Java Shell (Read-Eval-Print Loop)

> 相关JEP

- [JEP 222: jshell: The Java Shell (Read-Eval-Print Loop)](https://link.segmentfault.com/?enc=9rN9bmwAe%2Fvprwr7BJYZiw%3D%3D.ZVqTR77VOJe22V8h3eeO82yUqnt2pQsciQYj5%2FWnnuKXCrtrk27s%2FkUJdsVJLyoB)

> 相关解读

- [java9系列(一)安装及jshell使用](https://segmentfault.com/a/1190000011321448)

### Convenience Factory Methods for Collections

> 相关JEP

- [JEP 269: Convenience Factory Methods for Collections](https://link.segmentfault.com/?enc=MMnfUGiDSGnRBTcwhecB7g%3D%3D.a1j6k0W4PItm6HYv8DTGkHPfIpQ8T%2BMOclb6tRtluN0774arJuynvwTuy4b75Rl7)

以前大多使用Guava类库集合类的工厂，比如

```apache
Lists.newArrayList(1,2,3,4,5);
Sets.newHashSet(1,2,3,4,5);
Maps.newHashMap();
```

> 注意，上面这种返回的集合是mutable的

现在java9可以直接利用jdk内置的集合工厂，比如

```apache
List.of(1,2,3,4,5);
Set.of(1,2,3,4,5);
Map.of("key1","value1","key2","value2","key3","value3");
```

> 注意，jdk9上面这种集合工厂返回的是immutable的

### Process API Updates

> 相关JEP

- [JEP 102: Process API Updates](https://link.segmentfault.com/?enc=WujuO0vhyHOW7NSo%2Bf5xWw%3D%3D.EAdWk9Z7ynAVUlTJsXUxWH3bOqW1Pj5ZJOVwvLEhkUWIZipF9JPrhDI24EUS0%2FVY)

> 相关解读

- [java9系列(四)Process API更新](https://segmentfault.com/a/1190000013496056)

### Stack-Walking API

> 相关JEP

- [JEP 259: Stack-Walking API](https://link.segmentfault.com/?enc=NfSUKLx9uaKZdyoCdjxk1A%3D%3D.zrQs8RBiugPxo%2BrZQyu%2F7XGk%2BwhObuwh09KmIVSinbkj96lpeFQsVjsaIE5NVbkb)

> 相关解读

- [java9系列(五)Stack-Walking API](https://segmentfault.com/a/1190000013506140)

### Variable Handles

> 相关JEP

- [JEP 193: Variable Handles](https://link.segmentfault.com/?enc=x2A%2BXNz7n80k8d4LfbQLTw%3D%3D.NZsw5w8gZ4gIXLDhHHRzxGjaXjJg9EdRgO9uwuEhq346AaDMPb5S9BayhgysCtOK)

> 相关解读

- [java9系列(七)Variable Handles](https://segmentfault.com/a/1190000013544841)

### docker方面支持

- [Java SE support for Docker CPU and memory limits](https://link.segmentfault.com/?enc=1FfsczTOCqhUtI3ic%2BnvtA%3D%3D.2WQbIkW2p3lTFcVE4mwj8pQkEKMLUB6eTXN5vhhfTn3re8hJHI6x9UsWdJTNZBkl5OG0NBpmb2dejzF56gH%2BSHUjzGQAP4wHn7NRUZi6x%2FnYwqbbIQ5WHuexs%2BFacOf9)
- [Docker CPU limits](https://link.segmentfault.com/?enc=nbzyv6TmzUBzfZi2SzE5Lw%3D%3D.7aH1y1IyVOSv0tqAdfMswyU0Qto%2FTRxN2HYtrVo06xUrim%2BWbpvyzFYk27ypNWNzEGp1om3ySVDaD0OrEfxxSA%3D%3D)
- [Experimental support for Docker memory limits](https://link.segmentfault.com/?enc=Yh7xc8pBaEzeTMxtyvNJdQ%3D%3D.wS8ourWZtmmAX76is%2Bix55qnuI1tFZsEgWQRWSXpTgG6zVm3%2B%2B%2BpWGHQ7vO7oZbaFaYmyObl3qQpa6%2BPXKRdTg%3D%3D)
- [Docker memory limits](https://link.segmentfault.com/?enc=IIUkWayyquHml4iGaj8O7g%3D%3D.l%2FX1S%2FyuHK0VoJc5Y0STLKjz8tJGOIPOATLhTHvOu5ceEu0oc%2FIw3LRW6TUVpYIgpPSwB8LDg6tucrVnpn3mDg%3D%3D)

### 其他

- [JEP 238: Multi-Release JAR Files](https://link.segmentfault.com/?enc=GXQvp%2Bau68tUNHh8a%2FSe0Q%3D%3D.6GeGqbyCznsPFOtO4Jf24LeWA9eeOrjF5%2BICbuKqF%2Bpo3HgjkyAz7LJSyecwgdce)
  - [java9系列(八)Multi-Release JAR Files](https://segmentfault.com/a/1190000013584354)
- [JEP 266: More Concurrency Updates](https://link.segmentfault.com/?enc=JfgBxttK46nilyd5kys60g%3D%3D.th8cyl1jfhn3%2BjgMdebCT9jCofLA1CbGOKt8TjYEf%2FxxRVggqx6PXaHkpNPNhgdF)
- [JEP 274: Enhanced Method Handles](https://link.segmentfault.com/?enc=7o%2FgNwpD3BfPfmqL26ibWQ%3D%3D.YvGhD%2F3m4qmkaEbqKdyBKH0LVpFRpPHYnQEUyxO6jhSlo9Un4GeuTza4vPW993f0)
- [JEP 295: Ahead-of-Time Compilation](https://link.segmentfault.com/?enc=FGzZ%2FTqWfnNPGRfh6g%2FMtA%3D%3D.hi0X%2Fq9104wqecBp74dvYYrxG7fbwwDLnQg7iTBdmF1cZSAUn1E82HdDhJcjwACG)

## 小结

java9大刀阔斧，重磅引入了模块化系统，自身jdk的类库也首当其冲模块化。新引入的jlink可以精简化jdk的大小，外加Alpine Linux的docker镜像，可以大大减少java应用的docker镜像大小，同时也支持了Docker的cpu和memory限制(`Java SE 8u131及以上版本开始支持`)，非常值得使用。