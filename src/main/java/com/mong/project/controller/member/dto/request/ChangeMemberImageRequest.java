package com.mong.project.controller.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMemberImageRequest {
    private Long memberId;
    private MultipartFile image;
}
