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
import java.util.List;

@Slf4j
public class FeedDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeedResponse {

        private int count;
        private Long id;

        private String title;

        private String content;

        private List<String> hashtagList;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime created_at;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime updated_at;

        public static FeedResponse of(Feed feed,List<String> hashtagList) {
            return FeedResponse.builder()
                    .id(feed.getId())
                    .title(feed.getTitle())
                    .content(feed.getContent())
                    .created_at(feed.getCreatedDate())
                    .updated_at(feed.getUpdatedDate())
                    .hashtagList(hashtagList)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeedsWriteRequest {

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(min = 1, max = 100)
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(min = 1, max = 3000)
        private String content;

        private List<String> hashtagList;


        public Feed toEntity(Member member) {
            return Feed.builder()
                    .title(this.title)
                    .content(this.content)
                    .member(member)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeedModifyRequest {

        @NotNull
        private Long id;

        @Size(min = 1, max = 100)
        private String title;

        @Size(min = 1, max = 3000)
        private String content;

        private List<String> hashtagList;

    }


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


}
