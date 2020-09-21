package com.nanrailgun.order_api.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallOrderAddress implements Serializable {
    private Long orderId;

    private String userName;

    private String userPhone;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String detailAddress;
}
