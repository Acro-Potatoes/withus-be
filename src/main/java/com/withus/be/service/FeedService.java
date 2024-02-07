package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Feed;
import com.withus.be.domain.HashTag;
import com.withus.be.domain.Member;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FeedService {
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;


    //리스트 가져오기
    public List<FeedResponse> getList() {
        List<Feed> feed = feedRepository.findAll();
        return toStringList(feed);
    }

    //최신순 조회
    public List<FeedResponse> getListDateDesc() {
        List<Feed> feed = feedRepository.findByDate();
        return toStringList(feed);
    }


    //피드 키워드 검색
    public List<FeedResponse> getKeyword(String keyword) {
        List<Feed> feed = feedRepository.findByKeyword(keyword);
        if (feed.isEmpty()) {
            throw new EntityNotFoundException(keyword + "이란 단어가 존재하지 않음");
        }
        return toStringList(feed);
    }

    //피드 생성
    public List<FeedResponse> write(FeedsWriteRequest request) {
        String currentEmail = SecurityUtil.getCurrentEmail().orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(currentEmail).orElseThrow(EntityNotFoundException::new);
        Feed save = feedRepository.save(request.toEntity(member));
        //해쉬태그 List 추가하기
        createHashtag(save, request.getHashtagList());
        return getList();
    }

    //피드 수정
    @Transactional
    public List<FeedResponse> modify(FeedModifyRequest dto) {
        Feed feed = getFeeds(dto.getId());
        feed.setTitle(dto.getTitle());
        feed.setContent(dto.getContent());

        //피드에서 가져온 기존 해쉬태그리스트
        List<HashTag> oldHashtag = feed.getHashTags();
        List<String> oldHashtag_toString = new ArrayList<>();

        //수정한 해쉬태그
        List<String> newHashtagList = dto.getHashtagList();

        //기존 해쉬 태그리스트 String으로 변환
        for (HashTag hashTag : oldHashtag) oldHashtag_toString.add(hashTag.getHashtagContent());

        //기존해쉬 태그에 새로운 단어가 존재하지않는다면 해쉬태그 추가
        for (String new_hashtag : newHashtagList) if (!oldHashtag_toString.contains(new_hashtag)) addHashtag(feed, new_hashtag);
        //새로운해쉬태그 리스트에 기존단어가 존재하지않는다면 리스트에서 해쉬태그 삭제
        for (String old_Hashtag : oldHashtag_toString) if (!newHashtagList.contains(old_Hashtag)) deleteHashtag(feed, old_Hashtag);

        return getList();
    }

    //피드 삭제
    public void delete(Long feedId) {
        feedRepository.delete(getFeeds(feedId));
    }

    public Feed getFeeds(Long feedId) {
        Feed feeds = feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException(feedId + "번 피드 존재하지 않음"));
        return feeds;
    }

    public void createHashtag(Feed feed, List<String> hashtagList) {
        if (!hashtagList.isEmpty()) {
            for (String hashtag : hashtagList) {
                HashTag newHashtag = HashTag.builder()
                        .hashtagContent(hashtag)
                        .feed(feed)
                        .build();
                HashTag saveHashtag = hashtagRepository.save(newHashtag);
                feed.getHashTags().add(saveHashtag);
            }
        }
    }


    //새로운 해쉬태그 단어 추가
    public void addHashtag(Feed feed, String hashtag) {
        HashTag newHashtag = HashTag.builder()
                .hashtagContent(hashtag)
                .feed(feed)
                .build();
        HashTag saveHashtag = hashtagRepository.save(newHashtag);
        feed.getHashTags().add(saveHashtag);
        feedRepository.save(feed);
    }

    //해쉬태그 단어 삭제
    public void deleteHashtag(Feed feed, String hashtag) {
        HashTag delteHashtag = hashtagRepository.findByFeedIdAndHashtagContent(feed.getId(), hashtag);
        hashtagRepository.delete(delteHashtag);
        feed.getHashTags().remove(delteHashtag);
        feedRepository.save(feed);
    }

    //List<HashTag> -> List<String>
    public List<FeedResponse> toStringList(List<Feed> feedList) {
        List<FeedResponse> feedResponse = new ArrayList<>();
        for (Feed feed : feedList) {
            List<String> hashTagString = feed.getHashTags()
                    .stream().map(HashTag::getHashtagContent)
                    .toList();
            FeedResponse dto = FeedResponse.of(feed, hashTagString);
            feedResponse.add(dto);
        }
        return feedResponse;
    }

}
