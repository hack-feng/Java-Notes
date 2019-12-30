package algorithm.sort;

import java.util.Arrays;

/**
 * 选择排序
 * GitHub地址：https://github.com/hack-feng/algorithm
 * 动态模拟实现：https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html
 * CSDN博客(理论)：https://blog.csdn.net/qq_34988304/article/details/100269152
 * 联系作者：1150640979@qq.com
 */
public class SelectionSort {
    /**
     * 选择排序（Selection sort）是一种简单直观的排序算法。
     * 它的工作原理是每一次从待排序的数据元素中选出最小（或最大）的一个元素，存放在序列的起始位置，直到全部待排序的数据元素排完。
     */

    // 循环的次数
    private static int count;
    private static int[] sort(int[] dataArray){

        // 记录循环中最小值的下标
        int min;
        // 控制循环的次数，最后一次是已排序好的数组，故循环长度为：dataArray.length - 1
        for (int i = 0; i < dataArray.length - 1; i++) {
            // 设置下标为i的值最小
            min = i;
            // 控制比较次数，比较一次，便可以确认最小的值，j的初始值为 i + 1
            for (int j = i + 1; j < dataArray.length; j++) {
                // 比较，如果下标为j的值比下标为i小，则min记录j的下标
                if(dataArray[i] > dataArray[j]){
                    min = j;
                }
            }
            // 交换位置
            if(min != i){
                count ++;
                int temp;
                temp = dataArray[i];
                dataArray[i] = dataArray[min];
                dataArray[min] = temp;
            }
        }
        System.out.println("数据交换的次数" + count);
        return dataArray;
    }

    // 测试
    public static void main(String[] args){
//        int[] dataArray = new int[]{3, 5, 8, 4, 7, 1, 6, 2, 2, 15};
        int[] dataArray = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("排序后的数数组：" + Arrays.toString(sort(dataArray)));
    }
}
