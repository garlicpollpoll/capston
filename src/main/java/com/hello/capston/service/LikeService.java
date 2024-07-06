package com.hello.capston.service;

import com.hello.capston.dto.dto.like.LookUpLikeDto;
import com.hello.capston.entity.*;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.BucketRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.repository.cache.KeyGenerator;
import io.lettuce.core.support.caching.CacheFrontend;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final CacheRepository cacheRepository;
    private final WhatIsRoleService roleService;
    private final CacheFrontend<String, Member> frontend;

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

    public LookUpLikeDto lookUpLikeList(Authentication authentication) {
        List<Likes> findLikes = new ArrayList<>();
        MemberRole role = null;

        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        if (isRoleMember(memberRole)) {
            String key = KeyGenerator.memberKeyGenerate(username);
            Member findMember = frontend.get(key);
//            Member findMember = cacheRepository.findMemberAtCache(username);

            findLikes = likeRepository.findMyLikesByMemberId(findMember.getId());
            role = findMember.getRole();
        }

        return new LookUpLikeDto(findLikes, role);
    }

    private boolean isRoleMember(MemberRole memberRole) {
        return memberRole.equals(MemberRole.ROLE_MEMBER) || memberRole.equals(MemberRole.ROLE_SOCIAL);
    }
}
