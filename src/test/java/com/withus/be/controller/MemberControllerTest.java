package com.withus.be.controller;

import com.withus.be.common.response.Response.Body;
import com.withus.be.domain.Member;
import com.withus.be.dto.MemberDto.MemberResponse;
import com.withus.be.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .name("acro")
                .email("acro@acro.com")
                .build();
    }

    @Test
    void memberSuccessTest() {
        when(memberService.getMemberInfo(1L)).thenReturn(MemberResponse.of(member));

        ResponseEntity<Body> response = memberController.getMember(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        MemberResponse responseBody = (MemberResponse) Objects.requireNonNull(response.getBody()).data();
        assertEquals("acro", responseBody.getName());
        assertEquals("acro@acro.com", responseBody.getEmail());
    }

    @Test
    void memberNotFoundTest() {
        when(memberService.getMemberInfo(2L)).thenReturn(null);
        ResponseEntity<Body> response = memberController.getMember(2L);
        // 잘못된 값이 들어왔을때 터지는 exception 이 custom exception 이기에 controller advice 에서 custom response 로 응답을 날려줘 상태코드는 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void memberInvalidIdTest() {
        ResponseEntity<Body> response = memberController.getMember(-1L);
        // 잘못된 값이 들어왔을때 터지는 exception 이 custom exception 이기에 controller advice 에서 custom response 로 응답을 날려줘 상태코드는 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}