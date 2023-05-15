package com.hello.capston.controller.login;

import com.hello.capston.dto.form.LoginIdDuplicateForm;
import com.hello.capston.entity.Member;
import com.hello.capston.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginIdDuplicateController {

    private final MemberService memberService;

    /**
     * 로그인 중복 확인 페이지
     * @param model
     * @return
     */
    @GetMapping("/login_id_duplicate")
    public String loginIdDuplicate(Model model) {
        LoginIdDuplicateForm form = new LoginIdDuplicateForm();
        model.addAttribute("duplicate", form);
        return "login_id_duplicate";
    }

    /**
     * 로그인 아이디 중복 방지 로직
     * @param form
     * @return
     */
    @PostMapping("/login_id_duplicate")
    @ResponseBody
    public Map<String, String> loginIdDuplicatePost(@RequestBody LoginIdDuplicateForm form) {
        Map<String, String> map = new HashMap<>();
        String message;

        Member findMember = memberService.findMember(form.getLoginId());

        if (findMember == null) {
            message = "사용가능한 아이디입니다.";
        }
        else {
            message = "중복된 아이디입니다.";
        }

        if (form.getLoginId().equals("")) {
            message = "아이디를 입력해주세요";
        }

        map.put("message", message);

        return map;
    }
}
