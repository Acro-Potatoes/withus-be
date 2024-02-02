package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Feed;
import com.withus.be.domain.FeedReply;
import com.withus.be.domain.Member;
import com.withus.be.dto.FeedDto.FeedRelyResponse;
import com.withus.be.dto.FeedDto.FeedReplyInsertRequest;
import com.withus.be.dto.FeedDto.FeedReplyModifyRequest;
import com.withus.be.repository.FeedReplyRepository;
import com.withus.be.repository.FeedRepository;
import com.withus.be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedReplyService {

    private final FeedReplyRepository feedReplyRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;


    public List<FeedRelyResponse> getList(Long feedId) {
        List<FeedReply> replyList = feedReplyRepository.findByFeedFeedId(feedId);

        List<FeedRelyResponse> replyDtoList = replyList.stream()
                .map(FeedRelyResponse::new)
                .collect(Collectors.toList());
        return replyDtoList;
    }

    public void writeReply(FeedReplyInsertRequest dto, Member member) {
        Feed feed = feedRepository.findById(dto.getFeedId()).orElseThrow(EntityNotFoundException::new);

        FeedReply feedReply = FeedReplyInsertRequest.builder().feedId(dto.getFeedId())
                .replyContent(dto.getReplyContent())
                .build().toEntity(member, feed);
        feedReplyRepository.save(feedReply);
    }

    public String modify(FeedReplyModifyRequest dto) {
        FeedReply feedReply = feedReplyRepository.findById(dto.getReplyId()).orElseThrow(EntityNotFoundException::new);
        feedReply.setReplyContent(dto.getReplyContent());
        feedReplyRepository.save(feedReply);
        return "성공";
    }

    public void delete(Long replyId) {
        feedReplyRepository.deleteById(replyId);
    }
}
