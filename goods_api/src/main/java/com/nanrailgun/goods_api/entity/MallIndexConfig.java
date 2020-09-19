package com.nanrailgun.goods_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MallIndexConfig {
    private Long configId;

    private String configName;

    private Byte configType;

    private Long goodsId;

    private String redirectUrl;

    private Integer configRank;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer updateUser;
}
