package com.withus.be.controller;

import com.withus.be.service.FeedReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/withus/reply")
public class FeedReplyController {
    private final FeedReplyService feedReplyService;
}
