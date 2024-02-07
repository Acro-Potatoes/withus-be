package com.withus.be.domain;

import com.withus.be.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feed_reply")
public class FeedReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reply_content", nullable = false, length = 1000)
    private String replyContent;

    @Column(name = "reply_writer", nullable = false, length = 30)
    private String replyWriter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id") // FK
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK
    private Member member;

    public void update(String replyContent) {
        this.replyContent = replyContent;
    }
}
