package myjava;

import java.util.*;

/**
 * 正则表达式
 */
public class Test {

    public static void main(String[] args) {
        int [][] test = new int [][]{{2, 3}, {1, 6}, {8, 10}, {15, 18}};
        Arrays.sort(test, (o1, o2) -> {
            if(o1[0] == o2[0]){
                return o1[1] - o2[1];
            }else{
                return o1[0] - o2[0];
            }
        });

        List<int[]> list = new ArrayList<>();
        for (int[] array : test){
            if(list.isEmpty()){
                list.add(array);
            }else{
                int [] flag = list.get(list.size() - 1);

                if(array[0] > flag[1]){
                    list.add(array);
                }else{
                    if(flag[1] < array[1]){
                        list.remove(flag);
                        list.add(new int[]{flag[0], array[1]});
                    }
                }
            }
        }

        int result [][] = list.toArray(new int[list.size()][2]);
        System.out.println(Arrays.deepToString(result));
    }
}
