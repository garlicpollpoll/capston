package com.hello.capston.controller.login;

import com.hello.capston.dto.form.LoginForm;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        LoginForm form = new LoginForm();

        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberService.findMember(loginId);

        if (findMember != null) {
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("login", form);

        return "login";
    }

    @PostMapping("/login")
    public String login_post(@Validated @ModelAttribute("login") LoginForm form, BindingResult bindingResult, HttpSession session) {
        LoginForm login = new LoginForm();
        login.setLoginId(form.getLoginId());
        login.setLoginPw(form.getLoginPw());

        if (bindingResult.hasErrors()) {
            return "normal_login";
        }

        Member findMember = memberRepository.findByLoginIdAndLoginPw(form.getLoginId(), form.getLoginPw()).orElse(null);

        if (findMember == null) {
            bindingResult.reject("MemberNotFound");
            return "normal_login";
        }

        session.setAttribute("loginId", findMember.getUsername());

        return "redirect:/";
    }
}
