package com.hello.capston.controller.inquiry;

import com.hello.capston.entity.Inquiry;
import com.hello.capston.repository.InquiryRepository;
import com.hello.capston.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class InquiryDeleteController {

    private final InquiryRepository inquiryRepository;

    private final InquiryService inquiryService;

    /**
     * 문의하기 세부내용 확인
     * @param contentId
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/inquiry_delete/{id}")
    public String deleteInquiry(@PathVariable("id") Long contentId, RedirectAttributes redirectAttributes) {
        Inquiry findInquiry = inquiryRepository.findById(contentId).orElse(null);

        inquiryService.deleteInquiry(findInquiry);

        redirectAttributes.addAttribute("contentId", contentId);

        return "redirect:/inquiry";
    }
}
