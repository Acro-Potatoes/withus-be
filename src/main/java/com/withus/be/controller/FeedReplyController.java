package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.dto.FeedDto.FeedRelyResponse;
import com.withus.be.dto.FeedDto.FeedReplyInsertRequest;
import com.withus.be.dto.FeedDto.FeedReplyModifyRequest;
import com.withus.be.service.FeedReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/feeds-reply")
public class FeedReplyController {

    private final FeedReplyService feedReplyService;

    @GetMapping("/{Id}")
    public ResponseEntity<Body> list(@PathVariable("Id") Long feedId) {
        List<FeedRelyResponse> list = feedReplyService.getList(feedId);
        log.info("/feeds/reply/{} - {}번 피드 댓글 전체 보기", feedId, feedId);

        return new ResponseSuccess().success(list);
    }

    @PostMapping("/write")
    public ResponseEntity<Body> writeReply(@RequestBody FeedReplyInsertRequest dto) {
        feedReplyService.writeReply(dto);
        log.info("/feeds/reply/write - {}번 피드에 \"{}\" 댓글 작성", dto.getId(), dto.getReplyContent());
        return new ResponseSuccess().success("댓글 작성 완료");
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<Body> deleteReply(
            @PathVariable("Id") Long replyId
    ) {
        log.info("DELETE : feeds/reply/delete/ {}번댓글 삭제", replyId);
        feedReplyService.delete(replyId);
        return new ResponseSuccess().success("댓글 삭제 성공");
    }

    @PatchMapping("/modify")
    public ResponseEntity<Body> modifyReply(@Validated @RequestBody FeedReplyModifyRequest dto) {
        String message = feedReplyService.modify(dto);
        log.info("feeds/reply/modify/{} - 댓글 수정 내용: {}", dto.getId(), dto.getReplyContent());
        return new ResponseSuccess().success(message);
    }

}




