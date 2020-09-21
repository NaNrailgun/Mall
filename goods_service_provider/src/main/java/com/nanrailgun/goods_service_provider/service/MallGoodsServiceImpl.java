package com.nanrailgun.goods_service_provider.service;

import com.nanrailgun.config.utils.MyBeanUtil;
import com.nanrailgun.config.utils.PageQueryUtil;
import com.nanrailgun.config.utils.PageResult;
import com.nanrailgun.goods_api.api.MallGoodsService;
import com.nanrailgun.goods_api.api.dto.MallSearchGoodsDTO;
import com.nanrailgun.goods_api.entity.MallGoods;
import com.nanrailgun.goods_api.entity.StockNum;
import com.nanrailgun.goods_service_provider.dao.MallGoodsMapper;
import org.apache.dubbo.config.annotation.Service;
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
    public PageResult<MallSearchGoodsDTO> getGoodsListBySearch(PageQueryUtil pageUtil, int page, int limit) {
        pageUtil.setPage(page);
        pageUtil.setLimit(limit);
        List<MallGoods> mallGoodsList = mallGoodsMapper.selectBySearch(pageUtil);
        int total = mallGoodsMapper.getTotalMallGoodsBySearch(pageUtil);
        List<MallSearchGoodsDTO> list = MyBeanUtil.copyList(mallGoodsList, MallSearchGoodsDTO.class);
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

    @Override
    public MallGoods selectByPrimaryKey(Long goodsId) {
        return mallGoodsMapper.selectByPrimaryKey(goodsId);
    }

    @Override
    public List<MallGoods> selectByPrimaryKeys(List<Long> goodsId) {
        return mallGoodsMapper.selectByPrimaryKeys(goodsId);
    }

    @Override
    public int updateStockNum(List<StockNum> stockNums) {
        return mallGoodsMapper.updateStockNum(stockNums);
    }
}
