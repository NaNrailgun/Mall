package com.nanrailgun.mall.service.impl;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.controller.param.MallUserUpdateParam;
import com.nanrailgun.mall.dao.MallUserMapper;
import com.nanrailgun.mall.dao.MallUserTokenMapper;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.entity.MallUserToken;
import com.nanrailgun.mall.service.MallUserService;
import com.nanrailgun.mall.utils.MD5Util;
import com.nanrailgun.mall.utils.TokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MallUserServiceImpl implements MallUserService {

    @Resource
    MallUserMapper mallUserMapper;

    @Resource
    MallUserTokenMapper mallUserTokenMapper;

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
        MallUser user = mallUserMapper.selectByLoginName(loginName);
        if (user == null || !user.getPasswordMD5().equals(passwordMD5)) {
            return ServiceResultEnum.LOGIN_ERROR.getResult();
        }
        if (user.getLockedFlag() == 1) {
            return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
        }
        String token = TokenUtil.genToken(user.getUserId());
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);
        MallUserToken mallUserToken = MallUserToken.builder()
                .userId(user.getUserId())
                .token(token)
                .updateTime(now)
                .expireTime(expireTime)
                .build();
        if (mallUserTokenMapper.selectByPrimaryKey(user.getUserId()) == null) {
            if (mallUserTokenMapper.insert(mallUserToken) > 0)
                return token;
        } else {
            if (mallUserTokenMapper.updateByPrimaryKey(mallUserToken) > 0)
                return token;
        }
        return ServiceResultEnum.DB_ERROR.getResult();
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
