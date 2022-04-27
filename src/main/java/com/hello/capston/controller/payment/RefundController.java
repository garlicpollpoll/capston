package com.hello.capston.controller.payment;

import com.hello.capston.dto.dto.CancelDto;
import com.hello.capston.service.iamport.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RefundController {

    private final PaymentService paymentService;

    @PostMapping("/payment_cancel")
    @ResponseBody
    public Map<String, String> cancel(@RequestBody CancelDto dto) throws IOException {
        Map<String, String> map = new HashMap<>();

        String token = paymentService.getToken();

        paymentService.paymentCancel(token, dto.getImp_uid(), Integer.parseInt(dto.getAmount()), dto.getReason());

        map.put("message", "결제가 취소되었습니다.");

        return map;
    }
}
