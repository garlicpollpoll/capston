package com.hello.capston.controller.send;

import com.hello.capston.dto.dto.CheckNumberDto;
import com.hello.capston.dto.form.JoinForm;
import com.hello.capston.service.AlertService;
import com.hello.capston.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CheckNumberController {

    private final AlertService alertService;

    /**
     * 이메일 인증 확인 로직
     * @param dto
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/checkNumber")
    public Map<String, String> checkNumber(@RequestBody CheckNumberDto dto, HttpSession session) {
        String checkNumber = (String) session.getAttribute("checkNumber");
        String message = null;
        Map<String, String> map = new HashMap<>();

        if (checkNumber.equals(dto.getCheckNum())) {
            session.setAttribute("passOrNot", "true");
            message = "인증에 성공했습니다.";
        }
        else {
            session.setAttribute("passOrNot", "false");
            message = "인증에 실패했습니다.";
        }
        map.put("message", message);

        return map;
    }
}
