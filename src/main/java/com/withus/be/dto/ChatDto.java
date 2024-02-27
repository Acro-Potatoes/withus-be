package com.withus.be.dto;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

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
}
