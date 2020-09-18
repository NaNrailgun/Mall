package com.nanrailgun.mall_gateway.controller;

import com.nanrailgun.config.common.MallException;
import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.utils.Result;
import com.nanrailgun.config.utils.ResultGenerator;
import com.nanrailgun.goods_api.api.MallGoodsCategoryService;
import com.nanrailgun.goods_api.api.dto.MallIndexCategoryDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MallGoodsCategoryController {

    @Reference
    private MallGoodsCategoryService mallGoodsCategoryService;

    @GetMapping("/categories")
    public Result getCategories() {
        List<MallIndexCategoryDTO> list = mallGoodsCategoryService.getAllGoodsCategory();
        if (CollectionUtils.isEmpty(list)) {
            MallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(list);
    }

}
