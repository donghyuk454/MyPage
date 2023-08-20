package com.mong.project.util.log.aspect;

import com.mong.project.util.log.service.dto.ExceptionLogDto;
import com.mong.project.util.log.service.message.MessageService;
import com.mong.project.util.log.service.ServerExceptionLogService;
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
    private final ServerExceptionLogService logService;

    @Around("execution(* com.mong.project..*(..))")
    public Object sendLogMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            // db 에 log message 저장
            logService.addExceptionLog(new ExceptionLogDto(joinPoint, (Exception) e));

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
