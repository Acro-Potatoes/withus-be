package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseFail;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.domain.Feed;
import com.withus.be.domain.Member;
import com.withus.be.dto.FeedDto.FeedRelyResponse;
import com.withus.be.dto.FeedDto.FeedReplyInsertRequest;
import com.withus.be.dto.FeedDto.FeedReplyModifyRequest;
import com.withus.be.repository.FeedRepository;
import com.withus.be.repository.MemberRepository;
import com.withus.be.service.FeedReplyService;
import com.withus.be.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.withus.be.common.response.Result.NOT_FOUND;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/feeds-reply")
public class FeedReplyController {

    private final FeedReplyService feedReplyService;
    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;

    @GetMapping("/{feedId}")
    public ResponseEntity<?> list(@PathVariable("feedId") Long feedId) {

        Optional<Feed> feedOptional = feedRepository.findById(feedId);
        if (feedOptional.isPresent()){
            List<FeedRelyResponse> list = feedReplyService.getList(feedId);
            log.info("/feeds/reply/{} - {}번 피드 댓글 전체 보기", feedId,feedId);

            return new ResponseSuccess().success(list);
        }else{
            return new ResponseFail(NOT_FOUND).fail();
        }
    }

    @PostMapping("/write")
    public ResponseEntity<Body> writeReply(
            @RequestBody FeedReplyInsertRequest dto
    ) {
        Optional<String> currentEmail = SecurityUtil.getCurrentEmail();
        if (currentEmail.isPresent()) {
            Optional<Member> memberOptional = memberRepository.findByEmail(currentEmail.get());
            if (memberOptional.isPresent()) {
                Member member = memberOptional.get();
                log.info("/feeds/reply/write - {}가 {}번 피드에 '{}' 댓글 작성", member.getNickname(), dto.getFeedId(), dto.getReplyContent());
                feedReplyService.writeReply(dto, member);
            }
        } else {
            return new ResponseFail(NOT_FOUND).fail();
        }
        return new ResponseSuccess().success("댓글 작성 완료 !!");
    }

    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<?> deleteReply(
            @PathVariable("replyId") Long replyId
    ) {
        log.info("DELETE : feeds/reply/delete/ {}번댓글 삭제", replyId);
        feedReplyService.delete(replyId);
        return new ResponseSuccess().success("댓글 삭제 성공!!");
    }

    @PatchMapping("/modify")
    public ResponseEntity<?> modifyReply(@RequestBody FeedReplyModifyRequest dto) {

        Optional<String> currentEmail = SecurityUtil.getCurrentEmail();
        if (currentEmail.isPresent()) {
            String message = feedReplyService.modify(dto);
            log.info("feeds/reply/modify/{} - 피드 댓글 수정 내용: {}", dto.getReplyId(),dto.getReplyContent());
            return new ResponseSuccess().success(message);
        } else {
            return new ResponseFail(NOT_FOUND).fail();
        }
    }

}




