package com.nanrailgun.config.common;

import org.apache.dubbo.rpc.RpcException;

import java.io.Serializable;

public class MallException extends RpcException implements Serializable {

    public MallException() {
    }

    public MallException(String message) {
        super(message);
    }

    public static void fail(String message) {
        throw new MallException(message);
    }
}
