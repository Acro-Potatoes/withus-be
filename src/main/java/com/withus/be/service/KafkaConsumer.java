package com.withus.be.service;

import com.withus.be.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "chat", groupId = "chat")
    public void listener(ConsumerRecord<String, ChatDto> consumerRecord) {
        ChatDto payload = consumerRecord.value();
        log.info("[Consumer] received payload : {}", payload);

        sendNotification(payload.getSender(), payload.getMessage());
    }

    private void sendNotification(String sender, String message) {
        log.info("sender : {}, message : {}", sender, message);
        kafkaTemplate.send("notification", Map.of("sender", sender, "message", message));
    }
}
