package com.mong.project.service.log;

import com.mong.project.service.log.dto.MessageDto;

public interface MessageService {
    MessageDto sendMessage(String title, String message);
}
