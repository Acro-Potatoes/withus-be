package com.withus.be.domain;

import com.withus.be.common.BaseEntity;
import com.withus.be.dto.CalendarDto.CalRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Calendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String startDate;
    private String endDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateSchedule(CalRequest request) {
        this.title = request.getTitle() == null ? this.title : request.getTitle();
        this.content = request.getContent() == null ? this.content : request.getContent();
        this.startDate = request.getStartDate() == null ? this.startDate :request.getStartDate();
        this.endDate = request.getEndDate() == null ? this.endDate : request.getEndDate();
    }
}
