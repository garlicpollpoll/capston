package com.hello.capston.controller.payment;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.OrderItem;
import com.hello.capston.entity.TemporaryOrder;
import com.hello.capston.entity.User;
import com.hello.capston.repository.OrderItemRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.TemporaryOrderService;
import com.hello.capston.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PaymentCompleteController {

    private final OrderItemRepository orderItemRepository;
    private final CacheRepository cacheRepository;

    /**
     * 결제 완료 페이지로 이동
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/paymentComplete")
    public String paymentComplete(Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        String loginId = (String) session.getAttribute("loginId");
        int orderPrice = 0;

        Member findMember = cacheRepository.findMemberAtCache(loginId);
        User findUser = cacheRepository.findUserAtCache(userEmail);

        if (findMember == null) {
            List<OrderItem> findTOrder = orderItemRepository.findOrdersByUserId(findUser.getId());
            for (OrderItem orderItem : findTOrder) {
                orderPrice += orderItem.getCount() * orderItem.getOrderPrice();
            }
            model.addAttribute("tOrder", findTOrder);
        }

        if (findUser == null) {
            List<OrderItem> findTOrder = orderItemRepository.findOrdersByMemberId(findMember.getId());
            for (OrderItem orderItem : findTOrder) {
                orderPrice += orderItem.getCount() * orderItem.getOrderPrice();
            }
            model.addAttribute("tOrder", findTOrder);
            model.addAttribute("status", findMember.getRole());
        }

        model.addAttribute("orderPrice", orderPrice);

        return "payment_complete";
    }
}
