package com.withus.be.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatDto {

    private String sender;
    private String roomId;
    private String message;
    private String createdDate;
    private String currentName;

    public static ChatDto of(ChatRequest chatDto, String name) {
        return ChatDto.builder()
                .sender(chatDto.getSender())
                .roomId(chatDto.getRoomId())
                .message(chatDto.getMessage())
                .createdDate(chatDto.getCreatedDate())
                .currentName(name)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ChatRequest {
        private String sender;
        private String roomId;
        private String message;
        private String createdDate;
    }
}
