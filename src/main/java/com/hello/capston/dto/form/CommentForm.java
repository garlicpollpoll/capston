package com.hello.capston.dto.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentForm {

    private MultipartFile commentImage;

    @NotEmpty
    private String comment;

    public CommentForm(MultipartFile commentImage, String comment) {
        this.commentImage = commentImage;
        this.comment = comment;
    }
}
