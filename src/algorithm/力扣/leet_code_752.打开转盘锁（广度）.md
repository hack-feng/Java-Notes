~~~java
package com.honsupply.api.core.util.logger;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        String[] deadends = {"0201", "0101", "0102", "1212", "2002"};
        String target = "0202";
        System.out.println(openLock(deadends, target));
    }

    /** 解决转盘锁的问题
     * 本题为自己的思路，效率不是很高，如需更有答案，请移步力扣，搜索"打开转盘锁"。
     * 解决问题的思想：每次可以转动一个，转动一次步长为1，第一次转动可以为"1000","1010","9000","0900"...
     * 将一次可转动的放到队列中，然后插入一个null值，然后基于前一次的基础上转动第二下
     * 遇到第一个插入的null时代表第一轮循环完毕，则步数+1，然后插入一个新的null值，代表在此之前的均为前一次步数可达的范围。
     */
    private static int openLock(String[] deadends, String target){
        if(target.equals("0000")) return 0;

        Set<String> set = new HashSet<>();
        Collections.addAll(set, deadends);

        // 定义出现过的方案，以防重复，进入死循环
        Set<String> answerSet = new HashSet<>();
        answerSet.add("0000");
        Queue<String> queue = new LinkedList<>();
        queue.offer("0000");
        // 插入null值，用于计算步长
        queue.offer(null);

        int step = 0;
        // 如果队列不为空，则一直循环下去，如果为空，且未找到答案，则返回-1
        while(!queue.isEmpty()){
            // 取队列的第一个值
            String answer = queue.remove();
            // 遇见null，则步长+1
            if(answer == null){
                step ++;
                // 如果null后面还有数据，则插入一个新的null，标志一轮结束
                if(queue.peek() != null){
                    queue.offer(null);
                }
                
            // 匹配到正确的答案，返回步长
            }else if(answer.equals(target)){
                return step;
                
            // 当遇到deadends内的值时，则无需继续向下找，故跳出for循环
            }else if(!set.contains(answer)){
                // 每次可以转动第1.2.3.4的数值
                for (int i = 0; i < 4; i ++){
                    // 取当前位置的数字+1 or -1。
                    // 如果为9，+ 1 则变成0
                    // 如果为0，- 1 则变成9
                    int number = Integer.parseInt(answer.substring(i, i+1));
                    int nextNum = number == 9 ? 0 : number+1;
                    String next = answer.substring(0, i) + String.valueOf(nextNum) + answer.substring(i + 1, 4);
                    if(!answerSet.contains(next)){
                        queue.offer(next);
                        answerSet.add(next);
                    }

                    int prevNum = number == 0 ? 9 : number-1;
                    String prev = answer.substring(0, i) + String.valueOf(prevNum) + answer.substring(i + 1, 4);
                    if(!answerSet.contains(prev)){
                        queue.offer(prev);
                        answerSet.add(prev);
                    }
                }
            }
        }
        return -1;
    }
}
~~~