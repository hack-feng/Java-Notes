package java.thread.create;

/**
 * 通过实现Runnable接口实现多线程
 */
public class ThreadImplements implements Runnable{

    private String param;

    ThreadImplements(String param){
        this.param = param;
    }

    @Override
    public void run() {

        System.out.println(param);
        try {
            // 延时3s,做start和run测试
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
