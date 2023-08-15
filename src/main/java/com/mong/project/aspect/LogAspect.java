package com.mong.project.aspect;

import com.mong.project.service.log.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private final MessageService messageService;

    @Around("execution(* com.mong.project..*(..))")
    public Object sendLogMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            // db 에 log message 저장

            // slack message 전송
            String message = createMessage(joinPoint, e);
            log.info(message);
            messageService.sendMessage(message);

            throw e;
        }
    }

    private String createMessage(ProceedingJoinPoint joinPoint, Throwable e) {
        String className = joinPoint
                .getTarget()
                .getClass()
                .getName();

        return "exception 발생" + ":" +
                className + ": " +
                e.getMessage();
    }
}
