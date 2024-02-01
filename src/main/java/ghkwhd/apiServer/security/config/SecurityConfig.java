package ghkwhd.apiServer.security.config;

import ghkwhd.apiServer.jwt.filter.JwtVerifyFilter;
import ghkwhd.apiServer.security.handler.CustomAccessDeniedHandler;
import ghkwhd.apiServer.security.handler.CustomLoginFailHandler;
import ghkwhd.apiServer.security.handler.CustomLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("################# Security Config filterChain #################");
        
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())); // CORS 설정
        
        http.csrf(AbstractHttpConfigurer::disable); // CSRF 보호를 비활성화, CSRF 보호를 해제하면 클라이언트에서 보내는 요청에 대해 CSRF 토큰을 검사하지 않습니다.

        // 세션 설정
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER); // 세션을 만들지 않는다
        });

        // 로그인 설정
        http.formLogin(config -> {
            config.loginPage("/api/member/login");
            config.successHandler(new CustomLoginSuccessHandler());
            config.failureHandler(new CustomLoginFailHandler());
        });

        // 필터 설정
        http.addFilterBefore(new JwtVerifyFilter(), UsernamePasswordAuthenticationFilter.class);

        // 접근 거부 시 핸들러 등록
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        return http.build();
    }

    // passwordEncoder 는 이전과 동일
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 경로에 대해서 CORS 설정을 적용

        return source;
    }
}
