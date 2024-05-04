package com.hello.capston.dto.dto.uitls;

import com.hello.capston.entity.enums.MemberRole;
import lombok.Data;

@Data
public class HomeDto {

    private String sessionAttribute;
    private String isMemberOrUser;
    private MemberRole role;

    public HomeDto(String sessionAttribute, String isMemberOrUser, MemberRole role) {
        this.sessionAttribute = sessionAttribute;
        this.isMemberOrUser = isMemberOrUser;
        this.role = role;
    }
}
