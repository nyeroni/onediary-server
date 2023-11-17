package onediary.onediary.config.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import onediary.onediary.domain.member.Member;
import onediary.onediary.domain.member.SocialProvider;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private SocialProvider socialProvider;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        switch (registrationId) {

            case "kakao":
                return ofKakao("email", attributes);
            case "naver":
                return ofNaver("id", attributes);
            default:
                throw new RuntimeException();
        }
    }
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        attributes.put("socialProvider", SocialProvider.KAKAO);
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .attributes(kakaoAccount)
                .nameAttributeKey(userNameAttributeName)
                .email((String) kakaoAccount.get("email"))
                .name((String) kakaoProfile.get("nickname"))
                .build();
    }
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        attributes.put("socialProvider", SocialProvider.NAVER);
        Map<String, Object> naverAccount = (Map<String, Object>) attributes.get("naver_account");
        Map<String, Object> naverProfile = (Map<String, Object>) naverAccount.get("profile");

        return OAuthAttributes.builder()
                .attributes(naverAccount)
                .nameAttributeKey(userNameAttributeName)
                .email((String) naverAccount.get("email"))
                .name((String) naverProfile.get("name"))
                .build();
    }
    public Member toEntity(){
        return Member.builder()
                .username(name)
                .email(email)
                .socialProvider(socialProvider)
                .build();
    }

}
