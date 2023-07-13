package com.hello.capston.controller.inquiry;

import com.hello.capston.dto.form.InquiryForm;
import com.hello.capston.entity.Inquiry;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.InquiryRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.InquiryService;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class InquiryWriteController {

    private final InquiryService inquiryService;
    private final CacheRepository cacheRepository;
    private final MemberRepository memberRepository;

    /**
     * 문의하기 작성 페이지 redirect
     * @param model
     * @return
     */
    @GetMapping("/inquiry_write")
    public String inquiryWrite(Model model) {
        InquiryForm form = new InquiryForm();
        model.addAttribute("inquiry", form);
        return "inquiry_write";
    }

    /**
     * 문의 등록
     * @param form
     * @param bindingResult
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/inquiry_write")
    public String inquiryWritePost(@Validated @ModelAttribute("inquiryForm") InquiryForm form, BindingResult bindingResult,
                                   HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "inquiry_write";
        }

        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        Member findMember = cacheRepository.findMemberAtCache(loginId);
        User findUser = cacheRepository.findUserAtCache(userEmail);

        if (findMember == null) {
            findMember = memberRepository.findByLoginId(loginId).orElse(null);
            cacheRepository.addMember(findMember);
        }

        inquiryService.saveInquiry(findMember, findUser, LocalDateTime.now().toString().substring(0, 10), form.getContent(), form.getTitle());

        return "redirect:/inquiry";
    }

}
