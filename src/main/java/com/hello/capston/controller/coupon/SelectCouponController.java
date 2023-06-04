package com.hello.capston.controller.coupon;

import com.hello.capston.dto.dto.SelectCouponDto;
import com.hello.capston.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SelectCouponController {

    private final CouponService couponService;

    /**
     * 결제 창에서 쿠폰 선택했을 때
     * @param dto
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/selectCoupon")
    public Map<String, Double> selectCoupon(@RequestBody SelectCouponDto dto, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        Map<String, Double> map = couponService.selectCoupon(loginId, userEmail, dto);

        return map;
    }
}
