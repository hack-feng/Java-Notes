package algorithm.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 * GitHub地址：https://github.com/hack-feng/algorithm
 * 动态模拟实现：https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html
 * CSDN博客(理论)：https://blog.csdn.net/qq_34988304/article/details/100269152
 * 联系作者：1150640979@qq.com
 */
public class BubbleSort {

    /**
     *  *
     *  * 冒泡排序的优点：每进行一趟排序，就会少比较一次，因为每进行一趟排序都会找出一个较大值。
     *  * 用时间复杂度来说：
     *  *
     *  * 　　1.如果我们的数据正序，只需要走一趟即可完成排序。
     *  *      所需的比较次数C和记录移动次数M均达到最小值，即：Cmin=n-1;Mmin=0;所以，冒泡排序最好的时间复杂度为O(n)。
     *  *
     *  * 　　2.如果很不幸我们的数据是反序的，则需要进行n-1趟排序。
     *  *      每趟排序要进行n-i次比较(1≤i≤n-1)，且每次比较都必须移动记录三次来达到交换记录位置。
     *  *      在这种情况下，比较和移动次数均达到最大值：冒泡排序的最坏时间复杂度为：O(n2)-->n2是n的平方 。
     *  *
     *  * 综上所述：冒泡排序总的平均时间复杂度为：O(n2) 。
     */

    // 数据交换的次数
    private static int count;
    private static int[] sort(int[] dataArray){

        // 控制循环的次数，最后一次是已排序好的数组，故循环长度为：dataArray.length - 1
        for (int i = dataArray.length - 1; i >= 0 ; i--) {
            // 控制比较次数，比较一次，便可以确认最大的值，故j > i
            for (int j = 0; j < i; j++) {
                // 比较，如果前面的数比后面的大，便交换位置
                if(dataArray[j] > dataArray[j + 1]){
                    count++ ;
                    int temp;
                    temp = dataArray[j];
                    dataArray[j] = dataArray[j + 1];
                    dataArray[j + 1] = temp;
                }
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
