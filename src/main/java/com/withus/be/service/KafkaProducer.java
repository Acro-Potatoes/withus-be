package com.withus.be.service;

import com.withus.be.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedDelay = 10_000, initialDelay = 5000)
    public void send() {
        log.info("Kafka Producer - BEGIN");

        String topic = "topic";

        for (int i = 0; i < 10; i++) {
            ChatDto payload = ChatDto.builder().message("message" + i).build();
            log.info("sending payload : {} to topic : {}", payload, topic);
            kafkaTemplate.send(topic, payload);
        }

        log.info("Kafka Producer - END");
    }
}
