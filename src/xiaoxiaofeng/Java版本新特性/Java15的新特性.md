## 序

本文主要讲述一下Java15的新特性

## 版本号

```apache
java -version
openjdk version "15" 2020-09-15
OpenJDK Runtime Environment (build 15+36-1562)
OpenJDK 64-Bit Server VM (build 15+36-1562, mixed mode, sharing)
```

> 从version信息可以看出是build 15+36

## 特性列表

### [339:Edwards-Curve Digital Signature Algorithm (EdDSA)](https://link.segmentfault.com/?enc=DXcEkyPXmJbPaiOhgAbuwA%3D%3D.w%2B2%2F7vtSuP4f7sdCsIrMyC%2Bi58rKUu4n%2B8mHOQsArUlu91%2FN8Tlf7Ecf3bVrFk36)

> 新增[rfc8032](https://link.segmentfault.com/?enc=b%2FxFvY1tz7bDQasvnYme%2Bg%3D%3D.otoVHEbcr%2F2s0h%2FsaZQfokh5OphPU%2Fo6TSCsDA%2FFg9gFXf1ARKit9U5rAYGoNiYw)描述的Edwards-Curve Digital Signature Algorithm (EdDSA)实现
> 使用示例

```reasonml
// example: generate a key pair and sign
KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");
KeyPair kp = kpg.generateKeyPair();
// algorithm is pure Ed25519
Signature sig = Signature.getInstance("Ed25519");
sig.initSign(kp.getPrivate());
sig.update(msg);
byte[] s = sig.sign();

// example: use KeyFactory to contruct a public key
KeyFactory kf = KeyFactory.getInstance("EdDSA");
boolean xOdd = ...
BigInteger y = ...
NamedParameterSpec paramSpec = new NamedParameterSpec("Ed25519");
EdECPublicKeySpec pubSpec = new EdECPublicKeySpec(paramSpec, new EdPoint(xOdd, y));
PublicKey pubKey = kf.generatePublic(pubSpec);
```

### [360:Sealed Classes (Preview)](https://link.segmentfault.com/?enc=LHv35CEXM14fV3VgTXqdLg%3D%3D.7itu3pY18MmabCp5Fub0rJmz%2FhWENtiva0fWA63tpiVIOqfjtQ%2FORE7xS5KP3j2w)

> JDK15引入了sealed classes and interfaces.用于限定实现类，限定父类的使用，为后续的pattern matching的exhaustive analysis提供便利
> 示例

```scala
package com.example.geometry;

public abstract sealed class Shape
    permits Circle, Rectangle, Square {...}

public final class Circle extends Shape {...}

public sealed class Rectangle extends Shape 
    permits TransparentRectangle, FilledRectangle {...}
public final class TransparentRectangle extends Rectangle {...}
public final class FilledRectangle extends Rectangle {...}

public non-sealed class Square extends Shape {...}
```

> 这里使用了3个关键字，一个是sealed，一个是permits，一个是non-sealed；permits的这些子类要么使用final，要么使用sealed，要么使用non-sealed修饰；针对record类型，也可以使用sealed，因为record类型暗示这final

```nim
package com.example.expression;

public sealed interface Expr
    permits ConstantExpr, PlusExpr, TimesExpr, NegExpr {...}

public record ConstantExpr(int i)       implements Expr {...}
public record PlusExpr(Expr a, Expr b)  implements Expr {...}
public record TimesExpr(Expr a, Expr b) implements Expr {...}
public record NegExpr(Expr e)           implements Expr {...}
```

### [371:Hidden Classes](https://link.segmentfault.com/?enc=7Do%2BQoYu1niY9Qjg7NZmSg%3D%3D.zlR4kb1Soi%2F27aXcGiKxz7V3bL5sIhdq3EDh965BZqt8Q%2BTk7jkm0YyAOYnDNqVb)

> JDK15引入了Hidden Classes，同时废弃了非标准的sun.misc.Unsafe::defineAnonymousClass，目标是为frameworks提供在运行时生成内部的class

### [372:Remove the Nashorn JavaScript Engine](https://link.segmentfault.com/?enc=5rKDbkB1%2BsmWuE9UzcRJVA%3D%3D.sPWfGTxIk3a9z47gM1v2aqFAZgsDNnsXHNeVC0O67Vo5lWhjwKl4ZspXbx1zLoE5)

> JDK15移除了Nashorn JavaScript Engine及jjs tool，它们在JDK11的被标记为废弃；具体就是jdk.scripting.nashorn及jdk.scripting.nashorn.shell这两个模块被移除了

### [373:Reimplement the Legacy DatagramSocket API](https://link.segmentfault.com/?enc=COx84mSBd1GzS268C3GR1Q%3D%3D.wSWJFExL6jTk%2BhzEWl9nqjFYwaSdg5Qz%2Fcw9P2MxKwzS97rjCIzS45U1Ucbt6jST)

> 该特性使用更简单及更现代的方式重新实现了java.net.DatagramSocket及java.net.MulticastSocket以方便更好的维护及debug，新的实现将会更容易支持virtual threads

### [374:Disable and Deprecate Biased Locking](https://link.segmentfault.com/?enc=%2FO%2BaVh0WXHDDCAkciQQb9Q%3D%3D.b12aBgupWqzd5cRNpU3VkrowCdAKyPoz42ZfUcrCl2Z7ydCTAdWbR1zNXuFyycAX)

> 该特性默认禁用了biased locking(`-XX:+UseBiasedLocking`)，并且废弃了所有相关的命令行选型(`BiasedLockingStartupDelay, BiasedLockingBulkRebiasThreshold, BiasedLockingBulkRevokeThreshold, BiasedLockingDecayTime, UseOptoBiasInlining, PrintBiasedLockingStatistics and PrintPreciseBiasedLockingStatistics`)

### [375:Pattern Matching for instanceof (Second Preview)](https://link.segmentfault.com/?enc=siVnDAiLeX4ZgbIGX4Ioag%3D%3D.JWMpAuz%2Fdg7h8GZEgW6kZZrlzhnNrHyh5H%2Foue93d%2FJBMMlK6SJjKww9g3l%2FvwRH)

> instanceof的Pattern Matching在JDK15进行Second Preview，示例如下:

```typescript
public boolean equals(Object o) { 
    return (o instanceof CaseInsensitiveString cis) && 
        cis.s.equalsIgnoreCase(s); 
}
```

### [377:ZGC: A Scalable Low-Latency Garbage Collector](https://link.segmentfault.com/?enc=JQxr7Xol%2BHTp%2FwWMDKnzjg%3D%3D.1eYMuBzFIku8SCNnNKYZ0lg7230Y2L5OedMG8KWrtCUxOaIUfQOzqtA2QY7ehycd)

> ZGC在JDK11被作为experimental feature引入，在JDK15变成Production，但是这并不是替换默认的GC，默认的GC仍然还是G1；之前需要通过`-XX:+UnlockExperimentalVMOptions -XX:+UseZGC`来启用ZGC，现在只需要`-XX:+UseZGC`就可以
> 相关的参数有ZAllocationSpikeTolerance、ZCollectionInterval、ZFragmentationLimit、ZMarkStackSpaceLimit、ZProactive、ZUncommit、ZUncommitDelay
> ZGC-specific JFR events(`ZAllocationStall、ZPageAllocation、ZPageCacheFlush、ZRelocationSet、ZRelocationSetGroup、ZUncommit`)也从experimental变为product

### [378:Text Blocks](https://link.segmentfault.com/?enc=A2%2FdRuRUX3QmGdObA8GZCw%3D%3D.oqS%2Fa32RXd8J%2BuKicyIcQoIuweXO0rvhco0rll2RPmFVG4PGIMrenIuFLJ9GsTiZ)

> Text Blocks在JDK13被作为preview feature引入，在JDK14作为Second Preview，在JDK15变为最终版

### [379:Shenandoah: A Low-Pause-Time Garbage Collector](https://link.segmentfault.com/?enc=nPofbKQumPj1SORjXIizXQ%3D%3D.bCa2zsxFc7ExPdwXWcX4IZ1VA%2B1TNxejuzYNddHnk0W5jiACovvgklq1%2FRGzAyms)

> Shenandoah在JDK12被作为experimental引入，在JDK15变为Production；之前需要通过`-XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC`来启用，现在只需要`-XX:+UseShenandoahGC`即可启用

### [381:Remove the Solaris and SPARC Ports](https://link.segmentfault.com/?enc=QoXptAfGEQvcXFjlhMiPOQ%3D%3D.5sOtovzo7JBxTE7OJMYoHN28R%2FiOKuheysJpIxcTK5c3TWvh4F6IYv2jTmwYDbK5)

> Solaris and SPARC Ports在JDK14被标记为废弃，在JDK15版本正式移除

### [383:Foreign-Memory Access API (Second Incubator)](https://link.segmentfault.com/?enc=mD5l3lRUbfhgYAZxLcXemA%3D%3D.NQx%2FYkStiyt36PF1lOq691NhPTT3wNIqyu8VL8DM9iagA8mkS9bJWLrOvtHzUwgc)

> Foreign-Memory Access API在JDK14被作为incubating API引入，在JDK15处于Second Incubator

### [384:Records (Second Preview)](https://link.segmentfault.com/?enc=WD52FxSc1u%2FiNZnCoXHgZA%3D%3D.ZjW1HkXFrOD87VO7xJiRZykmX%2BcnzfRbm5aE6c8vJATzuuJAOtJiE%2BcKCIm9nQNL)

> Records在JDK14被作为preview引入，在JDK15处于Second Preview

### [385:Deprecate RMI Activation for Removal](https://link.segmentfault.com/?enc=WAM%2FXjMjSywHnfAIHujIvw%3D%3D.w5KYxF4KK8tkk2msSEObNXG7Cv13uI%2FFHFH1o%2BZ0TFJz1Sa0XsR8dXp9PS8hxqyn)

> JDK15废弃了RMI Activation，后续将被移除

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 15 Release Notes](https://link.segmentfault.com/?enc=4g%2F%2BV0kPpz3KC7ea6Y1hIA%3D%3D.u2AXOH8cBWtSYotgt05lin%2BINRXsLDaG90V%2Fjs%2Fz9OU27Nz7wvyVR3t0g5sb45%2Ff)，这里举几个例子。

### 添加项

- Support for Unicode 13.0 ([JDK-8239383](https://link.segmentfault.com/?enc=UIQu9j%2BkQpFja%2FWdPHkANA%3D%3D.VoHq1QfpGhXI0dzVnk%2Fj33CFSKMoWJuwvCIUK70ZdDIBF5u1Phk13SVr3mG%2FY%2FjoznJJPlK5WJzsGn4KYNDsqw%3D%3D))

  > 升级了Unicode，支持Unicode 13.0

- Added isEmpty Default Method to CharSequence ([JDK-8215401](https://link.segmentfault.com/?enc=NZCOW8lUon9DZmIYmDcZwg%3D%3D.wvPzK7S9cyLLFN92YBT%2BxfY5N7ZGhjFXnebh7ZTMCM4hN5bJBJFUrA5dzLH1eWa3TFux4k5wqMUxut7c2gpgyw%3D%3D))

  > 给CharSequence新增了isEmpty方法
  > java.base/java/lang/CharSequence.java

  ```aspectj
    /**
     * Returns {@code true} if this character sequence is empty.
     *
     * @implSpec
     * The default implementation returns the result of calling {@code length() == 0}.
     *
     * @return {@code true} if {@link #length()} is {@code 0}, otherwise
     * {@code false}
     *
     * @since 15
     */
    default boolean isEmpty() {
        return this.length() == 0;
    }
  ```

- Added Support for SO_INCOMING_NAPI_ID Support ([JDK-8243099](https://link.segmentfault.com/?enc=ntQTIWoxzjzmPID9j%2BHWSw%3D%3D.%2B3FkiYEoXUoXkrVnGXfcTEjotR1sDIsfkt%2Ff3YAeKjaAm%2BNeYQGXVR%2Fc%2F2NzGqLS1C8ezXGNIGNVxP1bQ09NlQ%3D%3D))

  > jdk.net.ExtendedSocketOptions新增了SO_INCOMING_NAPI_ID选型

- Specialized Implementations of TreeMap Methods ([JDK-8176894](https://link.segmentfault.com/?enc=9tUe7RpSqAAz80Hg2kNgIw%3D%3D.wuUq1ye6v8AlQ3IBXNr4xdUnZcb7iV1aysi7QcUUEsPIN%2F5peAFe39ID11fiexpfjeBkT4E0TgYAQ60RDbXgVw%3D%3D))

  > JDK15对TreeMap提供了putIfAbsent, computeIfAbsent, computeIfPresent, compute, merge方法提供了overriding实现

- New Option Added to jcmd for Writing a gzipped Heap Dump ([JDK-8237354](https://link.segmentfault.com/?enc=w8ac0ghBOYPr2j7KbMjIYg%3D%3D.zPND2d4SYkgKHaG8Jyov7d%2Fjs%2Bz3xmL3lRkTsW%2BajvoRnttMzULmnC7mnlGpdhxLAY6W04AFMoq92dOftsKHxw%3D%3D))

  > jcmd的GC.heap_dump命令现在支持gz选型，以dump出gzip压缩版的heap；compression level从1(`fastest`)到9(`slowest, but best compression`)，默认为1

- New System Properties to Configure the TLS Signature Schemes ([JDK-8242141](https://link.segmentfault.com/?enc=%2Fe5vtcpYEogU%2BlWRZ%2FalXQ%3D%3D.ZsXepCV5hj2Mx6Jgun7%2BqZP0DBkBgDNjP9ikNbb7h%2FE0MS2gMwp9zL7sMicTYc3V%2FfMXcS8WHD%2BnZ84ZUeOHjg%3D%3D))

  > 新增了`jdk.tls.client.SignatureSchemes`及`jdk.tls.server.SignatureSchemes`用于配置TLS Signature Schemes

- Support for certificate_authorities Extension ([JDK-8206925](https://link.segmentfault.com/?enc=KXAtBnw9XN3xRr9k2yznAQ%3D%3D.vZD0u%2F%2Foz7E1nFn%2FbfV1hDTfDnZ1wvijC4dM2h%2FRCAGgkN8WtAgF5NTifJjTnfB2WEI9pILdLtoZEQsIosswmg%3D%3D))

  > 支持certificate_authorities的扩展

### 移除项

- Obsolete -XX:UseAdaptiveGCBoundary ([JDK-8228991](https://link.segmentfault.com/?enc=fo%2FdHYfwFvN%2FLlrOoLMLdw%3D%3D.rN0QVNxwdvQvQZq1RA8FtRP%2Fn4H%2F6A3J2BqBC%2BhACkVQIvN56B20VGLkP9lpmqbVbipdjOKlzoH1bRAP6izYTw%3D%3D))

  > 淘汰了-XX:UseAdaptiveGCBoundary

### 废弃项

- Deprecated -XX:ForceNUMA Option ([JDK-8243628](https://link.segmentfault.com/?enc=53SyZIYE3bLhlqtrtHIcow%3D%3D.v3AOka12q%2FuZzLHpeJ9OdT4LGtmstnLQ9yIioMVRNrwMME0jralCSXai492ClLGoDq3vdhLYeShv62eecJ7jAQ%3D%3D))

  > 废弃了ForceNUMA选项

- Disable Native SunEC Implementation by Default ([JDK-8237219](https://link.segmentfault.com/?enc=ODQs8udLH4L9qte%2BizkMew%3D%3D.e4mvnKCPwDJoaWyr54mEl3%2BGHFRgYL53Fx5%2ByOfDGMISGmvNGgPuYiCZnhn5S5UxbgbZYERdv%2FICpGr67EuXYA%3D%3D))

  > 默认禁用了Native SunEC Implementation

### 已知问题

- java.net.HttpClient Does Not Override Protocols Specified in SSLContext Default Parameters ([JDK-8239594](https://link.segmentfault.com/?enc=yR18M%2B9unUZ0BB5WvKUg6A%3D%3D.MNRCtno5oToTO9mltay3Wd9ZYAMBaS8XCjAkUdcpwsqkYOWSxyw2v4CM5j4DXcvKgw%2FAJvE%2BSmljsM37ygCCvw%3D%3D))

  > HttpClient现在没有覆盖在SSLContext Default Parameters中指定的Protocols

### 其他事项

- DatagramPacket.getPort() Returns 0 When the Port Is Not Set ([JDK-8237890](https://link.segmentfault.com/?enc=s1nE2O8bO3wpFA%2FXI%2FKoLA%3D%3D.kMJ%2FIxyjOAp3P%2BiyU0kWi5GEaJJ2zZUGG6%2F17mj6PvxOak7On3Z%2F%2BYBHgsh6EZs%2FGCClwEdWN%2FxZmv9W%2BWE5Og%3D%3D))

  > 当DatagramPacket没有设置port的时候，其getPort方法返回0

- Improved Ergonomics for G1 Heap Region Size ([JDK-8241670](https://link.segmentfault.com/?enc=Pn%2By5%2FI4qce2Teabq9oQkw%3D%3D.JghF%2BMBgaNiLfeKEB9Sy6WJgXGr6fX5jgHgATgqY%2FbTNNTnHM%2BV8D5pwK6W3na%2BnacQV6LrCf1PlDKDu1QfxUg%3D%3D))

  > 优化了默认G1 Heap Region Size的计算

## 小结

Java15主要有如下几个特性

- [339:Edwards-Curve Digital Signature Algorithm (EdDSA)](https://link.segmentfault.com/?enc=1o%2FcDxjvajtTQXTCCozZUg%3D%3D.Rpsr5CwsfUEOrYwHWQ1KdwKRTHo9YTpTKVk6sWI0U3C85c20D%2B5U0oEiO0%2FVYUtN)
- [360:Sealed Classes (Preview)](https://link.segmentfault.com/?enc=MEEvIXOLHSD5izaMQyIC8w%3D%3D.fl%2FyS4YK3VzjzwKAHVphid0WpVEZ8tf7Ky4iJjvRwz8BQuuvSZOlwLaL2LtX%2Fi9h)
- [371:Hidden Classes](https://link.segmentfault.com/?enc=sqM79Ca02BmrUU6WNIJ2Yg%3D%3D.QOmmF6PSRtyJgi4b%2FnvxS683Src%2B7YFRbuZYmHcQ%2BNNuLP4bmTnV%2Bo8NFP1oIGf6)
- [372:Remove the Nashorn JavaScript Engine](https://link.segmentfault.com/?enc=Hcj2HqEsbryMZmu%2BkFBT2A%3D%3D.HTQ%2BoDUz1Bd%2Fcs5UZjOUiX%2BqJ%2Bs%2BO4fC8LCLL5aEto0spiWr9DZkUQ4rwHMHTMTB)
- [373:Reimplement the Legacy DatagramSocket API](https://link.segmentfault.com/?enc=nvQQ5%2B3KphEMWz8SNbpqWQ%3D%3D.39HM9cwlx0vIYhRfEgesHO6nQREXNd%2FYjRmhoe9eH3Tp0vYqKymovGQDf%2BYLGjkM)
- [374:Disable and Deprecate Biased Locking](https://link.segmentfault.com/?enc=z3A%2FuwWgGEXquDb9EP4fYA%3D%3D.I%2B2LjP%2B6O3CXUYGo0fGkzOPa%2FFQGrKMvh%2FYXtj423V%2B7MVnISeqtDdRLtG6by7hd)
- [375:Pattern Matching for instanceof (Second Preview)](https://link.segmentfault.com/?enc=JLCnEEQgBf9%2BGEPIuxauNg%3D%3D.RsjeuUF8z6ArjKA90UbdSBFWZD%2FzQu0PB4QVRnU%2BlMInxWuQdYbz93vrLq6ivr4a)
- [377:ZGC: A Scalable Low-Latency Garbage Collector](https://link.segmentfault.com/?enc=WvQziu%2BlH8mkSI7mrw8zmQ%3D%3D.kM5vJ7mPFsRaKZNSBKEUyFMzQHGXN4xP6Uh1wvbKvW9xjF7F0rIeGQ1PwqZvWUFW)
- [378:Text Blocks](https://link.segmentfault.com/?enc=actA5DalAAALB0gFtkFeoQ%3D%3D.8qjK740TzF7CaSchFsF9U%2BjMjFfJ2ISymqxGwRczbQTDGAZ3dnvxVmEQzm9eoxPM)
- [379:Shenandoah: A Low-Pause-Time Garbage Collector](https://link.segmentfault.com/?enc=xXrOwq%2B2y6kEnEeZ212IHw%3D%3D.NSyRb0wP%2FSsZ9%2F0uJTSbE%2FwLu%2Fx%2FxO72WNLMWGhOsdVhT4yeylZXJeKVlZheDnit)
- [381:Remove the Solaris and SPARC Ports](https://link.segmentfault.com/?enc=p4yIToRsJxC5fph7dFOl2A%3D%3D.sQQh9ZUxF7Z3B5elE%2B9z4HK46UqqwM%2FWSkFSzPez%2BVT%2BGpulZDDlS%2FcJNwMfkayt)
- [383:Foreign-Memory Access API (Second Incubator)](https://link.segmentfault.com/?enc=GxrZlY3m7fUdP1B6tpf1kg%3D%3D.LzlsXjAgUNG5gKGHGu2WtXwJ%2FGcs7WMRWHIdjJKbZqi0j46M7uUpifE7i7mbEP%2BF)
- [384:Records (Second Preview)](https://link.segmentfault.com/?enc=%2FwlZz2O7u1TEbVGwCNBv6g%3D%3D.6lQ9TxS9vy2ni%2BahWV7QjkV0s03TFyRM98boSoJv8ceCTTSZ%2FVtZ5bFxP%2Fd9DnRg)
- [385:Deprecate RMI Activation for Removal](https://link.segmentfault.com/?enc=o%2Bm49eIPXo%2Fz6ekt2d08Eg%3D%3D.C23iwT8hsQ50t75yHMKQcsIlLytOM5Om4FlwCHzVTngo9BNoLlMzSVCivpqiF7Bh)

## doc

- [JDK 15 Features](https://link.segmentfault.com/?enc=sx05c7BU0V3DneSgv0INeg%3D%3D.BrSuExlAwn4y2QhEO70gp%2BrkfTBH5A4aTyM6fGxZfpFcCcEpMhJYGJS%2BmsKox0QR)
- [JDK 15 Release Notes](https://link.segmentfault.com/?enc=uSSO4NTaV6uKuvi5Lsf6og%3D%3D.4y9OP%2BNkiiQe16I8i3CjBGQPrE82KsSGz5RcqUcc%2BPFX3aehJmbsIrIyh7kkT8UG)
- [Oracle JDK 15 Release Notes](https://link.segmentfault.com/?enc=%2Fq46ZWJGkXAtafF1XyGpww%3D%3D.zgybJhGp%2F%2B%2FIOYyhhB46XEZRUL1fy3ERA4sPGMN6GspZcb%2FQWnX7KQ0VGxuPcf6qjc%2BYHhdDxit0WANVyPX28yejxAM21HEGlcEhMsM3%2Brc%3D)
- [Java SE deprecated-list](https://link.segmentfault.com/?enc=RNMXCInGpbiAyBmNc18Lwg%3D%3D.p6RMvtn5xyDGXlZapExwiuaxq5PQYPouIyzHgc6RWXWqz30dXHRxeMPM%2FXM7clGTd3xMyorkNBY%2Bo3tg3SGg9UAJsOjtErHZQj1YGg3SfdU%3D)
- [The Arrival of Java 15](https://link.segmentfault.com/?enc=2sy7%2BJ4Eyc7662%2FOcFZc9w%3D%3D.wqmHFqZjGktguhWwzd9qAilGNctLQ%2FTqo134TseDmDkbfqtmbimqdqXa9AjIjMPcQEj8nKEekoOGBUQfh3NKx73psjVcSwrzVLJOktJ0gzI%3D)
- [Java 15 and IntelliJ IDEA](https://link.segmentfault.com/?enc=RgV9zjX8gG%2FsqhuLmn%2BLPg%3D%3D.%2BC%2Fbg84bWUORdcGFaZcr1vKZvm0Sz4a3zanP0CaHvfTS%2FdNQ6%2Bm%2FJgjyppyUKLfF52FcDOqgTaU47t9lcEcLNM%2B7DuinETPSX8%2Fcg3hzWDg%3D)