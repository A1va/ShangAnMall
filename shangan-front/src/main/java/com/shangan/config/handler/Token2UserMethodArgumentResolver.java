package com.shangan.config.handler;

import com.shangan.config.GlobalExceptionHandler;
import com.shangan.common.ServiceResultEnum;
import com.shangan.config.Token2User;
import com.shangan.mall.dao.UserMapper;
import com.shangan.mall.dao.UserTokenMapper;
import com.shangan.mall.entity.User;
import com.shangan.mall.entity.UserToken;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Author Alva
 * @CreateTime 2021/1/27 11:55
 * 方法参数解析器，在需要用户身份信息的方法中加上 @Token2User 注解，之后通过方法参数解析器来获得当前登录的对象信息。
 */
@Component
public class Token2UserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserMapper userMapper;
    private final UserTokenMapper userToeknMapper;

    public Token2UserMethodArgumentResolver(UserMapper userMapper, UserTokenMapper userToeknMapper) {
        this.userMapper = userMapper;
        this.userToeknMapper = userToeknMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
//        检查参数是否为 Token2User 类
        return methodParameter.hasParameterAnnotation(Token2User.class);
    }

    /**
     * 首先获取请求头中的 token 值，不存在则返回错误信息给前端，存在则继续后续流程。
     * 通过 token 值来查询 UserToken 对象，是否存在或者是否过期，不存在或者已过期则返回错误信息给前端，正常则继续后续流程。
     * 通过 UserToken 对象中的 userId 字段来查询 User 用户对象，判断是否存在和是否已被封禁，用户状态正常则返回用户对象供对应的方法使用，否则返回错误信息。
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        if (methodParameter.getParameterAnnotation(Token2User.class) != null) {
            User user = null;
//            获取请求头中的 header
            String token = nativeWebRequest.getHeader("token");
//            验证 token 是否存在
            if (null != token && !"".equals(token)) {
//                通过 token 值查询用户对象
                UserToken userToken = userToeknMapper.selectByToken(token);
//                token 不存在 / token 过期
                if (userToken == null || userToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    GlobalExceptionHandler.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }
                user = userMapper.selectByPrimaryKey(userToken.getUserId());
//                用户不存在
                if (user == null) {
                    GlobalExceptionHandler.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
                }
//                是否禁用
                if (user.getLockedFlag().intValue() == 1) {
                    GlobalExceptionHandler.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
                }
//                返回用户对象供对应的方法使用
                return user;
            } else {
                GlobalExceptionHandler.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }
}
