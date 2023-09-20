package com.mong.project.controller.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBoardRequest {
    @NotNull
    private Long boardId;
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
