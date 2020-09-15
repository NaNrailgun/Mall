package com.nanrailgun.config.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class TokenUtil {

    public static String genToken(Long userId) {
        String src= System.currentTimeMillis() + userId.toString() + NumberUtil.genRandomNumber(4);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result = new BigInteger(1, md.digest()).toString(16);
            if (result.length() == 31) {
                result = result + "-";
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}
