package com.hello.capston.controller.item;

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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MostPopularItemController {

    private final ItemRepository itemRepository;

    private final PagingService pagingService;
    private final CacheRepository cacheRepository;
    private final CacheFrontend<String, Member> frontend;

    /**
     * 인기 상품 리스트
     * @param model
     * @param pageNow
     * @param session
     * @return
     */
    @GetMapping("/item_list_popular")
    public String popularItem(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNow,
                              Authentication authentication) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

//        Member findMember = cacheRepository.findMemberAtCache(username);
        String key = KeyGenerator.memberKeyGenerate(username);
        Member findMember = frontend.get(key);

        model.addAttribute("status", findMember.getRole());

        Pageable page = PageRequest.of(pageNow, 9);
        List<Item> findAll = itemRepository.findAllItemByCount(page);

        PagingDto pagingDto = pagingService.paging(9, pageNow, itemRepository.count());

        model.addAttribute("items", findAll);
        model.addAttribute("pageCount", pagingDto.getMap());
        model.addAttribute("lastPage", pagingDto.getTotalPage());

        return "item_list_popular";
    }
}
