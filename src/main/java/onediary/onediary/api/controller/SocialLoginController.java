//package onediary.onediary.api.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//public class SocialLoginController {
//
//    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
//    private String naverClientId;
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String kakaoClientId;
//
//    @Autowired
//    @Qualifier("google")
//    private ClientRegistrationRepository clientRegistrationRepository;
//
//    @Autowired
//    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
//
//    @GetMapping("/api/v1/auth/social/naver/login")
//    public String loginWithNaver() {
//        return "redirect:/oauth2/authorization/naver";
//    }
//
//    @GetMapping("/api/v1/auth/social/naver/callback")
//    public String naverLoginCallback(@RequestParam("code") String code, OAuth2AuthenticationToken authenticationToken) {
//        // ... (Naver 로그인 처리 코드)
//        return "redirect:/home";
//    }
//
//    @GetMapping("/api/v1/auth/social/kakao/login")
//    public String loginWithKakao() {
//        return "redirect:/oauth2/authorization/kakao";
//    }
//
//    @GetMapping("/api/v1/auth/social/kakao/callback")
//    public String kakaoLoginCallback(@RequestParam("code") String code, OAuth2AuthenticationToken authenticationToken) {
//        // ... (Kakao 로그인 처리 코드)
//        return "redirect:/home";
//    }
//}
