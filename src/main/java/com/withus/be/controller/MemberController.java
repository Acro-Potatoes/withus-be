package com.withus.be.controller;

import com.withus.be.common.exception.UnAuthenticationException;
import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.dto.MemberDto.ModifyInfoRequest;
import com.withus.be.dto.MemberDto.PasswordRequest;
import com.withus.be.service.MemberService;
import com.withus.be.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseSuccess().success(memberService.getMyInfo(getCurrentEmail()));
    }

    @Operation(summary = "비밀번호 변경 API", description = "(미로그인) 비밀번호 찾기 > 비밀번호 변경")
    @PostMapping("/pwd")
    public ResponseEntity<Body> changePassword(@Valid @RequestBody PasswordRequest passwordRequest) {
        return new ResponseSuccess().success(memberService.changePassword(passwordRequest));
    }

    @Operation(summary = "회원 정보 수정 API")
    @PutMapping("/myInfo")
    public ResponseEntity<Body> changeMyInfo(@Valid @RequestBody ModifyInfoRequest request) {
        return new ResponseSuccess().success(memberService.modifyInfo(getCurrentEmail(), request));
    }

    private String getCurrentEmail() {
        return SecurityUtil.getCurrentEmail().orElseThrow(UnAuthenticationException::new);
    }

}
