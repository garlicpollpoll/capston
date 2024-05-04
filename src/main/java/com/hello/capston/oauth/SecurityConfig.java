package com.hello.capston.oauth;

import com.hello.capston.jwt.JwtAuthenticationFilter;
import com.hello.capston.jwt.JwtTokenProvider;
import com.hello.capston.jwt.OAuth2LoginSuccessHandler;
import com.hello.capston.jwt.gitbefore.RedisTokenRepository;
import com.hello.capston.principal.PrincipalDetailService;
import com.hello.capston.repository.MemberRepository;
import com.hello.capston.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final PrincipalDetailService principalDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final RedisTokenRepository redisTokenRepository;

    @Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests().antMatchers(
                        "/", "/css/**", "/image/**", "/js/**", "/h2-console/**", "/login", "/item_list/**",
                        "/item_detail/**", "/social_login", "/join", "/login_id_duplicate", "/sendEmail", "/checkNumber",
                        "/find_by_detail_category/**", "/item_list_popular/**", "/inquiry/**", "/custom/login", "/custom/logout",
                        "/oauth2/authorization/google", "/oauth2/authorization/naver", "/oauth2/authorization/kakao", "/erase/authToken/authentication",
                        "/log/test/**", "/warmup/**", "/actuator/**", "/login/**"
                ).permitAll()
                .antMatchers("/item_upload", "/detail_upload/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/admin/**", "/coupon_upload").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)
//                .formLogin().loginPage("/login").loginProcessingUrl("/custom/login").defaultSuccessUrl("/")
//                .loginProcessingUrl("/auth/loginProc")
//                .and()
                .oauth2Login().loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(new OAuth2LoginSuccessHandler(jwtTokenProvider, redisTemplate, redisTokenRepository, userRepository));

        //중복 로그인
        http.sessionManagement()
                .maximumSessions(1) //세션 최대 허용 수
                .maxSessionsPreventsLogin(false); // false 이면 중복 로그인하면 이전 로그인이 풀린다.
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
