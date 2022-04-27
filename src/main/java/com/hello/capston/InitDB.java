package com.hello.capston;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.ItemDetail;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.ItemDetailRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final BCryptPasswordEncoder encoder;

    @PostConstruct
    public void init() {
        String password = "123";
        String encode = encoder.encode(password);
        Member member = new Member("ks3254", encode, "981007", "남자", MemberRole.ROLE_ADMIN);
        memberRepository.save(member);

        Item itemA = new Item("상품A", "상품A", "https://ks-capston.s3.ap-northeast-2.amazonaws.com/static/7d34cab0-718d-444c-b487-a6e8ecf450d4heart.jpg", 25000, "1234", member, "#상의,#후드티,#오버핏,#남녀공용", "파랑", 11);
        Item itemB = new Item("상품B", "상품B", "https://ks-capston.s3.ap-northeast-2.amazonaws.com/static/bda0f2ab-d07b-4aa4-adc7-8e91b1c79fe4%EB%94%94%EC%BD%94%ED%94%84%EC%82%AC.png", 35000, "5678", member, "#상의,#반팔티,#오버핏", "파랑", 7);
        Item itemC = new Item("2 TONE ARCH HOODIE GRAY","2 TONE ARCH HOODIE", "https://ks-capston.s3.ap-northeast-2.amazonaws.com/static/47ed10b9-b62d-4686-b237-e0ce5abd222e%EC%83%81%EC%9D%981.jpg", 55300, "YA8HD1071GR", member, "#상의,#후드티,#오버핏,#남성용,#로고", "회색", 3);
        Item itemD = new Item("2 TONE ARCH HOODIE BLUE", "2 TONE ARCH HOODIE", "https://ks-capston.s3.ap-northeast-2.amazonaws.com/static/d22c9fd3-1a52-42b2-8d5f-9ec3c9703b57%EC%83%81%EC%9D%982.jpg", 55300, "YC1HD0011RB", member, "#상의,#후드티,#오버핏,#남성용,#로고", "파랑", 5);
        Item itemE = new Item("2 TONE ARCH HOODIE NAVY BLUE", "2 TONE ARCH HOODIE", "https://ks-capston.s3.ap-northeast-2.amazonaws.com/static/6074bee4-9468-4280-8972-9521aa9b1bbb%EC%83%81%EC%9D%983.jpg", 55300, "YC1HD0011DB", member, "#상의,#후드티,#오버핏,#남성용,#로고", "군청", 10);
        Item itemF = new Item("2 TONE ARCH HOODIE NAVY", "2 TONE ARCH HOODIE", "https://ks-capston.s3.ap-northeast-2.amazonaws.com/static/3d5c2de2-16de-46ba-84fc-d5c6a3841060%EC%83%81%EC%9D%984.jpg", 55300, "YB9HG0011NA", member, "#상의,#후드티,#오버핏,#남성용,#로고", "남색", 15);
        Item itemG = new Item("2 TONE ARCH HOODIE BLACK", "2 TONE ARCH HOODIE", "https://ks-capston.s3.ap-northeast-2.amazonaws.com/static/4e77d4ae-34db-43c6-91c2-a3e18e477945%EC%83%81%EC%9D%985.jpg", 55300, "YB9HG0011BK", member, "#상의,#후드티,#오버핏,#남성용,#로고", "검정", 12);

        itemRepository.save(itemA);
        itemRepository.save(itemB);
        itemRepository.save(itemC);
        itemRepository.save(itemD);
        itemRepository.save(itemE);
        itemRepository.save(itemF);
        itemRepository.save(itemG);

        ItemDetail itemDetail1 = new ItemDetail(100, "S", itemA);
        ItemDetail itemDetail2 = new ItemDetail(100, "M", itemA);
        ItemDetail itemDetail3 = new ItemDetail(100, "S", itemB);
        ItemDetail itemDetail4 = new ItemDetail(100, "M", itemB);

        ItemDetail itemDetail5 = new ItemDetail(0, "S", itemC);
        ItemDetail itemDetail6 = new ItemDetail(0, "M", itemC);
        ItemDetail itemDetail7 = new ItemDetail(2, "L", itemC);
        ItemDetail itemDetail8 = new ItemDetail(100, "XL", itemC);

        ItemDetail itemDetail9 = new ItemDetail(100, "S", itemD);
        ItemDetail itemDetail10 = new ItemDetail(100, "M", itemD);
        ItemDetail itemDetail11 = new ItemDetail(100, "L", itemD);
        ItemDetail itemDetail12 = new ItemDetail(100, "XL", itemD);

        ItemDetail itemDetail13 = new ItemDetail(100, "S", itemE);
        ItemDetail itemDetail14 = new ItemDetail(100, "M", itemE);
        ItemDetail itemDetail15 = new ItemDetail(100, "L", itemE);
        ItemDetail itemDetail16 = new ItemDetail(100, "XL", itemE);

        ItemDetail itemDetail17 = new ItemDetail(100, "S", itemF);
        ItemDetail itemDetail18 = new ItemDetail(100, "M", itemF);
        ItemDetail itemDetail19 = new ItemDetail(100, "L", itemF);
        ItemDetail itemDetail20 = new ItemDetail(100, "XL", itemF);

        ItemDetail itemDetail21 = new ItemDetail(100, "S", itemG);
        ItemDetail itemDetail22 = new ItemDetail(100, "M", itemG);
        ItemDetail itemDetail23 = new ItemDetail(100, "L", itemG);
        ItemDetail itemDetail24 = new ItemDetail(100, "XL", itemG);

        itemDetailRepository.save(itemDetail1);
        itemDetailRepository.save(itemDetail2);
        itemDetailRepository.save(itemDetail3);
        itemDetailRepository.save(itemDetail4);
        itemDetailRepository.save(itemDetail5);
        itemDetailRepository.save(itemDetail6);
        itemDetailRepository.save(itemDetail7);
        itemDetailRepository.save(itemDetail8);
        itemDetailRepository.save(itemDetail9);
        itemDetailRepository.save(itemDetail10);
        itemDetailRepository.save(itemDetail11);
        itemDetailRepository.save(itemDetail12);
        itemDetailRepository.save(itemDetail13);
        itemDetailRepository.save(itemDetail14);
        itemDetailRepository.save(itemDetail15);
        itemDetailRepository.save(itemDetail16);
        itemDetailRepository.save(itemDetail17);
        itemDetailRepository.save(itemDetail18);
        itemDetailRepository.save(itemDetail19);
        itemDetailRepository.save(itemDetail20);
        itemDetailRepository.save(itemDetail21);
        itemDetailRepository.save(itemDetail22);
        itemDetailRepository.save(itemDetail23);
        itemDetailRepository.save(itemDetail24);

    }
}
