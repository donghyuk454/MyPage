package com.mong.util.log.service.dto;

import com.mong.util.log.domain.ServerExceptionLog;
import com.mong.util.log.service.dto.ExceptionLogDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionLogDtoTest {

    @DisplayName("joint point 와 Exception 을 통해 객체를 생성합니다.")
    @Test
    void createLogDto() {
        //given
        Object simpleObj = new Object();
        String methodName = "method name";
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.getTarget())
                .thenReturn(simpleObj);
        when(joinPoint.getKind())
                .thenReturn(methodName);
        Exception exception = new Exception("message");

        //when
        ExceptionLogDto exceptionLogDto = new ExceptionLogDto(joinPoint, exception);

        //then
        assertThat(exceptionLogDto.getClassName())
                .isEqualTo(simpleObj.getClass().getName());
        assertThat(exceptionLogDto.getMethodName())
                .isEqualTo(methodName);
        assertThat(exceptionLogDto.getExceptionType())
                .isEqualTo(exception.getClass().getSimpleName());
        assertThat(exceptionLogDto.getMessage())
                .isEqualTo(exception.getMessage());
    }

    @DisplayName("to entity 메서드를 통해 dto 객체를 domain 객체로 변환합니다.")
    @Test
    void toEntitySuccessfully() {
        //given
        ExceptionLogDto logDto = ExceptionLogDto.builder()
                .className("class name")
                .methodName("method name")
                .exceptionType("exception type")
                .message("message")
                .build();
        //when
        ServerExceptionLog exceptionData = logDto.toEntity();

        //then
        assertThat(exceptionData.getMetaData().getClassName())
                .isEqualTo(logDto.getClassName());
        assertThat(exceptionData.getMetaData().getMethodName())
                .isEqualTo(logDto.getMethodName());
        assertThat(exceptionData.getExceptionData().getExceptionType())
                .isEqualTo(logDto.getExceptionType());
        assertThat(exceptionData.getExceptionData().getMessage())
                .isEqualTo(logDto.getMessage());
    }
}