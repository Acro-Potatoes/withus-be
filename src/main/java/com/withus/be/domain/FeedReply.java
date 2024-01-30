package com.withus.be.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString(exclude = {"feed","member"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feed_reply")
public class FeedReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "reply_content", nullable = false, length = 1000)
    private String replyContent;

    @Column(name = "reply_writer", nullable = false, length = 10)
    private String replyWriter;

    @CreationTimestamp
    @Column(name = "reply_date")
    private LocalDateTime replyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id") // FK
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id") // FK
    private Member member;




}
