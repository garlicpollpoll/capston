package com.hello.capston.dto.dto.like;

import com.hello.capston.entity.Likes;
import com.hello.capston.entity.enums.MemberRole;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LookUpLikeDto {

    private List<Likes> findLikes = new ArrayList<>();
    private MemberRole role;

    public LookUpLikeDto(List<Likes> findLikes, MemberRole role) {
        this.findLikes = findLikes;
        this.role = role;
    }
}
