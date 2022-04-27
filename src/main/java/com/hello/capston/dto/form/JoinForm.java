package com.hello.capston.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JoinForm {

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String loginPw;
    @NotEmpty
    private String birth;
    @NotEmpty
    private String gender;
}
