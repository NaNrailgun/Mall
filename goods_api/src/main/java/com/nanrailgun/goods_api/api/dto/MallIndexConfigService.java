package com.nanrailgun.goods_api.api.dto;

import java.util.List;

public interface MallIndexConfigService {
    List<MallIndexConfigGoodDTO> getIndexConfig(int configType, int number);
}
