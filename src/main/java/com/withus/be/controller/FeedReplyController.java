package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.dto.FeedReplyDto.FeedRelyResponse;
import com.withus.be.dto.FeedReplyDto.FeedReplyInsertRequest;
import com.withus.be.dto.FeedReplyDto.FeedReplyModifyRequest;
import com.withus.be.dto.FeedReplyDto.FeedRereplyInsertRequest;
import com.withus.be.service.FeedReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Feed Reply Controller", description = "피드댓글 관련 컨트롤러")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/feeds-reply")
public class FeedReplyController {

    private final FeedReplyService feedReplyService;

    @Operation(summary = "피드별 댓글 list API")
    @GetMapping("/{Id}")
    public ResponseEntity<Body> list(@PathVariable("Id") Long feedId) {
        List<FeedRelyResponse> list = feedReplyService.getList(feedId);
        log.info("/feeds/reply/{} - {}번 피드 댓글 전체 보기", feedId, feedId);

        return new ResponseSuccess().success(list);
    }
    
    @Operation(summary = "댓글 생성 API")
    @PostMapping("/write")
    public ResponseEntity<Body> writeReply(@RequestBody FeedReplyInsertRequest dto) {
        feedReplyService.writeReply(dto);
        log.info("/feeds/reply/write - {}번 피드에 \"{}\" 댓글 작성", dto.getId(), dto.getReplyContent());
        return new ResponseSuccess().success("댓글 작성 완료");
    }

    @Operation(summary = "댓글 삭제 API")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<Body> deleteReply(
            @PathVariable("Id") Long replyId
    ) {
        log.info("DELETE : feeds/reply/delete/ {}번댓글 삭제", replyId);
        feedReplyService.delete(replyId);
        return new ResponseSuccess().success("댓글 삭제 성공");
    }

    @Operation(summary = "댓글 수정 API")
    @PatchMapping("/modify")
    public ResponseEntity<Body> modifyReply(@Validated @RequestBody FeedReplyModifyRequest dto) {
        String message = feedReplyService.modify(dto);
        log.info("feeds/reply/modify/{} - 댓글 수정 내용: {}", dto.getId(), dto.getReplyContent());
        return new ResponseSuccess().success(message);
    }

    @Operation(summary = "대댓글 생성 API")
    @PostMapping("/replywrite")
    public ResponseEntity<Body> writeRereply(@RequestBody FeedRereplyInsertRequest dto) {
        feedReplyService.writeRereply(dto);
        log.info("/feeds/reply/replywrite - {}번 댓글에 \"{}\" 대댓글 작성", dto.getParentId(), dto.getReplyContent());
        return new ResponseSuccess().success("대댓글 작성 완료");
    }

    @Operation(summary = "대댓글 삭제 API")
    @DeleteMapping("/replydelete/{Id}")
    public ResponseEntity<Body> deleteRereply(
            @PathVariable("Id") Long replyId
    ) {
        log.info("DELETE : feeds/reply/delete/ {}번대댓글 삭제", replyId);
        feedReplyService.deleteRereply(replyId);
        return new ResponseSuccess().success("댓글 삭제 성공");
    }

    @Operation(summary = "대댓글 수정 API")
    @PatchMapping("/replymodify")
    public ResponseEntity<Body> modifyRereply(@Validated @RequestBody FeedReplyModifyRequest dto) {
        String message = feedReplyService.modifyRereply(dto);
        log.info("feeds/reply/modify/{} - 댓글 수정 내용: {}", dto.getId(), dto.getReplyContent());
        return new ResponseSuccess().success(message);
    }






}
