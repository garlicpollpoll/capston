package com.hello.capston.jwtDeprecated.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter {
    /*
    private final JwtUtil jwtUtil;

    // HTTP 요청이 오면 WAS 가 HttpServletRequest, HttpServletResponse 객체를 만들어 줌
    // 만든 인자 값을 받아온다.
    // 요청이 들어오면 doFilterInternal 이 딱 한번 실행된다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // WebSecurityConfig 에서 보았던 UsernamePasswordAuthenticationFilter 보다 먼저 동작을 하게 된다.
        // Access / Refresh 헤더에서 토큰을 가져옴
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");

        if (accessToken != null) {
            // access token 이 유효하다면 setAuthentication 을 통해 security context 에 인증 정보 저장
            if (jwtUtil.tokenValidation(accessToken)) {
                setAuthentication(jwtUtil.getEmailFromToken(accessToken));
            }
            // access token 이 만료된 상황 && refresh token 또한 존재하는 상황
            else if (refreshToken != null) {
                // refresh token 검증 && refresh token DB 에서 토큰 존재 유무 확인
                Boolean isRefreshTokenValidate = jwtUtil.refreshTokenValidation(refreshToken);

                // refresh token 이 유효하고 refresh token 이 DB 와 비교했을 때 똑같다면
                if (isRefreshTokenValidate) {
                    // refresh token 으로 아이디 정보 가져오기
                    String email = jwtUtil.getEmailFromToken(refreshToken);
                    // 새로운 access token 발급
                    String newAccessToken = jwtUtil.createToken(email, "Access");
                    // 헤더에 access token 추가
                    jwtUtil.setHeaderAccessToken(response, newAccessToken);
                    // Security Context 에 인증 정보 넣기
                    setAuthentication(jwtUtil.getEmailFromToken(newAccessToken));
                }
                else {
                    jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                    return;
                }
            }
        }
    }

    // SecurityContext 에 Authentication 객체를 저장한다.
    public void setAuthentication(String email) {
        Authentication authentication = jwtUtil.createAuthentication(email);
        // security 가 만들어 주는 securityContextHolder 그 안에 authentication 을 넣어준다.
        // security 가 securityContextHolder 에서 인증 객체를 확인하는데
        // jwtAuthFilter 에서 authentication 을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고
        // 추가적인 작업을 진행하지 않는다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Jwt 예외 처리
    public void jwtExceptionHandler(HttpServletResponse response, String message, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new GlobalResDto(message, status.value()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
     */
}
