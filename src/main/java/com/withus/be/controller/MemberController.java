package com.withus.be.controller;

import com.withus.be.common.exception.InvalidParameterException;
import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.service.MemberService;
import com.withus.be.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member Controller", description = "회원 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "특정 회원 정보 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<Body> getMember(@PathVariable(value = "id") Long id) {
        return new ResponseSuccess().success(memberService.getMemberInfo(id));
    }

    @Operation(summary = "개인정보 조회 API")
    @GetMapping
    public ResponseEntity<Body> getMyInfo() {
        return new ResponseSuccess().success(memberService.getMyInfo(SecurityUtil.getCurrentEmail().orElseThrow(
                () -> new InvalidParameterException("인증이 필요합니다.")))
        );
    }

}
