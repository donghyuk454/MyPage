package com.mong.project.controller;

import com.mong.project.config.arguementresolver.LoginArgumentResolver;
import com.mong.project.config.interceptor.LoginInterceptor;
import com.mong.project.exception.ExceptionHandlers;

import com.google.gson.Gson;
import com.mong.project.util.document.ApiDocumentUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;
    protected Gson gson = new Gson();
    protected OperationRequestPreprocessor requestPreprocessor = ApiDocumentUtil.requestPreprocessor();
    protected OperationResponsePreprocessor responsePreprocessor = ApiDocumentUtil.responsePreprocessor();

    @Mock
    private LoginArgumentResolver loginArgumentResolver;

    @Mock
    private LoginInterceptor loginInterceptor;

    @BeforeEach
    void beforeEach(RestDocumentationContextProvider restDocumentationContextProvider) throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(setController())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addInterceptors(loginInterceptor)
                .setCustomArgumentResolvers(loginArgumentResolver)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .setControllerAdvice(new ExceptionHandlers())
                .build();


        when(loginInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any()))
                .thenReturn(true);

        when(loginArgumentResolver.supportsParameter(any(MethodParameter.class))).thenReturn(true);
        when(loginArgumentResolver.resolveArgument(any(MethodParameter.class), any(ModelAndViewContainer.class),
                any(NativeWebRequest.class), any())).thenReturn(1L);
    }

    protected abstract Object setController();

    protected MockHttpServletRequestBuilder createPostMockHttpServletRequest(Object object, String uri) {
        return post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(object));
    }

    protected MockHttpServletRequestBuilder createPutMockHttpServletRequest(Object object, String uri) {
        return put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(object));
    }

    protected MockHttpServletRequestBuilder createDeleteMockHttpServletRequest(Object object, String uri) {
        return delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(object));
    }
}
