使用Java随机生成一个6位的字符串，包含数据和大写英文字母

~~~java
import java.util.Random;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static String generateSoleName() {
        StringBuilder soleResult = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            //判断产生的随机数是0还是1，是0进入if语句用于输出数字，是1进入else用于输出字符
            int mark = Math.random() >= 0.5 ? 1 : 0;
            if (0 == mark) {
                soleResult.append(RANDOM.nextInt(10));
            } else {
                soleResult.append( (char)('A' + RANDOM.nextInt(26)));
            }
        }
        return soleResult.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i <20; i++) {
            System.out.println(generateSoleName());
        }
    }
}

~~~

测试效果如下：

![image-20241016180031896](https://image.xiaoxiaofeng.site/blog/2024/10/16/xxf-20241016180038.png?xxfjava)