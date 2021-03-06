package com.mong.MyProject.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMemberImageRequest {
    private Long member_id;
    private MultipartFile image;
}
