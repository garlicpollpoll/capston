package com.hello.capston.repository;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.Likes;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LikeRepositoryTest {

    @Autowired ItemRepository itemRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired LikeRepository likeRepository;

    @Test
    @DisplayName("좋아요를 클릭하면 좋아요가 하나 늘어야 한다.")
    public void likeAdd() throws Exception {
        //given
        Member member = new Member("임경석", "ks3254", "ks32541007!", "981007", MemberRole.ROLE_ADMIN);
        Item item = new Item("상품A", null);
        Likes like = new Likes(member, null, item, "좋아요");
        //when
        memberRepository.save(member);
        itemRepository.save(item);
        likeRepository.save(like);

        List<Likes> likes = likeRepository.likeCount(item.getId());
        //then
        Assertions.assertThat(likes.size()).isEqualTo(1);

    }

}