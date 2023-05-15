package com.hello.capston.controller.coupon;

import com.hello.capston.S3.S3Uploader;
import com.hello.capston.dto.dto.CouponUploadDto;
import com.hello.capston.entity.Coupon;
import com.hello.capston.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class CouponUploadController {

    private final S3Uploader s3Uploader;
    private final CouponRepository couponRepository;

    /**
     * 단순 쿠폰 업로드 페이지 redirect
     * @return
     */
    @GetMapping("/coupon_upload")
    public String couponUpload() {
        return "coupon_upload";
    }

    /**
     * ADMIN 등급에 의해 쿠폰 등록
     * @param dto
     * @return
     * @throws IOException
     */
    @PostMapping("/coupon_upload")
    public String couponUploadPost(@ModelAttribute("coupon")CouponUploadDto dto) throws IOException {
        MultipartFile couponImage = dto.getCouponImage();

        String couponUpload = s3Uploader.upload(couponImage, "static");

        Coupon coupon = new Coupon(couponUpload, dto.getCouponDetail(), Integer.parseInt(dto.getPercentage()), dto.getExpireDate(), dto.getCouponCode());

        couponRepository.save(coupon);

        return "redirect:/coupon";
    }
}
