package com.withus.be.repository;

import com.withus.be.domain.FeedReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedReplyRepository extends JpaRepository<FeedReply, Long> {

    List<FeedReply> findByFeedId(Long feedId);
}
