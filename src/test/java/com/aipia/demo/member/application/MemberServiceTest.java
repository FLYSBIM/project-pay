package com.aipia.demo.member.application;

import com.aipia.demo.member.domain.Member;
import com.aipia.demo.member.domain.MemberRepository;
import com.aipia.demo.member.dto.MemberCreateRequestDto;
import com.aipia.demo.member.dto.MemberResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 회원 조회")
    void findByEmail(){
        //given
        String myEmail = "aa@gmail.com";
        Member mockMember = Member.builder().email(myEmail).password("123").name("Joe").build();
        when(memberRepository.findByEmail(myEmail)).thenReturn(Optional.of(mockMember));

        //when
        MemberResponseDto responseDto = memberService.findByEmail(myEmail);

        //then
        Assertions.assertThat(responseDto.getEmail()).isEqualTo(myEmail);
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회 시 예외 발생")
    void findByEmail_MemberNotFound(){
        //given
        String notExistEmail = "notexist@gmail.com";
        when(memberRepository.findByEmail(notExistEmail)).thenReturn(Optional.empty());

        //when then
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.findByEmail(notExistEmail);
        });
    }

    @Test
    @DisplayName("멤버 생성 테스트")
    void createMember(){
        //given
        String email = "aa@gmail.com";
        String password = "123";
        String name = "Joe";
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(email, password, name);

        when(memberRepository.save(any(Member.class))).then(returnsFirstArg());

        //when
        MemberResponseDto member = memberService.createMember(requestDto);

        //then
        Assertions.assertThat(email).isEqualTo(member.getEmail());
        Assertions.assertThat(name).isEqualTo(member.getName());

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("중복된 이메일로 생성 시도 시 예외 발생")
    void createMember_DuplicateEmail(){
        //given
        String existEmail = "aa@gmail.com";
        String password = "123";
        String name = "Joe";
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto(existEmail, password, name);
        when(memberRepository.findByEmail(existEmail)).thenReturn(Optional.of(Member.builder().build()));

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.createMember(requestDto);
        });
    }
}