package onediary.onediary.oauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.common.apiResponse.ApiResponse;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.oauth.service.KakaoOauthService;
import onediary.onediary.oauth.token.AuthResponseDto;
import onediary.onediary.oauth.token.AuthToken;
import onediary.onediary.oauth.token.AuthTokenProvider;
import onediary.onediary.oauth.utils.JwtHeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController

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
    @RequestMapping(value = "/auth/login/kakao", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<AuthResponseDto> kakaoAuthRequest(HttpServletRequest request) {
        String accessToken = JwtHeaderUtil.getAccessToken(request);
        if (accessToken == null) {
            return ApiResponse.forbidden(null);
        }
        return ApiResponse.success(kakaoAuthService.login(accessToken));
    }


    /**
     * appToken 갱신
     * @return ResponseEntity<AuthResponse>
     */
    @Operation(summary = "jwtToken 갱신", description = "jwtToken 갱신")
    @RequestMapping(value = "/auth//refresh", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<AuthResponseDto> refreshToken (HttpServletRequest request) {
        String appToken = JwtHeaderUtil.getAccessToken(request);
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