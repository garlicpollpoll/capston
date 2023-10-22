package com.hello.capston.controller.payment;

import com.hello.capston.dto.dto.payment.LookUpPaymentDto;
import com.hello.capston.dto.dto.payment.PaymentCompleteDto;
import com.hello.capston.dto.dto.payment.PaymentDto;
import com.hello.capston.entity.*;
import com.hello.capston.repository.*;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.*;
import com.hello.capston.service.iamport.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final ItemDetailRepository itemDetailRepository;
    private final MemberWhoGetCouponRepository memberWhoGetCouponRepository;

    private final DeliveryService deliveryService;
    private final TemporaryOrderService temporaryOrderService;
    private final BucketService bucketService;
    private final MemberService memberService;
    private final UserService userService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final AlertService alertService;

    private final PaymentService paymentService;

    /**
     * 결제 페이지로 이동
     * @param model
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/payment")
    public String payment(Model model, HttpSession session, HttpServletResponse response) throws IOException {
        PaymentDto paymentDto = new PaymentDto();
        String loginId = (String) session.getAttribute("loginId");
        String userEmail = (String) session.getAttribute("userEmail");

        LookUpPaymentDto dto = paymentService.lookUpPayment(loginId, userEmail);

        if (!dto.getCheckStock()) {
            checkStockAndRedirect(response, dto.getMessage());
        }

        model.addAttribute("orderPrice", dto.getOrderPrice());
        model.addAttribute("tOrder", dto.getFindTOrder());
        model.addAttribute("tOrderSize", dto.getFindTOrderSize());
        model.addAttribute("itemName", dto.getItemName());
        model.addAttribute("status", dto.getRole());
        model.addAttribute("coupon", dto.getFindCoupon());
        model.addAttribute("member", dto.getMember());
        model.addAttribute("user", dto.getUser());
        model.addAttribute("payment", paymentDto);

        return "payment";
    }

    private void checkStockAndRedirect(HttpServletResponse response, String message) throws IOException {
        alertService.alertAndRedirect(response, message, "/bucket");
    }

//    @PostMapping("/paymentCompleteMember")
//    public synchronized String paymentCompleteMember(@RequestBody PaymentCompleteDto dto) {
//        Member findMember = memberService.findMemberById(Long.parseLong(dto.getMemberId()));
//        Delivery delivery = deliveryService.save();
//
//        Order order = orderService.saveUsingMember(findMember, delivery, dto);
//
//        List<TemporaryOrder> findTOrder = temporaryOrderService.findTOrderListByMemberId(findMember.getId());
//
//        orderItemService.saveUsingTemporaryOrder(findTOrder, order);
//
//        for (TemporaryOrder temporaryOrder : findTOrder) {
//            temporaryOrderService.delete(temporaryOrder);
//            bucketService.delete(temporaryOrder.getBucket());
//        }
//
//        return "redirect:/paymentComplete";
//    }
//
//    @PostMapping("/paymentCompleteUser")
//    public synchronized String paymentCompleteUser(@RequestBody PaymentCompleteDto dto) {
//        User findUser = userService.findUserById(Long.parseLong(dto.getUserId()));
//        Delivery delivery = deliveryService.save();
//
//        Order order = orderService.saveUsingUser(findUser, delivery, dto);
//
//        List<TemporaryOrder> findTOrder = temporaryOrderService.findTOrderListByUserId(findUser.getId());
//
//        orderItemService.saveUsingTemporaryOrder(findTOrder, order);
//
//        for (TemporaryOrder temporaryOrder : findTOrder) {
//            temporaryOrderService.delete(temporaryOrder);
//            bucketService.delete(temporaryOrder.getBucket());
//        }
//
//        return "redirect:/paymentComplete";
//    }

    /**
     * 결제 완료 로직
     * @param dto
     * @return
     */
    @PostMapping("/paymentComplete")
    @ResponseBody
    @Transactional
    public Map<String, String> paymentComplete(@RequestBody PaymentCompleteDto dto) {
        Member findMember = null;
        User findUser = null;
        List<TemporaryOrder> findTOrder = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        MemberWhoGetCoupon findMyCoupon = null;

        if (dto.getMemberId().equals("")) {
            findUser = userService.findUserById(Long.parseLong(dto.getUserId()));
            findMyCoupon = memberWhoGetCouponRepository.findByDetailAndUserId(dto.getTarget(), findUser.getId()).orElse(null);
            findTOrder = temporaryOrderService.findTOrderListByUserId(findUser.getId());
        }
        else if (dto.getUserId().equals("")) {
            findMember = memberService.findMemberById(Long.parseLong(dto.getMemberId()));
            findMyCoupon = memberWhoGetCouponRepository.findByDetailAndMemberId(dto.getTarget(), findMember.getId()).orElse(null);
            findTOrder = temporaryOrderService.findTOrderListByMemberId(findMember.getId());
        }

        boolean checkDelete = checkDeleteTOrderAndBucket(findTOrder);

        if (checkDelete) {
            for (TemporaryOrder temporaryOrder : findTOrder) {
                temporaryOrderService.delete(temporaryOrder);
                bucketService.delete(temporaryOrder.getBucket());
            }
            Delivery delivery = deliveryService.save();
            Order order = orderService.save(findUser, findMember, delivery, dto);
            orderItemService.saveUsingTemporaryOrder(findTOrder, order);

            if (findMyCoupon != null) {
                findMyCoupon.changeCheckUsedToOne();
            }

            map.put("message", "결제가 정상적으로 완료되었습니다.");
        }
        else {
            map.put("message", "선택하신 수량보다 재고가 존재하지 않습니다.");
        }

        return map;
    }

    private boolean checkDeleteTOrderAndBucket(List<TemporaryOrder> findTOrder) {
        for (TemporaryOrder temporaryOrder : findTOrder) {
            ItemDetail findItemDetail = itemDetailRepository.findByItemIdAndSize(temporaryOrder.getBucket().getItem().getId(), temporaryOrder.getSize());

            boolean isStockZero = checkStockEqualZero(findItemDetail, temporaryOrder.getCount());

            if (isStockZero) {
                return false;
            }
        }
        return true;
    }

    private boolean checkStockEqualZero(ItemDetail findItemDetail, int temporaryCount) {
        if (findItemDetail.getStock() - temporaryCount < 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
