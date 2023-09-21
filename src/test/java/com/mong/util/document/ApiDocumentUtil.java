package com.mong.util.document;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

public class ApiDocumentUtil {

    private ApiDocumentUtil(){}

    public static OperationRequestPreprocessor requestPreprocessor() {
        return preprocessRequest(prettyPrint());
    }

    public static OperationResponsePreprocessor responsePreprocessor() {
        return preprocessResponse(prettyPrint());
    }
}
