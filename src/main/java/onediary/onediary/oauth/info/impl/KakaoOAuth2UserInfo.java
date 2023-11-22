package onediary.onediary.oauth.info.impl;

import onediary.onediary.oauth.info.OAuth2UserInfo;

import java.util.Map;
import java.util.Objects;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String , Object> attributes){
        super(attributes);
    }
    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        if (properties == null) {
            return null;
        }

        return (String) properties.get("nickname");
    }
    @Override
    public String getEmail()
    {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        return (String) kakaoAccount.get("email");
    }
}
