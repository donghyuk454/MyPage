package com.mong.project.controller;

import com.mong.project.util.readme.ReadmeWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
class ReadmeControllerTest extends AbstractControllerTest {

    @InjectMocks
    private ReadmeController readmeController;

    @Mock
    private ReadmeWriter readmeWriter;

    @Override
    protected Object setController() {
        return readmeController;
    }

    @Test
    @DisplayName("readme 에 test coverage 를 작성합니다.")
    void checkWriteReadme() throws Exception {
        when(readmeWriter.writeReadme(any(), any()))
                .thenReturn(true);

        mockMvc.perform(get("/readme"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("test coverage 작성에 실패합니다.")
    void checkFailWriteReadme() throws Exception {
        when(readmeWriter.writeReadme(any(String.class), any(String.class)))
                .thenReturn(false);

        mockMvc.perform(get("/readme"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }
}