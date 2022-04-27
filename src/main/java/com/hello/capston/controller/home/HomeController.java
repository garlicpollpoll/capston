package com.hello.capston.controller.home;

import com.hello.capston.dto.form.UploadForm;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        Member findMember = memberRepository.findByLoginId(loginId).orElse(null);

        if (findMember != null) {
            model.addAttribute("status", findMember.getRole());
            log.info("MemberStatus = {}", findMember.getRole());
        }
        else {
            model.addAttribute("status", "Member");
            log.info("NotLogin");
        }

        return "main";
    }

    @GetMapping("/social_login")
    public String login() {
        return "login";
    }

    @GetMapping("/item_upload")
    public String itemUpload(Model model) {
        UploadForm form = new UploadForm();
        model.addAttribute("upload", form);
        return "upload";
    }

    @GetMapping("/dumy_page")
    public String dumy() {
        return "main";
    }
}
