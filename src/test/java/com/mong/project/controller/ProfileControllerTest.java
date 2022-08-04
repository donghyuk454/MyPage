package com.mong.project.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("dev")
class ProfileControllerTest extends AbstractControllerTest{

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private Environment environment;

    @Autowired
    Environment env;

    @Test
    @DisplayName("현재 실행중인 profile 을 확인합니다. profile 을 dev 으로 설정하였으므로, dev 을 반환합니다.")
    void checkProfile() throws Exception {
        when(environment.getActiveProfiles()).thenReturn(env.getActiveProfiles());

        MockHttpServletRequestBuilder builder = get("/profile");

        MvcResult mvcResult = mockMvc.perform( get("/profile"))
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