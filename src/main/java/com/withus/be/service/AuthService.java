package com.withus.be.service;

import com.withus.be.common.exception.DuplicatedException;
import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.common.exception.InvalidParameterException;
import com.withus.be.domain.Member;
import com.withus.be.dto.MemberDto.MemberRequest;
import com.withus.be.dto.MemberDto.MemberResponse;
import com.withus.be.dto.MemberDto.PhoneNumCertResponse;
import com.withus.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${cloud.aws.s3.default-image}")
    private String imageUrl;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public MemberResponse signup(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent())
            throw new DuplicatedException("이미 가입되어 있는 유저입니다.");

        return MemberResponse.of(memberRepository.save(
                MemberRequest.from(memberRequest, passwordEncoder.encode(memberRequest.getPassword()), imageUrl)));
    }

    public String getCertNum(String pnum) {
        Optional<Member> member = memberRepository.findByPhoneNum(pnum);
        if (member.isEmpty()) throw new EntityNotFoundException("해당 번호를 가진 회원이 존재하지 않습니다.");

        String certNum = generateCertNum();
        saveToRedis(pnum, certNum);
        return certNum;
    }

    private String generateCertNum() {
        return String.valueOf(
                ThreadLocalRandom.current()
                        .ints(100_000, 1_000_000)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No value present"))
        );
    }

    private void saveToRedis(String pnum, String certNum) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(pnum, certNum);
        redisTemplate.expire(pnum, 300, TimeUnit.SECONDS);
    }

    public PhoneNumCertResponse confirmCertNum(String pnum, String certNum) {
        if (!(Objects.equals(redisTemplate.opsForValue().get(pnum), certNum)))
            throw new InvalidParameterException("인증 번호가 올바르지 않습니다.");

        redisTemplate.delete(pnum);
        return new PhoneNumCertResponse(
                memberRepository.findByPhoneNum(pnum)
                        .orElseThrow(EntityNotFoundException::new)
                        .getEmail(),
                "인증에 성공했습니다."
        );
    }

}
