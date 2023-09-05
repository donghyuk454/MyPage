package com.mong.project.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UncheckedException extends RuntimeException {
    public UncheckedException(String message, Throwable e) {
        super(message, e);
    }
}