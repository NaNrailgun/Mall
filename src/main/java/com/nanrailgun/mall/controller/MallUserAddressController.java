package com.nanrailgun.mall.controller;

import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.config.annotation.MallToken;
import com.nanrailgun.mall.controller.param.MallUserAddressSaveParam;
import com.nanrailgun.mall.controller.vo.MallUserAddressVO;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.entity.MallUserAddress;
import com.nanrailgun.mall.service.MallUserAddressService;
import com.nanrailgun.mall.utils.Result;
import com.nanrailgun.mall.utils.ResultGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MallUserAddressController {

    @Autowired
    MallUserAddressService mallUserAddressService;

    /**
     * 获取地址列表
     */
    @GetMapping("/address")
    public Result getAllAddress(@MallToken MallUser user) {
        List<MallUserAddressVO> temp = mallUserAddressService.getAddressList(user.getUserId());
        return ResultGenerator.genSuccessResult(temp);
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
        MallUserAddress address = MallUserAddress.builder()
                .userId(user.getUserId())
                .build();
        BeanUtils.copyProperties(param, address);
        mallUserAddressService.saveAddress(address);
        return ResultGenerator.genSuccessResult();
    }
}
