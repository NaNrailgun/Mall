package com.nanrailgun.goods_service_provider.dao;

import com.nanrailgun.goods_api.entity.MallIndexConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallIndexConfigMapper {

    List<MallIndexConfig> getIndexConfigByTypeAndNumber(@Param("configType") int configType, @Param("number") int number);

}
