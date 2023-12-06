package onediary.onediary.oauth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.common.exception.TokenValidFailedException;
import onediary.onediary.config.properties.AppProperties;
import onediary.onediary.domain.member.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class AuthTokenProvider {

    @Value("${jwt.token.accessTokenExpiry}")
    private String accessTokenExpiry;

    @Value("${jwt.token.refreshTokenExpiry}")
    private String refreshTokenExpiry;
    private Key key;

    private static final String AUTHORITIES_KEY = "role";

    public AuthTokenProvider(@Value("${jwt.token.secret-key}") String secretKeyString ){
        this.key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }
    private AppProperties appProperties;


    // 토큰 생성 메서드 (id와 만료일만 이용)
    public AuthToken createToken(String id, Role role, String expiry){
        Date expiryDate = getExpiryDate(expiry);
        return new AuthToken(id, role, expiryDate, key);
    }

    public AuthToken createMemberJwtToken(String id) { // Member에 대한 AccessToken(JwtToken) 생성
        return createToken(id, Role.USER, accessTokenExpiry);
    }
    // 리프레시 토큰 생성 메서드
    public AuthToken createRefreshToken(String socialId) {
        return createToken(socialId, Role.USER, refreshTokenExpiry);
    }
    // Token 반환 메서드
    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token);
    }

    public static Date getExpiryDate(String expiry){
        return new Date(System.currentTimeMillis() + Long.parseLong(expiry));
    }

    // 토큰을 이용한 인증 객체 생성
    public Authentication getAuthentication(AuthToken authToken) {
        // 토큰이 유효하다면
        if (authToken.validate()) {

            Claims claims = authToken.getTokenClaims();
            // 권한을 추출하여 Spring Security의 GrantedAuthority 형태로 변환
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[] {claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            // 사용자 정보를 담은 Spring Security의 User 객체 생성
            User principal = new User(claims.getSubject(), "", authorities);
            // 인증 객체인 UsernamePasswordAuthenticationToken을 반환
            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            // 토큰이 유효하지 않다면 예외 발생
            throw new TokenValidFailedException("Token validation failed.");
        }
    }

    // 리프레시 토큰 갱신 및 저장 메서드
    public AuthToken refreshToken(String refreshToken) {
        AuthToken refreshTokenToken = convertAuthToken(refreshToken);

        // 리프레시 토큰이 유효하다면
        if (refreshTokenToken.validate()) {
            // 유저 정보 추출
            Claims refreshTokenClaims = refreshTokenToken.getTokenClaims();
            String subject = refreshTokenClaims.getSubject();
            String role = refreshTokenClaims.get(AUTHORITIES_KEY, String.class);


            AuthToken newAccessToken = createToken(subject, Role.valueOf(role), accessTokenExpiry);

            return newAccessToken;
        }

        // 리프레시 토큰이 유효하지 않은 경우, 예외 처리 또는 다른 로직 수행
        log.warn("Refresh token is not valid or expired. User needs to re-login.");

        // 예외 처리 또는 다른 로직: 예를 들어, 클라이언트에게 리다이렉션 URL을 전달하여 재로그인 유도 등의 동작 수행
        // 여기서는 간단히 null을 반환하도록 설정하고, 클라이언트에서 null을 받았을 때 재로그인 로직을 수행하도록 처리합니다.
        return null;
    }


    // 액세스 토큰의 새로운 만료 시간을 계산하는 메서드
    private Date calculateNewExpirationTime() {
        return new Date(System.currentTimeMillis() + accessTokenExpiry);
    }
    // 리프레시 토큰의 만료 시간을 설정하는 메서드
    private Date calculateRefreshTokenExpirationTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpiry));
    }
}