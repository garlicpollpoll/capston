package com.hello.capston.controller.inquiry;

import com.hello.capston.dto.dto.PagingDto;
import com.hello.capston.entity.Inquiry;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.InquiryRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.PagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryRepository inquiryRepository;


    private final PagingService pagingService;
    private final CacheRepository cacheRepository;
    private final MemberRepository memberRepository;

    /**
     * 문의하기 페이지
     * @param model
     * @param pageNow
     * @param session
     * @return
     */
    @GetMapping("/inquiry")
    public String inquiry(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNow,
                          HttpSession session) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        PageRequest page = PageRequest.of(pageNow, 10);
        List<Inquiry> findAll = inquiryRepository.findAllInquiry(page);

        PagingDto pagingDto = pagingService.paging(10, pageNow, inquiryRepository.count());


        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);
            if (findMember == null) {
                findMember = memberRepository.findByLoginId(loginId).orElse(null);
                cacheRepository.addMember(findMember);
            }
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("inquiry", findAll);
        model.addAttribute("pageCount", pagingDto.getMap());
        model.addAttribute("lastPage", pagingDto.getTotalPage());

        return "inquiry_list";
    }
}
