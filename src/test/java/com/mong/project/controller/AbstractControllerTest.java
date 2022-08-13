package com.mong.project.controller;

import com.mong.project.exception.ExceptionHandlers;

import com.google.gson.Gson;
import com.mong.project.util.document.ApiDocumentUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;
    protected Gson gson = new Gson();
    protected OperationRequestPreprocessor requestPreprocessor = ApiDocumentUtil.requestPreprocessor();
    protected OperationResponsePreprocessor responsePreprocessor = ApiDocumentUtil.responsePreprocessor();

    @BeforeEach
    void beforeEach(RestDocumentationContextProvider restDocumentationContextProvider){
        mockMvc = MockMvcBuilders.standaloneSetup(setController())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .setControllerAdvice(new ExceptionHandlers())
                .build();
    }

    protected abstract Object setController();
}
