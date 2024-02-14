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
        if (request.getImage() == null) request.setImage(" ");
        feedRepository.save(request.toEntity(member));
        return getList();
    }

    //피드 수정
    public List<FeedResponse> modify(FeedModifyRequest dto) {

        Feed feed = getFeeds(dto.getId());
        feed.update(dto.getTitle(), dto.getContent());

        if(dto.getImage() == null) feed.setImage(" ");
        else feed.setImage(dto.getImage());

        feedRepository.save(feed);
        return getList();
    }

    //피드 삭제
    public void delete(Long feedId) {
        feedRepository.delete(getFeeds(feedId));
    }

    public Feed getFeeds(Long feedId) {
        return feedRepository.findById(feedId).orElseThrow(() -> new EntityNotFoundException(feedId + "번 피드 존재하지 않음"));
    }

    public void createHashtag(Feed feed, List<String> hashtagList) {
        if (!hashtagList.isEmpty()) {
            for (String hashtag : hashtagList) {
                HashTag build = HashTag.builder()
                        .hashtagContent(hashtag)
                        .feed(feed)
                        .build();
                HashTag saveHashtag = hashtagRepository.save(build);
                feed.getHashTags().add(saveHashtag);
            }
        }
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
