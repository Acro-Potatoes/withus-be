package com.withus.be.controller;

import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.dto.FeedDto.FeedModifyRequest;
import com.withus.be.dto.FeedDto.FeedResponse;
import com.withus.be.dto.FeedDto.FeedsWriteRequest;
import com.withus.be.repository.MemberRepository;
import com.withus.be.service.FeedLikeService;
import com.withus.be.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;
    private final FeedLikeService feedLikeService;
    private final MemberRepository memberRepository;

    @GetMapping("/list")
    public ResponseEntity<?> list(){
        log.info("/feeds/list");
        List<FeedResponse> feedResponses = feedService.getList();
        return new ResponseSuccess().success(feedResponses);
    }

    //최신순으로 피드 조회
    @GetMapping("/listdesc")
    public ResponseEntity<?> listDesc(){
        log.info("/feeds/listdesc");
        List<FeedResponse> feedResponses = feedService.getListDateDesc();
        return new ResponseSuccess().success(feedResponses);
    }

    //키워드 검색
    @GetMapping("/search")
    public ResponseEntity<?> keywordList(@RequestParam("keyword") String keyword){
        log.info("/feeds/search?keyword ={}",keyword);
        List<FeedResponse> feedResponses = feedService.getKeyword(keyword);
        return new ResponseSuccess().success(feedResponses);

    }

    //피드 생성
    @PostMapping("/write") //List로 반환하는게 맞는지 확인
    public ResponseEntity<?> write(@Validated @RequestBody FeedsWriteRequest request){

        log.info("POST: /feeds/write - 피드 생성 {}", request);
        //에러처리 필요
        List<FeedResponse> feedResponse = feedService.write(request);
        return new ResponseSuccess().success(feedResponse);
    }

    //피드 수정
    @RequestMapping(value = "/modify",method = {RequestMethod.PUT,RequestMethod.PATCH})
    public ResponseEntity<?> modify(
            @Validated @RequestBody FeedModifyRequest dto
    ){
        log.info("/feeds/modify - 피드 수정{}",dto);

        List<FeedResponse> responseDTO = feedService.modify(dto);
        return new ResponseSuccess().success(responseDTO);
    }

    //피드 삭제
    @DeleteMapping(value = "/delete/{Id}")
    public ResponseEntity<?> delete(@PathVariable("Id") Long feedId){
        log.info("/feeds/delete - 피드 삭제{}",feedId);
        feedService.delete(feedId);
        return new ResponseSuccess().success("삭제 성공!!");
    }

    /**
     * 피드 - 좋아요 기능
     * 한 게시물에 (회원당 ) 좋아요 1번만 가능
     *
//     * @param tokenUserInfo : 로그인 중인 유저의 정보
     * @param feedId : 좋아요 누른 게시글 번호
     */

    @PostMapping("/like/{Id}")
    public ResponseEntity<?> handleLike(
            @PathVariable("Id") Long feedId
    ){
        log.info("like click : {}번 피드 좋아요 누름!",feedId);
        boolean isLiked = feedLikeService.checkIfLiked(feedId);
        feedLikeService.handleLike(feedId);
        System.out.println("isLiked =  "+ isLiked);

        if(isLiked){
            return ResponseEntity.ok().body("좋아요 취소");
        }else{
            return ResponseEntity.ok().body("좋아요 +1");
        }
    }









}
