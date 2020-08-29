package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.controller.vo.MallIndexConfigGoodVO;
import com.nanrailgun.mall.dao.MallGoodsMapper;
import com.nanrailgun.mall.dao.MallIndexConfigMapper;
import com.nanrailgun.mall.entity.MallGoods;
import com.nanrailgun.mall.entity.MallIndexConfig;
import com.nanrailgun.mall.service.MallIndexConfigService;
import com.nanrailgun.mall.utils.MyBeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallIndexConfigServiceImpl implements MallIndexConfigService {

    @Resource
    MallIndexConfigMapper mallIndexConfigMapper;

    @Resource
    MallGoodsMapper mallGoodsMapper;

    @Override
    public List<MallIndexConfigGoodVO> getIndexConfig(int configType, int number) {
        List<MallIndexConfigGoodVO> list = new ArrayList<>();
        List<MallIndexConfig> mallIndexConfigs = mallIndexConfigMapper.getIndexConfigByTypeAndNumber(configType, number);
        if (!CollectionUtils.isEmpty(mallIndexConfigs)) {
            List<Long> goodsId = mallIndexConfigs.stream().map(MallIndexConfig::getGoodsId).collect(Collectors.toList());
            List<MallGoods> mallGoods = mallGoodsMapper.selectByPrimaryKeys(goodsId);
            list = MyBeanUtil.copyList(mallGoods, MallIndexConfigGoodVO.class);
            list.forEach(item -> {
                if (item.getGoodsIntro().length() > 22) {
                    item.setGoodsIntro(item.getGoodsIntro().substring(0, 22) + "...");
                }
                if (item.getGoodsName().length() > 30) {
                    item.setGoodsName(item.getGoodsName().substring(0, 30) + "...");
                }
            });
        }
        return list;
    }
}
