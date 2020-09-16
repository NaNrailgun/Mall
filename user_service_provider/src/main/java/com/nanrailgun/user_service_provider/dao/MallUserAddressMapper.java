package com.nanrailgun.user_service_provider.dao;

import com.nanrailgun.user_api.entity.MallUserAddress;

import java.util.List;

public interface MallUserAddressMapper {

    /**
     * 用户新增地址
     */
    int insert(MallUserAddress userAddress);

    /**
     * 按用户id查找所有地址
     */
    List<MallUserAddress> selectAllAddress(Long userId);

    /**
     * 按用户id查找默认地址
     */
    MallUserAddress selectDefaultAddress(Long userId);

    /**
     * 按地址id查找地址
     */
    MallUserAddress selectByPrimaryKey(Long addressId);

    /**
     * 按地址id删除
     */
    int deleteByPrimaryKey(Long addressId);

    /**
     * 按地址id更新
     */
    int updateByPrimaryKey(MallUserAddress address);
}
