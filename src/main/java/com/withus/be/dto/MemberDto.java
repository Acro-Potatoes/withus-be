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
        private String name;
        private String nickname;
        private String phoneNum;
        private String profileImage;
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Authority authority;

        public static Member from(MemberRequest memberRequest, String password, String profileUrl) {
            if (memberRequest == null) return null;

            return Member.builder()
                    .email(memberRequest.getEmail())
                    .password(password)
                    .name(memberRequest.getName())
                    .nickname(memberRequest.getNickname())
                    .phoneNum(memberRequest.getPhoneNum())
                    .profileImage(
                            memberRequest.getProfileImage() == null || memberRequest.getProfileImage().isEmpty()
                                    ? profileUrl : memberRequest.getProfileImage()
                    )
                    .activated(true)
                    .authority(Authority.ROLE_USER)
                    .provider(Provider.DEFAULT)
                    .build();
        }

        public static Member from(String email, String password, String name, String image, Provider provider) {
            return Member.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .nickname("")
                    .phoneNum("")
                    .profileImage(image)
                    .activated(true)
                    .authority(Authority.ROLE_USER)
                    .provider(provider)
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
        private String phoneNum;
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
                    .phoneNum(member.getPhoneNum())
                    .profileImage(member.getProfileImage())
                    .activated(member.isActivated())
                    .authority(member.getAuthority())
                    .provider(member.getProvider())
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PasswordRequest {
        private String email;;
        private String newPassword;
        private String newPasswordRe;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhoneNumRequest {
        @NotNull
        private String phoneNum;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhoneNumCertRequest {
        @NotNull
        private String phoneNum;
        @NotNull
        private String certNum;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhoneNumCertResponse {
        private String email;
        private String msg;
    }

}
