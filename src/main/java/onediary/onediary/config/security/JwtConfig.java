package onediary.onediary.config.security;

import onediary.onediary.oauth.token.AuthTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.token.secret-key}")
    private String secret;

    // jwt를 사용하기 위한 설정
    @Bean
    public AuthTokenProvider jwtProvider() {
        // JWT Secret 값 로깅 추가
        return new AuthTokenProvider(secret);
    }

}