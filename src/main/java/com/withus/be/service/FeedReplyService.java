package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Feed;
import com.withus.be.domain.FeedReply;
import com.withus.be.domain.Member;
import com.withus.be.dto.FeedReplyDto.FeedRelyResponse;
import com.withus.be.dto.FeedReplyDto.FeedReplyInsertRequest;
import com.withus.be.dto.FeedReplyDto.FeedReplyModifyRequest;
import com.withus.be.dto.FeedReplyDto.FeedRereplyInsertRequest;
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
        if (feedOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        List<FeedReply> replyList = feedReplyRepository.findByFeedId(feedId);
        return replyList.stream()
                .map(FeedRelyResponse::new)
                .collect(Collectors.toList());
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
        FeedReply feedReply = getReply(dto.getId());
        feedReply.update(dto.getReplyContent());
        return "댓글 수정 성공";
    }

    //댓글 삭제
    public void delete(Long replyId) {
        feedReplyRepository.deleteById(replyId);
    }

    //대댓글 삭제
    public void deleteRereply(Long replyId) {
        FeedReply feedReply = getReply(replyId);
        log.info(String.valueOf(feedReply.getComment()));
        if (feedReply.getComment() == null) throw new EntityNotFoundException("대댓글 삭제가 불가합니다!!");
        else feedReplyRepository.deleteById(replyId);
    }

    //대댓글 수정

    public String modifyRereply(FeedReplyModifyRequest dto) {
        FeedReply feedReply = getReply(dto.getId());
        feedReply.update(dto.getReplyContent());
        return "댓글 수정 성공";
    }

    //대댓글 생성
    public void writeRereply(FeedRereplyInsertRequest dto) {
        String currentEmail = SecurityUtil.getCurrentEmail().orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(currentEmail).orElseThrow(EntityNotFoundException::new);

        FeedReply feedReply_parent = getReply(dto.getParentId());
        Feed feed = feedRepository.findById(feedReply_parent.getFeed().getId()).orElseThrow(EntityNotFoundException::new);

        FeedReply feedReply = FeedRereplyInsertRequest.builder()
                .parentId(feedReply_parent.getId())
                .replyContent(dto.getReplyContent())
                .build().toEntity(member, feedReply_parent, feed);

        feedReplyRepository.save(feedReply);
        log.info("{}가 {}번 댓글에 \"{}\"댓글 작성", feedReply.getReplyWriter(), feedReply_parent.getId(), feedReply.getReplyContent());

    }

    //피드댓글 가져오기
    private FeedReply getReply(Long replyId){
        return feedReplyRepository.findById(replyId).orElseThrow(EntityNotFoundException::new);
    }

}
