package com.withus.be.controller;

import com.withus.be.common.auth.jwt.JwtTokenProvider;
import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.dto.EmailDto;
import com.withus.be.dto.LoginDto;
import com.withus.be.dto.MemberDto;
import com.withus.be.dto.MemberDto.PhoneNumCertRequest;
import com.withus.be.dto.MemberDto.PhoneNumRequest;
import com.withus.be.dto.TokenDto;
import com.withus.be.service.AuthService;
import com.withus.be.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.withus.be.common.auth.oauth.OAuthOption.AUTHORIZATION_HEADER;
import static com.withus.be.common.auth.oauth.OAuthOption.JWT_HEADER_PREFIX;

@Tag(name = "Auth Controller", description = "인증 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Operation(summary = "회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<Body> signup(@Valid @RequestBody MemberDto.MemberRequest memberRequest) {
        return new ResponseSuccess().success(authService.signup(memberRequest));
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<Body> authorize(@Valid @RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto token = jwtTokenProvider.generateToken(authentication);

        return getTokenRes(token);
    }

    @Operation(summary = "Token 재발급", description = "Refresh Token으로 Token 재발급 API")
    @PostMapping("/refresh")
    public ResponseEntity<Body> getRefresh(@RequestBody TokenDto tokenDto) {
        return getTokenRes(
                jwtTokenProvider.generateTokenByRefreshToken(
                        tokenDto.getAccessToken(),
                        tokenDto.getRefreshToken())
        );
    }

    private ResponseEntity<Body> getTokenRes(TokenDto token) {
        new HttpHeaders().add(AUTHORIZATION_HEADER, JWT_HEADER_PREFIX + token.getAccessToken());
        return new ResponseSuccess().success(token);
    }

    @Operation(summary = "Email 인증 번호 전송")
    @PostMapping("/cert-mail")
    public ResponseEntity<Body> certificationMail(@RequestParam("email") String email) throws Exception {
        return new ResponseSuccess().success(mailService.sendSimpleMessage(email));
    }

    @Operation(summary = "Email 인증 번호 확인", description = "Email 인증 번호 확인 (5분 이내)")
    @PostMapping("/cert-mail/confirm")
    public ResponseEntity<Body> confirmCertNum(@Valid @RequestBody EmailDto emailDto) {
        return new ResponseSuccess().success(mailService.confirmCertNum(emailDto));
    }

    @Operation(summary = "휴대폰 인증 번호 전송")
    @PostMapping("/cert-pnum")
    public ResponseEntity<Body> certificationPhoneNum(@Valid @RequestBody PhoneNumRequest phoneNumRequest) {
        return new ResponseSuccess().success(authService.getCertNum(phoneNumRequest.getPhoneNum()));
    }

    @Operation(summary = "휴대폰 인증번호 확인")
    @PostMapping("/cert-pnum/confirm")
    public ResponseEntity<Body> confirmCertPhoneNum(@Valid @RequestBody PhoneNumCertRequest request) {
        return new ResponseSuccess().success(authService.confirmCertNum(request.getPhoneNum(), request.getCertNum()));
    }

}
