package com.withus.be.domain;

import com.withus.be.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feed")
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 3000, nullable = false)
    private String content;

    @Column(length = 100,nullable = false)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<FeedLike> feedLikes = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<FeedReply> feedReplyList = new ArrayList<>();

    //Like
    private int likeCount;

    //좋아요 누른 멤버 리스트
    @OneToMany(mappedBy = "feed", orphanRemoval = true)
    @Builder.Default
    private List<FeedLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<HashTag> hashTags = new ArrayList<>();

    public void update(String feedTitle, String feedContent) {
        this.title = feedTitle;
        this.content = feedContent;
    }

    public void countlike(int LikeCount){
        this.likeCount = LikeCount;
    }

}
