package com.hello.capston.controller.login;

import com.hello.capston.dto.form.JoinForm;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final CacheRepository cacheRepository;

    /**
     * 회원가입 페이지로 이동
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/join")
    public String join(Model model, HttpSession session) {
        JoinForm form = new JoinForm();

        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);
            if (findMember == null) {
                findMember = memberRepository.findByLoginId(loginId).orElse(null);
                cacheRepository.addMember(findMember);
            }
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("join", form);
        return "join";
    }

    /**
     * 회원가입
     * @param form
     * @param bindingResult
     * @param session
     * @return
     */
    @PostMapping("/join")
    public String joinPost(@Validated @ModelAttribute("join") JoinForm form, BindingResult bindingResult, HttpSession session, HttpServletResponse response) throws IOException {
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

        Member member = new Member(form.getLoginId(), encode, form.getBirth(), "null", MemberRole.ROLE_ADMIN, form.getEmail(), "");
        memberRepository.save(member);

        return "redirect:/login";
    }
}
