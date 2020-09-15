package com.nanrailgun.user_api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallUserUpdateDTO implements Serializable {
    private String nickName;
    private String introduceSign;
    private String passwordMd5;
}
