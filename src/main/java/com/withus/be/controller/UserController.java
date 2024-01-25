package com.withus.be.controller;

import com.withus.be.dto.MemberDto;
import com.withus.be.dto.MemberDto.MemberRequest;
import com.withus.be.dto.MemberDto.MemberResponse;
import com.withus.be.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@Valid @RequestBody MemberRequest memberRequest) {
        return ResponseEntity.ok(authService.signup(memberRequest));
    }

}
