package com.nanrailgun.goods_service_provider.service;

import com.nanrailgun.config.utils.MyBeanUtil;
import com.nanrailgun.goods_api.api.dto.MallIndexConfigGoodDTO;
import com.nanrailgun.goods_api.api.dto.MallIndexConfigService;
import com.nanrailgun.goods_api.entity.MallGoods;
import com.nanrailgun.goods_api.entity.MallIndexConfig;
import com.nanrailgun.goods_service_provider.dao.MallGoodsMapper;
import com.nanrailgun.goods_service_provider.dao.MallIndexConfigMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallIndexConfigServiceImpl implements MallIndexConfigService {

    @Resource
    MallIndexConfigMapper mallIndexConfigMapper;

    @Resource
    MallGoodsMapper mallGoodsMapper;

    @Resource
    RedisTemplate<String, List<MallIndexConfigGoodDTO>> redisTemplate;

    private String indexConfigKey = "indexConfigKey";

    @Override
    public List<MallIndexConfigGoodDTO> getIndexConfig(int configType, int number) {
        List<MallIndexConfigGoodDTO> list = null;
        try {
            list = redisTemplate.opsForValue().get(indexConfigKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CollectionUtils.isEmpty(list)) {
            List<MallIndexConfig> mallIndexConfigs = mallIndexConfigMapper.getIndexConfigByTypeAndNumber(configType, number);
            if (!CollectionUtils.isEmpty(mallIndexConfigs)) {
                List<Long> goodsId = mallIndexConfigs.stream().map(MallIndexConfig::getGoodsId).collect(Collectors.toList());
                List<MallGoods> mallGoods = mallGoodsMapper.selectByPrimaryKeys(goodsId);
                list = MyBeanUtil.copyList(mallGoods, MallIndexConfigGoodDTO.class);
                list.forEach(item -> {
                    if (item.getGoodsIntro().length() > 22) {
                        item.setGoodsIntro(item.getGoodsIntro().substring(0, 22) + "...");
                    }
                    if (item.getGoodsName().length() > 30) {
                        item.setGoodsName(item.getGoodsName().substring(0, 30) + "...");
                    }
                });
                redisTemplate.opsForValue().set(indexConfigKey, list);
            }
        }
        return list;
    }
}
