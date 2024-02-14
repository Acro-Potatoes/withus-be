package com.withus.be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.withus.be.domain.Feed;
import com.withus.be.domain.FeedReply;
import com.withus.be.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class FeedReplyDto {



    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class FeedRelyResponse {

        private Long id;
        private String replyContent;
        private String replyWriter;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdDate;

        public FeedRelyResponse(FeedReply feedReply) {
            this.id = feedReply.getId();
            this.createdDate = feedReply.getCreatedDate();
            this.replyContent = feedReply.getReplyContent();
            this.replyWriter = feedReply.getReplyWriter();
        }


    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class FeedReplyInsertRequest {

        private Long id;

        @NotBlank
        @Size(min = 1, max = 1000)
        private String replyContent;


        public FeedReply toEntity(Member member, Feed feed) {
            return FeedReply.builder().replyContent(this.replyContent)
                    .feed(feed)
                    .replyWriter(member.getNickname())
                    .member(member)
                    .build();
        }

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class FeedReplyModifyRequest {

        @NotNull
        private Long id;

        private String replyContent;

    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class FeedRereplyInsertRequest {

        private Long parentId;

        @NotBlank
        @Size(min = 1, max = 1000)
        private String replyContent;



        public FeedReply toEntity(Member member, FeedReply reply,Feed feed) {
            return FeedReply.builder().replyContent(this.replyContent)
                    .replyWriter(member.getNickname())
                    .comment(reply)
                    .member(member)
                    .feed(feed)
                    .build();
        }

    }
}
