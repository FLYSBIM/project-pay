package com.aipia.demo.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "MEMBER_EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "MEMBER_PASSWORD", nullable = false)
    private String password;

    @Column(name = "MEMBER_NAME", nullable = false)
    private String name;

    @Builder
    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}