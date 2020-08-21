package com.nanrailgun.mall.config.annotation.resolver;

import com.nanrailgun.mall.common.Constants;
import com.nanrailgun.mall.common.MallException;
import com.nanrailgun.mall.common.ServiceResultEnum;
import com.nanrailgun.mall.config.annotation.MallToken;
import com.nanrailgun.mall.dao.MallUserMapper;
import com.nanrailgun.mall.dao.MallUserTokenMapper;
import com.nanrailgun.mall.entity.MallUser;
import com.nanrailgun.mall.entity.MallUserToken;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class MallTokenHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    MallUserMapper mallUserMapper;

    @Resource
    MallUserTokenMapper mallUserTokenMapper;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(MallToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        String token = nativeWebRequest.getHeader("token");
        if (token == null || "".equals(token) || token.length() == Constants.TOKEN_LENGTH) {
            MallException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
        MallUserToken userToken = mallUserTokenMapper.selectByToken(token);
        if (userToken == null || userToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
            MallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
        }
        MallUser user = mallUserMapper.selectByPrimaryKey(userToken.getUserId());
        if (user == null) {
            MallException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
        }
        if (user.getLockedFlag() == 1) {
            MallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
        }
        //续杯
        Date now = new Date();
        userToken.setUpdateTime(now);
        userToken.setExpireTime(new Date(now.getTime() + 2 * 24 * 3600 * 1000));
        mallUserTokenMapper.updateByPrimaryKey(userToken);
        return user;
    }
}
