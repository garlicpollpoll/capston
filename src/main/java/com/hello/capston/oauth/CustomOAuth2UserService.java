package com.hello.capston.oauth;

import com.hello.capston.entity.User;
import com.hello.capston.jwt.JwtUtil;
import com.hello.capston.jwt.dto.TokenDto;
import com.hello.capston.jwt.repository.RefreshTokenRepository;
import com.hello.capston.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService {

    private final UserRepository userRepository;
    private final HttpSession session;
    private final HttpServletResponse response;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행 중인 서비스를 구분한다. (구글인지 네이버인지)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //  OAuth2 로그인 진행시 키가 되는 필드 값 (PK 개념, 네이버는 id 구굴은 sub)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // CustomOAuth2UserService 를 통해 가져온 OAuth2User 의 Attribute 를 담은 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

//        session.setAttribute("userName", new SessionUser(user).getName());
//        session.setAttribute("userEmail", new SessionUser(user).getEmail());
//        session.setAttribute("userPicture", new SessionUser(user).getPicture());

        /*
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        TokenDto token = jwtUtil.createAllToken(new SessionUser(user).getEmail());

        Optional<TokenDto> refreshToken = refreshTokenRepository.findByEmail(token.getEmail());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(token.getRefreshToken()));
        }
        else {
            TokenDto tokenDto = new TokenDto(token.getAccessToken(), token.getRefreshToken(), token.getEmail());
            refreshTokenRepository.save(tokenDto);
        }

        response.addHeader(JwtUtil.ACCESS_TOKEN, token.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, token.getRefreshToken());
         */

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        //기존 유저라면 Update, 신규 유저라면 User 를 생성
        //기존 유저가 소셜 서비스에서 이름을 바꾸거나 사진을 바꾸면 이를 적용
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
