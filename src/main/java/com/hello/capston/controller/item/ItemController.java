package com.hello.capston.controller.item;

import com.hello.capston.entity.*;
import com.hello.capston.repository.CommentRepository;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
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

    private final MemberService memberService;
    private final UserService userService;
    private final LikeService likeService;
    private final ItemService itemService;

    private final ClickDuplicationPreventService clickService;

    @GetMapping("/item_list")
    public String itemList(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNow, HttpSession session) {
        if (pageNow != 0) {
            pageNow -= 1;
        }

        PageRequest page = PageRequest.of(pageNow, 9);
        List<Item> findAll = itemRepository.findAllItem(page);

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
        } else if (pageEnd == totalPage + 1) { // 마지막 하나 전 페이지
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 3;
            }
        } else if (pageEnd == totalPage + 2) { // 마지막 페이지
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

        String loginId = (String) session.getAttribute("loginId");
        Member findMember = memberService.findMember(loginId);

        if (findMember != null) {
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("pageCount", map);
        model.addAttribute("lastPage", totalPage);

        model.addAttribute("items", findAll);

        return "item_list";
    }

    @GetMapping("/item_detail/{id}")
    @Transactional
    public String itemDetail(@PathVariable("id") Long id, Model model, HttpSession session,
                             @RequestParam(value = "page", defaultValue = "0") Integer pageNow,
                             HttpServletRequest request, HttpServletResponse response) {
        Item findItem = itemRepository.findById(id).orElse(new Item());
        List<String> categories = new ArrayList<>();

        String userEmail = (String) session.getAttribute("userEmail");
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberService.findMember(loginId);
        User findUser = userService.findUser(userEmail);
        List<Likes> findLikes = likeService.likeCount(id);

        List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(id);
        List<Item> items = itemRepository.itemListByItemName(findItem.getItemName());

        /**
         * 리뷰 페이징
         */
        if (pageNow != 0) {
            pageNow -= 1;
        }

        PageRequest page = PageRequest.of(pageNow, 3);
        List<Comment> findComment = commentRepository.findCommentByItemId(id, page);
        List<Comment> commentAllByItemId = commentRepository.findCommentAllByItemId(id);

        pageNow += 1;

        long pageStart, pageEnd;

        long size = commentAllByItemId.size();

        long totalPage = 0;

        if (size % 3 == 0) {
            totalPage = size / 3;
        }
        else {
            totalPage = size / 3 + 1;
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
        } else if (pageEnd == totalPage + 1) { // 마지막 하나 전 페이지
            pageEnd = totalPage;
            if (totalPage < 5) {
                pageStart = 1;
            }
            else {
                pageStart = pageNow - 3;
            }
        } else if (pageEnd == totalPage + 2) { // 마지막 페이지
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
