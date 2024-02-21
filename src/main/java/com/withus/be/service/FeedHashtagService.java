package com.withus.be.service;


import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.domain.Feed;
import com.withus.be.domain.HashTag;
import com.withus.be.dto.FeedDto.FeedModifyRequest;
import com.withus.be.repository.FeedRepository;
import com.withus.be.repository.HashtagRepository;
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
public class FeedHashtagService {
    private final HashtagRepository hashtagRepository;
    private final FeedRepository feedRepository;

    public void modify(FeedModifyRequest dto) {
        Feed feed = feedRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException(dto.getId() + "번 피드 존재하지 않음"));

        //피드에서 가져온 기존 해쉬태그리스트
        List<HashTag> oldHashtag = feed.getHashTags();
        List<String> oldHashtagToString = new ArrayList<>();

        //수정한 해쉬태그
        List<String> newHashtagList = dto.getHashtagList();

        //기존 해쉬 태그리스트 StringList 변환
        oldHashtag.stream().map(HashTag::getHashtagContent).forEach(oldHashtagToString::add);

        //기존해쉬 태그에 새로운 단어가 존재하지않는다면 해쉬태그 추가
        newHashtagList.stream().filter(h->!oldHashtagToString.contains(h)).forEach(h->addHashtag(feed,h));
        //새로운해쉬태그 리스트에 기존단어가 존재하지않는다면 리스트에서 해쉬태그 삭제
        oldHashtagToString.stream().filter(h->!newHashtagList.contains(h)).forEach(h->deleteHashtag(feed,h));
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

}
