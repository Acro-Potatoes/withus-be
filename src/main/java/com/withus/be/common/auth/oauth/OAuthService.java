package com.withus.be.common.auth.oauth;

import com.withus.be.common.auth.oauth.dto.GoogleOAuthToken;
import com.withus.be.common.auth.oauth.dto.GoogleUser;
import com.withus.be.domain.constant.Provider;
import com.withus.be.dto.MemberDto.MemberRequest;
import com.withus.be.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOAuth googleOauth;
    private final HttpServletResponse response;
    private final MemberRepository memberRepository;

    public void request(Provider provider) throws IOException {
        String redirectURL;
        switch (provider) {
            case GOOGLE: {
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL = googleOauth.getOAuthRedirectURL();
            }
            break;
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }

        response.sendRedirect(redirectURL);
    }

    public GoogleOAuthToken requestOAuthLogin(Provider socialLoginType, String code, HttpServletRequest request) throws Exception {

        switch (socialLoginType) {
            case GOOGLE: {
                // 구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                // 응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담음
                GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);

                // 액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아옴
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                // 다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);

                log.info("Google User : {}", googleUser);

                String email = googleUser.getEmail();
                if (memberRepository.findByEmail(email).isEmpty()) {
                    memberRepository.save(MemberRequest.from(
                            email,
                            googleUser.getName(),
                            googleUser.getPicture(),
                            Provider.GOOGLE)
                    );
                }

                return oAuthToken;
            }
            default:
                throw new RuntimeException();
        }
    }
}
