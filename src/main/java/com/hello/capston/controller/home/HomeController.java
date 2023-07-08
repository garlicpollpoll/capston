package com.hello.capston.controller.home;

import com.hello.capston.dto.dto.HomeDto;
import com.hello.capston.dto.form.UploadForm;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final ItemRepository itemRepository;
    private final CacheRepository cacheRepository;
    private final HomeService homeService;

    /**
     * 메인 페이지
     * @param model
     * @param session
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/")
    public String home(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String loginId = (String) session.getAttribute("loginId");
        Member findMember = cacheRepository.findMemberAtCache(loginId);

        HomeDto dto = homeService.homeSetting();

        if (dto.getIsMemberOrUser() != null && dto.getSessionAttribute() != null && dto.getRole() != null) {
            if (dto.getIsMemberOrUser().equals("loginId")) {
                session.setAttribute(dto.getIsMemberOrUser(), dto.getSessionAttribute());
            }
            else {
                session.setAttribute(dto.getIsMemberOrUser(), dto.getSessionAttribute());
            }
        }

        model.addAttribute("status", dto.getRole());

        Pageable page = PageRequest.of(0, 8);
        List<Item> findNewItem = itemRepository.findAllItem(page);
        List<Item> findPopularItem = itemRepository.findAllItemByCount(page);

        model.addAttribute("newItem", findNewItem);
        model.addAttribute("popularItem", findPopularItem);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
            }
        }

        return "index";
    }

    @GetMapping("/erase/authToken/authentication")
    public String erase(HttpSession session, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie cookie = new Cookie("AUTH-TOKEN", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }

    @GetMapping("/social_login")
    public String login() {
        return "login";
    }

    /**
     * MANAGER 등급에 의해 상품 등록
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/item_upload")
    public String itemUpload(Model model, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);
            model.addAttribute("status", findMember.getRole());
        }

        UploadForm form = new UploadForm();
        model.addAttribute("upload", form);
        return "upload";
    }
}
