package com.mong.project.service.log;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@PropertySource("classpath:application.properties")
public class SlackMessageService implements MessageService {

    private final String token;
    private final String channel;

    public SlackMessageService(@Value("${spring.slack.token}") String token,
                               @Value("${spring.slack.channel}") String channel) {
        this.token = token;
        this.channel = channel;
    }

    @Override
    public Boolean sendMessage(String text) {
        // message 전송 로직
        MethodsClient client = Slack.getInstance().methods();
        try {
            ChatPostMessageResponse chatPostMessageResponse = client
                    .chatPostMessage(requestBuilder -> requestBuilder
                            .token(token)
                            .channel(channel)
                            .text(text));

            return chatPostMessageResponse.isOk();

        } catch (IOException | SlackApiException e) {
            log.error("Slack error: {}", e.getMessage(), e);
            return false;
        }
    }
}
