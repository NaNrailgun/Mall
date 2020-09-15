package com.nanrailgun.mall_gateway.controller.param;

import lombok.Data;

@Data
public class MallUserAddressSaveParam {

    private String userName;

    private String userPhone;

    private Byte defaultFlag;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;
}
