package com.nanrailgun.order_api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallShoppingCartItemUpdateParamDTO implements Serializable {
    private Long cartItemId;
    private Integer goodsCount;
}
