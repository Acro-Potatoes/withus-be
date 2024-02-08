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
import com.withus.be.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeedReplyService {

    private final FeedReplyRepository feedReplyRepository;
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;

    //피드별 전체 댓글 조회
    public List<FeedRelyResponse> getList(Long feedId) {
        //해당피드가 있는지 확인하기
        Optional<Feed> feedOptional = feedRepository.findById(feedId);
        if (feedOptional.isPresent()) {
            List<FeedReply> replyList = feedReplyRepository.findByFeedId(feedId);
            return replyList.stream()
                    .map(FeedRelyResponse::new)
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException();
        }

    }

    //댓글 생성
    public void writeReply(FeedReplyInsertRequest dto) {

        String currentEmail = SecurityUtil.getCurrentEmail().orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(currentEmail).orElseThrow(EntityNotFoundException::new);

        Feed feed = feedRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);

        FeedReply feedReply = FeedReplyInsertRequest.builder().id(dto.getId())
                .replyContent(dto.getReplyContent())
                .build().toEntity(member, feed);

        feedReplyRepository.save(feedReply);
        log.info("{}가 {}번 피드에 \"{}\"댓글 작성", feedReply.getReplyWriter(), feed.getId(), feedReply.getReplyContent());
    }

    //댓글 수정
    public String modify(FeedReplyModifyRequest dto) {
        FeedReply feedReply = feedReplyRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        feedReply.update(dto.getReplyContent());
        return "댓글 수정 성공";
    }

    //댓글 삭제
    public void delete(Long replyId) {
        feedReplyRepository.deleteById(replyId);
    }
}
