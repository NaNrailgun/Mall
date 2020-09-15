package com.nanrailgun.mall_gateway.controller.vo;

import lombok.Data;

@Data
public class MallUserAddressVO {
    private Long addressId;

    private Long userId;

    private String userName;

    private String userPhone;

    private Byte defaultFlag;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;
}
