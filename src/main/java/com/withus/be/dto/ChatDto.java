package com.withus.be.dto;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private String message;
}
