package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeedService {
    private final FeedRepository feedRepository;
    private  final MemberRepository memberRepository;


    //리스트 가져오기
    public List<FeedResponse> getList() {
        List<Feed> feed = feedRepository.findAll();
        return feed.stream().map(FeedResponse::of).collect(Collectors.toList());
    }

    //최신순 조회
    public List<FeedResponse> getListDateDesc(){
        List<Feed> feeds = feedRepository.findByDate();
        return  feeds.stream().map(FeedResponse::of).collect(Collectors.toList());
    }


    //피드 키워드 검색
    public List<FeedResponse> getKeyword(String keyword) {
        List<Feed> feeds = feedRepository.findByKeyword(keyword);
        if (feeds.isEmpty()){
            throw new EntityNotFoundException(keyword +"이란 단어가 존재하지 않음");
        }
        return feeds.stream().map(FeedResponse::of).collect(Collectors.toList());
    }

    //피드 생성
    public List<FeedResponse> write(FeedsWriteRequest request) {
        String currentEmail = SecurityUtil.getCurrentEmail().orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(currentEmail).orElseThrow(EntityNotFoundException::new);
        feedRepository.save(request.toEntity(member));
        return getList();
    }

    //피드 수정
    public List<FeedResponse> modify(FeedModifyRequest dto) {
        Feed feed = feedRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("피드가 존재하지 않음"));
        feed.setTitle(dto.getTitle());
        feed.setContent(dto.getContent());
        feedRepository.save(feed);
        return getList();
    }

    //피드 삭제
    public void delete(Long feedId) {
        Feed feeds = feedRepository.findById(feedId).orElseThrow(()->new EntityNotFoundException(feedId+"번 피드 존재하지 않음"));
        feedRepository.delete(feeds);
    }


    public Feed getFeed(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException( feedId +"번 게시물이 존재하지 않습니다."));
    }
}
