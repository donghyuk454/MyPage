package com.mong.project.config.interceptor;

import org.hibernate.SessionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static com.mong.project.config.interceptor.LoginConst.LOGIN_MEMBER;
import static com.mong.project.exception.ErrorCode.INVALID_SESSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginInterceptorTest {

    @InjectMocks
    private LoginInterceptor loginInterceptor;

    @Spy
    private MockHttpServletRequest request;

    @Spy
    private MockHttpServletResponse response;

    @Spy
    private MockMvcResultHandlers handler;

    @Spy
    private MockHttpSession session;

    @Test
    @DisplayName("세션이 존재하는 경우, 회원 정보(memberId)를 포함하고 있다면 true 를 반환합니다.")
    void preHandle() throws Exception{
        session.setAttribute(LOGIN_MEMBER, 1);
        request.setSession(session);

        boolean result = loginInterceptor.preHandle(request, response, handler);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Preflight 요청일 경우, true 를 반환합니다.")
    void preHandlePreflight() throws Exception{
        request.setMethod("OPTIONS");
        boolean result = loginInterceptor.preHandle(request, response, handler);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("세션이 존재하지 않는 경우, SessionException 이 발생합니다.")
    void preHandleNotExistSession() {
        SessionException exception = assertThrows(SessionException.class, () -> loginInterceptor.preHandle(request, response, handler));

        assertThat(exception.getMessage()).isEqualTo(INVALID_SESSION);
    }

}