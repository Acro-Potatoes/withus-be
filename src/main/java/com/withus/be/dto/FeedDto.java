package com.withus.be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.withus.be.domain.Feed;
import com.withus.be.domain.FeedReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class FeedDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeedResponse{

        private int count;
        private Long feedId;

        private String title;

        private String content;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime created_at;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime updated_at;

        public static FeedResponse of(Feed feed){
            return FeedResponse.builder()
                    .feedId(feed.getFeedId())
                    .title(feed.getTitle())
                    .content(feed.getContent())
                    .created_at(feed.getCreatedAt())
                    .updated_at(feed.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public  static class FeedsWriteRequest{

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(min = 1, max = 100)
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(min = 1, max = 3000)
        private String content;

        public Feed toEntity() {
            return Feed.builder()
                    .content(this.content)
                    .title(this.title)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public  static class FeedModifyRequest{

        @NotNull
        private Long feedId;

        @Size(min = 1, max = 100)
        private String title;

        @Size(min = 1, max = 3000)
        private String content;

//        @CreationTimestamp //수정시간(저절로 생성)
//        private LocalDateTime update_time;

        public Feed toEntity() {
            return Feed.builder().content(this.content).title(this.title)
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
    public  static class FeedRelyResponse{

        private Long feedId;
        private String content;
        private String replyWriter;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime replyDate;

        public FeedRelyResponse(FeedReply feedReply) {
            this.feedId = feedId;
            this.content = content;
            this.replyWriter = replyWriter;
            this.replyDate = replyDate;
        }
    }



    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    public  static class FeedReplyInsertRequest{

        private Long feedId;
        private String content;
        private String replyWriter;

//        @JsonFormat(pattern = "yyyy-MM-dd")
//        private LocalDateTime replyDate;

        public FeedReply toEntity() {
            return FeedReply.builder().replyContent(this.content).replyWriter(this.replyWriter)
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
    public  static class FeedReplyModifyRequest{

        private Long feedId;
        private String content;
        private String replyWriter;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime replyDate;

        public FeedReply toEntity() {
            return FeedReply.builder().replyContent(this.content).replyWriter(this.replyWriter)
                    .build();
        }

    }



}
