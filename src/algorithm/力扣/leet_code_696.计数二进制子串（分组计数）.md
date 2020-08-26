~~~java
public class Test {
    public static void main(String[] args) {
        String a = "00110";
        System.out.println(getNum(a));
    }

    /**
     * 计数二进制字串-696
     * 解题思路：
     * 
     */
    private static int getNum(String a){

        // 记录当前字串，用于判断是否与下一个字符一致
        char flag = ' ';
        // result：记录返回的结果
        // next：用于记录当前循环的位置
        // step:记录步长，用于判断是否需要计算
        // length: a字符串的长度
        // nowCount：当前字符连续出现的数量
        // prevCount：上一字符连续出现的数量
        int result = 0, next = 0, step = 1, length = a.length(), nowCount = 0, prevCount = 0;
        while(next <= length){

            // 用于最后一次计算，例如为1100时，最后一个0循环结束，步长到达2时，但不会执行内部的运算。
            if(next == length){
                result += Math.min(prevCount, nowCount);
            }else{
                // 字符一致时，当前字符出现数量累加
                if(flag == a.charAt(next)){
                    nowCount ++;
                }else{
                    // 如果步长到达2，则需要计算符合答案的数量，并把步长设置为1
                    if(step == 2){
                        result += Math.min(prevCount, nowCount);
                        step = 1;
                    }
                    // 出现不一致字符，替换flag字符为新的字符，prevCount为上一个字符的nowCount，步长从1开始，nowCount从1开始
                    flag = a.charAt(next);
                    prevCount = nowCount;
                    nowCount = 1;
                    step ++;
                }
            }
            next ++;
        }
        return result;
    }
}
~~~