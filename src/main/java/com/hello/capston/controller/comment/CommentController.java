package com.hello.capston.controller.comment;

import com.hello.capston.S3.S3Uploader;
import com.hello.capston.dto.form.CommentForm;
import com.hello.capston.entity.Comment;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.CommentRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.ItemService;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;

    private final ItemService itemService;
    private final CacheRepository cacheRepository;

    /**
     * 댓글 작성
     * @param form
     * @param bindingResult
     * @param session
     * @param itemId
     * @param redirectAttributes
     * @return
     * @throws IOException
     */
    @PostMapping("/comment/{id}")
    public String comment(@Validated @ModelAttribute("comment")CommentForm form, BindingResult bindingResult,
                          HttpSession session, @PathVariable("id") String itemId, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "item_detail";
        }

        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        Member findMember = cacheRepository.findMemberAtCache(loginId);
        User findUser = cacheRepository.findUserAtCache(userEmail);
        Item findItem = itemService.findItem(itemId);

        String imageUrl = s3Uploader.upload(form.getCommentImage(), "static");

        if (findMember == null) {
            Comment comment = new Comment(null, findUser, findItem, form.getComment(), imageUrl);
            commentRepository.save(comment);
        }

        if (findUser == null) {
            Comment comment = new Comment(findMember, null, findItem, form.getComment(), imageUrl);
            commentRepository.save(comment);
        }

        redirectAttributes.addAttribute("itemId", itemId);

        return "redirect:/item_detail/{itemId}";
    }
}
