package com.mong.project.config.arguementresolver;

import com.mong.project.domain.member.annotation.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

import static com.mong.project.config.interceptor.LoginConst.LOGIN_MEMBER;

@Slf4j
@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberIdType = Long.class.isAssignableFrom(parameter.getParameterType());

        log.info("Login : {} , Id type : {}", hasLoginAnnotation, hasMemberIdType);

        return hasLoginAnnotation && hasMemberIdType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        return request.getSession(false).getAttribute(LOGIN_MEMBER);
    }
}
