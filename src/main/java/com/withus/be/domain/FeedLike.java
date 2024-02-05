package com.withus.be.domain;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
//@ToString(exclude = {"member","feed"})
//@EqualsAndHashCode(of = {"like_id"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feed_like")
public class FeedLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    private  Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id",nullable = false)
    private Feed feed;

}
