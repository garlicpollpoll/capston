package com.hello.capston.service;

import com.hello.capston.S3.S3Uploader;
import com.hello.capston.dto.form.CommentForm;
import com.hello.capston.entity.Comment;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.CommentRepository;
import com.hello.capston.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ItemService itemService;
    private final S3Uploader s3Uploader;
    private final CacheRepository cacheRepository;
    private final CommentRepository commentRepository;

    public Comment saveComment(String itemId, CommentForm form, String loginId, String userEmail) {
        Item findItem = itemService.findItem(itemId);
        Comment saveComment = null;

        String imageUrl = null;
        try {
            imageUrl = s3Uploader.upload(form.getCommentImage(), "static");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (loginId == null) {
            User findUser = cacheRepository.findUserAtCache(userEmail);
            Comment comment = new Comment(null, findUser, findItem, form.getComment(), imageUrl);
            saveComment = commentRepository.save(comment);
        }

        if (userEmail == null) {
            Member findMember = cacheRepository.findMemberAtCache(loginId);
            Comment comment = new Comment(findMember, null, findItem, form.getComment(), imageUrl);
            saveComment = commentRepository.save(comment);
        }
        return saveComment;
    }
}
