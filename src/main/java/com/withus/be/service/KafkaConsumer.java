package com.withus.be.service;

import com.withus.be.dto.ChatDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "topic", groupId = "chat")
    public void listener(ConsumerRecord<String, ChatDto> consumerRecord) {
        ChatDto payload = consumerRecord.value();
        log.info("[Consumer] received payload : {}", payload);
    }
}
