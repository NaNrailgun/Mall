package com.nanrailgun.goods_api.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MallIndexCategoryDTO implements Serializable {

    private Long categoryId;

    private Byte categoryLevel;

    private String categoryName;

    private List<SecondLevelCategoryDTO> secondLevelCategoryVOS;
}
