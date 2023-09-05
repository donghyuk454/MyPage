package com.mong.project.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MyPageException extends RuntimeException {

    public MyPageException(String message, Throwable e) {
        super(message, e);
    }
    public MyPageException(String message) {
        super(message, null);
    }

    public MyPageException(Throwable cause) {
        super("", cause);
    }
}
