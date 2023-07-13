package com.hello.capston.service;

import com.hello.capston.dto.dto.like.LikeToBucketDto;
import com.hello.capston.dto.dto.like.LookUpLikeDto;
import com.hello.capston.dto.form.LikeForm;
import com.hello.capston.dto.form.LikeFormWithSize;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.BucketRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ItemRepository itemRepository;
    private final CacheRepository cacheRepository;
    private final BucketRepository bucketRepository;
    private final MemberRepository memberRepository;

    public List<Likes> likeCount(Long itemId) {
        return likeRepository.likeCount(itemId);
    }

    public Likes findByMemberId(Long memberId, Long itemId) {
        return likeRepository.findByMemberId(memberId, itemId).orElse(new Likes());
    }

    public Likes findByUserId(Long userId, Long itemId) {
        return likeRepository.findByUserId(userId, itemId).orElse(new Likes());
    }

    public Likes save(Member member, User user, Item item, String size) {
        Likes like = new Likes(member, user, item, "좋아요", size);
        likeRepository.save(like);
        return like;
    }

    public void delete(Likes like) {
        likeRepository.delete(like);
    }

    public Likes saveLike(LikeFormWithSize form, String loginId, String userEmail) {
        Member findMember = null;
        User findUser = null;

        Item findItem = itemRepository.findById(Long.parseLong(form.getId())).orElse(new Item());

        if (loginId == null) {
            findUser = cacheRepository.findUserAtCache(userEmail);
        }
        else {
            findMember = cacheRepository.findMemberAtCache(loginId);
        }

        Likes like = new Likes(findMember, findUser, findItem, "좋아요", form.getSize());

        return likeRepository.save(like);
    }

    public LookUpLikeDto lookUpLikeList(String loginId, String userEmail) {
        List<Likes> findLikes = new ArrayList<>();
        MemberRole role = null;

        if (userEmail == null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);

            if (findMember == null && loginId != null) {
                findMember = memberRepository.findByLoginId(loginId).orElse(null);
                cacheRepository.addMember(findMember);
            }

            findLikes = likeRepository.findMyLikesByMemberId(findMember.getId());
            role = findMember.getRole();
        }

        if (loginId == null) {
            User findUser = cacheRepository.findUserAtCache(userEmail);

            findLikes = likeRepository.findMyLikesByUserId(findUser.getId());
        }

        return new LookUpLikeDto(findLikes, role);
    }

    // TODO return 값은 findMember, findUser, findItem, orders, findLike
    public LikeToBucketDto likeToBucket(LikeForm form, String loginId, String userEmail) {
        Likes findLike = likeRepository.findById(Long.parseLong(form.getId())).orElse(null);
        Item findItem = itemRepository.findById(findLike.getItem().getId()).orElse(null);

        Member findMember = null;
        User findUser = null;

        Integer orders = 0;

        if (loginId == null) {
            findUser = cacheRepository.findUserAtCache(userEmail);
            List<Bucket> findBucket = bucketRepository.findByUserId(findUser.getId());
            orders = findBucket.size();
        }

        if (userEmail == null) {
            findMember = cacheRepository.findMemberAtCache(loginId);
            List<Bucket> findBucket = bucketRepository.findByMemberId(findMember.getId());
            orders = findBucket.size();
        }

        return new LikeToBucketDto(findMember, findUser, findItem, orders, findLike);
    }
}
