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


    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        switch (registrationId) {
//            case "google":
//                return ofGoogle(attributeKey, attributes);
            case "kakao":
                return ofKakao("email", attributes);
//            case "naver":
//                return ofNaver("id", attributes);
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
    public Member toEntity(){
        return Member.builder()
                .username(name)
                .email(email)
                .socialProvider(SocialProvider.KAKAO)
                .build();
    }

}
