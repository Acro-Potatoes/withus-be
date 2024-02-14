package com.withus.be.domain;

import com.withus.be.common.BaseEntity;
import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    /*대댓글 self join*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_Id")
    private FeedReply comment;

    @OneToMany(mappedBy = "comment",cascade = CascadeType.ALL)
    private List<FeedReply> commentList = new ArrayList<>();
    /*self join end*/

    public void update(String replyContent) {
        this.replyContent = replyContent;
    }
}
