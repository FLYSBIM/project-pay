package com.aipia.demo.member.application;

import com.aipia.demo.member.domain.Member;
import com.aipia.demo.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다."));
    }

    public Member createMember(final String email, final String password, final String name) {
        if(memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        Member member = Member.builder().email(email).password(password).name(name).build();
        return memberRepository.save(member);
    }
}