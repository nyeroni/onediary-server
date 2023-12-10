package onediary.onediary.oauth.utils;
import jakarta.servlet.http.HttpServletRequest;

public class JwtHeaderUtil {
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);

        if (headerValue == null) {
            return null;
        }

        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
    // 추가된 메서드: 헤더에서 직접 토큰 추출
    public static String extractAccessTokenFromHeader(String header) {
        if (header == null) {
            return null;
        }

        if (header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}