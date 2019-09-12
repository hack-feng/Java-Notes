package thread.create;

public class CreateThread {

    /**
     * 多线程实现方式：
     * 1、继承Thread类
     * 2、实现Runnable
     * 3、通过Callable 和 Future
     *
     * 多线程中start和run的区别
     * start方法是启动一个线程，让线程进入就绪状态，等待CPU调度的执行，无需等在run的执行，便可以执行下一个start
     * run方法是执行这个线程的方法，同普通方法一样，必须等在这个方法执行完，才可以执行下一个。
     */
    public static void main(String[] args){
        ThreadExtends threadExtends = new ThreadExtends("继承多线程1");
        ThreadExtends threadExtends2 = new ThreadExtends("继承多线程2");
        threadExtends.run();
        threadExtends2.run();

        ThreadImplements threadImplements = new ThreadImplements("实现多线程");
        ThreadImplements threadImplements2= new ThreadImplements("实现多线程");
        threadImplements.run();
        threadImplements2.run();

        // 测试start执行后，无需等待，直接执行下面方法
        Thread thread = new Thread(new ThreadImplements("123"));
        thread.start();
        System.out.println("等待中...");
        thread.run();

        // 测试run执行后，需等待，然后执行下面方法
        Thread thread2 = new Thread(new ThreadImplements("456"));
        thread2.run();
        System.out.println("等待中...");
        thread2.start();
    }
}
