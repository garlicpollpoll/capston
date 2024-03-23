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
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final WhatIsRoleService roleService;

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

    public Likes saveLike(LikeFormWithSize form, Authentication authentication) {
        Member findMember = null;
        User findUser = null;

        Item findItem = itemRepository.findById(Long.parseLong(form.getId())).orElse(new Item());

        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        if (memberRole.equals(MemberRole.ROLE_MEMBER)) {
            findMember = cacheRepository.findMemberAtCache(username);
        }

        if (memberRole.equals(MemberRole.ROLE_SOCIAL)) {
            findUser = cacheRepository.findUserAtCache(username);
        }

        Likes like = new Likes(findMember, findUser, findItem, "좋아요", form.getSize());

        return likeRepository.save(like);
    }

    public LookUpLikeDto lookUpLikeList(Authentication authentication) {
        List<Likes> findLikes = new ArrayList<>();
        MemberRole role = null;

        boolean isRoleMember = authentication.getAuthorities().stream().anyMatch(r -> r.equals("ROLE_MEMBER"));
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        if (isRoleMember) {
            Member findMember = cacheRepository.findMemberAtCache(principal.getUsername());

            findLikes = likeRepository.findMyLikesByMemberId(findMember.getId());
            role = findMember.getRole();
        }
        else {
            User findUser = cacheRepository.findUserAtCache(principal.getUsername());

            findLikes = likeRepository.findMyLikesByUserId(findUser.getId());
        }

        return new LookUpLikeDto(findLikes, role);
    }

    // TODO return 값은 findMember, findUser, findItem, orders, findLike
    public LikeToBucketDto likeToBucket(LikeForm form, Authentication authentication) {
        Likes findLike = likeRepository.findById(Long.parseLong(form.getId())).orElse(null);
        Item findItem = itemRepository.findById(findLike.getItem().getId()).orElse(null);

        Member findMember = null;
        User findUser = null;

        boolean isRoleMember = authentication.getAuthorities().stream().anyMatch(r -> r.equals("ROLE_MEMBER"));
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        Integer orders = 0;

        if (isRoleMember) {
            findMember = cacheRepository.findMemberAtCache(principal.getUsername());
            List<Bucket> findBucket = bucketRepository.findByMemberId(findMember.getId());
            orders = findBucket.size();
        }
        else {
            findUser = cacheRepository.findUserAtCache(principal.getUsername());
            List<Bucket> findBucket = bucketRepository.findByUserId(findUser.getId());
            orders = findBucket.size();
        }

        return new LikeToBucketDto(findMember, findUser, findItem, orders, findLike);
    }
}
