package com.shangan.handler;

import com.shangan.annotation.Token2Administrator;
import com.shangan.common.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.mall.dao.AdministratorMapper;
import com.shangan.mall.dao.AdminstratorTokenMapper;
import com.shangan.mall.entity.Administrator;
import com.shangan.mall.entity.AdministratorToken;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class Token2AdministratorArgumentResolver implements HandlerMethodArgumentResolver {

    private final AdministratorMapper administratorMapper;
    private final AdminstratorTokenMapper adminstratorTokenMapper;

    public Token2AdministratorArgumentResolver(AdministratorMapper administratorMapper, AdminstratorTokenMapper adminstratorTokenMapper) {
        this.administratorMapper = administratorMapper;
        this.adminstratorTokenMapper = adminstratorTokenMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Token2Administrator.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        if(methodParameter.getParameterAnnotation(Token2Administrator.class) != null) {
            Administrator administrator = null;
            //获取请求头的中的token字段
            String token = nativeWebRequest.getHeader("token");
            //判断token是否存在或过期
            System.out.println("=======" + token);
            if(token != null && !token.equals("")) {
                AdministratorToken administratorToken = adminstratorTokenMapper.selectByToken(token);

                if(administratorToken == null || administratorToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    GlobalExceptionHandler.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }

                administrator = administratorMapper.selectByPrimaryKey(administratorToken.getAdministratorId());

                if(administrator == null) {
                    GlobalExceptionHandler.fail(ServiceResultEnum.ADMIN_NULL_ERROR.getResult());
                }

                if(administrator.getLocked().intValue() == 1) {
                    GlobalExceptionHandler.fail(ServiceResultEnum.LOGIN_ADMIN_LOCKED_ERROR.getResult());
                }

                return administrator;
            } else {
                GlobalExceptionHandler.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }
}
