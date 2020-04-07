package myjava;

/**
 * 正则表达式
 */
public class JavaSort {

    public static void main(String[] args) {
        int [] nums ={-4,0,7,4,9,-5,-1,0,-7,-1};

        // 快速排序， 思想：有点类似二分查找
        // 确认一个基数，两个指针， low 指向最前， high指向最后
        // 先比较high，如果high位置上的指针大于或等于基数， high -- ，继续比较直到high = low或者high位置上的数据大于基数
        // 然后比较 low ，同理
        // 一轮循环过后，基础前面的均比基数小，基数后面的均比基数大
        // 递归分别处理基数前面的和基数后面的，基数不用再参与比较
        quickSort(nums, 0, nums.length -1);
        for (int i : nums) {
            System.out.println(i);
        }
    }

    private static void quickSort(int[] nums, int low, int high){
        if(low < high){
            // 获取基数的位置
            int index = getIndex(nums, low, high);
            // 处理基数前面的排序
            quickSort(nums, low, index -1);
            // 处理基数后面的排序
            quickSort(nums, index + 1, high);
        }
    }

    private static int getIndex(int[] nums, int low, int high){
        // 取得基数的值，保存临时变量
        int flag = nums[low];
        while(low < high){
            // 当队尾的元素大于等于基准数据时,向前挪动high指针
            while(flag <= nums[high] && low < high){
                high --;
            }
            // 如果队尾元素小于tmp了,需要将其赋值给low
            nums[low] = nums[high];
            // 当队首元素小于等于tmp时,向前挪动low指针
            while(flag >= nums[low] && low < high){
                low ++;
            }
            // 当队首元素大于tmp时,需要将其赋值给high
            nums[high] = nums[low];
        }
        // 基础位置调整，重新赋值基数
        nums[low] = flag;
        return low;
    }
}
