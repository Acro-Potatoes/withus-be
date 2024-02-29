package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.dto.ChatDto;
import com.withus.be.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/chat")
    public ResponseEntity<Body> sendChatMessage(@RequestBody ChatDto chatDto) {
        kafkaProducer.sendMessage(chatDto);
        return new ResponseSuccess().success();
    }

}
