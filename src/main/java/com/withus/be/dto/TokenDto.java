package com.withus.be.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;


    @Builder
    @Getter
    @AllArgsConstructor
    public static class RefreshDto {
        private String accessToken;
        private String email;
        private String authority;

        public static RefreshDto of(String accessToken, String email, String authority) {
            return RefreshDto.builder()
                    .accessToken(accessToken)
                    .email(email)
                    .authority(authority)
                    .build();
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        private String result;

        public static TokenResponse of(String accessToken, String refreshToken) {
            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .result("SUCCESS")
                    .build();
        }

        public static TokenResponse of(String content) {
            return TokenResponse.builder()
                    .result(content)
                    .build();
        }
    }

    public record OauthRefreshDto(String refreshToken) {
    }

}
