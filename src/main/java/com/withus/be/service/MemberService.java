package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Member;
import com.withus.be.dto.MemberDto.MemberResponse;
import com.withus.be.dto.MemberDto.ModifyInfoRequest;
import com.withus.be.dto.MemberDto.PasswordRequest;
import com.withus.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::of)
                .orElseThrow(() -> new EntityNotFoundException("There are no members with that ID."));
    }

    @Transactional(readOnly = true)
    public MemberResponse getMyInfo(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponse::of)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "There is no corresponding member for '%s'.", email
                )));
    }

    @Transactional
    public String changePassword(PasswordRequest passwordRequest) {
        Member member = getMember(passwordRequest.getEmail());
        String newPwd = passwordRequest.getNewPassword();
        if (!newPwd.equals(passwordRequest.getNewPasswordRe())) return "비밀번호가 일치하지 않습니다.";

        member.changePassword(passwordEncoder.encode(newPwd));
        return "비밀번호가 변경됐습니다.";
    }

    @Transactional
    public String modifyInfo(String email, ModifyInfoRequest request) {
        Member member = getMember(email);
        member.modifyInfo(request, member.getName(), member.getNickname(), member.getPhoneNum());
        return "정보 수정이 완료됐습니다.";
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(String.format("There is no corresponding member for '%s'.", email))
        );
    }

}
