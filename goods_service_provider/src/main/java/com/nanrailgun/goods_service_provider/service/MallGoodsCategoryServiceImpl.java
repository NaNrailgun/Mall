package com.nanrailgun.goods_service_provider.service;

import com.nanrailgun.config.utils.MyBeanUtil;
import com.nanrailgun.goods_api.api.MallGoodsCategoryService;
import com.nanrailgun.goods_api.api.dto.MallIndexCategoryDTO;
import com.nanrailgun.goods_api.api.dto.SecondLevelCategoryDTO;
import com.nanrailgun.goods_api.api.dto.ThirdLevelCategoryDTO;
import com.nanrailgun.goods_api.entity.MallGoodsCategory;
import com.nanrailgun.goods_service_provider.dao.MallGoodsCategoryMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
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
    public List<MallIndexCategoryDTO> getAllGoodsCategory() {
        List<MallIndexCategoryDTO> mallIndexCategoryVOList = new ArrayList<>();
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
                    List<SecondLevelCategoryDTO> secondLevelCategoryVOS = new ArrayList<>();
                    for (MallGoodsCategory category : secondCategory) {
                        SecondLevelCategoryDTO temp = new SecondLevelCategoryDTO();
                        BeanUtils.copyProperties(category, temp);
                        if (thirdMap.containsKey(category.getCategoryId())) {
                            temp.setThirdLevelCategoryVOS(MyBeanUtil.copyList(thirdMap.get(category.getCategoryId()), ThirdLevelCategoryDTO.class));
                            secondLevelCategoryVOS.add(temp);
                        }
                    }
                    //处理一级分类标签
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                        Map<Long, List<SecondLevelCategoryDTO>> secondMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryDTO::getParentId));
                        for (MallGoodsCategory category : firstCategory) {
                            MallIndexCategoryDTO temp = new MallIndexCategoryDTO();
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
