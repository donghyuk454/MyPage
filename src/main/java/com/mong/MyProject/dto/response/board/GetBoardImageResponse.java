package com.mong.MyProject.dto.response.board;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GetBoardImageResponse {
    private Long image_id;
    private String url;
}
