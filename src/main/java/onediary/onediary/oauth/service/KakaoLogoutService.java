//package onediary.onediary.oauth.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.RequestEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.util.Collections;
//
//@Service
//public class KakaoLogoutService {
//
//    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
//    private String kakaoClientId;
//
//    private final RestTemplate restTemplate;
//
//    public KakaoLogoutService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public ResponseEntity<String> logout(String accessToken) {
//        // 카카오 로그아웃 처리
//        String logoutUrl = "https://kapi.kakao.com/v1/user/logout";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        RequestEntity<Void> requestEntity = RequestEntity.post(URI.create(logoutUrl))
//                .headers(headers)
//                .build();
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
//
//        // 만약 카카오 로그아웃이 성공하면 SecurityContextHolder를 통해 현재 사용자의 컨텍스트를 지움
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            return ResponseEntity.ok("Logout success");
//        } else {
//            return ResponseEntity.badRequest().body("Logout failed");
//        }
//    }
//}
