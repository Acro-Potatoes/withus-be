package com.withus.be.service;

import com.withus.be.domain.Feed;
import com.withus.be.repository.FeedRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeedServiceTest {

    FeedService feedService;
    FeedRepository feedRepository;

    @Test
    void write(){
        //given
        Feed feed = new Feed();
        feed.setTitle("아령아령하세요");
        feed.setContent("아령아령아령하시라");
        //when

        //then


    }




}