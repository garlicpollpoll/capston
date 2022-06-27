package com.hello.capston.dto.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ItemNameDuplicateForm {

    @NotEmpty
    private String itemName;

}
