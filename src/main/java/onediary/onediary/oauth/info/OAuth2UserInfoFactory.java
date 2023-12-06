package onediary.onediary.oauth.info;

import onediary.onediary.domain.member.entity.SocialProvider;
import onediary.onediary.oauth.info.impl.KakaoOAuth2UserInfo;
import onediary.onediary.oauth.info.impl.NaverOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(SocialProvider socialProvider, Map<String ,Object> attributes){
        switch (socialProvider){
            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case  KAKAO:return new KakaoOAuth2UserInfo(attributes);
         //   case  APPLE:return new AppleOAuth2UserInfo(attributes);

            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}