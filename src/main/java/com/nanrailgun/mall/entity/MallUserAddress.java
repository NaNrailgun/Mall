package com.nanrailgun.mall.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MallUserAddress {
    private Long addressId;

    private Long userId;

    private String userName;

    private String userPhone;

    private Byte defaultFlag;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;
}
