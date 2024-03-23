package com.hello.capston.service;

import com.hello.capston.S3.S3Uploader;
import com.hello.capston.dto.form.CommentForm;
import com.hello.capston.entity.Comment;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.repository.CommentRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ItemService itemService;
    private final S3Uploader s3Uploader;
    private final CacheRepository cacheRepository;
    private final CommentRepository commentRepository;
    private final WhatIsRoleService roleService;

    @Transactional
    public Comment saveComment(String itemId, CommentForm form, Authentication authentication) {
        Item findItem = itemService.findItem(itemId);
        Comment saveComment = null;

        String imageUrl = null;
        try {
            imageUrl = s3Uploader.upload(form.getCommentImage(), "static");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MemberRole memberRole = roleService.whatIsRole(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String username = principal.getUsername();

        if (memberRole.equals(MemberRole.ROLE_SOCIAL)) {
            User findUser = cacheRepository.findUserAtCache(username);
            Comment comment = new Comment(null, findUser, findItem, form.getComment(), imageUrl);
            saveComment = commentRepository.save(comment);
        }

        if (memberRole.equals(MemberRole.ROLE_MEMBER)) {
            Member findMember = cacheRepository.findMemberAtCache(username);
            Comment comment = new Comment(findMember, null, findItem, form.getComment(), imageUrl);
            saveComment = commentRepository.save(comment);
        }
        return saveComment;
    }
}
