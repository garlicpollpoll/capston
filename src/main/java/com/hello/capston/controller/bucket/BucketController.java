package com.hello.capston.controller.bucket;

import com.hello.capston.dto.dto.bucket.LookUpBucketDto;
import com.hello.capston.dto.form.BucketForm;
import com.hello.capston.repository.*;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class BucketController {


    private final BucketService bucketService;

    /**
     * 장바구니 추가
     * @param form
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/addBucket")
    public String addBucket(@RequestBody BucketForm form, RedirectAttributes redirectAttributes, Authentication authentication) {
        bucketService.addBucket(authentication, form);

        redirectAttributes.addAttribute("itemId", form.getId());

        return "redirect:/item_detail/{itemId}";
    }

    /**
     * 장바구니 조회
     * @param model
     * @return
     */
    @GetMapping("/bucket")
    public String myBucket(Model model, Authentication authentication) {
        LookUpBucketDto dto = bucketService.lookUpMyBucket(authentication);

        model.addAttribute("bucket", dto.getMyBucket());
        model.addAttribute("bucketCount", dto.getBucketSize());
        model.addAttribute("totalAmount", dto.getTotalAmount());
        model.addAttribute("status", dto.getRole());

        return "bucket";
    }

    /**
     * 장바구니 선택 후 취소
     * @param form
     * @return
     */
    @PostMapping("/cancelBucket")
    public String cancelBucket(@RequestBody BucketForm form) {
        bucketService.cancelBucket(form);

        return "redirect:/bucket";
    }
}
