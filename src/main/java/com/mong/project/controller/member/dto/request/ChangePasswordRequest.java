package com.mong.project.controller.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

import static com.mong.project.exception.ErrorCode.INVALID_PASSWORD_FORMAT;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank
    @Size(min = 8, max = 15, message = INVALID_PASSWORD_FORMAT)
    private String newPasswd;
}
