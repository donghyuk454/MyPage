package com.mong.MyProject.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(SpringExtension.class)
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;
    protected Gson gson = new Gson();

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(setController())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    protected abstract Object setController();
}
