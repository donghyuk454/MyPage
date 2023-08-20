package com.mong.project.util.log.service;

import com.mong.project.util.log.domain.ServerExceptionLog;
import com.mong.project.util.log.repository.ServerExceptionLogRepository;
import com.mong.project.util.log.service.dto.ExceptionLogDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServerExceptionLogServiceTest {

    private ServerExceptionLogService logService;
    private ServerExceptionLogRepository logRepository;

    @BeforeEach
    void setup() {
        logRepository = mock(ServerExceptionLogRepository.class);
        logService = new ServerExceptionLogService(logRepository);
    }

    @DisplayName("exception 이 발생하면 db 에 exception 이 발생한 정보를 입력합니다.")
    @Test
    void addExceptionLog() {
        //given
        ExceptionLogDto exceptionLogDto = ExceptionLogDto.builder()
                .className("class name")
                .methodName("method name")
                .exceptionType("exception type")
                .message("message")
                .build();
        when(logRepository.save(any(ServerExceptionLog.class)))
                .thenReturn(mock(ServerExceptionLog.class));

        //when
        logService.addExceptionLog(exceptionLogDto);

        //then
        verify(logRepository, times(1))
                .save(any(ServerExceptionLog.class));
    }
}