package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.common.exception.InvalidTokenException;
import com.withus.be.domain.Feed;
import com.withus.be.domain.Member;
import com.withus.be.dto.FeedDto.FeedModifyRequest;
import com.withus.be.dto.FeedDto.FeedResponse;
import com.withus.be.dto.FeedDto.FeedsWriteRequest;
import com.withus.be.repository.FeedRepository;
import com.withus.be.repository.MemberRepository;
import com.withus.be.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeedService {

//    private SecurityUtil securityUtil;
    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;


    //리스트 가져오기
    public List<FeedResponse> getList() {
        Optional<String> currentEmail = SecurityUtil.getCurrentEmail();
        if(currentEmail.isPresent()){
            Member member = memberRepository.findByEmail(currentEmail.get()).orElseThrow(
                    () -> new EntityNotFoundException("없는 이메일입니다.")
            );

            List<Feed> feed = member.getFeeds();

            return feed.stream().map(FeedResponse::of).collect(Collectors.toList());
        }else{
            throw new InvalidTokenException("해당하는 이메일이 없습니다");

        }
    }

    //최신순 조회
    public List<FeedResponse> getListDateDesc(){
        List<Feed> feeds = feedRepository.findByDate();
        return  feeds.stream().map(FeedResponse::of).collect(Collectors.toList());
    }


    //피드 키워드 검색
    public List<FeedResponse> getKeyword(String keyword) {
        List<Feed> feeds = feedRepository.findByKeyword(keyword);
        return feeds.stream().map(FeedResponse::of).collect(Collectors.toList());
    }

    //피드 생성
    public List<FeedResponse> write(FeedsWriteRequest request) {
        try {
            //멤버넣기
            feedRepository.save(request.toEntity());
            return getList();
        } catch (Exception e){
            log.error("실패!!");
            return Collections.emptyList();
        }
    }

    //피드 수정
    public List<FeedResponse> modify(FeedModifyRequest dto) {
        Optional<Feed> optionalFeed = feedRepository.findById(dto.getFeedId());
        Feed feed = optionalFeed.orElseThrow();

        feed.setTitle(dto.getTitle());
        feed.setContent(dto.getContent());

        feedRepository.save(feed);

        return getList();
    }

    //피드 삭제
    public void delete(Long id) {
        Feed feeds = feedRepository.findById(id).orElseThrow();
        feedRepository.delete(feeds);
    }


    public Feed getFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new RuntimeException( feedId +"번 게시물이 존재하지 않습니다."));
    }
}
