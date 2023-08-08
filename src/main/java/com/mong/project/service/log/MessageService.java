package com.mong.project.service.log;

import com.mong.project.service.log.dto.MessageDto;

public interface MessageService {
    Boolean sendMessage(String text);
}
