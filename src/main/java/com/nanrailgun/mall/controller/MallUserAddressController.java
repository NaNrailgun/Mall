package com.nanrailgun.mall.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MallUserAddressController {

    @Autowired
    MallUserAddressService mallUserAddressService;

    @GetMapping("/address")
    public Result getAllAddress(@MallToken MallUser user) {
        List<MallUserAddressVO> temp = mallUserAddressService.getAddressList(user.getUserId());
        return ResultGenerator.genSuccessResult(temp);
    }

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
