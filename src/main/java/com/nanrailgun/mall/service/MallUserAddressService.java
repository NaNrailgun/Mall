package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.vo.MallUserAddressVO;
import com.nanrailgun.mall.entity.MallUserAddress;

import java.util.List;

public interface MallUserAddressService {

    /**
     * 获取地址列表
     */
    List<MallUserAddressVO> getAddressList(Long userId);

    /**
     * 获取地址详情
     */
    MallUserAddress getAddressByAddressId(Long addressId);

    /**
     * 获取默认地址
     */
    MallUserAddress getDefaultAddress(Long userId);

    /**
     * 保存地址
     */
    void saveAddress(MallUserAddress address);

    /**
     * 修改地址
     */
    void updateAddress(MallUserAddress address);

    /**
     * 删除地址
     */
    Boolean deleteAddress(Long addressId);

}
