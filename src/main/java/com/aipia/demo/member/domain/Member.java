package com.aipia.demo.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "MEMBER_BALANCE")
    private Long balance;

    public void decreaseBalance(Long balance) {
        if(balance > this.balance) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        this.balance -= balance;
    }
}