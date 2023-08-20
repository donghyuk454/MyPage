package com.mong.project.util.log.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
public class ExceptionData {
    private String exceptionType;
    private String message;
}
