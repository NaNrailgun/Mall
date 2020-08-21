package com.nanrailgun.mall.dao;

import com.nanrailgun.mall.entity.MallUserToken;

public interface MallUserTokenMapper {

    /**
     * 新增token
     */
    int insert(MallUserToken mallUserToken);

    /**
     * 根据用户id删除token
     */
    int deleteByPrimaryKey(Long userId);

    /**
     * 根据用户id查找token
     */
    MallUserToken selectByPrimaryKey(Long userId);

    /**
     * 根据token值查找token
     */
    MallUserToken selectByToken(String token);

    /**
     * 根据用户id更新token
     */
    int updateByPrimaryKey(MallUserToken mallUserToken);

}
