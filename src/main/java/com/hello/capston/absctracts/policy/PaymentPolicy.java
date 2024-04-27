package com.hello.capston.absctracts.policy;

import com.hello.capston.dto.dto.payment.LookUpPaymentCompleteDto;
import com.hello.capston.dto.dto.payment.LookUpPaymentDto;

public interface PaymentPolicy {

    LookUpPaymentCompleteDto paymentComplete(String username);

    LookUpPaymentDto lookUpPayment(String username);
}
