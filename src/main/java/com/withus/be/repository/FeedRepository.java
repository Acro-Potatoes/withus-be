package com.withus.be.repository;

import com.withus.be.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed,Long > {

    //날짜별로 내림차순 정렬(최신순 정렬)
    @Query("SELECT f FROM Feed f ORDER BY f.created_at DESC")
    List<Feed> findByDate();

    //키워드 검색
    @Query("SELECT f FROM Feed f WHERE f.title LIKE %:keyword% OR f.content LIKE %:keyword% ORDER BY f.created_at DESC" )
    List<Feed> findByKeyword(@Param("keyword") String keyword);















}
