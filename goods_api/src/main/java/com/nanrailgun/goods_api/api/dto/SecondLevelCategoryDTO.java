package com.nanrailgun.goods_api.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SecondLevelCategoryDTO implements Serializable {

    private Long categoryId;

    private Long parentId;

    private Byte categoryLevel;

    private String categoryName;

    private List<ThirdLevelCategoryDTO> thirdLevelCategoryVOS;
}
