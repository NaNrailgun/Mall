package com.nanrailgun.user_api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallUserAddressDTO implements Serializable {
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
