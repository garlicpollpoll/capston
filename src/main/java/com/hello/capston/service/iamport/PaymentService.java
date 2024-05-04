package com.hello.capston.service.iamport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hello.capston.absctracts.policy.PaymentPolicy;
import com.hello.capston.absctracts.policy.config.PolicyManager;
import com.hello.capston.dto.dto.payment.LookUpPaymentCompleteDto;
import com.hello.capston.dto.dto.payment.LookUpPaymentDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.entity.enums.OrderStatus;
import com.hello.capston.repository.*;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.BucketService;
import com.hello.capston.service.TemporaryOrderService;
import com.hello.capston.service.WhatIsRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private static final String apikey = "6095336006008053";
    private static final String secretKey = "78cf233a1240b3f2c2356d42f973a292b05b2b474736fc3cda55b98946ff394a0ad9d07e8fe9f569";

    private final CacheRepository cacheRepository;
    private final OrderItemRepository orderItemRepository;

    private final BucketRepository bucketRepository;
    private final TemporaryOrderRepository temporaryOrderRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;
    private final WhatIsRoleService roleService;

    private final PolicyManager policyManager;

    public String getToken() throws IOException {
        HttpsURLConnection conn = null;

        URL url = new URL("https://api.iamport.kr/users/getToken");

        conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        JsonObject json = new JsonObject();

        json.addProperty("imp_key", apikey);
        json.addProperty("imp_secret", secretKey);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

        bw.write(json.toString());
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        Gson gson = new Gson();

        String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

        log.info("response : {}", response);

        String token = gson.fromJson(response, Map.class).get("access_token").toString();

        br.close();
        conn.disconnect();

        return token;
    }

    public void paymentCancel(String accessToken, String imp_uid, int amount, String reason) throws IOException {
        log.info("결제 취소");
        log.info("access_token = {}", accessToken);
        log.info("imp_uid = {}", imp_uid);

        HttpsURLConnection conn = null;
        URL url = new URL("https://api.iamport.kr/payments/cancel");

        conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", accessToken);

        conn.setDoOutput(true);

        JsonObject json = new JsonObject();

        json.addProperty("reason", reason);
        json.addProperty("imp_uid", imp_uid);
        json.addProperty("amount", amount);
        json.addProperty("checksum", amount);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

        bw.write(json.toString());
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

        br.close();
        conn.disconnect();
    }

    @Transactional
    public LookUpPaymentCompleteDto paymentComplete(Authentication authentication) {
        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        PaymentPolicy policy = policyManager.paymentPolicy(memberRole);
        LookUpPaymentCompleteDto dto = policy.paymentComplete(username);

        return dto;
    }

    @Transactional
    public LookUpPaymentDto lookUpPayment(Authentication authentication) {
        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        PaymentPolicy policy = policyManager.paymentPolicy(memberRole);
        LookUpPaymentDto dto = policy.lookUpPayment(username);

        return dto;
    }
}
