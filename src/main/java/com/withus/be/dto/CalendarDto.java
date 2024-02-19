package com.withus.be.dto;

import com.withus.be.domain.Calendar;
import com.withus.be.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CalendarDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CalRequest {
        private String title;
        private String content;
        @Schema(example = "Date Format : yyyy-MM-dd hh:mm:ss")
        private String startDate;
        @Schema(example = "Date Format : yyyy-MM-dd hh:mm:ss")
        private String endDate;

        public static Calendar from(CalRequest calRequest, Member member) {
            return Calendar.builder()
                    .title(calRequest.title)
                    .content(calRequest.content)
                    .startDate(calRequest.startDate)
                    .endDate(calRequest.endDate)
                    .member(member)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CalResponse {
        private String title;
        private String content;
        private String startDate;
        private String endDate;

        public static CalResponse of(Calendar calendar) {
            return CalResponse.builder()
                    .title(calendar.getTitle())
                    .content(calendar.getContent())
                    .startDate(calendar.getStartDate())
                    .endDate(calendar.getEndDate())
                    .build();
        }
    }
}
