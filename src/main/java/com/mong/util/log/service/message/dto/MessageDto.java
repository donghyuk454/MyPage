package com.mong.util.log.service.message.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageDto {
    private final String title;
    private final String message;
}
