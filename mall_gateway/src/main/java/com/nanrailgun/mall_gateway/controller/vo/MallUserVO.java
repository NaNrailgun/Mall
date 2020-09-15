package com.nanrailgun.mall_gateway.controller.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class MallUserVO implements Serializable {
    private String loginName;
    private String nickName;
    private String introduceSign;
}
