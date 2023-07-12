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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @ResponseBody
    public Map<String, String> joinPost(@Validated @RequestBody JoinForm form, BindingResult bindingResult, HttpSession session, HttpServletResponse response) throws IOException {
        Map<String, String> map = new HashMap<>();

        if (bindingResult.hasErrors()) {
            map.put("message", "빈칸이 있어서는 안됩니다.");
            return map;
        }

        boolean isDuplicate = memberService.findByLoginId(form.getLoginId());

        if (!isDuplicate) {
            map.put("message", "아이디가 중복되었습니다.");
            return map;
        }

        String passOrNot = (String) session.getAttribute("passOrNot");

        if (passOrNot == null || passOrNot.equals("false")) {
            map.put("message", "이메일 인증을 진행해주세요.");
            return map;
        }

        String encode = encoder.encode(form.getLoginPw());

        Member member = new Member(form.getLoginId(), encode, form.getBirth(), "null", MemberRole.ROLE_ADMIN, form.getEmail(), "");
        memberRepository.save(member);

        map.put("message", "환영합니다!");
        return map;
    }
}
