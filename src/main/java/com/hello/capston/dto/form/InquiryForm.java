package com.hello.capston.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class InquiryForm {

    @NotEmpty
    private String title;

    private String content;
}
