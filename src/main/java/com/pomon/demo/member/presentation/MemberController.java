package com.pomon.demo.member.presentation;

import com.pomon.demo.member.application.MemberService;
import com.pomon.demo.member.dto.MemberCreateRequestDto;
import com.pomon.demo.member.dto.MemberResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody MemberCreateRequestDto requestDto) {
        MemberResponseDto responseDto = memberService.createMember(requestDto);
        URI location = URI.create("/api/members/" + responseDto.getId());
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/{email}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable String email) {
        MemberResponseDto responseDto = memberService.findByEmail(email);
        return ResponseEntity.ok(responseDto);
    }
}
