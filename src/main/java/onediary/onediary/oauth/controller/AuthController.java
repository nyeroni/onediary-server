package onediary.onediary.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.common.apiResponse.ApiResponse;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.oauth.service.KakaoOauthService;
import onediary.onediary.oauth.token.AuthRequestDto;
import onediary.onediary.oauth.token.AuthResponseDto;
import onediary.onediary.oauth.token.AuthToken;
import onediary.onediary.oauth.token.AuthTokenProvider;
import onediary.onediary.oauth.utils.JwtHeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoOauthService kakaoAuthService;
    private final AuthTokenProvider authTokenProvider;
    private final AuthService authService;

    /**
     * KAKAO 소셜 로그인 기능
     * @return ResponseEntity<AuthResponse>
     */
    @Operation(summary = "카카오 로그인", description = "카카오 엑세스 토큰을 이용하여 사용자 정보 받아 저장하고 앱의 토큰 반환")
    @PostMapping(value = "/login/kakao", produces = "application/json")
    public ResponseEntity<AuthResponseDto> kakaoAuthRequest(@RequestHeader("Authorization") String authHeader) {
        String token = JwtHeaderUtil.extractAccessTokenFromHeader(authHeader);

        AuthRequestDto authRequestDto = new AuthRequestDto(token);
        return ApiResponse.success(kakaoAuthService.login(authRequestDto));
    }

    /**
     * appToken 갱신
     * @return ResponseEntity<AuthResponse>
     */
    @Operation(summary = "jwtToken 갱신", description = "jwtToken 갱신")
    @PostMapping(value = "/refresh", produces = "application/json")
    public ResponseEntity<AuthResponseDto> refreshToken (@RequestHeader("Authorization") String authHeader) {

        String appToken = JwtHeaderUtil.extractAccessTokenFromHeader(authHeader);
        AuthToken authToken = authTokenProvider.convertAuthToken(appToken);
        if (!authToken.validate()) { // 형식에 맞지 않는 token
            return ApiResponse.forbidden(null);
        }

        AuthResponseDto authResponse = authService.updateToken(authToken);
        if (authResponse == null) { // token 만료
            return ApiResponse.forbidden(null);
        }
        return ApiResponse.success(authResponse);
    }
}