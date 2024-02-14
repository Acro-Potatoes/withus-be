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
            feedLike = FeedLike.builder()
                    .member(member)
                    .feed(feed)
                    .build();
            feedLikeRepository.save(feedLike);

            //좋아요 수 증가
            feed.countlike(feed.getLikeCount() + 1);
            log.info("멤버 {}가 좋아요 클릭 ->{}", member.getEmail(),feed.getLikeCount());
            return true;
        }else{
            //좋아요 취소
            feedLikeRepository.delete(feedLike);
            feed.countlike(feed.getLikeCount() - 1);
            log.info("멤버 {}가 좋아요 취소 ->{}", member.getEmail(),feed.getLikeCount());
            return false;
        }
    }

//    public boolean checkIfLiked(Long feedId) {
//        String currentEmail = SecurityUtil.getCurrentEmail().orElseThrow(EntityNotFoundException::new);
//        Member member = memberRepository.findByEmail(currentEmail).orElseThrow(EntityNotFoundException::new);
//
//        Feed feed = feedRepository.findById(feedId).orElseThrow(EntityNotFoundException::new);
//        FeedLike feedLike = feedLikeRepository.findByMemberAndFeed(member,feed);
//
//        // feedLike가 null이 아니라면 좋아요 선택
//        return feedLike != null;
//
//    }
}
