package com.nanrailgun.mall_gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MallIndexCategoryVO implements Serializable {

    private Long categoryId;

    private Byte categoryLevel;

    private String categoryName;

    private List<SecondLevelCategoryVO> secondLevelCategoryVOS;
}
