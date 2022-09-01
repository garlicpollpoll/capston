package com.hello.capston.controller.find;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.ItemDetail;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.PagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FindByDetailCategoryController {

    private final ItemDetailRepository itemDetailRepository;
    private final ItemRepository itemRepository;

    private final MemberService memberService;
    private final PagingService pagingService;

    @GetMapping("/find_by_detail_category")
    public String findDetailCategory(HttpServletRequest request, Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNow) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = memberService.findMember(loginId);
            model.addAttribute("status", findMember.getRole());
        }

        String category = request.getParameter("category");

        PageRequest page = PageRequest.of(pageNow, 9);
        List<Item> items = itemRepository.findByCategory(category, page);
        List<Item> findItems = itemRepository.findByCategoryAll(category);

        pagingService.paging(model, 9, pageNow, findItems.size());

        model.addAttribute("items", items);
        model.addAttribute("category", category);

        return "find_by_detail_category";
    }
}
