package com.hello.capston.controller.coupon;

import com.hello.capston.dto.dto.coupon.CouponDto;
import com.hello.capston.entity.Coupon;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.AlertService;
import com.hello.capston.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final CouponRepository couponRepository;
    private final AlertService alertService;
    private final CacheRepository cacheRepository;

    /**
     * 쿠폰 조회
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/coupon")
    public String coupon(Model model, HttpSession session) {
        List<Coupon> findAllCoupon = couponRepository.findAll();

        String loginId = (String) session.getAttribute("loginId");
        Member findMember = cacheRepository.findMemberAtCache(loginId);

        model.addAttribute("coupon", findAllCoupon);
        model.addAttribute("status", findMember.getRole());

        return "coupon";
    }

    /**
     * 쿠폰 저장
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @PostMapping("/coupon")
    public String couponPost(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String code = request.getParameter("code");

        CouponDto coupon = couponService.isCoupon(authentication, code);
        boolean isCouponHas = coupon.isCoupon();
        String message = coupon.getMap().get("message");
        String url = coupon.getMap().get("url");

        if (isCouponHas) {
            alertService.alertAndRedirect(response, message, url);
        }
        else {
            alertService.alertAndRedirect(response, message, url);
        }

        return "coupon";
    }
}
