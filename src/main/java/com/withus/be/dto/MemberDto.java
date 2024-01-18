package com.withus.be.dto;

import com.withus.be.domain.Member;
import com.withus.be.domain.constant.Authority;
import com.withus.be.domain.constant.Provider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
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
