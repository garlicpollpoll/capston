package com.hello.capston.controller.item;

import com.hello.capston.dto.dto.PagingDto;
import com.hello.capston.entity.*;
import com.hello.capston.repository.CommentRepository;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final CommentRepository commentRepository;

    private final LikeService likeService;
    private final ItemService itemService;

    private final ClickDuplicationPreventService clickService;
    private final PagingService pagingService;
    private final CacheRepository cacheRepository;
    private final MemberRepository memberRepository;

    /**
     * 아이템 리스트 조회
     * @param model
     * @param pageNow
     * @param session
     * @return
     */
    @GetMapping("/item_list")
    public String itemList(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNow, HttpSession session) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        PageRequest page = PageRequest.of(pageNow, 9);
        List<Item> findAll = itemRepository.findAllItem(page);

        PagingDto pagingDto = pagingService.paging(9, pageNow, itemRepository.count());

        String loginId = (String) session.getAttribute("loginId");
        Member findMember = cacheRepository.findMemberAtCache(loginId);

        if (findMember == null && loginId != null){
            findMember = memberRepository.findByLoginId(loginId).orElse(null);
            cacheRepository.addMember(findMember);
        }

        if (findMember != null) {
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("items", findAll);
        model.addAttribute("pageCount", pagingDto.getMap());
        model.addAttribute("lastPage", pagingDto.getTotalPage());

        return "item_list";
    }

    /**
     * 아이템 세부 내용 확인
     * @param id
     * @param model
     * @param session
     * @param pageNow
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/item_detail/{id}")
    @Transactional
    public String itemDetail(@PathVariable("id") Long id, Model model, HttpSession session,
                             @RequestParam(value = "page", defaultValue = "0") Integer pageNow,
                             HttpServletRequest request, HttpServletResponse response) {
        Item findItem = itemRepository.findById(id).orElse(new Item());
        List<String> categories = new ArrayList<>();

        String userEmail = (String) session.getAttribute("userEmail");
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = cacheRepository.findMemberAtCache(loginId);
        User findUser = cacheRepository.findUserAtCache(userEmail);
        List<Likes> findLikes = likeService.likeCount(id);

        List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(id);
        List<Item> items = itemRepository.findByItemName(findItem.getItemName());

        /**
         * 리뷰 페이징
         */
        if (pageNow != 0) {
            pageNow -= 1;
        }

        PageRequest page = PageRequest.of(pageNow, 3);
        List<Comment> findComment = commentRepository.findCommentByItemId(id, page);
        List<Comment> commentAllByItemId = commentRepository.findCommentAllByItemId(id);

        PagingDto pagingDto = pagingService.paging(3, pageNow, commentAllByItemId.size());

        model.addAttribute("pageCount", pagingDto.getMap());
        model.addAttribute("lastPage", pagingDto.getTotalPage());

        // 페이징 끝

        String[] category = findItem.getCategory().split(",");

        for (String s : category) {
            categories.add(s);
        }

        itemService.changeSizeToSoldOut(findItemDetail);
        clickService.viewCountUp(findItem, request, response);

        if (findMember == null && findUser == null) {
            Likes likes = new Likes();

            model.addAttribute("like", likes);
            model.addAttribute("item", findItem);
            model.addAttribute("likeCount", findLikes.size());
            model.addAttribute("itemDetail", findItemDetail);
            model.addAttribute("categories", categories);
            model.addAttribute("items", items);
            model.addAttribute("isLogin", 0);
            model.addAttribute("comment", findComment);
            return "item_detail";
        }

        if (findMember == null) {
            Likes likes = likeService.findByUserId(findUser.getId(), findItem.getId());

            model.addAttribute("like", likes);
            model.addAttribute("user", findUser);
            model.addAttribute("isLogin", 1);
        }

        if (findUser == null) {
            Likes likes = likeService.findByMemberId(findMember.getId(), findItem.getId());
            model.addAttribute("like", likes);
            model.addAttribute("member", findMember);
            model.addAttribute("isLogin", 1);
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("item", findItem);
        model.addAttribute("likeCount", findLikes.size());
        model.addAttribute("itemDetail", findItemDetail);
        model.addAttribute("categories", categories);
        model.addAttribute("items", items);
        model.addAttribute("comment", findComment);

        return "item_detail";
    }
}
