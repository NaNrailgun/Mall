package com.nanrailgun.mall.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallOrderSaveParam implements Serializable {
    private Long[] cartItemIds;

    private Long addressId;
}
