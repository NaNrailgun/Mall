package com.nanrailgun.mall.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallUserUpdateParam implements Serializable {
    private String nickName;
    private String introduceSign;
    private String passwordMd5;
}
