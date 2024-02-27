package com.withus.be.service;

import com.withus.be.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send() {
        log.info("Kafka Producer - BEGIN");

        String topic = "topic";

        for (int i = 0; i < 10; i++) {
            ChatDto payload = getTestDto(i);
            log.info("[Producer]sending payload : {} to topic : {}", payload, topic);
            kafkaTemplate.send(topic, payload);
        }

        log.info("Kafka Producer - END");
    }

    private ChatDto getTestDto(int i) {
        return ChatDto.builder()
                .message("message" + i)
                .roomId(String.valueOf(UUID.randomUUID()))
                .sender("sender" + i)
                .createdDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }
}
