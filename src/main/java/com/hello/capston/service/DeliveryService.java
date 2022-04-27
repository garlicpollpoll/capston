package com.hello.capston.service;

import com.hello.capston.entity.Delivery;
import com.hello.capston.entity.enums.DeliveryStatus;
import com.hello.capston.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Delivery save() {
        Delivery delivery = new Delivery(DeliveryStatus.READY);
        deliveryRepository.save(delivery);

        return delivery;
    }
}
