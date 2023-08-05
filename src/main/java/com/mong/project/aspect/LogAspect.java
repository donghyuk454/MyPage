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
    public void log(ProceedingJoinPoint joinPoint) {
        try {
            joinPoint.proceed();
        } catch (Throwable e) {
            // db 에 log message 저장

            // slack message 전송
            messageService.sendMessage("exception 발생",
                    getMessage(joinPoint, e));

            throw new RuntimeException(e);
        }
    }

    private String getMessage(ProceedingJoinPoint joinPoint, Throwable e) {
        String className = joinPoint.getTarget().getClass().getName();

        return className + ": " + e.getMessage();
    }
}
