package com.withus.be.service;

import com.withus.be.domain.FeedReply;
import com.withus.be.dto.FeedDto.FeedRelyResponse;
import com.withus.be.dto.FeedDto.FeedReplyInsertRequest;
import com.withus.be.dto.FeedDto.FeedReplyModifyRequest;
import com.withus.be.repository.FeedReplyRepository;
import com.withus.be.repository.FeedRepository;
import com.withus.be.repository.MemberRepository;
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
public class FeedReplyService {

    private final FeedReplyRepository feedReplyRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;


    public List<FeedRelyResponse> getList(final Long replyId) {
        List<FeedReply> replyList = feedReplyRepository.findAllById(Collections.singleton(replyId));


        List<FeedRelyResponse> replyDtoList = replyList.stream()
                .map(FeedRelyResponse::new)
                .collect(Collectors.toList());
//        List<FeedRelyResponse> replyDtoList = ;

//        return replyList.stream().map(FeedRelyResponse::of).collect(Collectors.toList());
        return replyDtoList;
    }

    public void writeReply(FeedReplyInsertRequest dto) {
        //토큰관련 처리 필요
//        Long memberId = Long.valueOf(Member)
//        Member member = memberRepository.findById(memberId);

//        Feed feed = feedRepository.findById()

        FeedReply feedReply = FeedReplyInsertRequest.builder().feedId(dto.getFeedId())
                .content(dto.getContent())
                .build().toEntity();

//        feedRepository.save(feed);
        feedReplyRepository.save(feedReply);
    }

    public String modify(FeedReplyModifyRequest dto) {
        Optional<FeedReply> feedReply = feedReplyRepository.findById(dto.getFeedId());
        FeedReply feedReply1 = feedReply.orElseThrow();

        feedReply1.setReplyContent(dto.getContent());
        feedReplyRepository.save(feedReply1);

        return "성공";
    }

    public void delete(Long replyId) {
        feedReplyRepository.deleteById(replyId);
    }
}
