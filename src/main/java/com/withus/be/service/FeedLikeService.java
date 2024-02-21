package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Feed;
import com.withus.be.domain.FeedLike;
import com.withus.be.domain.Member;
import com.withus.be.repository.FeedLikeRepository;
import com.withus.be.repository.FeedRepository;
import com.withus.be.repository.MemberRepository;
import com.withus.be.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeRepository feedLikeRepository;
    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;

    public boolean handleLike(Long feedId){

        String currentEmail = SecurityUtil.getCurrentEmail().orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(currentEmail).orElseThrow(EntityNotFoundException::new);

        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException(feedId + "번 게시물이 존재하지 않습니다."));
        FeedLike feedLike = feedLikeRepository.findByFeed(feed);

        if(feedLike == null){
            log.info("멤버 {}가 좋아요 클릭 ->{}", member.getEmail(),feed.getLikeCount());
            return upLike(feed,member);
        }
        //좋아요 취소
        log.info("멤버 {}가 좋아요 취소 ->{}", member.getEmail(),feed.getLikeCount());
        return cancelLIke(feedLike,feed);
    }

    private boolean upLike(Feed feed, Member member){

        FeedLike feedLike = FeedLike.builder()
                .member(member)
                .feed(feed)
                .build();
        feedLikeRepository.save(feedLike);

        //좋아요 수 증가
        feed.countlike(feed.getLikeCount() + 1);
        return true;
    }

    private boolean cancelLIke(FeedLike feedLike,Feed feed){
        feedLikeRepository.delete(feedLike);
        feed.countlike(feed.getLikeCount() - 1);
        return false;
    }







}
