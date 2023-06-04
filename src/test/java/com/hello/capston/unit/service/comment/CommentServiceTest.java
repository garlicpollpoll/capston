package com.hello.capston.unit.service.comment;

import com.hello.capston.S3.S3Uploader;
import com.hello.capston.dto.form.CommentForm;
import com.hello.capston.entity.Comment;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.entity.User;
import com.hello.capston.entity.enums.MemberRole;
import com.hello.capston.entity.enums.Role;
import com.hello.capston.repository.CommentRepository;
import com.hello.capston.repository.cache.CacheRepository;
import com.hello.capston.service.CommentService;
import com.hello.capston.service.ItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    ItemService itemService;

    @Mock
    S3Uploader s3Uploader;

    @Mock
    CacheRepository cacheRepository;

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    @Test
    @DisplayName("댓글 작성, Member == null")
    public void test1() throws Exception {
        //given
        String loginId = null;
        String userEmail = "kyoungsuk3254@naver.com";
        String itemId = "1";
        String path = "src/test/java/com/hello/capston/unit/service/coupon/cookie.png";
        FileInputStream inputStream = new FileInputStream(path);
        MultipartFile commentImage = new MockMultipartFile("name", "cookie.png", "png", inputStream);
        CommentForm form = new CommentForm(commentImage, "III");

        User user = createUser();
        Member member = createMember();
        Item item = createItem(member);

        when(itemService.findItem(itemId)).thenReturn(item);
        when(cacheRepository.findUserAtCache(userEmail)).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        Comment saveComment = commentService.saveComment(itemId, form, loginId, userEmail);
        //then
        Assertions.assertNotNull(saveComment);
        Assertions.assertEquals("III", saveComment.getComment());
    }

    @Test
    @DisplayName("댓글 작성, User == null")
    public void test2() throws Exception {
        //given
        String loginId = "ks3254";
        String userEmail = null;
        String itemId = "1";
        String path = "src/test/java/com/hello/capston/unit/service/coupon/cookie.png";
        FileInputStream inputStream = new FileInputStream(path);
        MultipartFile commentImage = new MockMultipartFile("name", "cookie.png", "png", inputStream);
        CommentForm form = new CommentForm(commentImage, "III");

        Member member = createMember();
        Item item = createItem(member);

        when(itemService.findItem(itemId)).thenReturn(item);
        when(cacheRepository.findMemberAtCache(loginId)).thenReturn(member);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        Comment saveComment = commentService.saveComment(itemId, form, loginId, userEmail);
        //then
        Assertions.assertNotNull(saveComment);
        Assertions.assertEquals("III", saveComment.getComment());
    }

    private Comment createComment(Member member, User user, Item item, String comment) {
        return new Comment(member, user, item, comment, "url");
    }

    private Item createItem(Member member) {
        return new Item("brandName", "viewName", "itemName", "url", 1000, "code", member, "category", "color", 0, 0);
    }

    private Member createMember() {
        return new Member("username", "password", "birth", "gender", MemberRole.ROLE_MEMBER, "email", "session");
    }

    private User createUser() {
        return new User("name", "email", "picture", Role.USER, "key", "session");
    }
}
