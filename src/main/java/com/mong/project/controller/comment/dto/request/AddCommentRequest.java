package com.mong.project.controller.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequest {
    @NotNull
    private Long boardId;
    @NotBlank
    private String content;
}
