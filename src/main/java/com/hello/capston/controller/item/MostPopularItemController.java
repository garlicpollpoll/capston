package com.hello.capston.controller.item;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class MostPopularItemController {

    private final ItemRepository itemRepository;

    private final MemberService memberService;

    @GetMapping("/item_list_popular")
    public String popularItem(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNow,
                              HttpSession session) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            Member findMember = memberService.findMember(loginId);
            model.addAttribute("status", findMember.getRole());
        }

        Pageable page = PageRequest.of(pageNow, 9);
        List<Item> findAll = itemRepository.findAllItemByCount(page);

        pageNow += 1;

        long pageStart, pageEnd;

        long size = itemRepository.count();

        long totalPage = 0;

        if (size % 9 == 0) {
            totalPage = size / 9;
        }
        else {
            totalPage = size / 9 + 1;
        }

        pageStart = pageNow - 2;
        pageEnd = pageNow + 2;

        if (pageStart == 0 || pageStart == -1) {
            pageStart = 1;
            if (totalPage < 5) {
                pageEnd = totalPage;
            }
            else {
                pageEnd = 5;
            }
        } else if (pageEnd == totalPage + 1) { // ????????? ?????? ??? ?????????
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 3;
            }
        } else if (pageEnd == totalPage + 2) { // ????????? ?????????
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 4;
            }
        }

        Map<Long, Long> map = new LinkedHashMap<>();

        for (long i = pageStart; i <= pageEnd; i++) {
            map.put(i, i);
        }

        model.addAttribute("pageCount", map);
        model.addAttribute("lastPage", totalPage);

        model.addAttribute("items", findAll);

        return "item_list_popular";
    }
}
