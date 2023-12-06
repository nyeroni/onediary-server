package onediary.onediary.oauth.service;

import lombok.RequiredArgsConstructor;
import onediary.onediary.domain.member.entity.SocialProvider;
import onediary.onediary.dto.member.MemberResponseDto;
import onediary.onediary.oauth.info.impl.KakaoOAuth2UserInfo;
import onediary.onediary.service.IMemberService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOauthService {
    private final IMemberService memberService;

    public Map<String, Object> getUserAttriibutesByToken(String accessToken){
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    public MemberResponseDto getUserProfileByToken(String accessToken){
        Map<String, Object> userAttributesByToken = getUserAttriibutesByToken(accessToken);
        KakaoOAuth2UserInfo kakaoOAuth2UserInfo = new KakaoOAuth2UserInfo(userAttributesByToken);

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .socialId(kakaoOAuth2UserInfo.getId())
                .email(kakaoOAuth2UserInfo.getEmail())
                .username(kakaoOAuth2UserInfo.getName())
                .socialProvider(SocialProvider.KAKAO)
                .build();
        if(memberService.findMemberBySocialId(kakaoOAuth2UserInfo.getId())!=null){

        }
        else{
            memberService.save(memberResponseDto);
        }
        return memberResponseDto;
    }
}
