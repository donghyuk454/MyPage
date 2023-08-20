package com.mong.project.util.log.service;

import com.mong.project.util.log.domain.ServerExceptionLog;
import com.mong.project.util.log.repository.ServerExceptionLogRepository;
import com.mong.project.util.log.service.dto.ExceptionLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerExceptionLogService {

    private final ServerExceptionLogRepository logRepository;

    public ServerExceptionLog addExceptionLog(ExceptionLogDto exceptionLogDto) {
        ServerExceptionLog exceptionLog = exceptionLogDto.toEntity();

        return logRepository.save(exceptionLog);
    }
}
