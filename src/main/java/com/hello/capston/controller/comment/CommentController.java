package com.hello.capston.controller.comment;

import com.hello.capston.dto.form.CommentForm;
import com.hello.capston.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     * @param form
     * @param bindingResult
     * @param itemId
     * @param redirectAttributes
     * @return
     * @throws IOException
     */
    @PostMapping("/comment/{id}")
    public String comment(@Validated @ModelAttribute("comment")CommentForm form, BindingResult bindingResult, Authentication authentication,
                          @PathVariable("id") String itemId, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "item_detail";
        }

        commentService.saveComment(itemId, form, authentication);

        redirectAttributes.addAttribute("itemId", itemId);

        return "redirect:/item_detail/{itemId}";
    }
}
