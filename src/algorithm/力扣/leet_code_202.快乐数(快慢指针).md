~~~java
/**
 * @author ZhangFZ
 * @date 2020/4/30 10:59
 **/
public class leet_code_202_快乐数 {

    /**
     * 编写一个算法来判断一个数 n 是不是快乐数。
     *
     * 「leet_code_202_快乐数」定义为：对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和，然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。
     * 如果 可以变为  1，那么这个数就是快乐数。
     *
     * 如果 n 是快乐数就返回 True ；不是，则返回 False 。
     *
     * 示例：
     * 输入：19
     * 输出：true
     * 解释：
     * 12 + 92 = 82
     * 82 + 22 = 68
     * 62 + 82 = 100
     * 12 + 02 + 02 = 1
     */

    public static void main(String[] args) {
        System.out.println(isHappy(19));
        System.out.println(isHappy(20));
    }

    private static boolean isHappy(int n) {
        int slow = n;
        int fast = getNext(n);
        while(slow != 1 && fast != slow){
            slow = getNext(slow);
            fast = getNext(getNext(fast));
        }
        return slow == 1;

    }

    private static int getNext(int num){
        int result = 0;
        while(num > 0){
            int flag = num % 10;
            num = num / 10;
            result =  result + flag * flag;
        }
        return result;
    }
}
~~~