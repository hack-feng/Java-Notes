package myjava.lambda;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JAVA8 Lambda表达式基础使用
 * GitHub地址：https://github.com/hack-feng/algorithm
 * CSDN地址：https://blog.csdn.net/qq_34988304/article/details/100920460
 * 联系作者：1150640979@qq.com
 */
public class Lambda {
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static void main(String[] args) throws InterruptedException {

        System.out.println("-----------------------1、Lambda的多线程写法--------------------------");
        // 普通
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("非Lambda的多线程写法");
            }
        }).start();

        // Lambda
        new Thread(() -> System.out.println("Lambda的多线程写法")).start();

        Thread.sleep(100);

        System.out.println("-----------------------2、Lambda的for循环写法--------------------------");
        List<String> forTestList = new ArrayList<>();
        forTestList.add("a");
        forTestList.add("b");
        forTestList.add("c");

        for (int i = 0; i < forTestList.size(); i++) {
            System.out.println("普通for循环" + forTestList.get(i));
        }

        for (String flag : forTestList) {
            System.out.println("forEach循环" + flag);
        }

        forTestList.forEach(list -> System.out.println("Lambda的写法" + list));

        System.out.println("-----------------------3、Lambda集合常用的操作--------------------------");
        System.out.println("初始化数据：");
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User(i, "姓名"+ i,  new Random().nextInt(5));
            System.out.println(user.toString());
            userList.add(user);
        }

        System.out.println("根据age分组--------------------------------------------------------");
        Map<Integer ,List<User>> groupMap = userList.stream().collect(Collectors.groupingBy(User::getAge));
        for(Map.Entry map : groupMap.entrySet()){
            System.out.println(map.getKey() + "    " + map.getValue());
        }

        System.out.println("根据age过滤--------------------------------------------------------");
        List<User> filerList = userList.stream().filter(list -> list.getAge() == 3).collect(Collectors.toList());
        filerList.forEach(list -> System.out.println("age为3的数据有：" + list.toString()));

        System.out.println("根据age, id进行倒序排序----------------------------------------------------");
        userList.sort(Comparator.comparing(User::getAge).thenComparing(User::getId).reversed());
        userList.forEach(list -> System.out.println(list.toString()));

        System.out.println("删除list的某个值----------------------------------------------------");
        String a = "a";
        forTestList.removeIf(s -> s.equals(a));

        System.out.println("list转为map---------------------------------------------------------------");
        Map<Integer, User> map = userList.stream().collect(Collectors.toMap(User :: getId, list -> list));
        map.forEach((k, v) -> System.out.println("id:" + k + "            value:" + v.toString()));

        System.out.println("打印map中value的值age为3的id-----------------------------------------------");
        map.forEach((k, v) -> {
            if(v.getAge() == 3){
                System.out.println("age的值为3的id是：" + k);
            }
        });

        Map<Integer, List<User>> fileMap = userList.stream().collect(Collectors.groupingBy(User::getAge));

        // List转String，逗号分隔
        List<String> strList = Arrays.asList("AA", "BB", "CC");
        String newStr = strList.stream().collect(Collectors.joining(","));
        String newStrB = String.join(",", strList);
        System.out.println("Output:" + newStr + newStrB); 
        
        List<Integer> ids = userList.stream().map(User::getId).collect(Collectors.toList());

        System.out.println("取数据的最大值和最小值-------------------------------------------------------");
        Integer maxId = userList.stream().map(User::getId).max(Integer::compareTo).get();
        System.out.println("最大id值为：" + maxId);
        Integer minId = userList.stream().map(User::getId).min(Integer::compareTo).get();
        System.out.println("最小id值为：" + minId);

        List<Integer> newIds = userList.stream().map(User::getId).collect(Collectors.toList());

        // 整形
        Integer intSum = userList.stream().mapToInt(User::getAge).sum();
        // double类型
        Double doubleSum = userList.stream().mapToDouble(User::getAge).sum();
        // Long类型
        Long longSum  = userList.stream().mapToLong(User::getAge).sum();
        // BigDecimal类型
        BigDecimal bigDecimalSum = userList.stream().map(User::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 定义User类，用作测试使用
    static class User{
        private Integer id;
        private String name;
        private Integer age;
        private BigDecimal money;
        

        User(Integer id, String name, Integer age){
            this.id = id;
            this.name = name;
            this.age = age;
        }
        Integer getId() {
            return id;
        }

        Integer getAge() {
            return age;
        }
        
        BigDecimal getMoney() {
            return money;
        }

        @Override
        public String toString(){
            return "id="+this.id + ",name="+this.name+",age="+this.age;
        }
    }
}
