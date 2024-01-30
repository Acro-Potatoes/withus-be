package com.withus.be.controller;

import com.withus.be.dto.FeedDto.FeedRelyResponse;
import com.withus.be.dto.FeedDto.FeedReplyInsertRequest;
import com.withus.be.dto.FeedDto.FeedReplyModifyRequest;
import com.withus.be.service.FeedReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/withus/reply")
public class FeedReplyController {

    private final FeedReplyService feedReplyService;

    @GetMapping("/{replyId}")
    public ResponseEntity<?> list(@PathVariable Long replyId){
        log.info("/withus/reply/{} - 댓글 전체 보기", replyId );

        List<FeedRelyResponse> list = feedReplyService.getList(replyId);

        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeReply(
            @RequestBody FeedReplyInsertRequest dto
    ) {
        log.info("/withus/reply/write - {}번 피드에 '{}' 댓글 작성", dto.getFeedId(), dto.getContent());

        feedReplyService.writeReply(dto);
        List<FeedRelyResponse> list = feedReplyService.getList(dto.getFeedId());

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<?> deleteReply(
            @PathVariable Long replyId
    ) {
        log.info("DELETE : withus/reply/delete/{} - 댓글 삭제", replyId);

        feedReplyService.delete(replyId);

        return ResponseEntity.ok().body("댓글 삭제 성공");
    }

    @PatchMapping("/modify")
    public ResponseEntity<?> modifyReply(
            @RequestBody FeedReplyModifyRequest dto
    ) {
        log.info("witus/reply/modify/{} - 댓글 수정", dto.getFeedId());

        String  message =  feedReplyService.modify(dto);

        return ResponseEntity.ok().body(message);
    }


}
