package sort;

import java.util.Arrays;

/**
 * 冒泡排序
 * GitHub地址：https://github.com/hack-feng/algorithm
 * CSDN博客地址：
 * 联系作者：1150640979@qq.com
 */
public class BubbleSort {

    // 循环的次数
    private static int count;
    private static int[] sort(int[] dataArray){

        // 控制循环的次数，最后一次是已排序好的数组，故循环长度为：dataArray.length - 1
        // 比较一次，便可以确认最大的值
        for (int i = 0; i < dataArray.length - 1; i++) {
            // 控制比较次数，每次比较，如果前面的数比后面的大，便交换位置
            for (int j = 0; j < dataArray.length - 1 - i; j++) {
                count++ ;
                // 交换位置
                if(dataArray[j] > dataArray[j + 1]){
                    int temp;
                    temp = dataArray[j];
                    dataArray[j] = dataArray[j + 1];
                    dataArray[j + 1] = temp;
                }
            }
        }
        return dataArray;
    }

    // 测试
    public static void main(String[] args){
        int[] dataArray = new int[]{3, 5, 8, 4, 7, 1, 6, 2, 2, 15};
        System.out.println("for调用循环的次数" + count);
        System.out.println("排序后的数数组：" + Arrays.toString(sort(dataArray)));
    }
}
