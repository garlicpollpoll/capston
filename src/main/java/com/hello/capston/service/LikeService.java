package com.hello.capston.service;

import com.hello.capston.entity.Item;
import com.hello.capston.entity.Likes;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public List<Likes> likeCount(Long itemId) {
        return likeRepository.likeCount(itemId);
    }

    public Likes findByMemberId(Long memberId, Long itemId) {
        return likeRepository.findByMemberId(memberId, itemId).orElse(new Likes());
    }

    public Likes findByUserId(Long userId, Long itemId) {
        return likeRepository.findByUserId(userId, itemId).orElse(new Likes());
    }

    public void save(Member member, User user, Item item, String size) {
        Likes like = new Likes(member, user, item, "좋아요", size);
        likeRepository.save(like);
    }

    public void delete(Likes like) {
        likeRepository.delete(like);
    }
}
