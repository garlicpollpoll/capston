package com.hello.capston.oauth;

import com.hello.capston.principal.PrincipalDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final PrincipalDetailService principalDetailService;

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
                .authorizeRequests().antMatchers(
                        "/", "/css/**", "/image/**", "/js/**", "/h2-console/**", "/login", "/item_list/**",
                        "/item_detail/**", "/social_login", "/join", "/login_id_duplicate", "/sendEmail", "/checkNumber",
                        "/find_by_detail_category/**", "/item_list_popular/**", "/inquiry/**"
                ).permitAll()
                .antMatchers("/item_upload", "/detail_upload/**").hasAnyRole("MANAGER", "ADMIN")
                .antMatchers("/admin/**", "/coupon_upload").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/auth/loginProc")
                .and()
                .oauth2Login().loginPage("/login")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);

        //?????? ?????????
        http.sessionManagement()
                .maximumSessions(1) //?????? ?????? ?????? ???
                .maxSessionsPreventsLogin(false); // false?????? ?????? ??????????????? ?????? ???????????? ?????????.
    }
}
