package com.nanrailgun.mall_gateway.controller;


import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.config.annotation.MallToken;
import com.nanrailgun.config.utils.MyBeanUtil;
import com.nanrailgun.config.utils.Result;
import com.nanrailgun.config.utils.ResultGenerator;
import com.nanrailgun.mall_gateway.controller.param.MallUserAddressSaveParam;
import com.nanrailgun.mall_gateway.controller.param.MallUserAddressUpdateParam;
import com.nanrailgun.mall_gateway.controller.vo.MallUserAddressVO;
import com.nanrailgun.user_api.api.MallUserAddressService;
import com.nanrailgun.user_api.api.dto.MallUserAddressDTO;
import com.nanrailgun.user_api.entity.MallUser;
import com.nanrailgun.user_api.entity.MallUserAddress;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MallUserAddressController {

    @Reference
    private MallUserAddressService mallUserAddressService;

    /**
     * 获取地址列表
     */
    @GetMapping("/address")
    public Result getAllAddress(@MallToken MallUser user) {
        List<MallUserAddressDTO> temp = mallUserAddressService.getAddressList(user.getUserId());
        List<MallUserAddressVO> mallUserAddressVOS = MyBeanUtil.copyList(temp, MallUserAddressVO.class);
        return ResultGenerator.genSuccessResult(mallUserAddressVOS);
    }

    /**
     * 获取地址详情
     */
    @GetMapping("/address/{addressId}")
    public Result getAddressDetail(@MallToken MallUser user, @PathVariable("addressId") Long addressId) {
        MallUserAddress address = mallUserAddressService.getAddressByAddressId(addressId);
        if (!address.getUserId().equals(user.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        MallUserAddressVO vo = new MallUserAddressVO();
        BeanUtils.copyProperties(address, vo);
        return ResultGenerator.genSuccessResult(vo);
    }

    /**
     * 保存地址
     */
    @PostMapping("/address")
    public Result saveAddress(@MallToken MallUser user, @RequestBody MallUserAddressSaveParam param) {
        MallUserAddress address = new MallUserAddress();
        address.setUserId(user.getUserId());
        BeanUtils.copyProperties(param, address);
        mallUserAddressService.saveAddress(address);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 获取默认地址
     */
    @GetMapping("/address/default")
    public Result getDefaultAddress(@MallToken MallUser user) {
        MallUserAddress address = mallUserAddressService.getDefaultAddress(user.getUserId());
        return ResultGenerator.genSuccessResult(address);
    }

    /**
     * 修改地址
     */
    @PutMapping("/address")
    public Result updateAddress(@MallToken MallUser user, @RequestBody MallUserAddressUpdateParam param) {
        MallUserAddress userAddress = mallUserAddressService.getAddressByAddressId(param.getAddressId());
        if (!userAddress.getUserId().equals(user.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        param.setUserId(user.getUserId());
        MallUserAddress address = new MallUserAddress();
        BeanUtils.copyProperties(param, address);
        mallUserAddressService.updateAddress(address);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 删除地址接口
     */
    @DeleteMapping("/address/{addressId}")
    public Result deleteAddress(@MallToken MallUser user, @PathVariable("addressId") Long addressId) {
        MallUserAddress userAddress = mallUserAddressService.getAddressByAddressId(addressId);
        if (!userAddress.getUserId().equals(user.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        mallUserAddressService.deleteAddress(addressId);
        return ResultGenerator.genSuccessResult();
    }
}
