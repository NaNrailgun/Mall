package com.nanrailgun.mall.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MallUserToken {
    private Long userId;
    private String token;
    private Date updateTime;
    private Date expireTime;
}
