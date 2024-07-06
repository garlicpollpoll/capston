package com.hello.capston.dto.dto.clientcache;

import com.hello.capston.entity.Member;
import com.hello.capston.entity.enums.MemberRole;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MemberCodec implements RedisCodec<String, Member> {

    private final StringCodec stringCodec = new StringCodec();

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return stringCodec.decodeKey(bytes);
    }

    @Override
    public Member decodeValue(ByteBuffer bytes) {
        String decodedString = StandardCharsets.UTF_8.decode(bytes).toString();
        String[] parts = decodedString.split(",");
        return new Member(Long.parseLong(parts[0]), parts[1], parts[2], parts[3], parts[4], MemberRole.valueOf(parts[5]), parts[6], parts[7]);
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return stringCodec.encodeKey(key);
    }

    @Override
    public ByteBuffer encodeValue(Member value) {
        String encodedString = String.join(",",
                value.getUsername(),
                value.getPassword(),
                value.getBirth(),
                value.getGender(),
                value.getRole().toString(),
                value.getEmail(),
                value.getSessionId());
        return StandardCharsets.UTF_8.encode(encodedString);
    }
}
