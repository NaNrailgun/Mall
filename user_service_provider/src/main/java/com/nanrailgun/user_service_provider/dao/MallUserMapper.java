package com.nanrailgun.user_service_provider.dao;

import com.nanrailgun.user_api.entity.MallUser;

public interface MallUserMapper {
    /**
     * 插入用户数据
     * 若MallUser任一成员变量为空 则该字段按默认值插入
     */
    int insert(MallUser mallUser);

    /**
     * 按用户id更新用户数据
     * 若MallUser任一成员变量为空 则该字段不做修改
     */
    int updateByPrimaryKey(MallUser mallUser);

    /**
     * 按用户id查找数据
     */
    MallUser selectByPrimaryKey(Long userId);

    /**
     * 按用户登录名查数据
     */
    MallUser selectByLoginName(String loginName);
}
