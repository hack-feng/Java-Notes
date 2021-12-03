package myjava;

/**
 * 通用的正则校验
 *
 * @author zhangfuzeng
 * @date 2021/11/19
 */
public class RegularUtil {

    private RegularUtil() {
    }

    /**
     * 身份证校验，支持15位和18位身份证校验
     *
     * @param idNumber 身份证号
     * @return true：校验通过  false：校验未通过
     */
    public static boolean checkIdNumber(String idNumber) {
        if (idNumber == null || "".equals(idNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        // 假设18位身份证号码:41000119910101123X  410001 19910101 123X
        // ^开头
        // [1-9] 第一位1-9中的一个         4
        // \\d{5} 五位数字                10001（前六位省市县地区）
        // (18|19|20)                    19（现阶段可能取值范围18xx-20xx年）
        // \\d{2}                        91（年份）
        // ((0[1-9])|(10|11|12))         01（月份）
        // (([0-2][1-9])|10|20|30|31)    01（日期）
        // \\d{3} 三位数字                123（第十七位奇数代表男，偶数代表女）
        // [0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        // $结尾
        // 
        // |代表或者，走18位或15位的规则
        // 
        // 假设15位身份证号码:410001910101123  410001 910101 123
        // ^开头
        // [1-9] 第一位1-9中的一个       4
        // \\d{5} 五位数字              10001（前六位省市县地区）
        // \\d{2}                      91（年份）
        // ((0[1-9])|(10|11|12))       01（月份）
        // (([0-2][1-9])|10|20|30|31)  01（日期）
        // \\d{3} 三位数字              123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        // $结尾
        String regularExpression;
        regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

        boolean matches = idNumber.matches(regularExpression);
        // 判断第18位校验值
        int idNumberLength = 18;
        if (matches && idNumber.length() == idNumberLength) {
            try {
                char[] charArray = idNumber.toCharArray();
                // 前十七位加权因子
                int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                // 这是除以11后，可能产生的11位余数对应的验证码
                String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                int sum = 0;
                for (int i = 0; i < idCardWi.length; i++) {
                    int current = Integer.parseInt(String.valueOf(charArray[i]));
                    int count = current * idCardWi[i];
                    sum += count;
                }
                char idCardLast = charArray[17];
                int idCardMod = sum % 11;
                return idCardY[idCardMod].equalsIgnoreCase(String.valueOf(idCardLast));

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return matches;
    }

    /**
     * 手机号校验，支持11位手机号校验
     *
     * @param phoneNumber 手机号
     * @return true：校验通过  false：校验未通过
     */
    public static boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || "".equals(phoneNumber)) {
            return false;
        }
        // ^        开头
        // 1        以1开头
        // [3-9]    第二位可以为3/4/5/6/7/8/9的任意一个
        // \\d{9}   加上后面的9位数字，总共为11位数字
        // $        结尾
        String regularExpression = "^1[3-9]\\d{9}$";
        try {
            return phoneNumber.matches(regularExpression);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 验证邮箱
     *
     * @param email 邮箱
     * @return true：校验通过  false：校验未通过
     */
    public static boolean checkEmail(String email) {
        if (email == null || "".equals(email)) {
            return false;
        }
        String regularExpression;
        regularExpression = "^([a-z0-9A-Z]+[-|_.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        try {
            return email.matches(regularExpression);
        } catch (Exception e) {
            return false;
        }
    }
}
