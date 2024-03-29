package com.withus.be.common.auth.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.be.common.auth.jwt.JwtTokenProvider;
import com.withus.be.common.auth.oauth.dto.GoogleOAuthToken;
import com.withus.be.common.auth.oauth.dto.GoogleUser;
import com.withus.be.common.exception.UnknownProviderException;
import com.withus.be.domain.constant.Provider;
import com.withus.be.dto.MemberDto.MemberRequest;
import com.withus.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final OAuthGoogleService oauthGoogleService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${OAuth2.google.default-password}")
    private String googlePassword;

    public String requestOAuthLogin(String code) throws Exception {
        // 구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아오고 토큰 요청
        return requestToken(oauthGoogleService.requestAccessToken(code));
    }

    public String requestToken(String responseBody) throws JsonProcessingException {
        GoogleUser googleUser = resObjDeserializeProc(responseBody);

        String email = googleUser.getEmail();
        ifYouDntHvMmbSv(email, googleUser);

        return generateToken(email);
    }

    /**
     * 응답 객체 == JSON. => deserialization => 자바 객체에 담음. <p>
     * 액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아옴. <p>
     * 다시 JSON 형식의 응답 객체 => 자바 객체로 deserialization.
     */
    private GoogleUser resObjDeserializeProc(String accessTokenResponse) throws JsonProcessingException {
        GoogleOAuthToken oAuthToken = oauthGoogleService.getAccessToken(accessTokenResponse);
        return new ObjectMapper().readValue(oauthGoogleService.requestUserInfo(oAuthToken), GoogleUser.class);
    }

    public void providerValidCheck(Provider socialLoginType) {
        if (Objects.requireNonNull(socialLoginType) != Provider.GOOGLE)
            throw new UnknownProviderException(
                    String.format("'%s'은 알 수 없는 소셜 로그인 형식입니다.", socialLoginType)
            );
    }

    private void ifYouDntHvMmbSv(String email, GoogleUser googleUser) {
        if (memberRepository.findByEmail(email).isPresent()) return;
        memberRepository.save(MemberRequest.from(
                email,
                passwordEncoder.encode(googlePassword),
                googleUser.getName(),
                googleUser.getPicture(),
                Provider.GOOGLE)
        );
    }

    public String generateToken(String email) {
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(new UsernamePasswordAuthenticationToken(email, googlePassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication).getAccessToken();
    }
}
