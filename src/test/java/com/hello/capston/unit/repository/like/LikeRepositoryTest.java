package com.hello.capston.unit.repository.like;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.Likes;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.UserRepository;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class LikeRepositoryTest {

    @Autowired
    LikeRepository likeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("해당 상품의 좋아요 수를 위해 실행하는 쿼리")
    public void test() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Likes like = Data.createLike(member, null, item);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);
        likeRepository.save(like);

        List<Likes> findLike = likeRepository.likeCount(saveItem.getId());
        //then
        Assertions.assertNotNull(findLike);
        Assertions.assertEquals(1, findLike.size());
    }

    @Test
    @DisplayName("Member Id 와 Item Id 를 이용해 좋아요를 특정해서 가져옴")
    public void test2() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Likes like = Data.createLike(member, null, item);
        //when
        Member saveMember = memberRepository.save(member);
        Item saveItem = itemRepository.save(item);
        likeRepository.save(like);

        Likes findLike = likeRepository.findByMemberId(saveMember.getId(), saveItem.getId()).orElse(null);
        //then
        Assertions.assertNotNull(findLike);
        Assertions.assertEquals(like.getSize(), findLike.getSize());
    }

    @Test
    @DisplayName("User Id 와 Item Id 를 이용해 좋아요를 특정해서 가져옴")
    public void test3() throws Exception {
        //given
        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Likes like = Data.createLike(null, user, item);
        //when
        memberRepository.save(member);
        User saveUser = userRepository.save(user);
        Item saveItem = itemRepository.save(item);
        likeRepository.save(like);

        Likes findLike = likeRepository.findByUserId(saveUser.getId(), saveItem.getId()).orElse(null);
        //then
        Assertions.assertNotNull(findLike);
        Assertions.assertEquals(like.getSize(), findLike.getSize());
    }

    @Test
    @DisplayName("Member Id 를 가지고 내가 선택한 좋아요 목록 가져오기")
    public void test4() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Likes like = Data.createLike(member, null, item);
        //when
        Member saveMember = memberRepository.save(member);
        itemRepository.save(item);
        likeRepository.save(like);

        List<Likes> findMyLike = likeRepository.findMyLikesByMemberId(saveMember.getId());
        //then
        Assertions.assertNotNull(findMyLike);
        Assertions.assertEquals(1, findMyLike.size());
    }

    @Test
    @DisplayName("User Id 를 가지고 내가 선택한 좋아요 목록 가져오기")
    public void test5() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        User user = Data.createUser();
        Likes like = Data.createLike(null, user, item);
        //when
        memberRepository.save(member);
        itemRepository.save(item);
        likeRepository.save(like);
        User saveUser = userRepository.save(user);

        List<Likes> findMyLike = likeRepository.findMyLikesByUserId(saveUser.getId());
        //then
        Assertions.assertNotNull(findMyLike);
        Assertions.assertEquals(1, findMyLike.size());
    }
}
