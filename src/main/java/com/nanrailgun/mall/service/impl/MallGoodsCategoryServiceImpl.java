package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.controller.vo.MallIndexCategoryVO;
import com.nanrailgun.mall.controller.vo.SecondLevelCategoryVO;
import com.nanrailgun.mall.controller.vo.ThirdLevelCategoryVO;
import com.nanrailgun.mall.dao.MallGoodsCategoryMapper;
import com.nanrailgun.mall.entity.MallGoodsCategory;
import com.nanrailgun.mall.service.MallGoodsCategoryService;
import com.nanrailgun.mall.utils.MyBeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MallGoodsCategoryServiceImpl implements MallGoodsCategoryService {

    @Resource
    MallGoodsCategoryMapper mallGoodsCategoryMapper;

    @Override
    public List<MallIndexCategoryVO> getAllGoodsCategory() {
        List<MallIndexCategoryVO> mallIndexCategoryVOList = new ArrayList<>();
        List<MallGoodsCategory> firstCategory = mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), 1, 0);
        if (!CollectionUtils.isEmpty(firstCategory)) {
            List<Long> firstId = firstCategory.stream().map(MallGoodsCategory::getCategoryId).collect(Collectors.toList());
            List<MallGoodsCategory> secondCategory = mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(firstId, 2, 0);
            if (!CollectionUtils.isEmpty(secondCategory)) {
                List<Long> secondId = secondCategory.stream().map(MallGoodsCategory::getCategoryId).collect(Collectors.toList());
                List<MallGoodsCategory> thirdCategory = mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(secondId, 3, 0);
                if (!CollectionUtils.isEmpty(thirdCategory)) {
                    //第三级标签按父标签分组
                    Map<Long, List<MallGoodsCategory>> thirdMap = thirdCategory.stream().collect(groupingBy(MallGoodsCategory::getParentId));
                    //处理二级分类标签
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                    for (MallGoodsCategory category : secondCategory) {
                        SecondLevelCategoryVO temp = new SecondLevelCategoryVO();
                        BeanUtils.copyProperties(category, temp);
                        if (thirdMap.containsKey(category.getCategoryId())) {
                            temp.setThirdLevelCategoryVOS(MyBeanUtil.copyList(thirdMap.get(category.getCategoryId()), ThirdLevelCategoryVO.class));
                            secondLevelCategoryVOS.add(temp);
                        }
                    }
                    //处理一级分类标签
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                        Map<Long, List<SecondLevelCategoryVO>> secondMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for (MallGoodsCategory category : firstCategory) {
                            MallIndexCategoryVO temp = new MallIndexCategoryVO();
                            BeanUtils.copyProperties(category, temp);
                            if (secondMap.containsKey(category.getCategoryId())) {
                                temp.setSecondLevelCategoryVOS(secondMap.get(category.getCategoryId()));
                                mallIndexCategoryVOList.add(temp);
                            }
                        }
                    }
                }
            }
            return mallIndexCategoryVOList;
        }
        return null;
    }
}
