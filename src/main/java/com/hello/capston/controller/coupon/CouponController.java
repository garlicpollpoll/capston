package com.hello.capston.controller.coupon;

import com.hello.capston.entity.Coupon;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.MemberWhoGetCoupon;
import com.hello.capston.entity.User;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.repository.MemberWhoGetCouponRepository;
import com.hello.capston.service.AlertService;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.UserService;
import lombok.RequiredArgsConstructor;
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

    private final CouponRepository couponRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;
    private final MemberService memberService;
    private final UserService userService;
    private final AlertService alertService;

    @GetMapping("/coupon")
    public String coupon(Model model, HttpSession session) {
        List<Coupon> findAllCoupon = couponRepository.findAll();

        String loginId = (String) session.getAttribute("loginId");
        Member findMember = memberService.findMember(loginId);

        model.addAttribute("coupon", findAllCoupon);
        model.addAttribute("status", findMember.getRole());

        return "coupon";
    }

    @PostMapping("/coupon")
    public String couponPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");
        String code = request.getParameter("code");

        Coupon coupon = couponRepository.findByCode(code).orElse(null);

        if (loginId == null) {
            User findUser = userService.findUser(userEmail);
            List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByUserId(findUser.getId());

            if (findCoupon.isEmpty()) {
                memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(findUser, null, coupon, 0));
            }

            for (MemberWhoGetCoupon memberWhoGetCoupon : findCoupon) {
                if (memberWhoGetCoupon.getCoupon().getCode().equals(code)) {
                    alertService.alertAndRedirect(response, "이미 가지고있는 쿠폰입니다.", "/coupon");
                    return "coupon";
                }
                else {
                    memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(findUser, null, coupon, 0));
                }
            }
        }

        if (userEmail == null) {
            Member findMember = memberService.findMember(loginId);
            List<MemberWhoGetCoupon> findCoupon = memberWhoGetCouponRepository.findCouponByMemberId(findMember.getId());

            if (findCoupon.isEmpty()) {
                memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(null, findMember, coupon, 0));
            }

            for (MemberWhoGetCoupon memberWhoGetCoupon : findCoupon) {
                if (memberWhoGetCoupon.getCoupon().getCode().equals(code)) {
                    alertService.alertAndRedirect(response, "이미 가지고있는 쿠폰입니다.", "/coupon");
                    return "coupon";
                }
                else {
                    memberWhoGetCouponRepository.save(new MemberWhoGetCoupon(null, findMember, coupon, 0));
                }
            }
        }

        alertService.alertAndRedirect(response, "쿠폰이 등록되었습니다.", "/");

        return "coupon";
    }
}
