package com.withus.be.controller;

import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.withus.be.dto.FeedDto;
import com.withus.be.dto.FeedDto.FeedRelyResponse;
import com.withus.be.dto.FeedDto.FeedReplyInsertRequest;
import com.withus.be.dto.FeedDto.FeedReplyModifyRequest;
import com.withus.be.service.FeedReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
        log.info("POST : /withus/reply/write - QNA {}번 게시글에 '{}' 댓글 작성", dto.getId(), dto.getContents());

        feedReplyService.writeReply(dto);
        List<FeedRelyResponse> list = feedReplyService.getList(dto.getId());

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/delete/{replyId}")
    public ResponseEntity<?> deleteReply(
            @PathVariable Long replyId
    ) {
//        log.info("DELETE : /qna-reply/delete/{} - QNA 댓글 삭제", replyIdx);

        feedReplyService.delete(replyId);

        return ResponseEntity.ok().body("댓글 삭제 성공");
    }

    @PatchMapping("/modify")
    public ResponseEntity<?> modifyReply(
            @RequestBody FeedReplyModifyRequest dto
    ) {
//        log.info("PATCH : /qna-reply/modify/{} - QNA 댓글 수정", dto.getReplyIdx());


        String  message =  feedReplyService.modify(dto);

        return ResponseEntity.ok().body(message);
    }


}
