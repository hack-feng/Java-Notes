package algorithm.力扣;

import java.util.Arrays;

/**
 * @author ZhangFZ
 * @date 2020/4/30 14:31
 **/
public class leet_code_62不同路径 {
    /**
     * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
     *
     * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
     *
     * 问总共有多少条不同的路径？
     *
     * 输入: m = 3, n = 2
     * 输出: 3
     * 解释:
     * 从左上角开始，总共有 3 条路径可以到达右下角。
     * 1. 向右 -> 向右 -> 向下
     * 2. 向右 -> 向下 -> 向右
     * 3. 向下 -> 向右 -> 向右
     */

    public static void main(String[] args) {
        System.out.println(uniquePaths(3,2));
    }

    private static int uniquePaths(int m, int n) {
        int [] array = new int[n];
        Arrays.fill(array, 1);
        for(int i = 1; i < m; i++){
            for(int j = 1; j < n; j ++){
                array[j] += array[j-1];
            }
        }
        return array[n-1];
    }
}
