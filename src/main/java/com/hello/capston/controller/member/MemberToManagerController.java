package com.hello.capston.controller.member;

import com.hello.capston.entity.Member;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class MemberToManagerController {

    private final CacheRepository cacheRepository;

    @GetMapping("/admin/member_to_manager")
    public String memberToManager() {
        return "member_to_manager";
    }

    /**
     * 일반 회원에서 MANGER 등급으로 승급
     * @param request
     * @return
     */
    @PostMapping("/member_to_manager")
    @Transactional
    public String memberToManagerPost(HttpServletRequest request) {
        String loginId = request.getParameter("loginId");
        Member findMember = cacheRepository.findMemberAtCache(loginId);

        findMember.changeRoleMemberToManager();

        return "redirect:/";
    }
}
