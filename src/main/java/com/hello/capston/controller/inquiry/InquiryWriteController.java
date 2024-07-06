package com.hello.capston.controller.inquiry;

import com.hello.capston.dto.form.InquiryForm;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.repository.cache.KeyGenerator;
import com.hello.capston.service.InquiryService;
import io.lettuce.core.support.caching.CacheFrontend;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class InquiryWriteController {

    private final InquiryService inquiryService;
    private final CacheRepository cacheRepository;
    private final CacheFrontend<String, Member> frontend;

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
     * @return
     */
    @PostMapping("/inquiry_write")
    public String inquiryWritePost(@Validated @ModelAttribute("inquiryForm") InquiryForm form, BindingResult bindingResult,
                                   Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "inquiry_write";
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

//        Member findMember = cacheRepository.findMemberAtCache(username);
        String key = KeyGenerator.memberKeyGenerate(username);
        Member findMember = frontend.get(key);

        inquiryService.saveInquiry(findMember, null, LocalDateTime.now().toString().substring(0, 10), form.getContent(), form.getTitle());

        return "redirect:/inquiry";
    }

}
