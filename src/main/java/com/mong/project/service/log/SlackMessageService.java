package com.mong.project.service.log;

import com.mong.project.service.log.dto.MessageDto;
import org.springframework.stereotype.Service;

@Service
public class SlackMessageService implements MessageService {
    @Override
    public MessageDto sendMessage(String title, String message) {
        MessageDto messageDto = new MessageDto(title, message);

        // message 전송 로직

        return messageDto;
    }
}
