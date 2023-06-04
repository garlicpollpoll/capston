package com.hello.capston.controller.payment;

import com.hello.capston.dto.dto.payment.LookUpPaymentCompleteDto;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.OrderItem;
import com.hello.capston.entity.TemporaryOrder;
import com.hello.capston.entity.User;
import com.hello.capston.repository.OrderItemRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.MemberService;
import com.hello.capston.service.TemporaryOrderService;
import com.hello.capston.service.UserService;
import com.hello.capston.service.iamport.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PaymentCompleteController {

    private final PaymentService paymentService;

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

        LookUpPaymentCompleteDto dto = paymentService.paymentComplete(loginId, userEmail);

        model.addAttribute("tOrder", dto.getFindOrderItem());
        model.addAttribute("status", dto.getRole());
        model.addAttribute("orderPrice", dto.getOrderPrice());

        return "payment_complete";
    }
}
