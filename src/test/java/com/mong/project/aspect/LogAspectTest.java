package com.mong.project.aspect;

import com.mong.project.domain.member.Member;
import com.mong.project.repository.member.MemberRepository;
import com.mong.project.service.log.MessageService;
import com.mong.project.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LogAspectTest {

    @Mock
    private MemberService memberService;
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
        logAspect = new LogAspect(messageService);
        factory = new AspectJProxyFactory(memberRepository);
        factory.addAspect(logAspect);
    }

    @DisplayName("Exception 이 발생하면 LogAspect 의 sendLogMessage 함수가 실행됩니다.")
    @Test
    void sendMessageWhenThereIsAnException() throws Throwable {
        //given
        String message = "message";
        when(memberRepository.save(mockMember))
                .thenThrow(new IllegalStateException(message));

        MemberRepository repository = factory.getProxy();

        //when
        try {
            repository.save(mockMember);
        } catch (IllegalStateException e) { }

        //then
        verify(messageService)
                .sendMessage(anyString());
    }
}