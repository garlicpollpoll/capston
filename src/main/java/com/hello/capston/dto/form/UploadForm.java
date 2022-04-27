package com.hello.capston.dto.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class UploadForm {

    private MultipartFile image;
    @NotEmpty
    private String viewName;
    @NotEmpty
    private String itemName;
    @NotEmpty
    private String price;
    @NotEmpty
    private String uniqueCode;
    @NotEmpty
    private String category;
    @NotEmpty
    private String color;
}
