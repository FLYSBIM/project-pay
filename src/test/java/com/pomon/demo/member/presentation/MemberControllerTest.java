package com.pomon.demo.member.presentation;

import com.pomon.demo.member.application.MemberService;
import com.pomon.demo.member.domain.Member;
import com.pomon.demo.member.dto.MemberCreateRequestDto;
import com.pomon.demo.member.dto.MemberResponseDto;
import com.pomon.demo.member.exception.EmailAlreadyExistsException;
import com.pomon.demo.member.exception.MemberNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MemberService orderService() {
            return Mockito.mock(MemberService.class);
        }
    }

    @Test
    @DisplayName("회원가입 성공 시 201 Created 상태 코드를 응답한다")
    void signUp_success() throws Exception {
        // given
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto("test@email.com", "password123", "John");
        String requestBodyJson = objectMapper.writeValueAsString(requestDto);

        Member createdMember = Member.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .name("John")
                .build();
        MemberResponseDto responseDto = new MemberResponseDto(createdMember);

        when(memberService.createMember(any(MemberCreateRequestDto.class))).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    @DisplayName("이메일로 회원 조회 성공 시 200 OK 상태 코드를 응답한다")
    void getMember_success() throws Exception {
        // given
        String testEmail = "test@email.com";
        String testName = "John";
        MemberResponseDto responseDto = new MemberResponseDto(
                Member.builder().email(testEmail).name(testName).password("pass").build()
        );

        when(memberService.findByEmail(testEmail)).thenReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/members/" + testEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testEmail))
                .andExpect(jsonPath("$.name").value(testName));
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회 시 404 Not Found 상태 코드 응답")
    void getMember_notFound() throws Exception {
        // given
        String nonExistentEmail = "notfound@email.com";
        String errorMessage = "해당 이메일의 회원을 찾을 수 없습니다.";

        when(memberService.findByEmail(nonExistentEmail))
                .thenThrow(new MemberNotFoundException(errorMessage));

        // when & then
        mockMvc.perform(get("/api/members/" + nonExistentEmail))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 가입 시도 시 409 Confilct 상태 코드 응답")
    void signUp_conflict() throws Exception {
        // given
        MemberCreateRequestDto requestDto = new MemberCreateRequestDto("test@email.com", "password123", "John");
        String requestBodyJson = objectMapper.writeValueAsString(requestDto);

        when(memberService.createMember(any(MemberCreateRequestDto.class)))
                .thenThrow(new EmailAlreadyExistsException("이미 존재하는 이메일입니다."));

        // when & then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(status().isConflict());
    }
}