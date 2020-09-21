package com.nanrailgun.order_api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MallShoppingCartItemSaveParamDTO implements Serializable {

    private Integer goodsCount;

    private Long goodsId;
}
