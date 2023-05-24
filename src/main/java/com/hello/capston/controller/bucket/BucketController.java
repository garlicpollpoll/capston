package com.hello.capston.controller.bucket;

import com.hello.capston.dto.dto.BucketDto;
import com.hello.capston.dto.form.BucketForm;
import com.hello.capston.entity.*;
import com.hello.capston.repository.*;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BucketController {

    private final ItemRepository itemRepository;
    private final TemporaryOrderRepository temporaryOrderRepository;

    private final BucketService bucketService;
    private final TemporaryOrderService temporaryOrderService;
    private final CacheRepository cacheRepository;

    /**
     * 장바구니 추가
     * @param form
     * @param session
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/addBucket")
    public String addBucket(@RequestBody BucketForm form, HttpSession session, RedirectAttributes redirectAttributes) {
        Item findItem = itemRepository.findById(Long.parseLong(form.getId())).orElse(new Item());

        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");
        Integer orders = 0;

        Member findMember = cacheRepository.findMemberAtCache(loginId);
        User findUser = cacheRepository.findUserAtCache(userEmail);

        if (findMember == null) {
            List<Bucket> findBucket = bucketService.findBucketByUserId(findUser.getId());
            orders = findBucket.size();
        }

        if (findUser == null) {
            List<Bucket> findBucket = bucketService.findBucketByMemberId(findMember.getId());
            orders = findBucket.size();
        }

        if (orders == null) {
            orders = 1;
        }

        Bucket bucket = bucketService.save(findMember, findUser, findItem, orders);

        temporaryOrderService.save(bucket, findItem.getPrice(), form.getSize());

        redirectAttributes.addAttribute("itemId", form.getId());

        return "redirect:/item_detail/{itemId}";
    }

    /**
     * 장바구니 조회
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/bucket")
    public String myBucket(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = cacheRepository.findMemberAtCache(loginId);
        User findUser = cacheRepository.findUserAtCache(userEmail);

        if (findMember == null) {
            List<TemporaryOrder> myBucket = temporaryOrderService.findTOrderListByUserId(findUser.getId());

            Integer totalAmount = bucketService.findTotalAmountByUserId(findUser.getId());

            model.addAttribute("bucket", myBucket);
            model.addAttribute("bucketCount", myBucket.size());
            model.addAttribute("totalAmount", totalAmount);
        }

        if (findUser == null) {
            List<TemporaryOrder> myBucket = temporaryOrderService.findTOrderListByMemberId(findMember.getId());

            Integer totalAmount = bucketService.findTotalAmountByMemberId(findMember.getId());

            model.addAttribute("bucket", myBucket);
            model.addAttribute("bucketCount", myBucket.size());
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("status", findMember.getRole());
        }

        return "bucket";
    }

    /**
     * 장바구니 선택 후 취소
     * @param form
     * @return
     */
    @PostMapping("/cancelBucket")
    public String cancelBucket(@RequestBody BucketForm form) {
        Bucket findBucket = bucketService.findById(Long.parseLong(form.getId()));

        TemporaryOrder tOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(Long.parseLong(form.getId()));

        temporaryOrderRepository.delete(tOrder);
        bucketService.delete(findBucket);

        return "redirect:/bucket";
    }
}
