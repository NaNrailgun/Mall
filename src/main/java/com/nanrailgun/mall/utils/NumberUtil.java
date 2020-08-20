package com.nanrailgun.mall.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

    public static boolean isPhone(String string){
        Pattern pattern = Pattern.compile("^1([34578])\\d{9}$");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
