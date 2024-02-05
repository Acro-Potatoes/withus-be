package com.withus.be.domain;

import com.withus.be.common.BaseEntity;
import com.withus.be.domain.constant.Authority;
import com.withus.be.domain.constant.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 20)
    private String nickname;

    @Column(length = 20, unique = true)
    private String phoneNum;

    private String profileImage;

    private boolean activated;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<FeedReply> feedReplyList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<FeedLike> feedLikes = new ArrayList<>();

    public void changePassword(String password) {
        this.password = password;
    }

}
