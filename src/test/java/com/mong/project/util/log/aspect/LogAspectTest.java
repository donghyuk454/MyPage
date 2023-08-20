package com.mong.project.util.log.aspect;

import com.mong.project.domain.member.Member;
import com.mong.project.repository.member.MemberRepository;
import com.mong.project.util.log.service.ServerExceptionLogService;
import com.mong.project.util.log.service.dto.ExceptionLogDto;
import com.mong.project.util.log.service.message.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LogAspectTest {

    @Mock
    private ServerExceptionLogService exceptionLogService;
    @Mock
    private MessageService messageService;
    @Mock
    private MemberRepository memberRepository;
    Member mockMember;
    AspectJProxyFactory factory;
    LogAspect logAspect;

    @BeforeEach
    void setup() {
        mockMember = mock(Member.class);
        logAspect = new LogAspect(messageService, exceptionLogService);
        factory = new AspectJProxyFactory(memberRepository);
        factory.addAspect(logAspect);
    }

    @DisplayName("Exception 이 발생하면 LogAspect 의 sendLogMessage 함수가 실행됩니다.")
    @Test
    void sendMessageWhenThereIsAnException() {
        //given
        String message = "message";
        Exception exception = new IllegalStateException(message);
        when(memberRepository.save(mockMember))
                .thenThrow(exception);

        MemberRepository repository = factory.getProxy();

        //when
        try {
            repository.save(mockMember);
        } catch (IllegalStateException e) {
            assertThat(e).isEqualTo(exception);
        }

        //then
        verify(messageService,
                times(1))
                .sendMessage(anyString());
        verify(exceptionLogService,
                times(1))
                .addExceptionLog(any(ExceptionLogDto.class));
    }

    @DisplayName("Exception 이 발생하면 LogAspect 의 sendLogMessage 함수가 실행됩니다.")
    @Test
    void sendNoMessageWhenThereIsNoException() {
        //given
        when(memberRepository.save(mockMember))
                .thenReturn(mock(Member.class));

        MemberRepository repository = factory.getProxy();

        //when
        repository.save(mockMember);

        //then
        verify(messageService,
                times(0))
                .sendMessage(anyString());
        verify(exceptionLogService,
                times(0))
                .addExceptionLog(any(ExceptionLogDto.class));
    }
}