
package onediary.onediary.oauth.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.domain.member.entity.Role;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {
    @Getter
    private final String token;

    @Value("${jwt.token.accessTokenExpiry}")
    private String accessTokenExpiry;

    @Value("${jwt.token.refreshTokenExpiry}")
    private String refreshTokenExpiry;
    @Value("${jwt.token.secret-key}")
    private Key key;

    private static final String AUTHORITIES_KEY = "role";

    AuthToken(String id, Role role, Date expiry, Key key) {
        String roleStr = role.toString();
        this.key = key;
        this.token = createAuthToken(id, roleStr, expiry);
    }

    public AuthToken(String newToken, Key key) {
        this.token = newToken;
        this.key = key;
    }

    // 토큰 생성 메서드 (id와 만료일만 이용)
    private String createAuthToken(String socialId, String role, Date expiry) {// AccessToken(JwtToken) 생성
        return Jwts.builder()
                .setSubject(socialId)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256) // 알고리즘, 시크릿 키
                .setExpiration(expiry)
                .compact();
    }
    // 토큰 유효성 검사
    public boolean validate(){
        return this.getTokenClaims() != null;
    }


    // 토큰에서 클레임 추출
    public Claims getTokenClaims(){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            // 처음 로그인(/auth/kakao, /auth/naver) 시, AccessToken(JwtToken) 없이 접근해도 token validate을 체크하기 때문에 exception 터트리지 않고 catch합니다.
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

    // 만료된 토큰의 클레임 추출
    public Claims getExpiredTokenClaims(){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
        }
    }
//    // 토큰 갱신 메서드
//    public AuthToken refreshAuthToken() {
//        // 만료된 토큰의 클레임 추출
//        Claims expiredTokenClaims = getExpiredTokenClaims();
//        Role role = Role.USER;
//        String roleStr = role.toString();
//        String newToken = createAuthToken(expiredTokenClaims.getId(), roleStr, refreshTokenExpiry);
//
//        // 새로운 토큰으로 AuthToken 객체 생성
//        return new AuthToken(newToken, key);
//    }
}