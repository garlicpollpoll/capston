package com.hello.capston.controller.like;

import com.hello.capston.dto.form.LikeForm;
import com.hello.capston.entity.*;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.repository.cache.KeyGenerator;
import com.hello.capston.service.*;
import io.lettuce.core.support.caching.CacheFrontend;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LikeToBucketController {

    private final LikeRepository likeRepository;

    private final ItemRepository itemRepository;

    private final ItemService itemService;
    private final BucketService bucketService;
    private final TemporaryOrderService temporaryOrderService;
    private final CacheRepository cacheRepository;
    private final CacheFrontend<String, Member> frontend;

    /**
     * 좋아요 리스트에서 장바구니로 이동
     * @param form
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/go_to_bucket")
    public Map<String, String> goToBucket(@RequestBody LikeForm form, Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        Map<String, String> map = new HashMap<>();

        Likes findLike = likeRepository.findById(Long.parseLong(form.getId())).orElse(null);

        Item findItem = itemRepository.findById(findLike.getItem().getId()).orElse(null);

        Integer orders = null;

//        Member findMember = cacheRepository.findMemberAtCache(username);
        String key = KeyGenerator.memberKeyGenerate(username);
        Member findMember = frontend.get(key);

        List<Bucket> findBucket = bucketService.findBucketByMemberId(findMember.getId());
        orders = findBucket.size();

        if (orders == null) {
            orders = 1;
        }

        boolean isStockZero = itemService.checkItemStock(findItem, findLike.getSize());

        if (isStockZero) {
            Bucket bucket = bucketService.save(findMember, null, findItem, orders);

            temporaryOrderService.save(bucket, findItem.getPrice(), findLike.getSize());

            map.put("message", "정상적으로 장바구니에 담았습니다");
        }
        else {
            map.put("message", "품절인 상품은 장바구니에 담을 수 없습니다.");
        }

        return map;
    }
}
