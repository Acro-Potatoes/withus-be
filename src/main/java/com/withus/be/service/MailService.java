package com.withus.be.service;

import com.withus.be.common.exception.InvalidParameterException;
import com.withus.be.dto.EmailDto;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String PK = createKey();

    private static String createKey() {
        StringBuilder key = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97));
                case 1 -> key.append((char) (random.nextInt(26) + 65));
                case 2 -> key.append((random.nextInt(10)));
            }
        }
        return key.toString();
    }

    private MimeMessage createMessage(String email) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : {}", email);
        log.info("인증 번호 : {}", PK);

        MimeMessage message = javaMailSender.createMimeMessage();
        prepareMessage(message, email);
        return message;
    }

    private void prepareMessage(MimeMessage message, String email) throws MessagingException, UnsupportedEncodingException {
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("WITH-US 이메일 인증 번호입니다.");

        String msg = getMailBody();
        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(sender, "with-us"));
    }

    private String getMailBody() {
        return "<div style='margin:20px;'>" +
                "<h1> 안녕하세요. WITH-US 입니다. </h1><br>" +
                "<p>아래 코드를 복사해 5분 이내로 입력해주세요.</p><br>" +
                "<p>감사합니다.</p><br>" +
                "<div align='center' style='border:1px solid black; font-family:verdana;'>" +
                "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>" +
                "<div style='font-size:130%'>" +
                "CODE : <strong>" +
                PK +
                "</strong></div><br/></div>";
    }

    public String sendSimpleMessage(String email) throws Exception {
        javaMailSender.send(createMessage(email));
        saveToRedis(email);
        return String.format("인증번호는 '%s' 입니다.", PK);
    }

    private void saveToRedis(String email) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(email, PK);
        redisTemplate.expire(email, 300, TimeUnit.SECONDS);
    }

    public String confirmCertNum(EmailDto emailDto) {
        String email = emailDto.getEmail();
        if (!(Objects.equals(redisTemplate.opsForValue().get(email), emailDto.getCertNum())))
            throw new InvalidParameterException("인증 번호가 올바르지 않습니다.");

        redisTemplate.delete(email);
        return "인증에 성공했습니다.";
    }

}
