
package com.nameslowly.coinauctions.chat.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomPreAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 사용자 정보와 역할(Role)을 추출
        String username = request.getHeader("X-User-Name");
        String rolesHeader = request.getHeader("X-User-Role");

        if (username != null && rolesHeader != null) {
            // rolesHeader에 저장된 역할을 SimpleGrantedAuthority로 변환
            List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesHeader.split(","))
                    .map(role -> new SimpleGrantedAuthority(role.trim()))
                    .collect(Collectors.toList());

            // 사용자 정보를 기반으로 인증 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } else {
            //request 헤더에 user 정보가 없을 경우 쿠키로부터 한번 더 가져온다
            Cookie[] cookies = request.getCookies();
            String tokenValue = "";
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        tokenValue = cookie.getValue();
                        if (tokenValue.startsWith("Bearer%20")) {
                            tokenValue = tokenValue.substring(9).trim();
                        }
                    }
                }
            }

            // JWT 토큰에서 페이로드 부분을 추출
            if (!tokenValue.isEmpty()) {
                String[] tokenParts = tokenValue.split("\\.");
                if (tokenParts.length == 3) {
                    String payload = tokenParts[1];  // 페이로드 부분

                    byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
                    String decodedPayload = new String(decodedBytes);

                    JSONObject jsonObject = new JSONObject(decodedPayload);
                    username = jsonObject.optString("sub");  // JWT의 sub 필드를 가져옴

                    List<SimpleGrantedAuthority> authorities = Arrays.stream(jsonObject.optString("auth").split(","))
                            .map(role -> new SimpleGrantedAuthority(role.trim()))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }else{
                // 빈 권한 처리
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        null,
                        null,
                        AuthorityUtils.NO_AUTHORITIES // 빈 권한 목록
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);

        // 필터 체인을 계속해서 진행
        filterChain.doFilter(request, response);
    }
}
