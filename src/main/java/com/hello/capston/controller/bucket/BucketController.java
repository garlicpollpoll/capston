package com.hello.capston.controller.bucket;

import com.hello.capston.dto.dto.BucketDto;
import com.hello.capston.dto.form.BucketForm;
import com.hello.capston.entity.*;
import com.hello.capston.repository.*;
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

    private final BucketRepository bucketRepository;
    private final ItemRepository itemRepository;
    private final TemporaryOrderRepository temporaryOrderRepository;

    private final MemberService memberService;
    private final UserService userService;
    private final BucketService bucketService;
    private final TemporaryOrderService temporaryOrderService;

    @PostMapping("/addBucket")
    public String addBucket(@RequestBody BucketForm form, HttpSession session, RedirectAttributes redirectAttributes) {
        Item findItem = itemRepository.findById(Long.parseLong(form.getId())).orElse(new Item());

        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");
        Integer orders = 0;

        Member findMember = memberService.findMember(loginId);
        User findUser = userService.findUser(userEmail);

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

    @GetMapping("/bucket")
    public String myBucket(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");
        String loginId = (String) session.getAttribute("loginId");

        Member findMember = memberService.findMember(loginId);
        User findUser = userService.findUser(userEmail);

        if (findMember == null) {
            List<Bucket> myBucket = bucketService.findBucketByUserId(findUser.getId());

            Integer totalAmount = bucketService.findTotalAmountByUserId(findUser.getId());

            model.addAttribute("bucket", myBucket);
            model.addAttribute("bucketCount", myBucket.size());
            model.addAttribute("totalAmount", totalAmount);
        }

        if (findUser == null) {
            List<Bucket> myBucket = bucketService.findBucketByMemberId(findMember.getId());

            Integer totalAmount = bucketService.findTotalAmountByMemberId(findMember.getId());

            model.addAttribute("bucket", myBucket);
            model.addAttribute("bucketCount", myBucket.size());
            model.addAttribute("totalAmount", totalAmount);
        }

        return "bucket";
    }

    @PostMapping("/cancelBucket")
    public String cancelBucket(@RequestBody BucketForm form) {
        Bucket findBucket = bucketService.findById(Long.parseLong(form.getId()));

        TemporaryOrder tOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(Long.parseLong(form.getId()));

        temporaryOrderRepository.delete(tOrder);
        bucketService.delete(findBucket);

        return "redirect:/bucket";
    }
}
