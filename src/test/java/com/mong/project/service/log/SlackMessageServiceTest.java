package com.mong.project.service.log;

import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SlackMessageServiceTest {

    MessageService messageService;

    String token = "token";
    String channel = "channel";
    MethodsClient client;
    ChatPostMessageResponse response;

    @BeforeEach
    void setup() {
        client = mock(MethodsClient.class);
        messageService = new SlackMessageService(token, channel, client);
        response = mock(ChatPostMessageResponse.class);
    }

    @DisplayName("Message 가 정상적으로 전달되면 True 를 return")
    @Test
    void sendMessageSuccessfully_ThenReturnTrue() throws Exception {
        //given
        when(client.chatPostMessage(any(ChatPostMessageRequest.class)))
                .thenReturn(response);
        when(response.isOk())
                .thenReturn(Boolean.TRUE);

        //when
        Boolean result = messageService.sendMessage("message");

        //then
        verify(client,
                times(1))
                .chatPostMessage(any(ChatPostMessageRequest.class));
        verify(response,
                times(1))
                .isOk();
        assertThat(result).isTrue();
    }

    @DisplayName("Message 가 정상적으로 전달되지 않으 False 를 return")
    @Test
    void sendMessageUnsuccessfully_ThenReturnFalse() throws Exception {
        //given
        when(client.chatPostMessage(any(ChatPostMessageRequest.class)))
                .thenReturn(response);
        when(response.isOk())
                .thenReturn(Boolean.FALSE);

        //when
        Boolean result = messageService.sendMessage("message");

        //then
        verify(client,
                times(1))
                .chatPostMessage(any(ChatPostMessageRequest.class));
        verify(response,
                times(1))
                .isOk();
        assertThat(result).isFalse();
    }

    @DisplayName("Message 전송 중 Exception 이 발생하면 False 를 return")
    @Test
    void sendMessageException_ThenReturnFalse() throws Exception {
        //given
        doThrow(mock(SlackApiException.class))
                .when(client)
                .chatPostMessage(any(ChatPostMessageRequest.class));

        //when
        Boolean result = messageService.sendMessage("message");

        //then
        verify(client,
                times(1))
                .chatPostMessage(any(ChatPostMessageRequest.class));
        verify(response,
                times(0))
                .isOk();
        assertThat(result).isFalse();
    }

}