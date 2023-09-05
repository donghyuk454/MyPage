package com.mong.project.exception;

import com.mong.project.exception.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseBody
    @ExceptionHandler(UncheckedException.class)
    public ResponseEntity<ErrorResponse> handleException(UncheckedException e){
        log.info("에러 발생 message = {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(e.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(MyPageException.class)
    public ResponseEntity<ErrorResponse> handleException(MyPageException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}