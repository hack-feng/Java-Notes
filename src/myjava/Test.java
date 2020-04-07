package myjava;

import java.util.Arrays;

/**
 * 正则表达式
 */
public class Test {

    public static void main(String[] args) {
        int [] a = {1,2,3};

        int [] b = a;
        a[0] = 4;
        System.out.println(Arrays.toString(b));
    }
}
