package com.hello.capston.controller.item;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.PagingService;
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
    private final PagingService pagingService;

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

        pagingService.paging(model, 9, pageNow, itemRepository.count());

        model.addAttribute("items", findAll);

        return "item_list_popular";
    }
}
