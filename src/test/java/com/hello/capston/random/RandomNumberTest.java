package com.hello.capston.random;

import org.junit.jupiter.api.Test;

public class RandomNumberTest {

    @Test
    public void test() throws Exception {
        //given
        int num1 = (int) (Math.random() * 10);
        int num2 = (int) (Math.random() * 10);
        int num3 = (int) (Math.random() * 10);
        //when
        String test = String.valueOf(num1) + String.valueOf(num2) + String.valueOf(num3);
        //then
        System.out.println(test);
    }
}
