package com.nanrailgun.mall.service;

import com.nanrailgun.mall.controller.param.MallUserUpdateParam;

public interface MallUserService {

    /**
     * 用户账号注册
     * @param loginName 登录名
     * @param password 密码
     */
    String register(String loginName,String password);

    /**
     * 登录
     * @param loginName 登录名
     * @param passwordMD5 密码MD5
     */
    String login(String loginName,String passwordMD5);

    /**
     * 退出登录
     * @param userId 用户id
     */
    Boolean logout(Long userId);

    /**
     * 更新信息
     * @param param 用户待更改信息参数（前端传入）
     * @param userId 用户id
     */
    Boolean update(MallUserUpdateParam param,Long userId);
}
