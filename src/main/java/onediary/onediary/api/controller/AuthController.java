package onediary.onediary.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.api.domain.auth.AuthReqModel;
import onediary.onediary.api.domain.member.entity.MemberRefreshToken;
import onediary.onediary.api.domain.member.entity.Role;
import onediary.onediary.api.domain.member.repository.MemberRefreshTokenRepository;
import onediary.onediary.common.ApiResponse;
import onediary.onediary.config.properties.AppProperties;
import onediary.onediary.oauth.entity.MemberPrincipal;
import onediary.onediary.oauth.token.AuthToken;
import onediary.onediary.oauth.token.AuthTokenProvider;
import onediary.onediary.utils.CookieUtil;
import onediary.onediary.utils.HeaderUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final WebClient.Builder webClientBuilder;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    private String naverAccessToken;  // accessToken를 저장할 필드 추가
    private String appleAccessToken;  // accessToken를 저장할 필드 추가
    private String kakaoAccessToken;  // accessToken를 저장할 필드 추가

    @PostMapping("/login")
    public ApiResponse login(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody AuthReqModel authReqModel
            )
    {
        try {
            log.info("AuthReqModel = " + authReqModel.getId());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authReqModel.getId(),
                            authReqModel.getPassword()
                    )
            );

            String userId = authReqModel.getId();
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Date now = new Date();
            AuthToken accessToken = tokenProvider.createAuthToken(
                    userId,
                    ((MemberPrincipal) authentication.getPrincipal()).getRole().getCode(),
                    new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
            );

            long refreshTokenExpiry = appProperties.getAuth().getTokenExpiry();
            AuthToken refreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            // userId refresh token 으로 DB 확인

            MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByUserId(userId);
            if (memberRefreshToken == null) {
                memberRefreshToken = new MemberRefreshToken(userId, refreshToken.getToken());
                memberRefreshTokenRepository.saveAndFlush(memberRefreshToken);
            } else {
                // DB에 refresh 토큰 업데이트
                memberRefreshToken.setRefreshToken(refreshToken.getToken());
            }

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

            return ApiResponse.success("token", accessToken.getToken());
        } catch (BadCredentialsException e) {
            // 로그인 실패: 잘못된 자격 증명
            return ApiResponse.fail("Invalid credentials. Please check your username and password.");
        } catch (LockedException e) {
            // 계정 잠김 처리 등 로그인 실패에 대한 추가 처리
            return ApiResponse.fail("Your account is locked. Please contact support.");
        } catch (DisabledException e) {
            // 계정 비활성화 처리 등 로그인 실패에 대한 추가 처리
            return ApiResponse.fail("Your account is disabled. Please contact support.");
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("Unexpected error during login", e);
            return ApiResponse.fail("Unexpected error");
        }

    }

    @GetMapping("/refresh")
    public ApiResponse refreshToken(HttpServletRequest request, HttpServletResponse response){
        //access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        if(!authToken.validate()){
            return ApiResponse.invalidAccessToken();
        }
        //expired access token인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if(claims==null){
            return ApiResponse.notExpiredTokenYet();
        }
        String userId  = claims.getSubject();
        Role role = Role.of(claims.get("role", String.class));

        //refresh token

        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse(null);
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if(!authRefreshToken.validate()){
            return ApiResponse.invalidRefreshToken();

        }

        //userId refresh token으로 DB확인
        MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if( memberRefreshToken == null){
            return ApiResponse.invalidRefreshToken();
        }

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                role.getCode(),
                new Date(now.getTime()+appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime()-now.getTime();

        log.info("validTime =- " + validTime);
    // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if(validTime<=THREE_DAYS_MSEC){
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime()+refreshTokenExpiry)
            );

            // DB에 refresh 토큰 업데이트
            memberRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int)refreshTokenExpiry/60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);

        }
        return ApiResponse.success("token", newAccessToken.getToken());
    }
    @GetMapping("/naver/callback")
    public String naverCallback(@RequestParam String code, @RequestParam String state) {
        // 네이버에 액세스 토큰 요청
        String naverTokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
                + "&client_id=" + naverClientId
                + "&client_secret=" + naverClientSecret
                + "&code=" + code
                + "&state=" + state;

        // WebClient를 사용하여 토큰 요청
        String responseBody = webClientBuilder.build()
                .post()
                .uri(naverTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // responseBody에서 액세스 토큰 추출
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            naverAccessToken = jsonNode.get("access_token").asText();  // accessToken 저장

            // 여기서 얻은 accessToken을 활용하여 다양한 네이버 API 호출 등을 수행할 수 있습니다.

            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "error";
        }

    }

    @GetMapping("/kakao/callback")
    public String kakaoCallback(@RequestParam String code, @RequestParam String state) {
        // 네이버에 액세스 토큰 요청
        // Kakao에 액세스 토큰 요청
        String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token"
                + "?grant_type=authorization_code"
                + "&client_id=" + kakaoClientId
                + "&client_secret=" + kakaoClientSecret
                + "&code=" + code
                + "&redirect_uri=http://your-redirect-uri-for-kakao-callback";
        // WebClient를 사용하여 토큰 요청
        String responseBody = webClientBuilder.build()
                .post()
                .uri(kakaoTokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // responseBody에서 액세스 토큰 추출
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            kakaoAccessToken = jsonNode.get("access_token").asText();  // accessToken 저장

            return "success";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "error";
        }

    }

//    @GetMapping("/apple/callback")
//    public String appleCallback(@RequestParam String code, @RequestParam String state) {
//        // Apple에 액세스 토큰 요청
//        String appleTokenUrl = "https://your-apple-token-url"
//                + "?grant_type=authorization_code"
//                + "&client_id=" + appleClientId
//                + "&client_secret=" + appleClientSecret
//                + "&code=" + code
//                + "&redirect_uri=http://your-redirect-uri-for-apple-callback";
//
//        // WebClient를 사용하여 토큰 요청
//        String responseBody = webClientBuilder.build()
//                .post()
//                .uri(appleTokenUrl)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        // responseBody에서 액세스 토큰 추출
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            JsonNode jsonNode = objectMapper.readTree(responseBody);
//            appleAccessToken = jsonNode.get("access_token").asText();  // accessToken 저장
//
//
//            return "success";
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return "error";
//        }
//
//    }

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;
    @GetMapping("login/naver")
    public String naverLogin() {
        // Naver 로그인 URL 생성
        String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + naverClientId
                + "&redirect_uri=http://localhost:8080/login/oauth2/code/naver"; // Redirect URI는 등록한 애플리케이션 설정에 맞게 변경

        // 생성된 URL로 리다이렉트
        return "redirect:" + naverLoginUrl;
    }
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;
    @GetMapping("login/kakao")
    public String kakaoLogin() {
        // Kakao 로그인 URL 생성
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + kakaoClientId
                + "&redirect_uri=http://localhost:8080/login/oauth2/code/kakao";

        // 생성된 URL로 리다이렉트
        return "redirect:" + kakaoLoginUrl;
    }


//    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
//    private String appleClientId;
//    @Value("${spring.security.oauth2.client.registration.apple.client-secret}")
//    private String appleClientSecret;
//    @GetMapping("login/apple")
//    public String appleLogin() {
//        // Apple 로그인 URL 생성
//        String appleLoginUrl = "https://appleid.apple.com/auth/authorize"
//                + "?response_type=code"
//                + "&client_id=" + appleClientId
//                + "&redirect_uri=http://localhost:8080/login/oauth2/code/apple";
//        // 생성된 URL로 리다이렉트
//        return "redirect:" + appleLoginUrl;
//    }

    @GetMapping("/logout/naver")
    public String naverLogout() {

        return "redirect:/";
    }

    @GetMapping("logout/kakao")
    public String kakaoLogout(){
        return "redirect:/";
    }

//    @GetMapping("logout/apple")
//    public String appleLogout(){
//        return "redirect:/";
//    }


}
