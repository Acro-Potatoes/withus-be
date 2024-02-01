package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Feed;
import com.withus.be.domain.FeedLike;
import com.withus.be.domain.Member;
import com.withus.be.repository.FeedLikeRepository;
import com.withus.be.repository.FeedRepository;
import com.withus.be.repository.MemberRepository;
import com.withus.be.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedLikeService {
    private final FeedLikeRepository feedLikeRepository;
    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;

    private final FeedService feedService;

    public void handleLike(Long feedId){

        Optional<String> currentEmail = SecurityUtil.getCurrentEmail();
        if (currentEmail.isPresent()) {
            Optional<Member> memberOptional = memberRepository.findByEmail(currentEmail.get());
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();

            }
        }
        Feed feed = feedService.getFeed(feedId);
        FeedLike feedLike = feedLikeRepository.findByFeed(feed);

        if(feedLike == null){
            feedLike = FeedLike.builder()
//                    .member(member)
                    .feed(feed)
                    .build();
            feedLikeRepository.save(feedLike);

            //좋아요 수 증가
            feed.setLikeCount(feed.getLikeCount() + 1);
            log.info("멤버가 좋아요 클릭 ->{}", feed.getLikeCount());
        }else{
            //좋아요 취소
            feedLikeRepository.delete(feedLike);
            log.info("멤버가 좋아요 취소 ->{}", feed.getLikeCount());
            feed.setLikeCount(feed.getLikeCount() - 1);
        }
        feedRepository.save(feed);

    }

    public boolean checkIfLiked(Long feedId) {
        Optional<Member> member = memberRepository.findByEmail(SecurityUtil.getCurrentEmail().get());
        if(member.isPresent()){
            FeedLike feedLike = feedLikeRepository.findByMemberAndFeed();
            System.out.println("좋아요 여부 = " + feedLike);
            // feedLike가 null이 아니라면 좋아요 선택
            System.out.println(feedLike != null);
            return feedLike != null;
        }else{
            return false;
        }

    }
}
