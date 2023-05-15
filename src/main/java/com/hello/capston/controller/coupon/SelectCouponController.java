package com.hello.capston.controller.coupon;

import com.hello.capston.dto.dto.PaymentDto;
import com.hello.capston.dto.dto.SelectCouponDto;
import com.hello.capston.entity.Coupon;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.TemporaryOrder;
import com.hello.capston.entity.User;
import com.hello.capston.repository.CouponRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.TemporaryOrderService;
import com.hello.capston.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SelectCouponController {

    private final CouponRepository couponRepository;

    private final TemporaryOrderService temporaryOrderService;
    private final CacheRepository cacheRepository;

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
        Map<String, Double> map = new HashMap<>();
        int totalPrice = 0;
        double percentage = 0;

        if (loginId == null) {
            User findUser = cacheRepository.findUserAtCache(userEmail);
            List<TemporaryOrder> findTOrder = temporaryOrderService.findTOrderListByUserId(findUser.getId());

            for (TemporaryOrder temporaryOrder : findTOrder) {
                totalPrice += temporaryOrder.getPrice() * temporaryOrder.getCount();
            }

            Coupon coupon = couponRepository.findByDetail(dto.getTarget()).orElse(null);

            percentage = 1 - (coupon.getPercentage() * 0.01);

            map.put("orderPrice", totalPrice * percentage);
            map.put("discountPrice", totalPrice - (totalPrice * percentage));
        }

        if (userEmail == null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);
            List<TemporaryOrder> findTOrder = temporaryOrderService.findTOrderListByMemberId(findMember.getId());

            for (TemporaryOrder temporaryOrder : findTOrder) {
                totalPrice += temporaryOrder.getPrice() * temporaryOrder.getCount();
            }

            Coupon coupon = couponRepository.findByDetail(dto.getTarget()).orElse(null);

            percentage = 1 - (coupon.getPercentage() * 0.01);

            map.put("orderPrice", totalPrice * percentage);
            map.put("discountPrice", totalPrice - (totalPrice * percentage));
        }

        return map;
    }
}
