package com.hello.capston.controller.like;

import com.hello.capston.dto.form.LikeForm;
import com.hello.capston.dto.form.LikeFormWithSize;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Likes;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.UserRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.ItemService;
import com.hello.capston.service.LikeService;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LIkeController {

    private final ItemRepository itemRepository;

    private final LikeService likeService;
    private final CacheRepository cacheRepository;
    private final MemberRepository memberRepository;

    /**
     * 좋아요 클릭 시 좋아요 목록에 담아짐
     * @param form
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/like")
    public String like(@RequestBody LikeFormWithSize form, Authentication authentication, RedirectAttributes redirectAttributes) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        Item findItem = itemRepository.findById(Long.parseLong(form.getId())).orElse(new Item());

        Member findMember = cacheRepository.findMemberAtCache(username);

        likeService.save(findMember, null, findItem, form.getSize());

        redirectAttributes.addAttribute("itemId", form.getId());

        return "redirect:item_detail/{itemId}";
    }

    /**
     * 좋아요 취소 시 좋아요 목록에서 없어짐
     * @param form
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/dislike")
    public String dislike(@RequestBody LikeForm form, Authentication authentication, RedirectAttributes redirectAttributes) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        Member findMember = cacheRepository.findMemberAtCache(username);

        Likes like = likeService.findByMemberId(findMember.getId(), Long.parseLong(form.getId()));
        likeService.delete(like);

        redirectAttributes.addAttribute("itemId", form.getId());

        return "redirect:/item_detail/{itemId}";
    }
}
