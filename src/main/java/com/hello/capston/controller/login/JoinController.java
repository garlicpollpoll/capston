package com.hello.capston.controller.login;

import com.hello.capston.dto.form.JoinForm;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class JoinController {

    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/join")
    public String join(Model model, HttpSession session) {
        JoinForm form = new JoinForm();

        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = memberService.findMember(loginId);
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("join", form);
        return "join";
    }

    @PostMapping("/join")
    public String joinPost(@Validated @ModelAttribute("join") JoinForm form, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "join";
        }

        boolean isDuplicate = memberService.findByLoginId(form.getLoginId());

        if (!isDuplicate) {
            bindingResult.reject("LoginIdDuplicate");
            return "join";
        }

        String passOrNot = (String) session.getAttribute("passOrNot");

        if (passOrNot == null || passOrNot.equals("false")) {
            bindingResult.reject("NotPassAuth");
            return "join";
        }

        String encode = encoder.encode(form.getLoginPw());

        Member member = new Member(form.getLoginId(), encode, form.getBirth(), form.getGender(), MemberRole.ROLE_MEMBER);
        memberRepository.save(member);

        return "redirect:/login";
    }
}
