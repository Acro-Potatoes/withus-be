package com.withus.be.repository;

import com.withus.be.domain.Feed;
import com.withus.be.domain.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike,Long> {

    FeedLike findByFeed(Feed feed);
}
