package search;

public class BinarySearch {

    /**
     * 非递归查找
     * @param array 查询数组
     * @param data 待查询的数据
     * @return data存在，返回其下标，不存在则返回-1
     */
    public static int search(int[] array, int data){
        int mid, start = 0, end = array.length - 1;
        while (end > start){
            mid = (end - start)/2 + start;
            if(array[mid] == data){
                return mid;
            }

            if(array[mid] > data){
                // 当前值 > data,查找当前值之前的值，因为比较的是下标为mid的值，故mid无需参与下次比较，故end = mid - 1
                end = mid - 1;
            }else{
                // 当前值 < data,查找当前值之后的值，因为比较的是下标为mid的值，故mid无需参与下次比较，故start = mid + 1
                start = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 递归查找
     * @param array 查询数组
     * @param data 待查询的数据
     * @param start 开始位置
     * @param end 结束位置
     * @return data存在，返回其下标，不存在则返回-1
     */
    public static int search(int[] array, int data, int start, int end){

        // 取中间值的下标
        int mid = (end - start)/2 + start;

        // 如果当前值 == data,则返回下标
        if(array[mid] == data){
            return mid;
        }

        // 如果start >= end 则表示数据不存在
        if(start >= end){
            return -1;
        }else if(array[mid] > data){
            // 当前值 > data,查找当前值之前的值
            return search(array, data, start, mid - 1);
        }else{
            // 当前值 < data,查找当前值之后的值
            return search(array, data, mid + 1, end);
        }
    }

    public static void main(String[] args){
        int[] dataArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int searchData = 3;
        System.out.println("非递归方法查询的下标为：" + search(dataArray, searchData));
        System.out.println("递归方法查询的下标为：" + search(dataArray,searchData, 0, dataArray.length - 1));
    }
}