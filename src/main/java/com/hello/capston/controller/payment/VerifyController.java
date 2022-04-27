package com.hello.capston.controller.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@Slf4j
public class VerifyController {

    private IamportClient api;

    public VerifyController() {
        this.api = new IamportClient("6095336006008053", "78cf233a1240b3f2c2356d42f973a292b05b2b474736fc3cda55b98946ff394a0ad9d07e8fe9f569");
    }

    @ResponseBody
    @PostMapping("/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid) throws IamportResponseException, IOException {
        log.info("verify complete");
        return api.paymentByImpUid(imp_uid);
    }
}
