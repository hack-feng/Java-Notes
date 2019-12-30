package java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 */
public class RegularExpression {

    public static void main(String[] args) {
        String str = "hello world 111";

        Pattern p = Pattern.compile("\\b\\w+\\b");

        Matcher m = p.matcher(str);
        while (m.find()){
            //hello-0-5
            //world-6-11
            System.out.println(m.group() + "-" + m.start() + "-" + m.end());
        }


        String i = "123";
        Pattern p1 = Pattern.compile("^\\d+$");
        Matcher m1 = p1.matcher(i);
        while(m1.find()){
            System.out.println(m1.group());
        }
    }
}
