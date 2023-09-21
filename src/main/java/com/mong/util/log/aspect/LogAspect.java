package com.mong.util.log.aspect;

import com.mong.project.exception.MyPageException;
import com.mong.project.exception.UncheckedException;
import com.mong.util.log.service.dto.ExceptionLogDto;
import com.mong.util.log.service.message.MessageService;
import com.mong.util.log.service.ServerExceptionLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.persistence.PersistenceException;

import static com.mong.project.exception.ErrorCode.UNCHECKED_EXCEPTION;

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
        } catch (MyPageException e) {
            // check 된 Exception
            throw e;
        } catch (PersistenceException e) {
            // check 된 Exception
            throw new MyPageException(e.getMessage(), e);
        } catch (RuntimeException e) { // MyPageException 이 아닌 Runtime exception 인 경우에만 동작
            // db 에 log message 저장
            logService.addExceptionLog(new ExceptionLogDto(joinPoint, e));

            // slack message 전송
            String message = createMessage(joinPoint, e);
            log.info(message);
            messageService.sendMessage(message);

            throw new UncheckedException(UNCHECKED_EXCEPTION, e);
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
