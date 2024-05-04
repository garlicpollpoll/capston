package com.hello.capston.controller.payment;

import com.hello.capston.dto.dto.payment.LookUpPaymentCompleteDto;
import com.hello.capston.service.iamport.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


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
        model.addAttribute("status", dto.getRole());
        model.addAttribute("orderPrice", dto.getOrderPrice());

        return "payment_complete";
    }
}
