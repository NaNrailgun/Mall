package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallIndexConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallIndexConfigMapper {

    List<MallIndexConfig> getIndexConfigByTypeAndNumber(@Param("configType") int configType, @Param("number") int number);

}
