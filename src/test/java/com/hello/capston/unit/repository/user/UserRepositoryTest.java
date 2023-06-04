package com.hello.capston.unit.repository.user;

import com.hello.capston.entity.User;
import com.hello.capston.repository.UserRepository;
import com.hello.capston.unit.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Email 로 User 를 특정해서 조회하기")
    public void test() throws Exception {
        //given
        User user = Data.createUser();
        //when
        User saveUser = userRepository.save(user);

        User findUser = userRepository.findByEmail(saveUser.getEmail()).orElse(null);
        //then
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(user.getEmail(), findUser.getEmail());
    }

    @Test
    @DisplayName("Session Id 로 User 를 특정해서 조회하기")
    public void test2() throws Exception {
        //given
        User user = Data.createUser();
        //when
        User saveUser = userRepository.save(user);

        User findUser = userRepository.findBySessionId(saveUser.getSessionId()).orElse(null);
        //then
        Assertions.assertNotNull(findUser);
        Assertions.assertEquals(user.getEmail(), findUser.getEmail());
    }
}
