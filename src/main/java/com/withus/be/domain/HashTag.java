package com.withus.be.domain;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feed_hashtag")
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hashtag_content")
    private String hashtagContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id") //FK
    private Feed feed;



}
