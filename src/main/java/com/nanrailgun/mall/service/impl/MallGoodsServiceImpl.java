package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.controller.vo.MallSearchGoodsVO;
import com.nanrailgun.mall.dao.MallGoodsMapper;
import com.nanrailgun.mall.entity.MallGoods;
import com.nanrailgun.mall.service.MallGoodsService;
import com.nanrailgun.mall.utils.MyBeanUtil;
import com.nanrailgun.mall.utils.PageQueryUtil;
import com.nanrailgun.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MallGoodsServiceImpl implements MallGoodsService {

    @Resource
    MallGoodsMapper mallGoodsMapper;

    @Override
    public MallGoods getGoodsDetailByGoodsId(Long goodsId) {
        return mallGoodsMapper.selectByPrimaryKey(goodsId);
    }

    @Override
    public PageResult<MallSearchGoodsVO> getGoodsListBySearch(PageQueryUtil pageUtil) {
        List<MallGoods> mallGoodsList = mallGoodsMapper.selectBySearch(pageUtil);
        int total = mallGoodsMapper.getTotalMallGoodsBySearch(pageUtil);
        List<MallSearchGoodsVO> list = MyBeanUtil.copyList(mallGoodsList, MallSearchGoodsVO.class);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                if (item.getGoodsIntro().length() > 30) {
                    item.setGoodsIntro(item.getGoodsIntro().substring(0, 30) + "...");
                }
                if (item.getGoodsName().length() > 28) {
                    item.setGoodsName(item.getGoodsName().substring(0, 28) + "...");
                }
            });
        }
        return new PageResult<>(list, total, pageUtil.getLimit(), pageUtil.getPage());
    }
}
