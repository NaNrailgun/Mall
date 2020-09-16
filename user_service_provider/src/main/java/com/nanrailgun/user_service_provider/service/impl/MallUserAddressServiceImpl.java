package com.nanrailgun.user_service_provider.service.impl;

import com.nanrailgun.config.common.MallException;
import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.utils.MyBeanUtil;
import com.nanrailgun.user_api.api.MallUserAddressService;
import com.nanrailgun.user_api.api.dto.MallUserAddressDTO;
import com.nanrailgun.user_api.entity.MallUserAddress;
import com.nanrailgun.user_service_provider.dao.MallUserAddressMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MallUserAddressServiceImpl implements MallUserAddressService {

    @Resource
    MallUserAddressMapper mallUserAddressMapper;

    @Override
    public List<MallUserAddressDTO> getAddressList(Long userId) {
        List<MallUserAddress> list = mallUserAddressMapper.selectAllAddress(userId);
        return MyBeanUtil.copyList(list, MallUserAddressDTO.class);
    }

    @Override
    public MallUserAddress getAddressByAddressId(Long addressId) {
        MallUserAddress address = mallUserAddressMapper.selectByPrimaryKey(addressId);
        if (address == null) {
            MallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return address;
    }

    @Override
    public MallUserAddress getDefaultAddress(Long userId) {
        return mallUserAddressMapper.selectDefaultAddress(userId);
    }

    @Override
    @Transactional
    public void saveAddress(MallUserAddress address) {
        Date now = new Date();
        if (address.getDefaultFlag() == 1) {
            MallUserAddress oldAddress = mallUserAddressMapper.selectDefaultAddress(address.getUserId());
            if (oldAddress != null) {
                oldAddress.setDefaultFlag((byte) 0);
                oldAddress.setUpdateTime(now);
                if (mallUserAddressMapper.updateByPrimaryKey(oldAddress) < 1) {
                    MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }
        if (mallUserAddressMapper.insert(address) < 1)
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
    }

    @Override
    @Transactional
    public void updateAddress(MallUserAddress address) {
        Date now = new Date();
        if (address.getDefaultFlag() == 1) {
            MallUserAddress oldAddress = mallUserAddressMapper.selectDefaultAddress(address.getUserId());
            if (oldAddress != null && !oldAddress.getAddressId().equals(address.getAddressId())) {
                oldAddress.setDefaultFlag((byte) 0);
                oldAddress.setUpdateTime(now);
                if (mallUserAddressMapper.updateByPrimaryKey(oldAddress) < 1) {
                    MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }
        address.setUpdateTime(now);
        if (mallUserAddressMapper.updateByPrimaryKey(address) < 1)
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
    }

    @Override
    public Boolean deleteAddress(Long addressId) {
        return mallUserAddressMapper.deleteByPrimaryKey(addressId) > 0;
    }
}
