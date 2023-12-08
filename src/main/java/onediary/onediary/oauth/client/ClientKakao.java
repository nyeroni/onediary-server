package onediary.onediary.oauth.client;

import lombok.RequiredArgsConstructor;
import onediary.onediary.common.exception.TokenValidFailedException;
import onediary.onediary.member.entity.Member;
import onediary.onediary.member.entity.Role;
import onediary.onediary.member.entity.SocialProvider;
import onediary.onediary.oauth.token.KakaoUserResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ClientKakao implements ClientProxy {

    private final WebClient webClient;

    // TODO ADMIN 유저 생성 시 getAdminUserData 메소드 생성 필요
    @Override
    public Member getUserData(String accessToken) {
        KakaoUserResponse kakaoUserResponse = webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new TokenValidFailedException("Social Access Token is unauthorized")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new TokenValidFailedException("Internal Server Error")))
                .bodyToMono(KakaoUserResponse.class)
                .block();

        return Member.builder()
                .socialId(String.valueOf(kakaoUserResponse.getId()))
                .username(kakaoUserResponse.getProperties().getNickname())
                .email(kakaoUserResponse.getKakaoAccount().getEmail())
                .socialProvider(SocialProvider.KAKAO)
                .role(Role.USER)
                .build();
    }
}