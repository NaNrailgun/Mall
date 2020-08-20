package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.controller.param.MallUserUpdateParam;
import com.nanrailgun.mall.dao.MallUserMapper;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.service.MallUserService;
import com.nanrailgun.mall.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MallUserServiceImpl implements MallUserService {

    @Resource
    MallUserMapper mallUserMapper;

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser mallUser = MallUser.builder()
                .loginName(loginName)
                .passwordMD5(MD5Util.MD5Encode(password))
                .nickName(loginName)
                .introduceSign(Constants.USER_INTRO)
                .build();
        if (mallUserMapper.insert(mallUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5) {
        return null;
    }

    @Override
    public Boolean logout(Long userId) {
        return null;
    }

    @Override
    public Boolean update(MallUserUpdateParam param, Long userId) {
        return null;
    }
}
