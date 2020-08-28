### 题目信息 
>你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 。每个拨轮可以自由旋转：例如把 '9' 变为  '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。

>锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。

>列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。

>字符串 target 代表可以解锁的数字，你需要给出最小的旋转次数，如果无论如何不能解锁，返回 -1。

 

示例 1:
~~~
输入：deadends = ["0201","0101","0102","1212","2002"], target = "0202"
输出：6
解释：
可能的移动序列为 "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202"。
注意 "0000" -> "0001" -> "0002" -> "0102" -> "0202" 这样的序列是不能解锁的，
因为当拨动到 "0102" 时这个锁就会被锁定。
~~~

示例 2:
~~~
输入: deadends = ["8888"], target = "0009"
输出：1
解释：
把最后一位反向旋转一次即可 "0000" -> "0009"。
~~~

示例 3:
~~~
输入: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
输出：-1
解释：
无法旋转到目标数字且不被锁定。
~~~

示例 4:
~~~
输入: deadends = ["0000"], target = "8888"
输出：-1
~~~

提示：

> 死亡列表 deadends 的长度范围为 [1, 500]。
目标数字 target 不会在 deadends 之中。
每个 deadends 和 target 中的字符串的数字会在 10,000 个可能的情况 '0000' 到 '9999' 中产生。

### 代码实现
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