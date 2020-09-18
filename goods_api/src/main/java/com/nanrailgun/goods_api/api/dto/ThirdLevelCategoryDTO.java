package com.nanrailgun.goods_api.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdLevelCategoryDTO implements Serializable {
    private Long categoryId;

    private Byte categoryLevel;

    private String categoryName;
}
