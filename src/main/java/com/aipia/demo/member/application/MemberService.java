package com.aipia.demo.member.application;

import com.aipia.demo.member.domain.Member;
import com.aipia.demo.member.domain.MemberRepository;
import com.aipia.demo.member.dto.MemberCreateRequestDto;
import com.aipia.demo.member.dto.MemberResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponseDto findByEmail(final String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if(optionalMember.isEmpty()) {
            throw new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다.");
        }

        Member findMember = optionalMember.get();

        return new MemberResponseDto(findMember);
    }

    public MemberResponseDto createMember(final MemberCreateRequestDto requestDto) {
        if(memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        Member member = Member.builder().email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .name(requestDto.getName())
                .build();

        memberRepository.save(member);

        return new MemberResponseDto(member);
    }
}