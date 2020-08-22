package com.nanrailgun.mall.controller;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.config.annotation.MallToken;
import com.nanrailgun.mall.controller.param.MallUserLoginParam;
import com.nanrailgun.mall.controller.param.MallUserRegisterParam;
import com.nanrailgun.mall.controller.param.MallUserUpdateParam;
import com.nanrailgun.mall.controller.vo.MallUserVO;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.service.MallUserService;
import com.nanrailgun.mall.utils.NumberUtil;
import com.nanrailgun.mall.utils.Result;
import com.nanrailgun.mall.utils.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MallUserController {

    @Autowired
    MallUserService mallUserService;

    @PostMapping("/user/register")
    public Result register(@RequestBody @Valid MallUserRegisterParam mallUserRegisterParam) {
        if (!NumberUtil.isPhone(mallUserRegisterParam.getLoginName())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String registerResult = mallUserService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword());
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(registerResult);
    }

    @PostMapping("/user/login")
    public Result login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
        if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = mallUserService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMd5());
        if (loginResult.length() == Constants.TOKEN_LENGTH) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        return ResultGenerator.genFailResult(loginResult);
    }

    @PostMapping("/user/logout")
    public Result logout(@MallToken MallUser user) {
        if (mallUserService.logout(user.getUserId())) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.LOGOUT_ERROR.getResult());
    }

    @PutMapping("/user/info")
    public Result update(@RequestBody MallUserUpdateParam param, @MallToken MallUser user) {
        if (mallUserService.update(param, user.getUserId())) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.UPDATE_ERROR.getResult());
    }

    @GetMapping("/user/info")
    public Result getUserInfo(@MallToken MallUser user) {
        MallUserVO mallUserVO = MallUserVO.builder()
                .loginName(user.getLoginName())
                .nickName(user.getNickName())
                .introduceSign(user.getIntroduceSign())
                .build();
        return ResultGenerator.genSuccessResult(mallUserVO);
    }
}
