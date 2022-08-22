package com.mong.project.exception;

import com.mong.project.dto.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseBody
    @ExceptionHandler({SessionException.class, NoSuchElementException.class, EntityNotFoundException.class, IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e){
        log.info("에러 발생 message = {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
