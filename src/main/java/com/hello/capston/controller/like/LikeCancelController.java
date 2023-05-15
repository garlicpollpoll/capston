package com.hello.capston.controller.like;

import com.hello.capston.dto.form.LikeForm;
import com.hello.capston.entity.Likes;
import com.hello.capston.repository.LikeRepository;
import com.hello.capston.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LikeCancelController {

    private final LikeRepository likeRepository;

    private final LikeService likeService;

    /**
     * 좋아요 취소
     * @param form
     * @return
     */
    @ResponseBody
    @PostMapping("/cancel_like")
    public Map<String, String> cancelLike(@RequestBody LikeForm form) {
        Likes findLike = likeRepository.findById(Long.parseLong(form.getId())).orElse(null);

        likeService.delete(findLike);

        Map<String, String> map = new HashMap<>();

        map.put("message", "성공적으로 삭제되었습니다.");

        return map;
    }
}
