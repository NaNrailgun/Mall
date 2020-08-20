package com.nanrailgun.mall.common;

public class MallException extends RuntimeException {

    public MallException() {
    }

    public MallException(String message) {
        super(message);
    }

    public static void fail(String message) {
        throw new MallException(message);
    }
}
