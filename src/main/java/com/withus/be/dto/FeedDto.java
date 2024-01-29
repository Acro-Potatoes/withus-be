package com.withus.be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.withus.be.domain.Feed;
import com.withus.be.domain.FeedReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Slf4j
public class FeedDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class FeedResponse{

        private int count;
        private Long id;

        private String title;

        private String content;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime created_at;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime updated_at;

        public static FeedResponse of(Feed feed){
            return FeedResponse.builder()
                    .id(feed.getFeed_id())
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
    public  static class FeedsWriteRequest{

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(min = 1, max = 100)
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(min = 1, max = 3000)
        private String contents;

        public Feed toEntity() {
            return Feed.builder().content(this.contents).title(this.title)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public  static class FeedModifyRequest{

        @NotNull
        private Long id;

        @Size(min = 1, max = 100)
        private String title;

        @Size(min = 1, max = 3000)
        private String contents;

//        @CreationTimestamp //수정시간(저절로 생성)
//        private LocalDateTime update_time;

        public Feed toEntity() {
            return Feed.builder().content(this.contents).title(this.title)
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

        private Long id;
        private String contents;
        private String replyWriter;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime replyDate;

        public FeedRelyResponse(FeedReply feedReply) {
            this.id = id;
            this.contents = contents;
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

        private Long id;
        private String contents;
        private String replyWriter;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime replyDate;

        public FeedReply toEntity() {
            return FeedReply.builder().replyContent(this.contents).replyWriter(this.replyWriter)
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

        private Long id;
        private String contents;
        private String replyWriter;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime replyDate;

        public FeedReply toEntity() {
            return FeedReply.builder().replyContent(this.contents).replyWriter(this.replyWriter)
                    .build();
        }

    }



}
