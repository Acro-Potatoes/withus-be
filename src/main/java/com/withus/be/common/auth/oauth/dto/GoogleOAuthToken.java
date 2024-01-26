package com.withus.be.common.auth.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GoogleOAuthToken {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
    private String refresh_token;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class GoogleOAuthResponse {
        private String jwtToken;
        private String accessToken;
        private String tokenType;
    }

}
