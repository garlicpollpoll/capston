package com.hello.capston.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UniqueCodeDuplicateForm {

    @NotEmpty
    private String uniqueCode;

}
