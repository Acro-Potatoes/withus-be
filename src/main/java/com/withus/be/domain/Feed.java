package com.withus.be.domain;

import com.withus.be.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
//@ToString(exclude = {"member"})
@NoArgsConstructor
@EqualsAndHashCode(of={"feed_id"})
@Entity
@Table(name = "feed")
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long feedId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 3000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private  Member member;

    @OneToMany(mappedBy = "feed")
    private List<FeedLike> feedLikes = new ArrayList<>();

    @OneToMany(mappedBy = "feed")
    private List<FeedReply> feedReplyList = new ArrayList<>();


//    @Column(nullable = false)
    private int likeCount;

    //좋아요 누른 멤버에 대한 리스트 필요

//    @CreationTimestamp
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @CreationTimestamp
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
}
