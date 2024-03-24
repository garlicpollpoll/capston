package com.hello.capston.controller.like;

import com.hello.capston.dto.dto.like.LookUpLikeDto;
import com.hello.capston.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LikeListController {

    private final LikeService likeService;

    /**
     * 좋아요 목록 조회
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/like_list")
    public String likeList(Model model, Authentication authentication) {

        LookUpLikeDto dto = likeService.lookUpLikeList(authentication);

        model.addAttribute("myLikes", dto.getFindLikes());
        model.addAttribute("status", dto.getRole());

        return "like_list";
    }
}
