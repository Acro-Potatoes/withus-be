package com.withus.be.service;

import com.withus.be.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(ChatDto chatDto) {
        log.info("[Producer]sending payload : {} to topic : {}", chatDto, "chat");
        kafkaTemplate.send("chat", chatDto);
    }

}
