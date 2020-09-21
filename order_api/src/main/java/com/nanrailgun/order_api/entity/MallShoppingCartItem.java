package com.nanrailgun.order_api.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MallShoppingCartItem implements Serializable {

    private Long cartItemId;

    private Long userId;

    private Long goodsId;

    private Integer goodsCount;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;

}
