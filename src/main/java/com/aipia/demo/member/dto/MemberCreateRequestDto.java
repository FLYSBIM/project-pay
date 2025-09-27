package com.aipia.demo.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateRequestDto {

    private String email;

    private String password;

    private String name;

    public MemberCreateRequestDto(final String email,
                                  final String password,
                                  final String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
