package com.withus.be.service;

import com.withus.be.repository.FeedReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedReplyService {

    private final FeedReplyRepository feedReplyRepository;

}
