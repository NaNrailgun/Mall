package com.nanrailgun.mall.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallShoppingCartItemUpdateParam implements Serializable {
    private Long cartItemId;
    private Integer goodsCount;
}
