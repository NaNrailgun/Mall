package com.nanrailgun.mall_gateway.config.resolver;

import com.nanrailgun.config.common.Constants;
import com.nanrailgun.config.common.MallException;
import com.nanrailgun.config.common.ServiceResultEnum;
import com.nanrailgun.config.config.annotation.MallToken;
import com.nanrailgun.user_api.api.MallUserService;
import com.nanrailgun.user_api.entity.MallUser;
import com.nanrailgun.user_api.entity.MallUserToken;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class MallTokenHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Reference
    MallUserService mallUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(MallToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        String token = nativeWebRequest.getHeader("token");
        if (token == null || "".equals(token) || token.length() != Constants.TOKEN_LENGTH) {
            MallException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
        MallUserToken userToken = mallUserService.selectByToken(token);
        if (userToken == null || userToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
            MallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
        }
        MallUser user = mallUserService.selectByPrimaryKey(userToken.getUserId());
        if (user == null) {
            MallException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
        }
        if (user.getLockedFlag() == 1) {
            MallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
        }
        /*//续杯
        Date now = new Date();
        userToken.setUpdateTime(now);
        userToken.setExpireTime(new Date(now.getTime() + 2 * 24 * 3600 * 1000));
        mallUserTokenMapper.updateByPrimaryKey(userToken);*/
        return user;
    }
}
