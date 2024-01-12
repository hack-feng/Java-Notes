package myjava;

import java.text.ParseException;
import java.util.*;

/**
 * 正则表达式
 */
public class Test {

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        System.out.println(calendar.get(Calendar.YEAR));
        if(year == 2023) {
            System.out.println("岁月如歌，坚持与放弃的交织");
        } else if(year == 2024) {
            System.out.println("新的征程，希望与期望的绽放");
        } else {
            System.out.println("似水流年，幸福与美好的迸发");
        }
    }
}
