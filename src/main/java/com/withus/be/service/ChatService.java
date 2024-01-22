package com.withus.be.service;

import com.withus.be.domain.Chat;
import com.withus.be.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat saveChat() {
        return chatRepository.save(Chat.builder()
                .roomId(String.valueOf(UUID.randomUUID()))
                .sender("Sender")
                .message("Message")
                .createdDate(String.valueOf(ZonedDateTime.now()))
                .build());
    }
}
