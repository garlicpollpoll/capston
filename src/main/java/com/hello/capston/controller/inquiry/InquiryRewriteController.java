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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class InquiryRewriteController {

    private final InquiryRepository inquiryRepository;

    private final MemberRepository memberRepository;
    private final CacheRepository cacheRepository;

    /**
     * 문의하기 세부 내용 확인
     * @param id
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/inquiry_detail/{id}")
    public String inquiryDetail(@PathVariable("id") Long id, Authentication authentication, Model model) {
        Long previousPage = id - 1L;
        Long afterPage = id + 1L;

        Inquiry findInquiry = inquiryRepository.findById(id).orElse(null);
        Inquiry previousInquiry = inquiryRepository.findById(previousPage).orElse(null);
        Inquiry afterInquiry = inquiryRepository.findById(afterPage).orElse(null);

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        Member findMemberAtCache = cacheRepository.findMemberAtCache(username);

        if (findMemberAtCache.getId() == findInquiry.getMember().getId()) {
            model.addAttribute("correct", 1);
        }

        model.addAttribute("status", findMemberAtCache.getRole());

        model.addAttribute("inquiry", findInquiry);
        model.addAttribute("previousInquiry", previousInquiry);
        model.addAttribute("afterInquiry", afterInquiry);

        return "inquiry_detail";
    }

    /**
     * 문의 수정하기
     * @param id
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/inquiry_rewrite/{id}")
    public String inquiryRewrite(@PathVariable("id") Long id, Model model, HttpSession session) {
        Inquiry findInquiry = inquiryRepository.findById(id).orElse(null);

        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("inquiry", findInquiry);

        session.setAttribute("contentId", findInquiry.getId());

        return "inquiry_rewrite";
    }

    /**
     * 문의 수정하기
     * @param form
     * @param bindingResult
     * @param session
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/inquiry_rewrite")
    @Transactional
    public String inquiryRewritePost(@Validated @ModelAttribute("inquiry") InquiryForm form, BindingResult bindingResult,
                                     HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "inquiry_rewrite";
        }

        Long contentId = (Long) session.getAttribute("contentId");
        Inquiry findInquiry = inquiryRepository.findById(contentId).orElse(null);

        findInquiry.changeTitle(form.getTitle());
        findInquiry.changeContent(form.getContent());

        redirectAttributes.addAttribute("contentId", contentId);

        return "redirect:/inquiry_detail/{contentId}";
    }
}
