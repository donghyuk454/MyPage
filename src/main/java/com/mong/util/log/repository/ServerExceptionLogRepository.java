package com.mong.util.log.repository;

import com.mong.util.log.domain.ServerExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerExceptionLogRepository extends JpaRepository<ServerExceptionLog, Long> {
}