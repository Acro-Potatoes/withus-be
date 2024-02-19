package com.withus.be.service;

import com.withus.be.common.exception.EntityNotFoundException;
import com.withus.be.common.exception.InvalidParameterException;
import com.withus.be.domain.Calendar;
import com.withus.be.domain.Member;
import com.withus.be.dto.CalendarDto.CalRequest;
import com.withus.be.dto.CalendarDto.CalResponse;
import com.withus.be.repository.CalendarRepository;
import com.withus.be.repository.MemberRepository;
import com.withus.be.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarService {

    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public void addSchedule(CalRequest calRequest) {
        String email = checkPermissionsAndGetCurrentEmail();
        calendarRepository.save(CalRequest.from(calRequest, getMember(email)));
    }

    @Transactional
    public void deleteSchedule(Long id) {
        checkPermissionsAndGetCurrentEmail();
        calendarRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CalResponse getMySchedule(Long id) {
        checkPermissionsAndGetCurrentEmail();
        return calendarRepository.findById(id)
                .map(CalResponse::of)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 일정입니다."));
    }

    @Transactional(readOnly = true)
    public List<CalResponse> getMyAllSchedules() {
        String email = checkPermissionsAndGetCurrentEmail();
        Long memberId = getMember(email).getId();

        return calendarRepository.findByMemberId(memberId).stream()
                .map(CalResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateSchedule(Long id, CalRequest request) {
        checkPermissionsAndGetCurrentEmail();
        Calendar calendar = calendarRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("존재하지 않는 일정입니다."));
        calendar.updateSchedule(request);
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(String.format("'%s'에 해당하는 계정이 없습니다.", email))
        );
    }

    private String checkPermissionsAndGetCurrentEmail() {
        return SecurityUtil.getCurrentEmail().orElseThrow(() -> new InvalidParameterException("인증이 필요합니다."));
    }

}
