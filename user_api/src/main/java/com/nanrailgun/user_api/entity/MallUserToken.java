package com.nanrailgun.user_api.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MallUserToken implements Serializable {
    private Long userId;
    private String token;
    private Date updateTime;
    private Date expireTime;
}
