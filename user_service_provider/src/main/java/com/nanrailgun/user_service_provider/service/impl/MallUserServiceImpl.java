package com.nanrailgun.user_service_provider.service.impl;

import com.nanrailgun.config.common.Constants;
import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.utils.MD5Util;
import com.nanrailgun.config.utils.TokenUtil;
import com.nanrailgun.user_api.api.MallUserService;
import com.nanrailgun.user_api.api.dto.MallUserUpdateDTO;
import com.nanrailgun.user_api.entity.MallUser;
import com.nanrailgun.user_api.entity.MallUserToken;
import com.nanrailgun.user_service_provider.dao.MallUserMapper;
import com.nanrailgun.user_service_provider.dao.MallUserTokenMapper;
import org.apache.dubbo.config.annotation.Service;

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
        MallUser mallUser=new MallUser();
        mallUser.setLoginName(loginName);
        mallUser.setPasswordMD5(MD5Util.MD5Encode(password));
        mallUser.setNickName(loginName);
        mallUser.setIntroduceSign(Constants.USER_INTRO);
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
        MallUserToken mallUserToken = new MallUserToken();
        mallUserToken.setUserId(user.getUserId());
        mallUserToken.setToken(token);
        mallUserToken.setUpdateTime(now);
        mallUserToken.setExpireTime(expireTime);

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
        return mallUserTokenMapper.deleteByPrimaryKey(userId) > 0;
    }

    @Override
    public Boolean update(MallUserUpdateDTO param, Long userId) {
        MallUser oldUser = mallUserMapper.selectByPrimaryKey(userId);
        if (oldUser != null) {
            oldUser.setNickName(param.getNickName());
            //若密码为空字符，则表明用户不打算修改密码，使用原密码保存
            if (!MD5Util.MD5Encode("", "UTF-8").equals(param.getPasswordMd5())) {
                oldUser.setPasswordMD5(param.getPasswordMd5());
            }
            oldUser.setIntroduceSign(param.getIntroduceSign());
            return mallUserMapper.updateByPrimaryKey(oldUser) > 0;
        }
        return false;
    }

    @Override
    public MallUserToken selectByToken(String token) {
        return mallUserTokenMapper.selectByToken(token);
    }

    @Override
    public MallUser selectByPrimaryKey(Long userId) {
        return mallUserMapper.selectByPrimaryKey(userId);
    }
}
