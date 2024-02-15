package com.withus.be.controller;

import com.withus.be.common.response.ResponseSuccess;
import com.withus.be.dto.CalendarDto.CalRequest;
import com.withus.be.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.withus.be.common.response.Response.Body;

@Tag(name = "Calendar Controller", description = "일정 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    @Operation(summary = "일정 조회 API")
    @GetMapping("{id}")
    public ResponseEntity<Body> getMySchedule(@PathVariable Long id) {
        return new ResponseSuccess().success(calendarService.getMySchedule(id));
    }

    @Operation(summary = "내 일정 전체 조회 API")
    @GetMapping()
    public ResponseEntity<Body> getMyAllSchedule() {
        return new ResponseSuccess().success(calendarService.getMyAllSchedules());
    }

    @Operation(summary = "일정 등록 API")
    @PostMapping
    public ResponseEntity<Body> addSchedule(@RequestBody CalRequest request) {
        calendarService.addSchedule(request);
        return new ResponseSuccess().success("일정 등록이 완료됐습니다.");
    }

    @Operation(summary = "일정 수정 API")
    @PutMapping("{id}")
    public ResponseEntity<Body> updateSchedule(@PathVariable(value = "id") Long id, @RequestBody CalRequest request) {
        calendarService.updateSchedule(id, request);
        return new ResponseSuccess().success("일정 수정이 완료됐습니다.");
    }

    @Operation(summary = "일정 삭제 API")
    @DeleteMapping("{id}")
    public ResponseEntity<Body> deleteSchedule(@PathVariable(value = "id") Long id) {
        calendarService.deleteSchedule(id);
        return new ResponseSuccess().success("일정 삭제가 완료됐습니다.");
    }


}
