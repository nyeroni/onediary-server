package onediary.onediary.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import onediary.onediary.common.apiResponse.ApiResponse;
import onediary.onediary.oauth.service.AuthService;
import onediary.onediary.oauth.token.*;
import onediary.onediary.oauth.utils.JwtHeaderUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OauthController {

    private final AuthService authService;
    private final AuthTokenProvider authTokenProvider;


    @Operation(summary = "카카오 로그인", description = "카카오 엑세스 토큰을 이용하여 사용자 정보 받아 저장하고 앱의 토큰 반환")
    @RequestMapping(value = "/login/oauth/kakao", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<AuthResponseDto> kakaoAuthRequest(@RequestBody AuthRequestDto authRequest, HttpServletResponse response)
    {
        AuthResponseDto authResponse = new AuthResponseDto();
        String accessToken = authService.loginKakao(authRequest.getAccessToken(), response).getToken();
        authResponse.setJwtToken(accessToken);

        return ApiResponse.success(authResponse);
    }

    @Operation(summary = "엑세스 토큰 재발급", description = "리프레시토큰을 이용해서 엑세스토큰 재발급")
    @RequestMapping(value = "/token/refresh", produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<RefreshTokenResponseDto> tokenRefresh(HttpServletRequest request){
        String appToken = JwtHeaderUtil.getAccessToken(request);
        AuthToken authToken = authTokenProvider.convertAuthToken(appToken);
        if (!authToken.validate()) { // 형식에 맞지 않는 token
            return ApiResponse.forbidden(null);
        }

        AuthResponseDto authResponse = new AuthResponseDto();
        authResponse.setAccessToken(authService.refreshAccessToken(authToken.getToken()).getToken());
        if (authResponse.getAccessToken() == null) { // token 만료
            return ApiResponse.forbidden(null);
        }
        // 새로 발급된 액세스 토큰과 리프레시 토큰을 함께 응답
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        refreshTokenResponseDto.setJwtToken(authResponse.getJwtToken());
        refreshTokenResponseDto.setRefreshToken(authToken.getToken());

        // HttpHeaders 객체를 이용하여 응답에 헤더 정보를 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("custom-header", "header-value");

        // ResponseEntity에 헤더 정보와 바디를 함께 넘겨줌
        return new ResponseEntity<>(refreshTokenResponseDto, headers, HttpStatus.OK);

    }

}
