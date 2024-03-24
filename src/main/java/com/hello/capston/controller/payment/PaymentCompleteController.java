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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
     * @return
     */
    @GetMapping("/paymentComplete")
    public String paymentComplete(Model model, Authentication authentication) {
        LookUpPaymentCompleteDto dto = paymentService.paymentComplete(authentication);

        model.addAttribute("tOrder", dto.getFindOrderItem());
        // TODO 이거 Order 에 있는 Delivery 기준 status 보고 가져 와야 할 것 같다.
        model.addAttribute("status", dto.getRole());
        model.addAttribute("orderPrice", dto.getOrderPrice());

        return "payment_complete";
    }
}
