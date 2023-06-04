package com.hello.capston.unit.repository.comment;

import com.hello.capston.entity.Comment;
import com.hello.capston.entity.Item;
import com.hello.capston.entity.Member;
import com.hello.capston.repository.CommentRepository;
import com.hello.capston.repository.ItemRepository;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("Item Id 로 해당 Item 의 댓글 페이징 쿼리")
    public void test() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Comment comment = Data.createComment(member, null, item);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);
        commentRepository.save(comment);

        List<Comment> findComments = commentRepository.findCommentByItemId(saveItem.getId(), PageRequest.of(0, 8));
        //then
        Assertions.assertNotNull(findComments);
        Assertions.assertEquals(1, findComments.size());
        Assertions.assertEquals(member.getUsername(), findComments.get(0).getMember().getUsername());
    }

    @Test
    @DisplayName("Item Id 로 모든 댓글 가져오기")
    public void test2() throws Exception {
        //given
        Member member = Data.createMember();
        Item item = Data.createItem(member);
        Comment comment = Data.createComment(member, null, item);
        //when
        memberRepository.save(member);
        Item saveItem = itemRepository.save(item);
        commentRepository.save(comment);

        List<Comment> findComments = commentRepository.findCommentAllByItemId(saveItem.getId());
        //then
        Assertions.assertNotNull(findComments);
        Assertions.assertEquals(1, findComments.size());
        Assertions.assertEquals(member.getUsername(), findComments.get(0).getMember().getUsername());
    }
}
