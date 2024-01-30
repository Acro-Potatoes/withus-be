package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.dto.MemberDto.MemberResponse;
import com.withus.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMemberInfo(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponse::of)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    }

    public MemberResponse getMyInfo(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponse::of)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "There is no corresponding member for '%s'.", email
                )));
    }
}
