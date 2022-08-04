package com.mong.project.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProfileControllerTest extends AbstractControllerTest{

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private Environment environment;

    @Test
    @DisplayName("현재 실행중인 profile 을 확인합니다. profile 을 dev 으로 설정한 경우, dev 을 반환합니다.")
    void checkProfile() throws Exception {
        String[] temp = new String[1];
        temp[0] = "dev";
        when(environment.getActiveProfiles())
                .thenReturn(temp);

        MvcResult mvcResult = mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("dev");
    }

    @Override
    protected Object setController() {
        return profileController;
    }
}