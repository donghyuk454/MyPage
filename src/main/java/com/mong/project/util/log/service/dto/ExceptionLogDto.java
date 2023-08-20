package com.mong.project.util.log.service.dto;

import com.mong.project.util.log.domain.ServerExceptionLog;
import com.mong.project.util.log.domain.exception.ExceptionData;
import com.mong.project.util.log.domain.meta.MetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;

@Builder
@AllArgsConstructor
@Getter
public class ExceptionLogDto {

    private final String className;
    private final String methodName;
    private final String exceptionType;
    private final String message;

    public ExceptionLogDto (ProceedingJoinPoint joinPoint,
                            Exception exception) {
        this.className = joinPoint.getTarget().getClass().getName();
        this.methodName = joinPoint.getKind();
        this.exceptionType = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
    }

    public ServerExceptionLog toEntity() {
        ExceptionData exceptionData = new ExceptionData(exceptionType, message);
        MetaData metaData = new MetaData(className, methodName);

        return ServerExceptionLog.builder()
                .metaData(metaData)
                .exceptionData(exceptionData)
                .build();
    }
}
