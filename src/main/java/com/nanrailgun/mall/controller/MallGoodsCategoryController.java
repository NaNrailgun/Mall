package com.nanrailgun.mall.controller;

import com.nanrailgun.mall.common.MallException;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.controller.vo.MallIndexCategoryVO;
import com.nanrailgun.mall.service.MallGoodsCategoryService;
import com.nanrailgun.mall.utils.Result;
import com.nanrailgun.mall.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MallGoodsCategoryController {

    @Autowired
    MallGoodsCategoryService mallGoodsCategoryService;

    @GetMapping("/categories")
    public Result getCategories() {
        List<MallIndexCategoryVO> list = mallGoodsCategoryService.getAllGoodsCategory();
        if (CollectionUtils.isEmpty(list)) {
            MallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(list);
    }

}
