
package com.nameslowly.coinauctions.chat.infrastructure;

import com.nameslowly.coinauctions.chat.infrastructure.security.CustomPreAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity //컨트롤러별로 인증인가 @PreAuthorize 사용하기 위한 설정임
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomPreAuthFilter customPreAuthFilter;

    //인증 객체 처리를 위한 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF disable
        http.csrf(AbstractHttpConfigurer::disable);

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 모든 요청 인증처리 하도록
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests.anyRequest().authenticated()
        );

        // 필터 관리
        http.addFilterBefore(customPreAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
