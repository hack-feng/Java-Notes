## 序

本文主要讲述一下Java16的新特性

## 版本号

```apache
java -version
openjdk version "16" 2021-03-16
OpenJDK Runtime Environment (build 16+36-2231)
OpenJDK 64-Bit Server VM (build 16+36-2231, mixed mode, sharing)
```

> 从version信息可以看出是build 16+36

## 特性列表

### [JEP 338: Vector API (Incubator)](https://link.segmentfault.com/?enc=nE0OXafGEn7fDNsWXmeCoQ%3D%3D.IqhMlydUYBGn74nU4TVm2XkqaCOPmcFNWBi9q0IJ1JV9lIIBmGID9ZS5VyttyZSy)

> 提供了jdk.incubator.vector来用于矢量计算，实例如下

```csharp
static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_256;

void vectorComputation(float[] a, float[] b, float[] c) {

    for (int i = 0; i < a.length; i += SPECIES.length()) {
        var m = SPECIES.indexInRange(i, a.length);
        // FloatVector va, vb, vc;
        var va = FloatVector.fromArray(SPECIES, a, i, m);
        var vb = FloatVector.fromArray(SPECIES, b, i, m);
        var vc = va.mul(va).
                    add(vb.mul(vb)).
                    neg();
        vc.intoArray(c, i, m);
    }
}
```

### [JEP 347: Enable C++14 Language Features](https://link.segmentfault.com/?enc=lfV33grzZR7Fx%2FtxRfhE1Q%3D%3D.0RaW0g7ThvNpieBXW2cpbBAH7vPxvVeCxYB8H5TFwW2LuezMAZoPF%2Bwlbf2NsJ0G)

> 在JDK C++的源码中允许使用C++14的语言特性

### [JEP 357: Migrate from Mercurial to Git](https://link.segmentfault.com/?enc=7yy6PqvY3ALEVi6nM%2F6Ikw%3D%3D.1PnKTeK4CDunvHIw75HAGYAwgE1CtqaWIIgdDRPFh3BwAZ%2B7MQYXUQvB0wpnUynk)

> OpenJDK源码的版本控制从Mercurial (hg) 迁移到git

### [JEP 369: Migrate to GitHub](https://link.segmentfault.com/?enc=6uwVnxHnWnBvYJXBJ0ThsA%3D%3D.NYxeKAn76TCy6UR%2FhmewFE0HA5poIurFuc1oC4PQPHzFrztjOkH9KTP6HZBIGyEc)

> 将OpenJDK源码的版本控制迁移到[github](https://link.segmentfault.com/?enc=4LSAV6%2Bod58W%2B2ArtOjhMg%3D%3D.9h5LqiKkXHtIL6Y%2FgYp%2BErrI00KyVEeO%2F9VES7H%2BRjY%3D)上

### [JEP 376: ZGC: Concurrent Thread-Stack Processing](https://link.segmentfault.com/?enc=EyiVRgnKZL3kL5eO9L1Q8w%3D%3D.Ss%2BvVNMO0%2BjKoUlMeeeJtUwpEAr29U68K7cFzRGOAeUlIPkxzN8A3XiALFSs3UEV)

> 实现了并发thread-stack处理来降低GC safepoints的负担

### [JEP 380: Unix-Domain Socket Channels](https://link.segmentfault.com/?enc=d4XLlCgeTg3lid8C2hQyIA%3D%3D.L5iQbflOswOpcb31CAxjkTt99pD%2FndV4jU1sdKRFbmxtaMqCxmEJCY3PALokK4P2)

> 对socket channel及server socket channel的api提供对unix domain socket的支持

### [JEP 386: Alpine Linux Port](https://link.segmentfault.com/?enc=FiJ%2BkJWK%2B1sWJLjHCpQ5Ow%3D%3D.2zS0R40HuPLzbhq2kV7HQ4SGVLxut%2BO05w0jk0M3GFcJ6lpJ9i1kWwuJ2iLPWkzg)

> 将glibc的jdk移植到使用musl的alpine linux上

### [JEP 387: Elastic Metaspace](https://link.segmentfault.com/?enc=Uv9TqZtfPG3STS%2BFB4d%2Frg%3D%3D.U%2FcNjokba7DmkF38JoD4y%2BjNDxwe%2FdIdkTRhO0cbwSNdXKgDKW4KF2bf014tOr9f)

> 支持不再使用的class metadata归还内存给操作系统，降低内存占用

### [JEP 388: Windows/AArch64 Port](https://link.segmentfault.com/?enc=DQVtcCkpVh6BUfTwNjbJbw%3D%3D.Zt0CQ%2FLY94YMrGsQgBNvTIdM173y%2B9zZj7RPket7hzVmEUHk8NeT50lI1WD1puxy)

> 移植JDK到Windows/AArch64

### [JEP 389: Foreign Linker API (Incubator)](https://link.segmentfault.com/?enc=96XCFer3ouOooI9HaTMbbQ%3D%3D.2QmFCL8gs04A150P%2FbydJRMdN46%2FXwXXoInWwhG4crTjUvrobX%2BIkJ%2FYXxOLvAiW)

> 提供jdk.incubator.foreign来简化native code的调用

### [JEP 390: Warnings for Value-Based Classes](https://link.segmentfault.com/?enc=8HNTaYK3QUFRtBt8e%2Bkkqg%3D%3D.fDw6316c%2FJXKnCHuw0oN8BmOBa%2FHhUYu2kuJBydN0nxdf9oK%2BXOLY1q8FF01sdms)

> 提供@jdk.internal.ValueBased来用于标注作为value-based的类，实例如下

```java
@jdk.internal.ValueBased
public final class SomeVbc {

    public SomeVbc() {}

    final String ref = "String";

    void abuseVbc() {

        synchronized(ref) {           // OK
            synchronized (this) {     // WARN
            }
        }
    }
}

final class AuxilliaryAbuseOfVbc {

    void abuseVbc(SomeVbc vbc) {

        synchronized(this) {           // OK
            synchronized (vbc) {       // WARN
            }
        }
    }
}
```

### [JEP 392: Packaging Tool](https://link.segmentfault.com/?enc=5q1lkHhN5x0wBvfOrY4ZLQ%3D%3D.o3%2FGuMGOCSyjedhw1yCjH0%2FrTk79fGtxwjiu5JTo3x4PlPNMOpuzAAPEkzX1b3vS)

> jpackage在JDK14引入，JDK15作为incubating工具，在JDK16转正，从`jdk.incubator.jpackage`转为`jdk.jpackage`。它支持Linux: deb and rpm、macOS: pkg and dmg、Windows: msi and exe

### [JEP 393: Foreign-Memory Access API (Third Incubator)](https://link.segmentfault.com/?enc=xh0Bx8db6UqYHAheB9Iv7g%3D%3D.Yf78Oz30OyV5rNU0z0N3sY6PPNFrX3CalKKglinmMiGVOxM3UtAFsqqGl5Ud9Tz9)

> Foreign-Memory Access API在JDK14首次引入作为incubating API，在JDK15处于第二轮incubating，在JDK16处于第三轮incubating

### [JEP 394: Pattern Matching for instanceof](https://link.segmentfault.com/?enc=CymI3rgBdd485ohLC6UIzg%3D%3D.VjlBQMCZ9PiJ%2Bc80%2FPWvbSGakQPzIgpEvOnLfrw6eGWTBLTgIFBlALUeQeyMnXpm)

> instanceof的模式匹配在JDK14作为preview，在JDK15作为第二轮的preview，在JDK16转正

### [JEP 395: Records](https://link.segmentfault.com/?enc=a0lPQJmb1Gmtf6qvHXkPOA%3D%3D.5D8wadqNU1niu1U7CrB%2Fyujl3lPDy3f3VJ0GN3QZV49wGWH%2FG2%2BPISh0qOW9N8B6)

> Record类型在JDK14作为preview，在JDK15处于第二轮preview，在JDK16转正

### [JEP 396: Strongly Encapsulate JDK Internals by Default](https://link.segmentfault.com/?enc=qNHyjqYU%2FToFwpht%2BREmDw%3D%3D.Tc1%2F9pqE%2FIkY0i0Mvg7%2FQn6C1jXAp0RhsqMOmeXO8eWvYskUk22v85zoRYPG6CFX)

> 对内部的api进行更多的封装，鼓励开发者从使用内部的方法迁移到标准的API，但是`sun.misc.Unsafe`还是继续保留

### [JEP 397: Sealed Classes (Second Preview)](https://link.segmentfault.com/?enc=1uZAsgIS%2BMjB6dL8auwnBQ%3D%3D.xCWR08R91SFMIyRnXTPwlNpTy0bj0y26k0Qdl0LVMVnATZ%2FC%2FqqyWydjx8iNra20)

> Sealed Classes在JDK15作为preview引入，在JDK16作为第二轮preview

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[JDK 16 Release Notes](https://link.segmentfault.com/?enc=%2BeWoWLVF7%2BgToq6NRMmdIA%3D%3D.IgpJzsyOMvya3yGD5pkE4U12%2F8a17oPsN9JYsm1oRkpD6TZwRkeeC9SEQeAAER0o)，这里举几个例子。

### 添加项

- Add InvocationHandler::invokeDefault Method for Proxy's Default Method Support ([JDK-8159746](https://link.segmentfault.com/?enc=AghujIBX8%2B4qu5mf3bUXLQ%3D%3D.tlDzZ2az%2BeFEqz6y46Fjc8JIMI9MCSiXVkmC%2FTnfJ%2FBquGFujxRP0ySYpoVS6NhBLPADF2ehq3HiDEppeDhEgw%3D%3D))

  > 给InvocationHandler添加invokeDefault方法

- Day Period Support Added to java.time Formats ([JDK-8247781](https://link.segmentfault.com/?enc=%2BCaQ3X0ewSMcipQR3%2F9Gzg%3D%3D.mkdFU6l9b01qxbnKh3%2FDUZmlLgPaHahrgjf%2FKYgN1Q3GdbXArFFu7HjqzhhkkbICU199k4tcGt5sFEF0LY%2FQtg%3D%3D))

  > java.time支持Day Period

- Add Stream.toList() Method (JDK-8180352) ([JDK-8180352](https://link.segmentfault.com/?enc=8YFQKoq5l2ynID%2Fu%2FyF8hQ%3D%3D.owaallMc8xeV1XM%2BUD3BsDzPytqkR2gRKU%2BaQ%2B1qxNvNrfn836jC8EZLDv33KCA9NAWNW1NZ0W9OncpQ8Umq1w%3D%3D))

  > Stream新增toList方法

- Concurrently Uncommit Memory in G1 ([JDK-8236926](https://link.segmentfault.com/?enc=EQ03XC63o68PdBxkmnVmPQ%3D%3D.ZozB%2BSgjB2nPENedm88tJMDvXVbf8ud2NYgxXTo%2BcDzGNWV3CDfvaHeCCcSNukyN%2FcXwI8Vbqtc1LPxr5naRTQ%3D%3D))

  > 针对G1提供了并发归还内存给操作系统

### 移除项

- Removal of Experimental Features AOT and Graal JIT ([JDK-8255616](https://link.segmentfault.com/?enc=qPWB8ZZI5SUnxh7vzsojGQ%3D%3D.opuTR2tJmeGPwAzqz0vK359rVlfjnU9iMJ9s38rE%2BNf57bvTgdslSj5Na8%2ByCx%2BYflnkvmrlYjpwfUfyWsTiWQ%3D%3D))

  > 移除jaotc工具

- Deprecated Tracing Flags Are Obsolete and Must Be Replaced With Unified Logging Equivalents ([JDK-8256718](https://link.segmentfault.com/?enc=JCwLWKXQDNciKWqDCAfXhg%3D%3D.ARkAKVUrcZwMqHF1DbVFzecpf8gbO5GjBP4FI0RiLNy7Rns88qIGj8G6P68kFBMas8ZGrjbbh3riqv4oYGz8Mw%3D%3D))

  > 使用`-Xlog:class+load=info`替代`-XX:+TraceClassLoading`；使用`-Xlog:class+unload=info`替代`-XX:+TraceClassUnloading`；使用`-Xlog:exceptions=info`替代`-XX:+TraceExceptions`

### 废弃项

- Terminally Deprecated ThreadGroup stop, destroy, isDestroyed, setDaemon and isDaemon ([JDK-8256643](https://link.segmentfault.com/?enc=CH0BL3Mi5XZWaJfEzhcWkw%3D%3D.fx5h84XPx39Mo3cpEkZVGWNtvse4VO%2BMnq4PketoCo2NyBUGCQirtk2tEyk%2FVsr4%2F%2BJVQBUl1qv%2FH8LR%2BMcHkw%3D%3D))

  > 废弃ThreadGroup的stop, destroy, isDestroyed, setDaemon, isDaemon方法

- Deprecated the java.security.cert APIs That Represent DNs as Principal or String Objects ([JDK-8241003](https://link.segmentfault.com/?enc=xym8CynBMU91tJ1YFkE8rQ%3D%3D.OcJfFJvkgKfBkSP3Gkm8QWYWOHdVEZVxR4OmNipECsub7w3NUa5gvu%2F23wVhLa8gRrd3gF0d%2Flsg29vKJwXTVA%3D%3D))

  > 废弃了java.security.cert.X509Certificate的getIssuerDN()、getSubjectDN()方法

### 已知问题

- Incomplete Support for Unix Domain Sockets in Windows 2019 Server ([JDK-8259014](https://link.segmentfault.com/?enc=lf000c%2FwfhEwoj9yfLI7fA%3D%3D.TrjDOw9KThg%2B7F1yG5MP25g%2F7knC1mJvbjLaXL0KqKznP%2FN5YQ%2FcAAu2OpsJXy3xFgyOegLFS0w29p%2BGaPTX%2BQ%3D%3D))

  > Unix Domain Sockets对Windows 2019 Server的支持还不完善

- TreeMap.computeIfAbsent Mishandles Existing Entries Whose Values Are null ([JDK-8259622](https://link.segmentfault.com/?enc=%2FCfkmEQlifpgbFQfa2JU0w%3D%3D.pCLLBQYwS3jh4hny9s%2FAPOGnONml8n4EmdumQrPbrKfsXZVL2vbuT2lOaEOWXPqlypv1%2BM4Trz50OlcQxGZwOQ%3D%3D))

  > TreeMap.computeIfAbsent方法针对null的处理与规范有偏差

### 其他事项

- Enhanced Support of Proxy Class ([JDK-8236862](https://link.segmentfault.com/?enc=nOggvwcr8vQyP6Dak4Imrw%3D%3D.%2BSW7kmqjDd34gF4ExQmBoJwcTfzfQ9Wn38vBJ9JhFgQWixEfDWXDQvCfzsLdlz%2Btf2Ws%2FEt6c7I0ghr1aTHzUA%3D%3D))

  > 对Proxy Class进行了增强，支持`jdk.serialProxyInterfaceLimit`属性

- Support Supplementary Characters in String Case Insensitive Operations ([JDK-8248655](https://link.segmentfault.com/?enc=EWpgsy%2BqvjPMnhk4%2F7hGQw%3D%3D.2yMEwxmvDUzKWDIc7pqUjpCRI3uwZCsgvN%2B%2Fq8iwjXp%2BAl1veKjjGYppTztTX1eoRUsVRXM6J9vcCESA%2FY3vjA%3D%3D))

  > 对compareToIgnoreCase、equalsIgnoreCase、regionMatches方法的Case Insensitive语义进行了增强

- HttpClient.newHttpClient and HttpClient.Builder.build Might Throw UncheckedIOException ([JDK-8248006](https://link.segmentfault.com/?enc=F%2FCKQ%2BEPPuCBym9jbyQ%2BOw%3D%3D.HreOgdgee0dckYRtNkgASX%2BfFsukXNI93bNu9WUwiriqbUugRrAkPRjpxhHtxVOnSCHnXWOIMe4u8oC6t65hdg%3D%3D))

  > HttpClient.newHttpClient及HttpClient.Builder.build方法可能抛出UncheckedIOException

- The Default HttpClient Implementation Returns Cancelable Futures ([JDK-8245462](https://link.segmentfault.com/?enc=hB8EswF5ufuY%2FIvkt8Q4ng%3D%3D.QyVdThSDUkf5bQIZK1eVaMeBtlrJgCr%2F36Pu%2Fy%2FxPM1ot5Nd0fg800r%2BotsmL1HAAeIg27at0vtIzrUQLjOwZg%3D%3D))

  > 默认的HttpClient实现返回Cancelable Futures

## 小结

Java16主要有如下几个特性

- [JEP 338: Vector API (Incubator)](https://link.segmentfault.com/?enc=i8bGZjwc%2FeSfk%2BzsU3Bp2Q%3D%3D.NAXXXuhBs4OikhbNKTGn7Jaeguvbc9n9Mjac%2FoYsjSCy97kyOzJz9x70z6XMNwhH)
- [JEP 347: Enable C++14 Language Features](https://link.segmentfault.com/?enc=BHjnKEjYkbmLKgVw4%2BwCtg%3D%3D.DsB0Vq%2FhY8Q%2BCswqa%2BVlr6doNAEgcorCycEMmuv%2Fn%2FWHs7BP87TLma39p4WtKf9I)
- [JEP 357: Migrate from Mercurial to Git](https://link.segmentfault.com/?enc=YrhyeACz8OgGU4qmxzhyrQ%3D%3D.PqEx%2BVYKgvy8d5vBUloRx49cku3PnAlzQPG3WmtXO%2Fq8i0whOrQua7LtC4wJsCYq)
- [JEP 369: Migrate to GitHub](https://link.segmentfault.com/?enc=CcZYFHDA4FlXlV7VjCT3%2BA%3D%3D.pBt56OSVUO3UudlOyhbVNYwSK7AuWWNgybvscBPXM64cEoDVScYUp76w0jRBb%2BY0)
- [JEP 376: ZGC: Concurrent Thread-Stack Processing](https://link.segmentfault.com/?enc=7UHRUKdaI7rqkxtS6jSklw%3D%3D.6epLYT2oId7dp0DjymG%2FH2EFS0tVIfBoQ%2FoWTwcbn%2FYUfCAUqtSLpC4bTjLlzrBW)
- [JEP 380: Unix-Domain Socket Channels](https://link.segmentfault.com/?enc=gwpPQcciXbC5Y4%2BgthgvEA%3D%3D.J5CM7YcaGSGo2e3dYTON6IONJOzKswdKaFFZ7umqNPbO36WeYIK42TxChGj16o2q)
- [JEP 386: Alpine Linux Port](https://link.segmentfault.com/?enc=6pf68ryOEF7Sk8a5FrZPhQ%3D%3D.acyJ5mrXtlJzZfsDfRDS10xCL27xdPwi%2B4VH%2FzpyNle5%2BGZYa%2BcCQ3KWK1qmNedg)
- [JEP 387: Elastic Metaspace](https://link.segmentfault.com/?enc=7LTWeNRLMvTdTXBVlwHWqw%3D%3D.ilZ9iS35hD2MTocGmkKNVw1fS0f5GKsqF8qiPZWI9162UV0hOA6mkv21m4SkFuTM)
- [JEP 388: Windows/AArch64 Port](https://link.segmentfault.com/?enc=Peq%2BlaarBhkDdsAykmMKBQ%3D%3D.fzaPyckVxqB3%2FaP6NY3MHtKUfp7ZhTZd5et5PkIJ6SrYTJBE2cx4WKImMweWu1r1)
- [JEP 389: Foreign Linker API (Incubator)](https://link.segmentfault.com/?enc=6lGlxQ0zAyBmkEa0aBbBjA%3D%3D.B%2ByCaluxmIedVoznlhvTrpbwLICIJvh1Z1D3gnAZCVKTnEvTq6ZGq0sC555HEkoG)
- [JEP 390: Warnings for Value-Based Classes](https://link.segmentfault.com/?enc=XWBUqBTSthZ0BzZM7o9vDA%3D%3D.E5I1DNLjOCXqJ8njpLVY6xKzodG3fT5se9jxcUf7iFGER1gB01iBDbGHaVfpNpk0)
- [JEP 392: Packaging Tool](https://link.segmentfault.com/?enc=d3od3QPodadrGgnEoOGYnw%3D%3D.B044OqAzrpwH9sF1%2B6WxqyZ1h0qzc%2BvXONdH9DtjD%2Bi%2BFeUJxwR0UwFWSMON%2BXAM)
- [JEP 393: Foreign-Memory Access API (Third Incubator)](https://link.segmentfault.com/?enc=lCfI4saeWpnaYQMEqiB93w%3D%3D.FA7mi0eaFPbdykyhgGHlckFtXXQT9e4K5xKZLqWLwVvVav5AX%2B%2FUgmpHKIgCkj5k)
- [JEP 394: Pattern Matching for instanceof](https://link.segmentfault.com/?enc=9JtUd%2FakMAbg8L3jvRNKrw%3D%3D.UIdMtLeUv7I3nJzKfgVytIUr%2F156yfBUq%2BDKdgVeRuy%2FGIcq168iW4N%2BzTqg25Wl)
- [JEP 395: Records](https://link.segmentfault.com/?enc=FumM4Qq3Ippe8C55kqdFFg%3D%3D.dvAMX7VMA4mn6wVZuxjbyY30cxkxfrbaAp7KBVmaMUwcxHRw%2B5GOi2X6IabmR2C%2B)
- [JEP 396: Strongly Encapsulate JDK Internals by Default](https://link.segmentfault.com/?enc=%2B0Mxq%2BO53IlbNAj4JZXaAQ%3D%3D.ZZ9T84hBNEzxi0uf3bv3iMHJZgbsGe8T%2BFfclBLnC6vj6AtYlF62juDo2qfgwYcg)
- [JEP 397: Sealed Classes (Second Preview)](https://link.segmentfault.com/?enc=gzOMd1BoR6BQsasUmnzENA%3D%3D.8i6n4snPf8FGCqjG1D%2B0q97JLIU258AKLp4fBbAFX8U7hGMYT6CScmtdjViLs2e4)

## doc

- [JDK 16 Features](https://link.segmentfault.com/?enc=7%2FbYhgHDlWCEo7RHHok%2F4g%3D%3D.1PC8sO6iMVyEV5f8aEHbxbV8zQGvCV%2BVU6s2LqaDdUlEEIAHxJhrkPhTtKAHwe4H)
- [JDK 16 Release Notes](https://link.segmentfault.com/?enc=OFwit8jgORLN2qamJUBlXw%3D%3D.GpRyTWcJ8p0Mnb5xGeqbU1bvU%2FIuVNpGXW1GcRPIyK4QFwY40IPPi4ViWHnOWPFC)
- [Oracle JDK 16 Release Notes](https://link.segmentfault.com/?enc=BZCnypA8j7Rqrxy53A8zKQ%3D%3D.FtEF2VCZ2oXqSZmFff9HjlOi5GuJj7RbGNa4ruMJIGrusIkbFWUegZMDcJFq4VwQH3lpK6SYCG1d6vcKDaXeQxUZkuDIEWJVv3%2BqJj%2FVZIs%3D)
- [Java SE deprecated-list](https://link.segmentfault.com/?enc=0LFD6MeXHKsk4GoGBe3Wnw%3D%3D.RP35i620Ks5u%2BuIS%2BKfcMu%2Bo%2F5RuG4210SKM91K1ZKXucd7Qr2pTg7HZoATWeCWPaKVOZxskJ5U%2BjMygEEE3jNfCChWNChiAKZzvWGJ%2B1Yg%3D)
- [The Arrival of Java 16](https://link.segmentfault.com/?enc=Br5HzQAC2CXCMMea9kcLrA%3D%3D.3DGoWwez9406D8enyE%2BxWsLM%2FV%2BSDJAWXyKbGGL3fx4LNAshyv6IhvkiLMXNGZAt%2B2lxIzRwEiNYzlwI7al%2FG1q8l1IQBwhJUIxDWeiSEsY%3D)
- [Java 16 and IntelliJ IDEA](https://link.segmentfault.com/?enc=fS57LRq0NSpeBGBZqWXQkA%3D%3D.bqi%2FuEi3bQFkYl%2BTsBa4mZaPrkC%2F0riMTaVKNu7mDUctWb82vSmBqxAatnjfNbgWrB2%2BWHBUJC5aRIc8cyewTvj3bErIEmuj3sK5JI8f9d8%3D)