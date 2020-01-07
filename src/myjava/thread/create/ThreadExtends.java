package myjava.thread.create;

/**
 * 通过继承Thread方式实现多线程
 */
public class ThreadExtends extends Thread{
    private String param;

    ThreadExtends(String param){
        this.param = param;
    }

    public void run(){
        System.out.println(param);
    }
}
