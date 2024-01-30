//package com.withus.be.repository;
//
//import com.withus.be.domain.Feed;
//import com.withus.be.domain.Member;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class FeedRepositoryTest {
//    @Autowired
//    private FeedRepository feedRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Test
//    @DisplayName("피드 5개 생성")
//    void builkTest(){
//
//        Optional<Member> member = memberRepository.findById(2L);
////        for(int i = 1; i< 5; i++){
//            feedRepository.save(
//                    Feed.builder()
//                            .title("hello")
//                            .content("heyhello")
////                            .member_id(member.get())
//                            .(LocalDateTime.parse("2024-03-05"))
//                            .updatedAt(LocalDateTime.parFse("2024-02-05"))
//                            .build()
//            );
////        }
//    }
//
//}