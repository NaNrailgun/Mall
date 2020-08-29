package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.vo.MallIndexConfigGoodVO;

import java.util.List;

public interface MallIndexConfigService {
    List<MallIndexConfigGoodVO> getIndexConfig(int configType, int number);
}
