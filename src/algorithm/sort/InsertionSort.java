package algorithm.sort;

import java.util.Arrays;

/**
 * 插入排序
 * GitHub地址：https://github.com/hack-feng/algorithm
 * 动态模拟实现：https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html
 * CSDN博客(理论)：https://blog.csdn.net/qq_34988304/article/details/100269152
 * 联系作者：1150640979@qq.com
 */
public class InsertionSort {

    /**
     * 插入排序原理很简单，讲一组数据分成两组，我分别将其称为有序组与待插入组。
     * 每次从待插入组中取出一个元素，与有序组的元素进行比较，并找到合适的位置，将该元素插到有序组当中。
     * 就这样，每次插入一个元素，有序组增加，待插入组减少。
     * 直到待插入组元素个数为0。当然，插入过程中涉及到了元素的移动
     */

    // 插入排序
    private static int[] sort(int[] array){
        // 定义temp：交换值  j：交换的下标
        int temp, j;
        for(int i = 1; i < array.length; i ++){

            // 将下标为i的值赋给temp
            temp = array[i];
            // i表示待插入的值， j表示有序组的最后一个元素
            j = i - 1;

            // 将temp的值与下标为j的值比较，如果小于，则将j位置的数据依次后移，继续与前一个比较，依次类推
            while( j >= 0 && temp < array[j]){
                array[j + 1] = array[j];
                j--;
            }
            // 如果大于，则插入
            array[j + 1] = temp;
        }
        return array;
    }

    // 测试
    public static void main(String[] args){
        int[] dataArray = new int[]{3, 5, 8, 4, 7, 1, 6, 2, 15, 2};
        System.out.println("排序后的数数组：" + Arrays.toString(sort(dataArray)));
    }
}
