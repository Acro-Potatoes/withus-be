package com.withus.be.service;

import com.withus.be.common.exception.DuplicatedException;
import com.withus.be.dto.MemberDto.MemberRequest;
import com.withus.be.dto.MemberDto.MemberResponse;
import com.withus.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse signup(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.getEmail()).orElse(null) != null) {
            throw new DuplicatedException("이미 가입되어 있는 유저입니다.");
        }

        return MemberResponse.of(memberRepository.save(
                MemberRequest.from(memberRequest, passwordEncoder.encode(memberRequest.getPassword()))));
    }

}
