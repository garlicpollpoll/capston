package com.hello.capston.controller.like;

import com.hello.capston.entity.Likes;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikeListController {

    private final LikeRepository likeRepository;

    private final UserService userService;
    private final MemberService memberService;

    @GetMapping("/like_list")
    public String likeList(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        String loginId = (String) session.getAttribute("loginId");

        if (userEmail == null) {
            Member findMember = memberService.findMember(loginId);

            List<Likes> findLikes = likeRepository.findMyLikesByMemberId(findMember.getId());

            model.addAttribute("myLikes", findLikes);
            model.addAttribute("status", findMember.getRole());
        }

        if (loginId == null) {
            User findUser = userService.findUser(userEmail);

            List<Likes> findLikes = likeRepository.findMyLikesByUserId(findUser.getId());

            model.addAttribute("myLikes", findLikes);
        }

        return "like_list";
    }
}
