package com.hello.capston.repository.cache;

public class KeyGenerator {

    private static final String MEMBER_KEY = "member";
    private static final String USER_KEY = "user";

    public static String memberKeyGenerate(String loginId) {
        return MEMBER_KEY + " : " + loginId;
    }

    public static String userKeyGenerate(String email) {
        return USER_KEY + " : " + email;
    }
}
