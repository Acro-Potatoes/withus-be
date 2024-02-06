package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.dto.FeedDto.FeedModifyRequest;
import com.withus.be.dto.FeedDto.FeedResponse;
import com.withus.be.dto.FeedDto.FeedsWriteRequest;
import com.withus.be.service.FeedLikeService;
import com.withus.be.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Feed Controller", description = "피드 관련 컨트롤러")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;
    private final FeedLikeService feedLikeService;

    @Operation(summary = "피드 list API")
    @GetMapping("/list")
    public ResponseEntity<Body> list(){
        log.info("/feeds/list");
        List<FeedResponse> feedResponses = feedService.getList();
        return new ResponseSuccess().success(feedResponses);
    }

    //최신순으로 피드 조회
    @Operation(summary = "피드 최신순 조회 API")
    @GetMapping("/listdesc")
    public ResponseEntity<Body> listDesc(){
        log.info("/feeds/listdesc");
        List<FeedResponse> feedResponses = feedService.getListDateDesc();
        return new ResponseSuccess().success(feedResponses);
    }

    //키워드 검색
    @Operation(summary = "피드 키워드 검색 API")
    @GetMapping("/search")
    public ResponseEntity<Body> keywordList(@RequestParam("keyword") String keyword){
        log.info("/feeds/search?keyword ={}",keyword);
        List<FeedResponse> feedResponses = feedService.getKeyword(keyword);
        return new ResponseSuccess().success(feedResponses);

    }

    //피드 생성
    @Operation(summary = "피드 생성 API")
    @PostMapping("/write")
    public ResponseEntity<Body> write(@Validated @RequestBody FeedsWriteRequest request){

        log.info("POST: /feeds/write - 피드 생성 {}", request);
        List<FeedResponse> feedResponse = feedService.write(request);
        return new ResponseSuccess().success(feedResponse);
    }

    //피드 수정
    @Operation(summary = "피드 수정 API")
    @RequestMapping(value = "/modify",method = {RequestMethod.PUT,RequestMethod.PATCH})
    public ResponseEntity<Body> modify(@Validated @RequestBody FeedModifyRequest dto){
        log.info("/feeds/modify - 피드 수정{}",dto);

        List<FeedResponse> responseDTO = feedService.modify(dto);
        return new ResponseSuccess().success(responseDTO);
    }

    //피드 삭제
    @Operation(summary = "피드 삭제 API")
    @DeleteMapping(value = "/delete/{Id}")
    public ResponseEntity<Body> delete(@PathVariable("Id") Long feedId){
        log.info("/feeds/delete - 피드 삭제{}",feedId);
        feedService.delete(feedId);
        return new ResponseSuccess().success(feedId +"번 피드 삭제 성공");
    }

    //좋아요
    @Operation(summary = "피드 좋아요 API")
    @PostMapping("/like/{Id}")
    public ResponseEntity<Body> handleLike(
            @PathVariable("Id") Long feedId
    ){
        log.info("like click : {}번 피드 좋아요 누르기",feedId);
        boolean isLiked = feedLikeService.checkIfLiked(feedId);
        feedLikeService.handleLike(feedId);

        if(isLiked){
            return new ResponseSuccess().success(feedId + "번 피드 좋아요 취소");
        }else{
            return new ResponseSuccess().success(feedId +"번 피드 좋아요");
        }
    }









}
