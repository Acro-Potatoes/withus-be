package com.withus.be.service;

import com.withus.be.common.exception.DuplicatedException;
import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Member;
import com.withus.be.dto.MemberDto.MemberRequest;
import com.withus.be.dto.MemberDto.MemberResponse;
import com.withus.be.dto.MemberDto.PasswordRequest;
import com.withus.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    @Value("${cloud.aws.s3.default-image}")
    private String imageUrl;

    @Transactional
    public MemberResponse signup(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent())
            throw new DuplicatedException("이미 가입되어 있는 유저입니다.");

        return MemberResponse.of(memberRepository.save(
                MemberRequest.from(memberRequest, passwordEncoder.encode(memberRequest.getPassword()), imageUrl)));
    }

    @Transactional
    public String changePassword(PasswordRequest passwordRequest) {
        Member member = memberRepository.findByEmail(passwordRequest.getEmail()).orElseThrow(() ->
                new EntityNotFoundException("존재하지 않는 회원입니다.")
        );

        String newPwd = passwordRequest.getNewPassword();
        if (!newPwd.equals(passwordRequest.getNewPasswordRe())) return "비밀번호가 일치하지 않습니다.";

        member.changePassword(passwordEncoder.encode(newPwd));
        return "비밀번호가 변경됐습니다.";
    }


}
