## 序

本文主要讲述一下Java10的新特性

## 特性列表

- [286: Local-Variable Type Inference](https://link.segmentfault.com/?enc=zuQIlwCLs7wDIKFjMm7ftA%3D%3D.8%2Fy4GC1EgpPTElvIyAvpomLGl8sLvOESQ8AzDl%2FvADBtFfXAvJNF9lQEQ7m2MGqY)(`重磅`)

  > 相关解读: [java10系列(二)Local-Variable Type Inference](https://segmentfault.com/a/1190000014025792)

- [296: Consolidate the JDK Forest into a Single Repository](https://link.segmentfault.com/?enc=T2tjrn0BKwVN4%2F69hssNZA%3D%3D.cmfhJ5%2BRLwfoajkQL7v%2BHPeAfRZLf2ppBANmcQVMQuKTVG1%2BHCTPCsjzLGMjLGKc)

- [304: Garbage-Collector Interface](https://link.segmentfault.com/?enc=uljnqsZhek78sYoMvIfW0A%3D%3D.Mj5h7986bxuWEHQCxAf12gE8Ime5JHj1U4UnRe%2BiVKTL%2FJW8KTcoUsYJoWKkIcl6)

- [307: Parallel Full GC for G1](https://link.segmentfault.com/?enc=NofwTUzq8FK8Iymdr14PGA%3D%3D.T4HmBtqFrM1u6%2FqCBWhUHDvwzBlzpfupRy4xZYsuPlSvaZ7R%2FFpTSsHljEsbhw59)

- [310: Application Class-Data Sharing](https://link.segmentfault.com/?enc=1HTi5T1s%2Fsz6vZZLg5Gurw%3D%3D.W7spCuinLzrRtg4BNBXT7z7JtJ7I0i53eqpY3BRyrPouYXfbfUSdogttTtYN0EdY)

- [312: Thread-Local Handshakes](https://link.segmentfault.com/?enc=gBSdYnBc2RBX6%2FK2Pt%2Bw8Q%3D%3D.nBbH1Gxt2LMQ8o41AUT9PHCGqYiAKPl4BxJF6E8po2AVA85a4767PdEs%2BsmK4yOY)

- [313: Remove the Native-Header Generation Tool (javah)](https://link.segmentfault.com/?enc=Awi0XkFt0uXjGsLlwuBlwQ%3D%3D.4CQsJkat98kXFg5Wot5oMFzbH9ADM%2BVd1EyPWEx66EA9iZxw1K61FR0SftwrguJE)

- [314: Additional Unicode Language-Tag Extensions](https://link.segmentfault.com/?enc=K%2BLDZGnVtXSHFOPd9onlMA%3D%3D.V1gsXB6Du6mRjK6LEiWMQmDIYdWpvEP%2BXWw2fe%2FYtGuRWw1Op4gE1A73965oA%2BB3)

- [316: Heap Allocation on Alternative Memory Devices](https://link.segmentfault.com/?enc=jmh3jNIzXMAPIwRCzwrYEg%3D%3D.Dh6anN7TnrXftobPTIc7aGWESO3KfQaGLpRDTOKQhkaYETWWTpyGg9vkSQCBvjUd)

- [317: Experimental Java-Based JIT Compiler](https://link.segmentfault.com/?enc=ofRR92N3Bjg4YtdVi8CydQ%3D%3D.HpRwuyiG0eluBAowWgh41S4IG%2FSwgVJVAcsVQmazNcd%2FmERpzYZypjahQqRF0lXS)(`重磅`)

  > 相关解读: [Java10来了，来看看它一同发布的全新JIT编译器](https://link.segmentfault.com/?enc=oY1k2Bi4lDmWuUA9V5NNLA%3D%3D.7ZYZI4xz7MbtSDzBiIkmCse7RKaxQ64bLgK4wSESV%2FC93dBvTcBmpn3aSfjzUYE9a0bKV8QtqBVGZJ%2F7pYsSaQ%3D%3D)

- [319: Root Certificates](https://link.segmentfault.com/?enc=baBf4UAgZ5MxfWgkR0hlqQ%3D%3D.Cut1rs4D9lESH3qp4EBFT976XL5KopndNL1v8K19JXUajnSb3R8GT6wWbVsH0zK4)

  > 相关解读: [OpenJDK 10 Now Includes Root CA Certificates](https://link.segmentfault.com/?enc=yLTE1k9vO63roZTPDMMr1Q%3D%3D.pPSYTn4nIsygp0Kk9z35z4Hkbgk6s2z%2F8kHnnTOIQn0G9LRNFlO3Pi8QJQcmv7cJMduSaahzRIoSPO6gxqOV8%2BGZHzRzAdqpPzy7fgHMaV4%3D)

- [322: Time-Based Release Versioning](https://link.segmentfault.com/?enc=9yDiPi4Py0%2FlTAhA7ZdI6g%3D%3D.wIndm245itAJqSGTvjOCmVYEdcNPYRh1u9iMN7QXxbV%2B5k5Fmv85VzEpXV%2BzACCU)

  > 相关解读: [java10系列(一)Time-Based Release Versioning](https://segmentfault.com/a/1190000013885784)

## 细项解读

上面列出的是大方面的特性，除此之外还有一些api的更新及废弃，主要见[What's New in JDK 10 - New Features and Enhancements](https://link.segmentfault.com/?enc=fRiQjQ5B2X%2FlBhJbhDXI8g%3D%3D.N0Fc5QDDieEnO%2B%2BkZyxEYxLW0ZgvjZqEJ5O5QcR1ViCw3jpsztno8%2FsFfLxxNFdouj%2FbUoinnB9RCM7wUDzyyFn3sA8kWDajFhxmVavl6pA%3D)，这里举几个例子。

### Optional.orElseThrow()

/Library/Java/JavaVirtualMachines/jdk-10.jdk/Contents/Home/lib/src.zip!/java.base/java/util/Optional.java

- 源码

  ```java
    /**
     * If a value is present, returns the value, otherwise throws
     * {@code NoSuchElementException}.
     *
     * @return the non-{@code null} value described by this {@code Optional}
     * @throws NoSuchElementException if no value is present
     * @since 10
     */
    public T orElseThrow() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }
  ```

- 实例

  ```java
    @Test
    public void testOrElseThrow(){
        var data = List.of("a","b","c");
        Optional<String> optional = data.stream()
                .filter(s -> s.startsWith("z"))
                .findAny();
        String res = optional.orElseThrow();
        System.out.println(res);
    }
  ```

  > 新增了orElseThrow与get相对应

输出

```stylus
java.util.NoSuchElementException: No value present

    at java.base/java.util.Optional.orElseThrow(Optional.java:371)
    at com.example.FeatureTest.testOrElseThrow(FeatureTest.java:19)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.base/java.lang.reflect.Method.invoke(Method.java:564)
    at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
    at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
    at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
    at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
    at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
    at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
    at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
    at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
    at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
    at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
    at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
    at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
    at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
    at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
    at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
    at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
    at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
    at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
```

### APIs for Creating Unmodifiable Collections

> java9新增的of工厂方法的接口参数是一个个元素，java10新增List.copyOf, Set.copyOf,及Map.copyOf用来从已有集合创建ImmutableCollections

- List.copyOf源码

  ```java
    /**
     * Returns an <a href="#unmodifiable">unmodifiable List</a> containing the elements of
     * the given Collection, in its iteration order. The given Collection must not be null,
     * and it must not contain any null elements. If the given Collection is subsequently
     * modified, the returned List will not reflect such modifications.
     *
     * @implNote
     * If the given Collection is an <a href="#unmodifiable">unmodifiable List</a>,
     * calling copyOf will generally not create a copy.
     *
     * @param <E> the {@code List}'s element type
     * @param coll a {@code Collection} from which elements are drawn, must be non-null
     * @return a {@code List} containing the elements of the given {@code Collection}
     * @throws NullPointerException if coll is null, or if it contains any nulls
     * @since 10
     */
    @SuppressWarnings("unchecked")
    static <E> List<E> copyOf(Collection<? extends E> coll) {
        if (coll instanceof ImmutableCollections.AbstractImmutableList) {
            return (List<E>)coll;
        } else {
            return (List<E>)List.of(coll.toArray());
        }
    }
  ```

- 实例

  ```java
    @Test(expected = UnsupportedOperationException.class)
    public void testCollectionCopyOf(){
        List<String> list = IntStream.rangeClosed(1,10)
                .mapToObj(i -> "num"+i)
                .collect(Collectors.toList());
        List<String> newList = List.copyOf(list);
        newList.add("not allowed");
    }
  ```

> Collectors新增了toUnmodifiableList, toUnmodifiableSet,以及 toUnmodifiableMap方法

```java
    @Test(expected = UnsupportedOperationException.class)
    public void testCollectionCopyOf(){
        List<String> list = IntStream.rangeClosed(1,10)
                .mapToObj(i -> "num"+i)
                .collect(Collectors.toUnmodifiableList());
        list.add("not allowed");
    }
```

## 小结

java10最主要的新特性，在语法层面就属于Local-Variable Type Inference，而在jvm方面[307: Parallel Full GC for G1](https://link.segmentfault.com/?enc=5uO9rh8R2erSxNrgBKKrnw%3D%3D.%2Fm7WsFZbtLMxiut7ZpB88%2BAWHPtRbiYTX8Jxr%2F9jIPehkhQzJu%2FnQHv9R%2B6DoyUU)以及[317: Experimental Java-Based JIT Compiler](https://link.segmentfault.com/?enc=WwdJGFaOUwXKwvvV67vbRg%3D%3D.dfRo5VFI%2BTLwmInZvbZ2xjxJAIxwqTPBirlkdAKWMJDRIdNwPAgv4S3z4aiafI1k)都比较重磅，值得深入研究。

## doc

- [JDK 10 Features](https://link.segmentfault.com/?enc=8eNtqsRYLIrpOWwdYpwliw%3D%3D.F5Mq6%2FYl2qSG642zAlwhhEHhClkISOsArI%2Butg7yzDAjBjxbHSfwqd002F0fmI8w)
- [Introducing Java SE 10](https://link.segmentfault.com/?enc=BuyfZn7AwNzICK5txogK%2FQ%3D%3D.jtdX7FAkg54t4YaTAjRQvJjhzHFnAAVOQ1SDn148RNmCUrJexftaOjxsNvmJhvDt%2BrKpF9rMoLSxYg%2B6hf5mJHP1IKMGBZQ7HTWfilM7BPg%3D)(`官方解读`)
- [What's New in JDK 10 - New Features and Enhancements](https://link.segmentfault.com/?enc=6RF7O%2F5cRXXdfzL5OSK%2Bag%3D%3D.435%2BjacsbSzxcj%2BzFVh0Lea%2B8WtiFZAMlNcJslwijgCZW6Cb8Hs5LQb4IpALPvo9XdwiqMGCRm210wflgwlOh0PojCANGmlnFZfP1TkmgJo%3D)(`官方细项解读`)
- [JDK-8140281 : (opt) add no-arg orElseThrow() as preferred alternative to get()](https://link.segmentfault.com/?enc=uthY0LYa%2BoCv4cVLV1ilVg%3D%3D.TnVvAphHwvT4FOG%2FZYXM%2FSBhn8jgrfbPeJdyXnh8O0lLeDWlDbY7GxwSBqlUUDNyxLLi8pNQaaH54ZpxaNZ02w%3D%3D)
- [JDK-8177290 : add copy factory methods for unmodifiable List, Set, Map](https://link.segmentfault.com/?enc=WRPJ6ONZbgsuqEYxUo2Vog%3D%3D.fnILZru3Dh3HHxGgaDr0O08fIWfMYrfLPvLWzJDB6mBWPw0emb%2FVNDVGEqVtvZeUpkDiA3GSRmfMQ4NnKKq5Zw%3D%3D)