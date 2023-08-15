package com.mong.project.service.log;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MethodsClient client;

    @Autowired
    public SlackMessageService(@Value("${spring.slack.token}") String token,
                               @Value("${spring.slack.channel}") String channel) {
        this(token, channel, Slack.getInstance().methods());
    }

    public SlackMessageService(String token, String channel, MethodsClient client) {
        this.token = token;
        this.channel = channel;
        this.client = client;
    }

    @Override
    public Boolean sendMessage(String text) {
        try {
            ChatPostMessageRequest request = createRequest(text);

            ChatPostMessageResponse chatPostMessageResponse = client
                    .chatPostMessage(request);

            return chatPostMessageResponse.isOk();

        } catch (IOException | SlackApiException e) {
            log.error("Slack error: {}", e.getMessage(), e);
            return false;
        }
    }

    private ChatPostMessageRequest createRequest(String text) {
        return ChatPostMessageRequest.builder()
                .token(token)
                .channel(channel)
                .text(text)
                .build();
    }
}
