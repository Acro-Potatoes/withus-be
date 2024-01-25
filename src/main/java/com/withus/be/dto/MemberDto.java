package com.withus.be.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.withus.be.domain.Member;
import com.withus.be.domain.constant.Authority;
import com.withus.be.domain.constant.Provider;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRequest {
        @NotNull
        @Size(min = 3, max = 50)
        private String email;

        @NotNull
        @Size(min = 3, max = 100)
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;

        @NotNull
        @Size(min = 3, max = 50)
        private String nickname;

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Authority authority;

        public static Member from(MemberRequest member, String password) {
            if (member == null) return null;

            return Member.builder()
                    .email(member.getEmail())
                    .password(password)
                    .nickname(member.getNickname())
                    .profileImage("")   // TODO 있으면 넣고 없으면 기본 이미지 넣기
                    .activated(true)
                    .authority(Authority.ROLE_USER)
                    .provider(Provider.DEFAULT)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberResponse {
        private Long id;
        private String name;
        private String email;
        private String nickname;
        private String profileImage;
        private boolean activated;
        private Authority authority;
        private Provider provider;

        public static MemberResponse of(Member member) {
            return MemberResponse.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .activated(member.isActivated())
                    .authority(member.getAuthority())
                    .provider(member.getProvider())
                    .build();
        }
    }
}
