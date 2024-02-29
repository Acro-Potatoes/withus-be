package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.common.exception.UnAuthenticationException;
import com.withus.be.domain.Member;
import com.withus.be.dto.ChatDto;
import com.withus.be.dto.ChatDto.ChatRequest;
import com.withus.be.repository.MemberRepository;
import com.withus.be.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final MemberRepository memberRepository;

    public void sendMessage(ChatRequest chatDto) {
        log.info("[Producer]sending payload : {} to topic : {}", chatDto, "chat");
        Member member = getMember();
        kafkaTemplate.send("chat", ChatDto.of(chatDto, member.getName()));
    }

    private Member getMember() {
        return memberRepository.findByEmail(getCurrentEmail()).orElseThrow(() ->
                new EntityNotFoundException("해당 이메일의 회원이 존재하지 않습니다."));
    }

    private String getCurrentEmail() {
        return SecurityUtil.getCurrentEmail().orElseThrow(UnAuthenticationException::new);
    }

}
