package com.hello.capston.jwt.interceptor;

import com.hello.capston.jwt.JwtUtil;
import com.hello.capston.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
     /*
    "/", "/css/**", "/image/**", "/js/**", "/h2-console/**", "/login", "/item_list/**",
            "/item_detail/**", "/social_login", "/join", "/login_id_duplicate", "/sendEmail", "/checkNumber",
            "/find_by_detail_category/**", "/item_list_popular/**", "/inquiry/**" */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor(jwtUtil))
                .excludePathPatterns("/", "/login", "/social_login").order(1);

        registry.addInterceptor(new AddHeaderInterceptor(jwtService))
                .addPathPatterns("/auth/loginProc").order(2);
    }
}
