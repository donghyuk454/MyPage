package com.mong.MyProject.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorCode {
    // member
    public static final String NOT_EXIST_MEMBER = "존재하지 않는 유저의 아이디 입니다.";
    public static final String INVALID_EMAIL = "잘못된 이메일 입니다.";
    public static final String INVALID_PASSWORD = "잘못된 비밀번호 입니다.";
    public static final String ALREADY_EXIST_ALIAS = "이미 존재하는 닉네임 입니다.";
    public static final String ALREADY_EXIST_MEMBER = "이미 존재하는 유저입니다.";

    // board
    public static final String NOT_EXIST_BOARD = "존재하지 않는 개시물의 아이디 입니다.";

    // comment
    public static final String NOT_EXIST_COMMENT = "존재하지 않는 댓글의 아이디 입니다.";

    // image
    public static final String FAIL_TO_WRITE_FILE = "파일 작성에 실패하였습니다.";
}
