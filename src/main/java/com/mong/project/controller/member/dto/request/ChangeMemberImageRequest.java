package com.mong.project.controller.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMemberImageRequest {
    @NotNull
    private Long memberId;
    @NotNull
    private MultipartFile image;
}