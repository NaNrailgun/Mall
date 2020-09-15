package com.nanrailgun.mall_gateway.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SecondLevelCategoryVO implements Serializable {

    private Long categoryId;

    private Long parentId;

    private Byte categoryLevel;

    private String categoryName;

    private List<ThirdLevelCategoryVO> thirdLevelCategoryVOS;
}
