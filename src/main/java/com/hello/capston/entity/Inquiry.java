package com.hello.capston.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Inquiry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String writeDate;

    @Lob
    private String content;

    public Inquiry(Member member, User user, String title, String writeDate, String content) {
        this.member = member;
        this.user = user;
        this.title = title;
        this.content = content;
        this.writeDate = writeDate;
    }

    public Inquiry(Long id, Member member, User user, String title, String writeDate, String content) {
        this.id = id;
        this.member = member;
        this.user = user;
        this.title = title;
        this.writeDate = writeDate;
        this.content = content;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
