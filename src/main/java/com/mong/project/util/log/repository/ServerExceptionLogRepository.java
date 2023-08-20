package com.mong.project.util.log.repository;

import com.mong.project.util.log.domain.ServerExceptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerExceptionLogRepository extends JpaRepository<ServerExceptionLog, Long> {
}