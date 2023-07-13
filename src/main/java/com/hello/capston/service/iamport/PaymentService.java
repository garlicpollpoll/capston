package com.hello.capston.service.iamport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hello.capston.dto.dto.payment.LookUpPaymentCompleteDto;
import com.hello.capston.dto.dto.payment.LookUpPaymentDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.*;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.BucketService;
import com.hello.capston.service.TemporaryOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public LookUpPaymentCompleteDto paymentComplete(String loginId, String userEmail) {
        int orderPrice = 0;
        Member findMember = null;
        User findUser = null;
        List<OrderItem> findOrderItem = new ArrayList<>();
        MemberRole role = null;

        if (loginId == null && userEmail != null) {
            findUser = cacheRepository.findUserAtCache(userEmail);
            findOrderItem = orderItemRepository.findOrdersByUserId(findUser.getId());
            for (OrderItem orderItem : findOrderItem) {
                orderPrice += orderItem.getCount() * orderItem.getOrderPrice();
            }
        }

        if (userEmail == null && loginId != null) {
            findMember = cacheRepository.findMemberAtCache(loginId);
            findOrderItem = orderItemRepository.findOrdersByMemberId(findMember.getId());
            for (OrderItem orderItem : findOrderItem) {
                orderPrice += orderItem.getCount() * orderItem.getOrderPrice();
            }
            role = findMember.getRole();
        }

        return new LookUpPaymentCompleteDto(findOrderItem, role, orderPrice);
    }

    public LookUpPaymentDto lookUpPayment(String loginId, String userEmail) {
        int orderPrice = 0;
        boolean checkStock = true;
        Member findMember = null;
        User findUser = null;
        List<TemporaryOrder> findTOrder = new ArrayList<>();
        List<MemberWhoGetCoupon> findCoupon = new ArrayList<>();
        String itemName = null;
        String message = null;
        MemberRole role = null;

        if (loginId == null && userEmail != null) {
            findUser = cacheRepository.findUserAtCache(userEmail);
            List<Bucket> findBucket = bucketRepository.findByUserId(findUser.getId());

            for (Bucket bucket : findBucket) {
                TemporaryOrder findTemporaryOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(bucket.getId());

                List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(bucket.getItem().getId());

                Map<String, Object> map = checkStockAndRedirect(findTemporaryOrder, findItemDetail);
                checkStock = (boolean) map.get("checkStock");
                message = (String) map.get("message");

                orderPrice += findTemporaryOrder.getCount() * findTemporaryOrder.getPrice();
                itemName = bucket.getItem().getItemName();
            }

            findTOrder = temporaryOrderRepository.findTemporaryOrderByUserId(findUser.getId());

            findCoupon = memberWhoGetCouponRepository.findCouponByUserIdAndCheckUsed(findUser.getId(), 0);
        }

        if (userEmail == null && loginId != null) {
            findMember = cacheRepository.findMemberAtCache(loginId);
            List<Bucket> findBucket = bucketRepository.findByMemberId(findMember.getId());

            for (Bucket bucket : findBucket) {
                TemporaryOrder findTemporaryOrder = temporaryOrderRepository.findTemporaryOrderByBucketId(bucket.getId());


                List<ItemDetail> findItemDetail = itemDetailRepository.findByItemId(bucket.getItem().getId());

                Map<String, Object> map = checkStockAndRedirect(findTemporaryOrder, findItemDetail);
                checkStock = (boolean) map.get("checkStock");
                message = (String) map.get("message");

                orderPrice += findTemporaryOrder.getCount() * findTemporaryOrder.getPrice();
                itemName = bucket.getItem().getItemName();
            }

            role = findMember.getRole();

            findTOrder = temporaryOrderRepository.findTemporaryOrderByMemberId(findMember.getId());

            findCoupon = memberWhoGetCouponRepository.findCouponByMemberIdAndCheckUsed(findMember.getId(), 0);
        }

        return new LookUpPaymentDto(findCoupon, findTOrder, orderPrice, findTOrder.size(), itemName, role, checkStock, findMember, findUser, message);
    }

    private Map<String, Object> checkStockAndRedirect(TemporaryOrder findTemporaryOrder, List<ItemDetail> findItemDetail) {
        Map<String, Object> map = new HashMap<>();
        for (ItemDetail itemDetail : findItemDetail) {
            if (findTemporaryOrder.getSize().equals(itemDetail.getSize())) {
                if (itemDetail.getStock() - findTemporaryOrder.getCount() < 0) {
                    map.put("checkStock", false);
                    map.put("message", "재고가 남아있지 않습니다. 상품이름 : " + itemDetail.getItem().getViewName() + "/ 남은 재고 : " + itemDetail.getStock());
                    return map;
                }
            }
        }
        map.put("checkStock", true);
        return map;
    }
}
