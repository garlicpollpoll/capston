package com.hello.capston.controller.find;

import com.hello.capston.dto.dto.uitls.PagingDto;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.ItemRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FindByDetailCategoryController {

    private final ItemRepository itemRepository;

    private final PagingService pagingService;
    private final CacheRepository cacheRepository;
    private final CacheFrontend<String, Member> frontend;

    /**
     * 카테고리별 제품 보는 페이지
     * @param request
     * @param model
     * @param pageNow
     * @return
     */
    @GetMapping("/find_by_detail_category")
    public String findDetailCategory(HttpServletRequest request, Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNow,
                                     Authentication authentication) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        String key = KeyGenerator.memberKeyGenerate(username);
        Member findMember = frontend.get(key);

//        Member findMemberAtCache = cacheRepository.findMemberAtCache(username);

        model.addAttribute("status", findMember.getRole());

        String category = request.getParameter("category");

        PageRequest page = PageRequest.of(pageNow, 9);
        List<Item> items = itemRepository.findByCategory(category, page);
        List<Item> findItems = itemRepository.findByCategoryAll(category);

        PagingDto pagingDto = pagingService.paging(9, pageNow, findItems.size());

        model.addAttribute("items", items);
        model.addAttribute("category", category);
        model.addAttribute("pageCount", pagingDto.getMap());
        model.addAttribute("lastPage", pagingDto.getTotalPage());

        return "find_by_detail_category";
    }
}
