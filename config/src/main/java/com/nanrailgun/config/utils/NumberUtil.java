package com.nanrailgun.config.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

    public static boolean isPhone(String string) {
        Pattern pattern = Pattern.compile("^1([34578])\\d{9}$");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static int genRandomNumber(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }
    public static String genOrderNo() {
        StringBuilder builder = new StringBuilder(String.valueOf(System.currentTimeMillis()));
        int num = genRandomNumber(4);
        builder.append(num);
        return builder.toString();
    }

}
