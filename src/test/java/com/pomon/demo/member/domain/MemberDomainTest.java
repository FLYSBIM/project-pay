package com.pomon.demo.member.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class MemberDomainTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 생성 테스트")
    void createMember() {
        Member member = Member.builder().email("abc@naver.com").password("abc").name("John").build();

        Assertions.assertThat(member.getEmail()).isEqualTo("abc@naver.com");
        Assertions.assertThat(member.getPassword()).isEqualTo("abc");
        Assertions.assertThat(member.getName()).isEqualTo("John");
    }

    @Test
    @DisplayName("멤버 이메일 조회 테스트")
    void findByEmailMember(){
        //given
        Member member = Member.builder().email("abc@naver.com").password("abc").name("John").build();
        memberRepository.save(member);

        //when
        Optional<Member> findOptionalMember = memberRepository.findByEmail("abc@naver.com");

        //then
        Assertions.assertThat(findOptionalMember).isPresent();

        Member findMember = findOptionalMember.get();

        Assertions.assertThat(findMember.getEmail()).isEqualTo("abc@naver.com");
        Assertions.assertThat(findMember.getPassword()).isEqualTo("abc");
        Assertions.assertThat(findMember.getName()).isEqualTo("John");
    }


}
