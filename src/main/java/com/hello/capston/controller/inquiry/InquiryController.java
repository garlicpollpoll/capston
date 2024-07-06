package com.hello.capston.controller.inquiry;

import com.hello.capston.dto.dto.uitls.PagingDto;
import com.hello.capston.entity.Inquiry;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.InquiryRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.repository.cache.KeyGenerator;
import com.hello.capston.service.PagingService;
import io.lettuce.core.support.caching.CacheFrontend;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryRepository inquiryRepository;


    private final PagingService pagingService;
    private final CacheRepository cacheRepository;
    private final CacheFrontend<String, Member> frontend;

    /**
     * 문의하기 페이지
     * @param model
     * @param pageNow
     * @param session
     * @return
     */
    @GetMapping("/inquiry")
    public String inquiry(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNow,
                          Authentication authentication) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        PageRequest page = PageRequest.of(pageNow, 10);
        List<Inquiry> findAll = inquiryRepository.findAllInquiry(page);

        PagingDto pagingDto = pagingService.paging(10, pageNow, inquiryRepository.count());

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

//        Member findMemberAtCache = cacheRepository.findMemberAtCache(username);
        String key = KeyGenerator.memberKeyGenerate(username);
        Member findMember = frontend.get(key);


        model.addAttribute("status", findMember.getRole());
        model.addAttribute("inquiry", findAll);
        model.addAttribute("pageCount", pagingDto.getMap());
        model.addAttribute("lastPage", pagingDto.getTotalPage());

        return "inquiry_list";
    }
}
