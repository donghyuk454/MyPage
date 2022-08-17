package com.mong.project.config.arguementresolver;

import com.mong.project.domain.member.annotation.Login;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.NativeWebRequest;

import static com.mong.project.config.interceptor.LoginConst.LOGIN_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginArgumentResolverTest {

    @InjectMocks
    private LoginArgumentResolver loginArgumentResolver;

    @Mock
    private MethodParameter parameter;

    @Mock
    private MockHttpServletRequest servletRequest;

    @Test
    @DisplayName("parameter 가 Login 어노테이션을 가지고 있고, Long 타입이면 true 를 반환합니다.")
    void supportsParameter() {
        when(parameter.hasParameterAnnotation(Login.class))
                .thenReturn(true);
        when(parameter.getParameterType())
                .thenReturn((Class) Long.class);

        boolean result = loginArgumentResolver.supportsParameter(parameter);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("parameter 가 Login 어노테이션을 가지고 있지만, Long 타입이 아니면 false 를 반환합니다.")
    void supportsParameterWithNotLong() {
        when(parameter.hasParameterAnnotation(Login.class))
                .thenReturn(true);
        when(parameter.getParameterType())
                .thenReturn((Class) Boolean.class);

        boolean result = loginArgumentResolver.supportsParameter(parameter);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("parameter 가 Login 어노테이션을 가지고 있지 않으면 false 를 반환합니다.")
    void supportsParameterWithNotLogin() {
        when(parameter.hasParameterAnnotation(Login.class))
                .thenReturn(false);
        when(parameter.getParameterType())
                .thenReturn((Class) Long.class);

        boolean result = loginArgumentResolver.supportsParameter(parameter);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("NativeRequest 를 HttpServletRequest 로 변환하고, 세션의 속성(memberId)을 반환한다.")
    void resolveArgument() throws Exception {
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(LOGIN_MEMBER, 1L);
        when(webRequest.getNativeRequest())
                .thenReturn(servletRequest);
        when(servletRequest.getSession(false))
                .thenReturn(session);

        Object result = loginArgumentResolver.resolveArgument(parameter, null, webRequest, null);

        assertThat(result).isEqualTo(1L);
    }
}