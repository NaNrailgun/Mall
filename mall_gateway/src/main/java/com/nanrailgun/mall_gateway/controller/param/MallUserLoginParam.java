package com.nanrailgun.mall_gateway.controller.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MallUserLoginParam {
    @NotEmpty(message = "登录名不能为空")
    private String loginName;
    @NotEmpty(message = "密码不能为空")
    private String passwordMd5;
}
