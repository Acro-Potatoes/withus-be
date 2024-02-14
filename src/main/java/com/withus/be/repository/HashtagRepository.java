package com.withus.be.repository;

import com.withus.be.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<HashTag, Long> {

    HashTag findByFeedIdAndHashtagContent(Long id, String newHashtag);
}
