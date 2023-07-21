## 序

本文梳理了下java6的新特性，相对于java5而言，java6的特性显得少些，分量也不那么重，相当于java5是windows xp，java6有点像vista。

## 特性列表

- JSR223脚本引擎
- JSR199--Java Compiler API
- JSR269--Pluggable Annotation Processing API
- 支持JDBC4.0规范
- JAX-WS 2.0规范

### 1、JSR223脚本引擎

Scripting for the Java Platform

- 基本使用

  ```java
  public class BasicScripting {
    public void greet() throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        //支持通过名称、文件扩展名、MIMEtype查找
        ScriptEngine engine = manager.getEngineByName("JavaScript");
  //        engine = manager.getEngineByExtension("js");
  //        engine = manager.getEngineByMimeType("text/javascript");
        if (engine == null) {
            throw new RuntimeException("找不到JavaScript语言执行引擎。");
        }
        engine.eval("println('Hello!');");
    }
    public static void main(String[] args) {
        try {
            new BasicScripting().greet();
        } catch (ScriptException ex) {
            Logger.getLogger(BasicScripting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }
  ```

- 绑定上下文

  ```java
  public class ScriptContextBindings extends JsScriptRunner {
    public void scriptContextBindings() throws ScriptException {
        ScriptEngine engine = getJavaScriptEngine();
        ScriptContext context = engine.getContext();
        Bindings bindings1 = engine.createBindings();
        bindings1.put("name", "Alex");
        context.setBindings(bindings1, ScriptContext.GLOBAL_SCOPE);
        Bindings bindings2 = engine.createBindings();
        bindings2.put("name", "Bob");
        context.setBindings(bindings2, ScriptContext.ENGINE_SCOPE);
        engine.eval("println(name);");
    }
    public void useScriptContextValues() throws ScriptException {
        ScriptEngine engine = getJavaScriptEngine();
        ScriptContext context = engine.getContext();
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("name", "Alex");
        engine.eval("println(name);");
    }
    public void attributeInBindings() throws ScriptException {
        ScriptEngine engine = getJavaScriptEngine();
        ScriptContext context = engine.getContext();
        context.setAttribute("name", "Alex", ScriptContext.GLOBAL_SCOPE);
        engine.eval("println(name);");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ScriptException {
        ScriptContextBindings scb = new ScriptContextBindings();
        scb.scriptContextBindings();
        scb.useScriptContextValues();
        scb.attributeInBindings();
    }
  }
  ```

  ### 2、JSR199--Java Compiler API

  ```java
  public class JavaCompilerAPICompiler {
    public void compile(Path src, Path output) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(src.toFile());
            Iterable<String> options = Arrays.asList("-d", output.toString());
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);
            boolean result = task.call();
        }
    }
  }
  ```

  ### 3、JSR269--Pluggable Annotation Processing API

  一部分是进行注解处理的javax.annotation.processing，另一部分是对程序的静态结构进行建模的javax.lang.model

  ### 4、其他

- 支持JDBC4.0规范

- JAX-WS 2.0规范(`包括JAXB 2.0`)

- 轻量级HttpServer

## 参考

- [Java 6 Features and Enhancements](https://link.segmentfault.com/?enc=pRsnxGuXAGbfKpZsW0drxg%3D%3D.oAs%2BQ3A6h5aNCjckAoKqWKwXJxsxvLeJaXDyBfoAYKiRJuFzBUzcajUJKsFTcnMwLyc%2Fe9w2igD0wmbt%2BnuR0E5OzE3eV6xtzV6hgp%2FCiz4%3D)
- [Java Platform, Standard Edition Differences between 5.0 fcs and 6 Beta](https://link.segmentfault.com/?enc=s9KkhALtIEA9CoHTSX1TkQ%3D%3D.n1YKVHEpaf2oCiuXkQOlBzyjfBL5mJqQ7d5ZhLq4XvBStd0C%2FDGCFUVkM5ch9l7P783%2F%2FeMniVuT%2Bc5Rt3V26b5cknYsr2zH2mH9nvxpPqo9E6PLUlfIxQm11AMdUsgd)