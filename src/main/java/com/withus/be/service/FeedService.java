package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Feed;
import com.withus.be.domain.HashTag;
import com.withus.be.domain.Member;
import com.withus.be.dto.FeedDto;
import com.withus.be.dto.FeedDto.FeedModifyRequest;
import com.withus.be.dto.FeedDto.FeedResponse;
import com.withus.be.dto.FeedDto.FeedsWriteRequest;
import com.withus.be.repository.FeedRepository;
import com.withus.be.repository.HashtagRepository;
import com.withus.be.repository.MemberRepository;
import com.withus.be.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeedService {
    private final FeedRepository feedRepository;
    private  final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;


    //리스트 가져오기
    public List<FeedResponse> getList() {
        List<Feed> feed = feedRepository.findAll();
        List<FeedResponse> feedResponses = new ArrayList<>();
        for (Feed feed1:feed){
            List<String> hashTagString = feed1.getHashTags()
                    .stream().map(HashTag::getHashtagContent)
                    .toList();

            FeedResponse feedResponse = FeedResponse.of(feed1, hashTagString);
            feedResponses.add(feedResponse);
        }
        return feedResponses;
    }

    //최신순 조회
    public List<FeedResponse> getListDateDesc(){
        List<Feed> feeds = feedRepository.findByDate();
        List<FeedResponse> feedResponses = new ArrayList<>();

        for (Feed feed1:feeds){
            List<String> hashTagString = feed1.getHashTags()
                    .stream().map(HashTag::getHashtagContent)
                    .toList();

            FeedResponse feedResponse = FeedResponse.of(feed1, hashTagString);
            feedResponses.add(feedResponse);
        }

        return feedResponses;

    }


    //피드 키워드 검색
    public List<FeedResponse> getKeyword(String keyword) {
        List<Feed> feeds = feedRepository.findByKeyword(keyword);
        List<FeedResponse> feedResponses = new ArrayList<>();

        if (feeds.isEmpty()){
            throw new EntityNotFoundException(keyword +"이란 단어가 존재하지 않음");
        }

        for (Feed feed1:feeds){
            List<String> hashTagString = feed1.getHashTags()
                    .stream().map(HashTag::getHashtagContent)
                    .toList();

            FeedResponse feedResponse = FeedResponse.of(feed1, hashTagString);
            feedResponses.add(feedResponse);
        }

        return feedResponses;
    }

    //피드 생성
    public List<FeedResponse> write(FeedsWriteRequest request) {
        String currentEmail = SecurityUtil.getCurrentEmail().orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(currentEmail).orElseThrow(EntityNotFoundException::new);
        Feed save = feedRepository.save(request.toEntity(member));



        createHashtag(save,request.getHashtagList());
        return getList();
    }

    //피드 수정
    @Transactional
    public List<FeedResponse> modify(FeedModifyRequest dto) {
        Feed feed =  getFeeds(dto.getId());
        feed.setTitle(dto.getTitle());
        feed.setContent(dto.getContent());

        //기존 해쉬태그
        List<HashTag> oldHashtag = feed.getHashTags();
        List<String> oldHashtag_toString = new ArrayList<>();

        //수정한 해쉬태그
        List<String> newHashtag = dto.getHashtagList();

        //기존 해쉬 태그리스트 String으로 변환하기
        for(HashTag hashTag: oldHashtag) oldHashtag_toString.add(hashTag.getHashtagContent());
//        List<String> hashtagRepositoryByFeedId = hashtagRepository.findByFeedId(dto.getId());

        //기존 해쉬태그 리스트와 수정한 해쉬태그 리스트 비교하기
        for ( String new_hashtag: newHashtag){
            //기존해쉬 태그에 새로운 단어가 존재하지않는다면 해쉬태그 추가 !
            if(!oldHashtag_toString.contains(new_hashtag)){
//                hashtagRepository.findByFeedIdAndString(dto.getId(),new_hashtag);
                saveHashtag(feed,new_hashtag);
            }

        }

        for ( String old_Hashtag: oldHashtag_toString){
            //새로운해쉬태그 리스트에 기존단어가 존재하지않는다면 리스트에서 해쉬태그 삭제
            if(!newHashtag.contains(old_Hashtag)){
                System.out.println(old_Hashtag);
                deleteHashtag(feed,old_Hashtag);
//                hashtagRepository.deleteByFeedIdAndHashtagContent(feed.getId(), old_Hashtag);
            }
        }

        Feed save = feedRepository.save(feed);


        //hashtag 저장
//        createHashtag(save,dto.getHashtagList());
        return getList();
    }

    //피드 삭제
    public void delete(Long feedId) {
        feedRepository.delete(getFeeds(feedId));
    }

    public Feed getFeeds(Long feedId){
        Feed feeds = feedRepository.findById(feedId).orElseThrow(()->new EntityNotFoundException(feedId+"번 피드 존재하지 않음"));
        return feeds;
    }

    public void createHashtag(Feed feed,List<String> hashtagList){
        if(hashtagList.size() > 0){
            for (String hashtag : hashtagList){
                HashTag newHashtag = HashTag.builder()
                        .hashtagContent(hashtag)
                        .feed(feed)
                        .build();
                HashTag saveHashtag = hashtagRepository.save(newHashtag);
                feed.getHashTags().add(saveHashtag);
            }
        }
    }


    public void saveHashtag(Feed feed,String hashtag){
        HashTag newHashtag = HashTag.builder()
                .hashtagContent(hashtag)
                .feed(feed)
                .build();
        HashTag saveHashtag = hashtagRepository.save(newHashtag);
        feed.getHashTags().add(saveHashtag);
    }

    public void deleteHashtag(Feed feed,String hashtag){
        HashTag newHashtag = HashTag.builder()
                .hashtagContent(hashtag)
                .feed(feed)
                .build();
        HashTag delteHashtag = hashtagRepository.findByFeedIdAndHashtagContent(feed.getId(),newHashtag.getHashtagContent());
        feed.getHashTags().remove(delteHashtag);
    }

}
