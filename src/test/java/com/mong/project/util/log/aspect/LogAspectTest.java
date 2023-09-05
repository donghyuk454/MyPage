package com.mong.project.util.log.aspect;

import com.mong.project.domain.member.Member;
import com.mong.project.exception.MyPageException;
import com.mong.project.exception.UncheckedException;
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

import javax.persistence.EntityNotFoundException;

import static com.mong.project.exception.ErrorCode.UNCHECKED_EXCEPTION;
import static org.assertj.core.api.AssertionsForClassTypes.*;
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

    @DisplayName("RuntimeException 이 발생하면 LogAspect 의 sendLogMessage 함수가 실행됩니다. 최종적으로 UncheckedException 을 throw 합니다.")
    @Test
    void sendMessageWhenThereIsRuntimeException() {
        //given
        String message = "message";
        Exception exception = new RuntimeException(message);
        when(memberRepository.save(mockMember))
                .thenThrow(exception);

        MemberRepository repository = factory.getProxy();

        //when
        Throwable throwable = catchThrowable(() -> repository.save(mockMember));

        //then
        assertThat(throwable.getCause()).isEqualTo(exception);
        assertThat(throwable.getMessage()).isEqualTo(UNCHECKED_EXCEPTION);
        assertThat(throwable.getClass()).isEqualTo(UncheckedException.class);
        verify(messageService,
                times(1))
                .sendMessage(anyString());
        verify(exceptionLogService,
                times(1))
                .addExceptionLog(any(ExceptionLogDto.class));
    }

    @DisplayName("Exception 이 발생하지 않으면 정상적으로 기능을 수행합니다.")
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

    @DisplayName("MyPageException 이 발생하면 LogAspect 의 sendLogMessage 함수가 실행되지 않으며 그대로 throw 합니다.")
    @Test
    void sendNoMessageWhenThereIsMyPageException() {
        //given
        String message = "message";
        Exception exception = new MyPageException(message);
        when(memberRepository.save(mockMember))
                .thenThrow(exception);

        MemberRepository repository = factory.getProxy();

        //when
        Throwable throwable = catchThrowable(() -> repository.save(mockMember));

        //then
        assertThat(throwable).isEqualTo(exception);
        verify(messageService,
                times(0))
                .sendMessage(anyString());
        verify(exceptionLogService,
                times(0))
                .addExceptionLog(any(ExceptionLogDto.class));
    }

    @DisplayName("PersistenceException 이 발생하면 LogAspect 의 sendLogMessage 함수가 실행되지 않으며, MyPageException 으로 throw 합니다.")
    @Test
    void sendNoMessageWhenThereIsPersistenceException() {
        //given
        String message = "message";
        Exception exception = new EntityNotFoundException(message);
        when(memberRepository.save(mockMember))
                .thenThrow(exception);

        MemberRepository repository = factory.getProxy();

        //when
        Throwable throwable = catchThrowable(() -> repository.save(mockMember));

        //then
        assertThat(throwable.getCause()).isEqualTo(exception);
        assertThat(throwable.getClass()).isEqualTo(MyPageException.class);
        verify(messageService,
                times(0))
                .sendMessage(anyString());
        verify(exceptionLogService,
                times(0))
                .addExceptionLog(any(ExceptionLogDto.class));
    }
}