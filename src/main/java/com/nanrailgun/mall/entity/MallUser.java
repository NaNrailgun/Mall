package com.nanrailgun.mall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MallUser {
    private Long userId;
    private String nickName;
    private String loginName;
    private String passwordMD5;
    private String introduceSign;
    private Byte isDeleted;
    private Byte lockedFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
