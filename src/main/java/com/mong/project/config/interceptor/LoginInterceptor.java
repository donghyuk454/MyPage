package com.mong.project.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.mong.project.config.interceptor.LoginConst.LOGIN_MEMBER;
import static com.mong.project.exception.ErrorCode.INVALID_SESSION;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isPreflightRequest(request))
            return true;

        HttpSession session = request.getSession(false);
        checkEmpty(session);

        log.info("session success : {}", session);

        return true;
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return request.getMethod().equals("OPTIONS");
    }

    private void checkEmpty(HttpSession session) {
        if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {
            throw new SessionException(INVALID_SESSION);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            log.error("session error : {}", ex.getMessage());
        }
    }
}
