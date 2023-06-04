package com.hello.capston.unit.service.item;

import com.hello.capston.dto.dto.like.LikeToBucketDto;
import com.hello.capston.dto.dto.like.LookUpLikeDto;
import com.hello.capston.dto.form.LikeForm;
import com.hello.capston.dto.form.LikeFormWithSize;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.BucketRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.LikeService;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    ItemRepository itemRepository;

    @Mock
    CacheRepository cacheRepository;

    @Mock
    LikeRepository likeRepository;

    @Mock
    BucketRepository bucketRepository;

    @InjectMocks
    LikeService likeService;

    @Test
    @DisplayName("누가 좋아요를 눌렀는지 데이터베이스에 저장되어야 함, and Member == null")
    public void test1() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";
        LikeFormWithSize form = new LikeFormWithSize();
        form.setId("1");
        form.setSize("XL");

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);

        when(itemRepository.findById(Long.parseLong(form.getId()))).thenReturn(Optional.of(item));
        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(likeRepository.save(any(Likes.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        Likes likes = likeService.saveLike(form, loginId, userEmail);
        //then
        Assertions.assertNotNull(likes);
    }

    @Test
    @DisplayName("누가 좋아요를 눌렀는지 데이터베이스에 저장되어야 함, and User == null")
    public void test2() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;
        LikeFormWithSize form = new LikeFormWithSize();
        form.setId("1");
        form.setSize("XL");

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);

        when(itemRepository.findById(Long.parseLong(form.getId()))).thenReturn(Optional.of(item));
        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(likeRepository.save(any(Likes.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        Likes likes = likeService.saveLike(form, loginId, userEmail);
        //then
        Assertions.assertNotNull(likes);
    }

    @Test
    @DisplayName("좋아요를 누른 상품만 조회할 수 있어야 한다. and Member == null")
    public void test3() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Likes like = Data.createLike(null, user, item);

        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(likeRepository.findMyLikesByUserId(user.getId())).thenReturn(Collections.singletonList(like));
        //when
        LookUpLikeDto dto = likeService.lookUpLikeList(loginId, userEmail);
        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1, dto.getFindLikes().size());
        Assertions.assertEquals(null, dto.getRole());
    }

    @Test
    @DisplayName("좋아요를 누른 상품만 조회할 수 있어야 한다. and User == null")
    public void test4() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Likes like = Data.createLike(null, user, item);

        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(likeRepository.findMyLikesByMemberId(member.getId())).thenReturn(Collections.singletonList(like));
        //when
        LookUpLikeDto dto = likeService.lookUpLikeList(loginId, userEmail);
        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1, dto.getFindLikes().size());
        Assertions.assertEquals(MemberRole.ROLE_MEMBER, dto.getRole());
    }

    @Test
    @DisplayName("좋아요에서 장바구니로 이동하는 로직, and Member == null")
    public void test5() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";
        LikeForm form = new LikeForm();
        form.setId("1");

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Likes like = Data.createLike(null, user, item);
        Bucket bucket = Data.createBucket(null, user, item);

        when(likeRepository.findById(Long.parseLong(form.getId()))).thenReturn(Optional.of(like));
        when(itemRepository.findById(like.getItem().getId())).thenReturn(Optional.of(item));
        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(bucketRepository.findByUserId(user.getId())).thenReturn(Collections.singletonList(bucket));
        //when
        LikeToBucketDto dto = likeService.likeToBucket(form, loginId, userEmail);
        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(user.getEmail(), dto.getFindUser().getEmail());
        Assertions.assertEquals(item.getItemName(), dto.getFindItem().getItemName());
        Assertions.assertEquals(1, dto.getOrders());
        Assertions.assertEquals(like.getSize(), dto.getFindLike().getSize());
    }

    @Test
    @DisplayName("좋아요에서 장바구니로 이동하는 로직, and User == null")
    public void test6() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;
        LikeForm form = new LikeForm();
        form.setId("1");

        Member member = Data.createMember();
        User user = Data.createUser();
        Item item = Data.createItem(member);
        Likes like = Data.createLike(null, user, item);
        Bucket bucket = Data.createBucket(null, user, item);

        when(likeRepository.findById(Long.parseLong(form.getId()))).thenReturn(Optional.of(like));
        when(itemRepository.findById(like.getItem().getId())).thenReturn(Optional.of(item));
        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(bucketRepository.findByMemberId(member.getId())).thenReturn(Collections.singletonList(bucket));
        //when
        LikeToBucketDto dto = likeService.likeToBucket(form, loginId, userEmail);
        //then
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(member.getUsername(), dto.getFindMember().getUsername());
        Assertions.assertEquals(item.getItemName(), dto.getFindItem().getItemName());
        Assertions.assertEquals(1, dto.getOrders());
        Assertions.assertEquals(like.getSize(), dto.getFindLike().getSize());
    }
}
